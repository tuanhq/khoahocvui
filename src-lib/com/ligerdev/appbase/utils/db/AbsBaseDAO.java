package com.ligerdev.appbase.utils.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;


public abstract class AbsBaseDAO {

	protected static final int DB_TYPE_MYSQL = 1;
	protected static final int DB_TYPE_ORACLE = 2;
	protected static ConcurrentHashMap<String, BaseDAO> listInstance = new ConcurrentHashMap<String, BaseDAO>();
	
	protected static Logger logger = Log4jLoader.getLogger();
	protected IDBPool dbpoolUtils = null;
	protected int dbType = 0;
	protected boolean enableLogger = true;
	
	public AbsBaseDAO(String poolName) {
		dbpoolUtils = DBPoolMnt.getInstance(poolName);
		if(dbpoolUtils == null){
			dbpoolUtils = DBPoolNormal.getInstance(poolName);
		}
		logger.info("Create db pool, class = " + dbpoolUtils.getClass().getName());
		
		if(dbpoolUtils.getDriver().toLowerCase().contains("mysql")){
			dbType = DB_TYPE_MYSQL;
			
		} else if(dbpoolUtils.getDriver().toLowerCase().contains("oracle")){
			dbType = DB_TYPE_ORACLE;
		}
	}
	
	public static void main(String[] args) {
	}
	
	public Connection getConnection() throws Exception {
		return dbpoolUtils.getConnection();
	}
	
	public Connection getConnection(boolean autoCommit) throws Exception {
		return dbpoolUtils.getConnection(autoCommit);
	}
	
	public Connection getConnection(Integer timeout) throws Exception {
		return dbpoolUtils.getConnection(timeout);
	}
	
	public Connection getConnection(String transid) throws Exception {
		return dbpoolUtils.getConnection(transid);
	}
	
	public Connection getConnection(String transid, Integer timeout) throws Exception {
		return dbpoolUtils.getConnection(transid, timeout);
	}
	
	public Connection getConnection(boolean autoCommit, Integer timeout) throws Exception {
		return dbpoolUtils.getConnection(autoCommit, timeout);
	}
	
	public Connection getConnection(String transid, boolean autoCommit) throws Exception {
		return dbpoolUtils.getConnection(transid, autoCommit);
	}
	
	public Connection getConnection(String transid, boolean autoCommit, Integer timeout) throws Exception {
		return dbpoolUtils.getConnection(transid, autoCommit, timeout);
	}
	
	public void releaseAll(Object... objects){
		dbpoolUtils.releaseAll(objects);
	}
	
	public static void closeAllConns(){
		DBPoolMnt.closeAllConns();
		DBPoolNormal.closeAllConns();
	}
	
	public String getInfoByDateStr() {
		return dbpoolUtils.getInfoByDateStr();
	}
	
	public String getInfoByHourStr(int hour) {
		return dbpoolUtils.getInfoByHourStr(hour);
	}

	protected String pagingSql(String sql, Integer beginIndex, Integer pageSize) {
		// beginIndex count from 0
		if(beginIndex == null || pageSize == null || (beginIndex == 0 && pageSize == 1)){
			return sql;
		}
		if(beginIndex < 0){
			beginIndex = 0;
		}
		if(pageSize < 0){
			pageSize = 0;
		}
		if(dbType == DB_TYPE_MYSQL){
			sql = "SELECT tblx.* FROM (" + sql + ") tblx LIMIT " + beginIndex + ", " + pageSize;
			return sql;
		}
		if(dbType == DB_TYPE_ORACLE){
			sql = "SELECT tblx.* FROM (SELECT subq.*, ROWNUM R FROM (" + sql 
					+ ") subq WHERE ROWNUM <= " + (beginIndex + pageSize) + ") tblx WHERE tblx.R > " + beginIndex;
			return sql;
		}
		return null;
	}
	
