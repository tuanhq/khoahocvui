package com.ligerdev.appbase.utils.db;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;

public class BaseDAO extends AbsBaseDAO {
	
	public static final String POOL_NAME_MAIN = "main";
	public static final String POOL_NAME_LOG = "log";
	public static final String POOL_NAME_GW = "gw";
	public static final String POOL_NAME_CMS = "cms";
	
	private BaseDAO(String poolName) {
		super(poolName);
	}
	
	public synchronized static BaseDAO getInstance(String poolName){
		return getInstance(poolName, null);
	}
	
	public synchronized static BaseDAO getInstance(String poolName, String nameSpace){
		String key = poolName + "-" + nameSpace;
		BaseDAO baseDAO = listInstance.get(key);
		if(baseDAO == null){
			baseDAO = new BaseDAO(poolName);
			listInstance.put(key, baseDAO);
		}
		return baseDAO;
	}
	
	public void setLogger(Logger logger){
		this.logger = logger;
	}
	
	public void disableLogger(boolean value){
		this.enableLogger = !value;
	}
	
	public <T> int[] deleteList(String transid, List<T> listBean){
		return deleteList(transid, null, listBean);
	}
	
	public <T> int[] deleteList(String transid, String tableName, List<T> listBean){
		if(listBean == null || listBean.size() == 0){
			logger.info(transid + ", listBean is blank => ignore");
			return new int[0];
		}
		int rs[] = new int[listBean.size()];
		for(int i = 0; i < listBean.size(); i ++){
			rs[i] = deleteBean(transid, tableName, listBean.get(i));
		}
		return rs;
	}
	
	public <T> int deleteBean(String transid, T bean){
		return deleteBean(transid, null, bean);
	}
	
	public <T> int deleteBean(String transid, String tableName, T bean){
		if(bean == null){
			logger.info(transid + ", bean is null => ignore");
			return -1;
		}
		Class<? extends Object> clazz = bean.getClass();
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr) || BaseUtils.isBlank(antTable.key())){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return -1;
		}
		String sql = "DELETE FROM " + tableNameStr + " WHERE " + antTable.key() + " = ?";
		Object keyValue = null;
		try {
			Method getKeyMethod = clazz.getMethod("get" + GenDbSource.formatName(antTable.key()));
			keyValue = getKeyMethod.invoke(bean);
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
			return -1;
		}
		int result =  execSql(transid, sql, keyValue);
		if(enableLogger){
			logger.info(transid + ", execUpdate " + tableNameStr + ", "  + result + " is deleted, key = " + String.valueOf(keyValue));
		}
		return result; 
	}
	
	public <T> int updateList(String transid, List<T> listBean){
		return updateListFields(transid, null, listBean, false);
	}
	
	public <T> int updateList(String transid, String tableName, List<T> listBean){
		return updateListFields(transid, tableName, listBean, false);
	}
	
	public <T> int updateListFields(String transid, List<T> listBean, boolean withFields, String ... fields){
		return updateListFields(transid, null, listBean, withFields, fields);
	}

	public <T> int updateListFields(String transid, String tableName, List<T> listBean, boolean withFields, String ... fields){
		if(listBean == null || listBean.size() == 0){
			logger.info(transid + ", listBean is blank => ignore");
			return -1;
		}
		long l1 = System.currentTimeMillis();
		Class<? extends Object> clazz = listBean.get(0).getClass();
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr) || BaseUtils.isBlank(antTable.key())){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return -1;
		}
		String sql = null;
		Connection conn = null; 
		PreparedStatement stmt = null;
		try {
			for(T bean : listBean){
				UpdateInfo updateInfo = getUpdateInfo(transid, tableNameStr, bean, withFields, antTable, fields);
				if(updateInfo == null){
					logger.info(transid + ", ignore bean: " + String.valueOf(bean));
					continue;
				}
				if(sql == null){
					sql = updateInfo.getSql();
					conn = getConnection(transid, false);
					stmt = conn.prepareStatement(sql);
				}
				ArrayList<Object> listValue = updateInfo.getListValue();
				for(int i = 1; listValue != null && i <= listValue.size(); i ++){
					stmt.setObject(i, listValue.get(i - 1));   
				}
				stmt.addBatch();
			}
			long l2 = System.currentTimeMillis();
			int rs[] = stmt.executeBatch();
			conn.commit();
			long l3 = System.currentTimeMillis();
			
			int result = (rs == null ? 0 : rs.length);
			if(enableLogger){
				logger.info(transid + ", execBatch SQL = " + sql + "| time1=" 
						+ (l2 - l1) + ", time2=" + (l3 - l2) + ", batchSize=" + result);
			}
			return result;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				logger.info(transid + ", Exception: " + e2.getMessage());
			}
		} finally {
			releaseAll(transid, stmt, conn);
		}
		return -1;
	}
	
	public <T> int updateBean(String transid, T bean){
		return updateBeanFields(transid, null,  bean, false);
	}
	
	public <T> int updateBean(String transid, String tableName, T bean){
		return updateBeanFields(transid, tableName, bean, false);
	}
	
	public <T> int updateBeanFields(String transid, T bean, boolean withFields, String ... fields){
		return updateBeanFields(transid, null, bean, withFields, fields);
	}
	
	public <T> int updateBeanFields(String transid, String tableName, T bean, boolean withFields, String ... fields){
		if(bean == null){
			logger.info(transid + ", bean is null => ignore");
			return -1;
		}
		Class<? extends Object> clazz = bean.getClass();
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr) || BaseUtils.isBlank(antTable.key())){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return -1;
		}
		UpdateInfo updateInfo = getUpdateInfo(transid, tableNameStr, bean, withFields, antTable, fields);
		if(updateInfo == null){
			logger.info(transid + ", not found any fields to update");
			return -1;
		}
		String sql = updateInfo.getSql();
		ArrayList<Object> listValue = updateInfo.getListValue();
		
		int result =  execSql(transid, sql, listValue.toArray());
		if(enableLogger){
			logger.info(transid + ", execUpdate " + tableNameStr + ", "  + result + " is updated, bean = " + String.valueOf(bean));
		}
		return result; 
	}
	
	public <T> int insertList(String transid, List<T> listBean){
		return insertListFields(transid, null, listBean, false);
	}
	
	public <T> int insertList(String transid, String tableName, List<T> listBean){
		return insertListFields(transid, tableName, listBean, false);
	}
	
	public <T> int insertListFields(String transid, List<T> listBean, boolean withFields, String ... fields){
		return insertListFields(transid, null, listBean, withFields, fields);
	}
	
	public <T> int insertListFields(String transid, String tableName, List<T> listBean, boolean withFields, String ... fields){
		if(listBean == null || listBean.size() == 0){
			logger.info(transid + ", listBean is blank => ignore");
			return -1;
		}
		long l1 = System.currentTimeMillis();
		Class<? extends Object> clazz = listBean.get(0).getClass();
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr)){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return -1;
		}
		String sql = null;
		Connection conn = null; 
		PreparedStatement stmt = null;
		try {
			for(T bean : listBean){
				UpdateInfo updateInfo = getInsertInfo(transid, tableNameStr, bean, withFields, antTable, fields);
				if(updateInfo == null){
					logger.info(transid + ", ignore bean: " + String.valueOf(bean));
					continue;
				}
				if(sql == null){
					sql = updateInfo.getSql();
					conn = getConnection(transid, false);
					stmt = conn.prepareStatement(sql);
				}
				ArrayList<Object> listValue = updateInfo.getListValue();
				for(int i = 1; listValue != null && i <= listValue.size(); i ++){
					stmt.setObject(i, listValue.get(i - 1));   
				}
				stmt.addBatch();
			}
			long l2 = System.currentTimeMillis();
			int rs[] = stmt.executeBatch();
			conn.commit();
			long l3 = System.currentTimeMillis();
			
			int result = (rs == null ? 0 : rs.length);
			if(enableLogger){
				logger.info(transid + ", execBatch SQL = " + sql + "| time1=" 
						+ (l2 - l1) + ", time2=" + (l3 - l2) + ", batchSize=" + result);
			}
			return result;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				logger.info(transid + ", Exception: " + e2.getMessage());
			}
		} finally {
			releaseAll(transid, stmt, conn);
		}
		return -1;
	}
	
	public <T> int insertBean(String transid, T bean){
		return insertBeanFields(transid, null, bean, false);
	}
	
	public <T> int insertBean(String transid, String tableName, T bean){
		return insertBeanFields(transid, tableName, bean, false);
	}
	
	public <T> int insertBeanFields(String transid, T bean, boolean withFields, String ... fields){
		return insertBeanFields(transid, null, bean, withFields, fields);
	}
	
	public <T> int insertBeanFields(String transid, String tableName, T bean, boolean withFields, String ... fields){
		if(bean == null){
			logger.info(transid + ", bean is null => ignore");
			return -1;
		}
		Class<? extends Object> clazz = bean.getClass();
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr)){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return -1;
		}
		UpdateInfo updateInfo = getInsertInfo(transid, tableNameStr, bean, withFields, antTable, fields);
		if(updateInfo == null){
			logger.info(transid + ", not found any fields to insert");
			return -1;
		}
		String sql = updateInfo.getSql();
		ArrayList<Object> listValue = updateInfo.getListValue();
		
		int result =  -1;
		if(updateInfo.isHasAutoIncreaseKey()){
			sql = sql.trim();
			if(dbType == DB_TYPE_ORACLE ){
				result = execSqlGetKeyOracle(transid, sql, antTable.key(), listValue.toArray());
			} else {
				result = execSqlGetKeyMySql(transid, sql, listValue.toArray());
			}
			if(enableLogger){
				logger.info(transid + ", execUpdate " + tableNameStr + ", id "  + result + " is inserted, bean = " + String.valueOf(bean));
			}
		} else {
			result = execSql(transid, sql, listValue.toArray());
			if(enableLogger){
				logger.info(transid + ", execUpdate " + tableNameStr + ", "  + result + " rows is inserted, bean = " + String.valueOf(bean));
			}
		}
		return result; 
	}
	
	private <T> int execSqlGetKeyOracle(String transid, String sql, String keyName, Object ... params){
		Connection conn = null; 
		CallableStatement stmt = null;
		sql = "BEGIN " + sql + " RETURNING " + keyName + " INTO ?; END;";
		try {
			conn = getConnection(transid);
			stmt = conn.prepareCall(sql);
			
			int indexIdReturn = 1;
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
				indexIdReturn ++;
			}
			int result = stmt.executeUpdate();
			if(result > 0){
				result = stmt.getInt(indexIdReturn);
				if(enableLogger){
					logger.info(transid + ", exec SQL = " + sql + "| id " + result + " is updated");
				}
			} else {
				if(enableLogger){
					logger.info(transid + ", exec SQL = " + sql + "| " + result + " rows is updated");
				}
			}
			return result;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, stmt, conn);
		}
		return -1;
	}
	
	private <T> int execSqlGetKeyMySql(String transid, String sql, Object ... params){
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			int result = stmt.executeUpdate();
			if(result > 0){
				rs = stmt.getGeneratedKeys();
		        if (rs.next()){
		        	result = rs.getInt(1);
		        	if(enableLogger){
						logger.info(transid + ", exec SQL = " + sql + "| id " + result + " is updated");
					}
		        }
			} else {
				if(enableLogger){
					logger.info(transid + ", exec SQL = " + sql + "| " + result + " rows is updated");
				}
			}
			return result;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return -1;
	}

	public <T> int execSql(String transid, String sql, Object ... params){
		Connection conn = null; 
		PreparedStatement stmt = null;
		sql = sql.trim();
		try {
			conn = getConnection(transid);
			stmt = conn.prepareStatement(sql);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			int result = stmt.executeUpdate();
			if(enableLogger){
				logger.info(transid + ", exec SQL = " + sql + "| " + result + " rows is updated");
			} 
			return result;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, stmt, conn);
		}
		return -1;
	}
	
	public <T> T getBeanByKey(String transid, Class<T> clazz, Object key){
		return getBeanByKey(transid, null, clazz, key);
	}
	
	public <T> T getBeanByKey(String transid, String tableName, Class<T> clazz, Object key){
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(antTable == null || BaseUtils.isBlank(tableNameStr) || BaseUtils.isBlank(antTable.key())){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return null;
		}
		String sql = "SELECT * FROM " + tableNameStr + " WHERE " + antTable.key() + " = ?"; 
		ArrayList<T> listBeans = getListBySql(transid, clazz, sql, null, null, key);
		
		if(listBeans != null && listBeans.size() > 0){
			T bean = listBeans.get(0);
			if(enableLogger){
				logger.info(transid + ", get " + tableNameStr + ", key = "  + String.valueOf(key) + ", bean = " + String.valueOf(bean)); 
			}
			return bean;
		}  
		if(enableLogger){
			logger.info(transid + ", get " + tableNameStr + ", key = " + String.valueOf(key) + ", not found!"); 
		}
		return null;
	}
	
	public <T> T getBeanByRstAnd(String transid, Class<T> clazz, Restriction ... restriction){
		 return getBeanByRstAnd(transid, null, clazz, restriction);
	}
	
	public <T> T getBeanByRstAnd(String transid, String tableName, Class<T> clazz, Restriction ... restriction){
		ArrayList<T> listBeans = getListByRstAnd(transid, tableName, clazz, 0, 1, restriction);
		if(listBeans != null && listBeans.size() > 0){
			T bean = listBeans.get(0);
			return bean;
		}  
		return null;
	}
	
	public <T> T getBeanByRstOr(String transid, Class<T> clazz, Restriction ... restriction){
		return getBeanByRstOr(transid, null, clazz, restriction);
	}
	
	public <T> T getBeanByRstOr(String transid, String tableName, Class<T> clazz, Restriction ... restriction){
		ArrayList<T> listBeans = getListByRstOr(transid, tableName, clazz, 0, 1, restriction);
		if(listBeans != null && listBeans.size() > 0){
			T bean = listBeans.get(0);
			return bean;
		}  
		return null;
	}
	
	public <T> ArrayList<T> getListAll(String transid, Class<T> clazz, Integer beginIndex, Integer pageSize){
		return getListByRstAnd(transid, null, clazz, beginIndex, pageSize);
	}
	
	public <T> ArrayList<T> getListAll(String transid, String tableName, Class<T> clazz, Integer beginIndex, Integer pageSize){
		return getListByRstAnd(transid, tableName, clazz, beginIndex, pageSize);
	}
	
	public <T> ArrayList<T> getListByRstAnd(String transid, String tableName, Class<T> clazz,  
			Integer beginIndex, Integer pageSize, Restriction ... restriction){
		return getByRestriction(transid, tableName, clazz, "AND", beginIndex, pageSize, restriction);
	}
	
	public <T> ArrayList<T> getListByRstOr(String transid, String tableName, Class<T> clazz,  
			Integer beginIndex, Integer pageSize, Restriction ... restriction){
		return getByRestriction(transid, tableName, clazz, "OR", beginIndex, pageSize, restriction);
	}
	
	public <T> ArrayList<T> getListByRstAnd(String transid, Class<T> clazz,  
			Integer beginIndex, Integer pageSize, Restriction ... restriction){
		return getByRestriction(transid, null, clazz, "AND", beginIndex, pageSize, restriction);
	}
	
	public <T> ArrayList<T> getListByRstOr(String transid, Class<T> clazz,  
			Integer beginIndex, Integer pageSize, Restriction ... restriction){
		return getByRestriction(transid, null, clazz, "OR", beginIndex, pageSize, restriction);
	}
	 
	private <T> ArrayList<T> getByRestriction(String transid, String tableName, 
			Class<T> clazz, String type, Integer beginIndex, Integer pageSize, Restriction ... restriction){
		
		AntTable antTable = (AntTable) clazz.getAnnotation(AntTable.class);
		String tableNameStr = getTableName(antTable, tableName);
		
		if(BaseUtils.isBlank(tableNameStr)){
			logger.info(transid + ", class " + clazz.getName() + " is not correct annotation present");
			return null;
		}
		String sql = null; 
		ArrayList<Object> listParams = new ArrayList<Object>();
		if(restriction == null || restriction.length == 0){
			sql = "SELECT * FROM " + tableNameStr;
		} else {
			sql = "SELECT * FROM " + tableNameStr + " WHERE";
			for(int i = 0; i < restriction.length; i ++){
				Object value = restriction[i].getValue();
				if(value == null){
					sql += " " + restriction[i].getColumn() + " is null";
				} else {
					sql += " " + restriction[i].getColumn() + " " + restriction[i].getCompare() + " ?";
					listParams.add(value);
				}
				if(i + 1 < restriction.length){
					sql += " " + type;
				}
			}
		}
		ArrayList<T> listBeans = getListBySql(transid, clazz, sql, beginIndex, pageSize, listParams.toArray());
		return listBeans;
	}
	
	public <T> T getBeanBySql(String transid, Class<T> clazz, String sql, Object ... params){
		ArrayList<T> listRs = getListBySql(transid, clazz, sql, 0, 1, params);
		if(listRs == null || listRs.size() == 0){
			return null;
		}
		return listRs.get(0); 
	}
	
	public <T> ArrayList<T> getListBySql(String transid, Class<T> clazz, String sql, Integer beginIndex, Integer pageSize, Object ... params){
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			sql = pagingSql(sql, beginIndex, pageSize);
			stmt = conn.prepareStatement(sql);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			if(pageSize != null && pageSize > 0){
				stmt.setFetchSize(pageSize);
			}
			rs = stmt.executeQuery();
			
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()){
				T bean = readRs(transid, clazz, rs);
				list.add(bean);
			}
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| found " + list.size() + " records");
			}
			return list;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return null;
	}
	
	public int handleSql(String transid, String sql, IReadRs iReadRs, Integer beginIndex, Integer pageSize, Object ... params){
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			sql = pagingSql(sql, beginIndex, pageSize);
			stmt = conn.prepareStatement(sql);
			
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			if(pageSize != null && pageSize > 0){
				stmt.setFetchSize(pageSize);
			}
			rs = stmt.executeQuery();
			
			int count = 0;
			while(rs.next()){
				try {
					boolean breakTmp = iReadRs.found(transid, count, rs);
					count ++;
					if(breakTmp == false){
						break;
					}
				} catch (Exception e) {
					logger.info(transid + ", Exception: " + e.getMessage(), e);
				}
			}
			if(count == 0){
				iReadRs.notFound(transid);
			}
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| found " + count + " records");
			}
			return count;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return -1;
	}
	
	public int handleSqlUpdatable(String transid, String sql, IReadRsUpdatale iReadRsUpdatale, 
			Integer beginIndex, Integer pageSize, Object ... params){
		
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			sql = pagingSql(sql, beginIndex, pageSize);
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			if(pageSize != null && pageSize > 0){
				stmt.setFetchSize(pageSize);
			}
			rs = stmt.executeQuery();
			
			int count = 0;
			while(iReadRsUpdatale.whereRs(rs)){ 
				try {
					boolean breakTmp = iReadRsUpdatale.found(transid, count, rs);
					count ++;
					if(breakTmp == false){
						break;
					}
				} catch (Exception e) {
					logger.info(transid + ", Exception: " + e.getMessage(), e);
				}
			}
			if(count == 0){
				iReadRsUpdatale.notFound(transid);
			}
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| found " + count + " records");
			}
			return count;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return -1;
	}
	
	public ArrayList<HashMap<String, Object>> getDataTable(String transid, String sql, 
			Integer beginIndex, Integer pageSize, Object ... params){  
		
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			sql = pagingSql(sql, beginIndex, pageSize);
			stmt = conn.prepareStatement(sql);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			if(pageSize != null && pageSize > 0){
				stmt.setFetchSize(pageSize);
			}
			rs = stmt.executeQuery();
			
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData(); 
			while(rs.next()){
				HashMap<String, Object> row = new HashMap<String, Object>();
				for(int i = 1; i <= meta.getColumnCount(); i ++){
					String columnName = meta.getColumnName(i);
					Object value = rs.getObject(columnName);
					row.put(columnName, value);
				}
				data.add(row);
			}
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| found " + data.size() + " records");
			}
			return data;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFirstCell(String transid, String sql, Class<T> clazz, Object ... params){  
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			sql = pagingSql(sql, 0, 1);
			stmt = conn.prepareStatement(sql);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			
			T value = null;
			if(rs.next()){
				if(clazz == Integer.class){
					value = (T) ((Integer) rs.getInt(1));
				} else {
					value = (T) rs.getObject(1);
				}
			}
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| result = " + String.valueOf(value));
			}
			return value;
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage() + ", SQL = " + sql, e);
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return null;
	}
	
	public boolean isValidSql(String transid, String sql, Object ... params){  
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(transid);
			stmt = conn.prepareStatement(sql);
			for(int i = 1; params != null && i <= params.length; i ++){
				stmt.setObject(i, params[i - 1]);  
			}
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| valid = true");
			}
			return true;
			
		} catch (Exception e) {
			if(enableLogger){
				logger.info(transid + ", run SQL = " + sql + "| valid = false, msg = " + e.getMessage());
			}
		} finally {
			releaseAll(transid, rs, stmt, conn);
		}
		return false;
	}
	
	public static void main(String[] args) {
		BaseDAO base = getInstance(DBPoolMnt.SchemaName.MAIN);
	}

	public String getDriver() {
		return dbpoolUtils.getDriver();
	}

	public String getUser() {
		return dbpoolUtils.getUser();
	}

	public String getPassword() {
		return dbpoolUtils.getPassword();
	}

	public String getUrl() { 
		return dbpoolUtils.getUrl();
	}
}