	public <T> T readRs(String transid, Class<T> clazz, ResultSet rs){
		try {
			Method ms[] = clazz.getDeclaredMethods();
			T bean = clazz.newInstance();
			for(Method m : ms){
				AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
				if(ant != null && (m.getName().startsWith("set"))){
					
					String columnName = null;
					Object value = null;
					try {
						columnName = ant.name();
						value = rs.getObject(columnName);
						m.invoke(bean, value);
						
					} catch (IllegalArgumentException e){
						logger.info(transid + ", Exception: " + e.getMessage() + ", columnName = " 
								+ columnName + ", dbValue = " + String.valueOf(value) 
								+ ", valueClass = " + (value == null ? null : value.getClass().getName()));
						
					} catch (Exception e) {
						 // logger.info(transid + ", Exception: " + e.getMessage() + ", columnName = " + columnName, e);
					}
				}
			}
			return bean;
 		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage(), e);
		}
		return null;
	}

	protected <T> UpdateInfo getUpdateInfo(String transid, String tableName, 
			T bean, boolean withFields, AntTable antTable, String ... fields) {
		
		String sql = "UPDATE " + tableName + " SET @fields WHERE " + antTable.key() + " = ?";
		String updateFields = "";
		ArrayList<Object> listValue = new ArrayList<Object>();
		
		ArrayList<String> listFields = new ArrayList<String>();
		if(fields != null && fields.length > 0){
			Collections.addAll(listFields, fields);
		}
		Method ms[] = bean.getClass().getDeclaredMethods();
		Object valueKey = null;
		
		for (Method m : ms) {
			AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
			if(ant == null || !m.getName().startsWith("get")){
				continue;
			}
			if(antTable.key().equalsIgnoreCase(ant.name())){
				try {
					valueKey = m.invoke(bean);
				} catch (Exception e) {
					logger.info(transid + ", Exception: " + e.getMessage(), e);
				}
				continue;
			}
			if(ant.auto_increment()){
				continue;
			}
			if(listFields.size() > 0 && withFields != listFields.contains(ant.name())){
				continue;
			}
			try {
				updateFields += ", " + ant.name() + " = ?";
				Object value = m.invoke(bean);
				listValue.add(value);
				
			} catch (Exception e) {
				logger.info(transid + ", Exception: " + e.getMessage(), e);
			}
		}
		if (listValue.size() == 0) {
			return null;
		}
		listValue.add(valueKey);
		updateFields = updateFields.replaceFirst(", ", "");
		sql = sql.replace("@fields", updateFields);
		return new UpdateInfo(sql, listValue, null);
	}
	
	protected <T> UpdateInfo getInsertInfo(String transid, String tableName, 
			T bean, boolean withFields, AntTable antTable, String ... fields) {
		
		String sql = "INSERT INTO " + tableName + "(@columns) VALUES(@values)";
		String columns = ""; 
		String values = "";
		ArrayList<Object> listValue = new ArrayList<Object>();

		ArrayList<String> listFields = new ArrayList<String>();
		if(fields != null && fields.length > 0){
			Collections.addAll(listFields, fields);
		}
		Method ms[] = bean.getClass().getDeclaredMethods();
		Boolean hasAutoIncreaseKey = false;
		
		for (Method m : ms) {
			AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
			if(ant == null || !m.getName().startsWith("get")){
				continue;
			}
			if (ant.auto_increment()) {
				if(BaseUtils.isNotBlank(antTable.key()) && ant.name().equals(antTable.key())){
					hasAutoIncreaseKey = true;
				}
				continue;
			}
			if(listFields.size() > 0 && withFields != listFields.contains(ant.name())){
				continue;
			}
			try {
				if(dbType == DB_TYPE_ORACLE 
						&& BaseUtils.isNotBlank(antTable.sequence()) 
						&& antTable.key().equalsIgnoreCase(ant.name())) {
					
					columns += ", " + ant.name();
					values += ", " + antTable.sequence() + ".nextVal";
					hasAutoIncreaseKey = true;
					
				} else {
					columns += ", " + ant.name();
					values += ", ?";
					Object value = m.invoke(bean);
					listValue.add(value);
				}
			} catch (Exception e) {
				logger.info(transid + ", Exception: " + e.getMessage(), e);
			}
		}
		if (listValue.size() == 0) {
			return null;
		}
		columns = columns.replaceFirst(", ", "");
		values = values.replaceFirst(", ", "");
		sql = sql.replace("@columns", columns).replace("@values", values);
		
		return new UpdateInfo(sql, listValue, hasAutoIncreaseKey);
	}
	
	protected class UpdateInfo {
		
		private Boolean hasAutoIncreaseKey;
		private String sql = null;
		private ArrayList<Object> listValue = null;

		public UpdateInfo(String sql, ArrayList<Object> listValue, Boolean hasAutoIncreaseKey) {
			super();
			this.sql = sql;
			this.listValue = listValue;
			this.hasAutoIncreaseKey = hasAutoIncreaseKey;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public ArrayList<Object> getListValue() {
			return listValue;
		}

		public void setListValue(ArrayList<Object> listValue) {
			this.listValue = listValue;
		}

		public Boolean isHasAutoIncreaseKey() {
			return hasAutoIncreaseKey != null && hasAutoIncreaseKey;
		}

		public void setHasAutoIncreaseKey(Boolean hasAutoIncreaseKey) {
			this.hasAutoIncreaseKey = hasAutoIncreaseKey;
		}
	}
	
	protected String getTableName(AntTable antTable, String modTblName){
		if(BaseUtils.isNotBlank(modTblName)){
			return modTblName;
		}
		if(antTable == null){
			return null;
		}
		if(BaseUtils.isBlank(antTable.time_pattern())){
			return antTable.name();
		}
		String time_pattern = BaseUtils.parseTime(antTable.time_pattern(), System.currentTimeMillis());
		if(BaseUtils.isBlank(time_pattern)){
			return antTable.name();
		}
		String newName = antTable.name().replace("$pattern$", time_pattern);
		return newName;
	}

	public IDBPool getDbpoolUtils() {
		return dbpoolUtils; 
	}

	public void setDbpoolUtils(DBPoolMnt dbpoolUtils) {
		this.dbpoolUtils = dbpoolUtils;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public boolean isEnableLogger() {
		return enableLogger;
	}

	public void setEnableLogger(boolean enableLogger) {
		this.enableLogger = enableLogger;
	}
	
	
}
