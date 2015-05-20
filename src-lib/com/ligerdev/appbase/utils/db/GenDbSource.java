package com.ligerdev.appbase.utils.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class GenDbSource {

	private static String DRIVER = "com.mysql.jdbc.Driver";
	private static String PASS = "123456";
	private static String USER = "root";
	private static String URL = "jdbc:mysql://localhost:3306/dbtest?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
	
	private static String tableName = "SUBSCRIBER";
	private static String sql = "SELECT * FROM " + tableName + " LIMIT 1"; 
	private static String keyName = "MSISDN";
	private static String packageName = "com.mg.aps.test.annotations.db";
	
	public static void main(String[] args) {
		System.out.println(genDTO());
		System.out.println(genDAO());
	}
	
	public static String genDAO(String poolName, String tableName, String sql, String keyName, String packageName) {
		Log4jLoader.disableLogAndErrOutput();
		BaseDAO pool = BaseDAO.getInstance(poolName);
		setInfoConn(pool, tableName, sql, keyName, packageName);
		String s = genDAO();
		System.out.println(s); 
		return s;
	}
	
	public static String genDTO(String poolName, String tableName, String sql, String keyName, String packageName) {
		Log4jLoader.disableLogAndErrOutput();
		BaseDAO pool = BaseDAO.getInstance(poolName);
		setInfoConn(pool, tableName, sql, keyName, packageName);
		String s = genDTO();
		System.out.println(s); 
		return s;
	}
	
	private static void setInfoConn(BaseDAO pool, String tableName, String sql, String keyName, String packageName){
		GenDbSource.DRIVER = pool.getDriver();
		GenDbSource.PASS = pool.getPassword();
		GenDbSource.USER = pool.getUser();
		GenDbSource.URL = pool.getUrl(); 
		
		GenDbSource.tableName = tableName;
		GenDbSource.keyName = keyName;
		GenDbSource.packageName = packageName;
		
		if(sql == null){
			if(pool.getDriver().toLowerCase().contains("oracle")){
				GenDbSource.sql = "SELECT * FROM " + tableName + " WHERE ROWNUM <= 1";
			} else {
				GenDbSource.sql = "SELECT * FROM " + tableName + " LIMIT 1";
			}
		} else {
			GenDbSource.sql = sql;
		}
	}
	
	public static String genDAO() {
		String daoTemplate = BaseUtils.readInputStream(GenDbSource.class.getResourceAsStream("DaoTemplate.txt"));
		
		String imports = "";
		String setBean = "";
		String columnInsertName = "";
		String columnInsertCount = "";
		String setStmtInsert = "";
		String columnUpdates = "";
		String setStmtUpdate = "";
		String className = formatName(tableName) + "DAO";
		
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			ResultSetMetaData meta = rs.getMetaData(); 
			int countColumn = meta.getColumnCount();
			int indexInsert = 1;
			int indexUpdate = 1;
			
			for(int i = 1; i <= countColumn; i ++){
				String columnName = meta.getColumnName(i);
				String type = meta.getColumnTypeName(i);
				
				String temp = formatName(columnName);
				String setter = "set" + temp;
				String getter = "get" + temp;
				
				String getterRs = "";
				
				if (type.toLowerCase().contains("varchar")) {
					getterRs = "getString";

				} else if (type.toLowerCase().contains("bool") 
						|| type.toLowerCase().contains("tiny")
						|| type.toLowerCase().contains("bit")) {
					getterRs = "getBoolean";
					
				} else if (type.toLowerCase().contains("int") 
						|| type.toLowerCase().contains("number")) {
					getterRs = "getInt";
					
				} else if (type.toLowerCase().contains("long")) {
					getterRs = "getLong";
					
				} else if (type.toLowerCase().contains("date") 
						|| type.toLowerCase().contains("timestamp")) {
					getterRs = "getTimestamp";
					
				} else if (type.toLowerCase().contains("float")) {
					getterRs = "getFloat";
					
				} else if (type.toLowerCase().contains("double")) {
					getterRs = "getDouble";
					
				}  else {
					System.out.println("#############, columnName = " + columnName + ", type = " + type);
					BaseUtils.sleep(1000); System.exit(-9);
				}
				String setterRs = "setObject";
				
				if(!keyName.equalsIgnoreCase(columnName)){
					columnUpdates += ", " + columnName + "=?";
					String setStmtUpdateTmp = "\t\t\tstmt." + setterRs + "(" + indexUpdate + ", bean." + getter + "());\n";
					setStmtUpdate += setStmtUpdateTmp;
					indexUpdate ++;
				}
				if(!meta.isAutoIncrement(i)){  
					columnInsertName += ", " + columnName;
					columnInsertCount += ", ?";
					String setStmtInsertTmp = "\t\t\tstmt." + setterRs + "(" + indexInsert + ", bean." + getter + "());\n";
					setStmtInsert += setStmtInsertTmp;
					indexInsert ++;
				} 
				String setBeansTmp = "\t\t\t\tbean." + setter + "(rs." + getterRs + "(\"" + columnName + "\"));\n";
				setBean += setBeansTmp;
			}
			setStmtUpdate += "\t\t\tstmt.setObject(" + indexUpdate + ", bean.get" + formatName(keyName) + "());\n";
			
		} catch (Exception e) {
			Log4jLoader.enableLogAndErrOutput();
			e.printStackTrace();
		} finally {
			releaseAll(rs, stmt, conn);
		}
		columnUpdates = columnUpdates.replaceFirst(", ", "");
		columnInsertName = columnInsertName.replaceFirst(", ", "");
		columnInsertCount = columnInsertCount.replaceFirst(", ", "");
		
		daoTemplate = daoTemplate.replace("@columnUpdates", columnUpdates);
		daoTemplate = daoTemplate.replace("@columnInsertName", columnInsertName);
		daoTemplate = daoTemplate.replace("@columnInsertCount", columnInsertCount);
		daoTemplate = daoTemplate.replace("@setStmtInsert", setStmtInsert);
		daoTemplate = daoTemplate.replace("@setStmtUpdate", setStmtUpdate);
		daoTemplate = daoTemplate.replace("@dto", formatName(tableName));
		daoTemplate = daoTemplate.replace("@import", imports);
		daoTemplate = daoTemplate.replace("@className", className);
		daoTemplate = daoTemplate.replace("@setBean", setBean);
		daoTemplate = daoTemplate.replace("@package", "package " + packageName + ";");
		daoTemplate = daoTemplate.replace("@key", formatNameField(keyName));
		daoTemplate = daoTemplate.replace("@primary", keyName);
		daoTemplate = daoTemplate.replace("@setKey", "stmt.setObject(1, " + formatNameField(keyName) + ");");
		
		daoTemplate = daoTemplate.replace("@tableName", tableName);
		return daoTemplate;
	}

	public static String genDTO() {
		String beanTemplate = BaseUtils.readInputStream(GenDbSource.class.getResourceAsStream("BeanTemplate.txt"));
		String beanMethodTemplate = BaseUtils.readInputStream(GenDbSource.class.getResourceAsStream("BeanMethodTemplate.txt"));
		
		String fields = "";
		String imports = "";
		String setgets = "";
		String toString = "";
		String catalog = "";
		String paramsContructor = "";
		String contructorAssign = "";
		String className = formatName(tableName);
		
		Connection conn = null; 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			ResultSetMetaData meta = rs.getMetaData(); 
			int countColumn = meta.getColumnCount();
			
			for(int i = 1; i <= countColumn; i ++){
				String columnName = meta.getColumnName(i);
				String type = meta.getColumnTypeName(i);
				String field = formatNameField(columnName);
				
				String temp = formatName(columnName);
				String setter = "set" + temp;
				String getter = "get" + temp;
				String dataType = "String";
				
				toString += "\n\t\t\t+ \", " + field + "=\" + " + field + "";
				catalog = meta.getCatalogName(i);
				
				if (type.toLowerCase().contains("varchar")) {
					dataType = "String";
					
				} else if (type.toLowerCase().contains("bool") 
						|| type.toLowerCase().contains("tiny")
						|| type.toLowerCase().contains("bit")) {
					if(meta.isNullable(i) == 1){
						dataType = "Boolean";
					} else {
						dataType = "boolean";
					}
				} else if (type.toLowerCase().contains("int") 
						|| type.toLowerCase().contains("number")) {
					
					if(meta.isNullable(i) == 1){
						dataType = "Integer";
					} else {
						dataType = "int";
					}
				} else if (type.toLowerCase().contains("long")) {
					if(meta.isNullable(i) == 1){
						dataType = "Long";
					} else {
						dataType = "long";
					}
				} else if (type.toLowerCase().contains("date") 
						|| type.toLowerCase().contains("timestamp")) {
					
					if(!imports.contains("java.util.Date")){
						imports += "import java.util.Date;\n";
					}
					dataType = "Date";
					
				} else if (type.toLowerCase().contains("float")) {
					if(meta.isNullable(i) == 1){
						dataType = "Float";
					} else {
						dataType = "float";
					}
				} else if (type.toLowerCase().contains("double")) {
					if(meta.isNullable(i) == 1){
						dataType = "Double";
					} else {
						dataType = "double";
					}
				}  else {
					System.out.println("#############, columnName = " + columnName + ", type = " + type);
					BaseUtils.sleep(1000); System.exit(-9);
				}
				paramsContructor += ", " + dataType + " " + field;
				contructorAssign += "\n\t\tthis." + field + " = " + field + ";";
				
				fields += "\tprivate " + dataType + " " + field + ";\n";
				String antDesc = getMethodAntSrc(columnName, meta.isAutoIncrement(i), meta.getColumnDisplaySize(i), meta.getColumnLabel(i));  
				
				String setgetsTmp =  
						antDesc + "\n" + getSetterSrc(field, dataType, setter, beanMethodTemplate) 
						+ "\n\n"  +
						antDesc + "\n" + getGetterSrc(field, dataType, getter, beanMethodTemplate) + "\n\n";
				
				setgets += setgetsTmp.replace("\n", "\n\t"); 
			}
		} catch (Exception e) {
			Log4jLoader.enableLogAndErrOutput();
			e.printStackTrace();
		} finally {
			releaseAll(rs, stmt, conn);
		}
		setgets = setgets.substring(0, setgets.length() - 4);
		toString = toString.replaceFirst(", ", "");
		paramsContructor = paramsContructor.replaceFirst(", ", "");
		contructorAssign = contructorAssign.replaceFirst("\n", "");
				
		beanTemplate = beanTemplate.replace("@contructorAssign", contructorAssign);
		beanTemplate = beanTemplate.replace("@paramsContructor", paramsContructor);
		beanTemplate = beanTemplate.replace("@catalog", catalog);
		beanTemplate = beanTemplate.replace("@table", tableName);
		beanTemplate = beanTemplate.replace("@key", keyName);
		beanTemplate = beanTemplate.replace("@toString", toString);
		beanTemplate = beanTemplate.replace("@import", imports);
		beanTemplate = beanTemplate.replace("@field", fields);
		beanTemplate = beanTemplate.replace("@className", className);
		beanTemplate = beanTemplate.replace("@set-get", setgets);
		beanTemplate = beanTemplate.replace("@package", "package " + packageName + ";");
		return beanTemplate;
	}

	private static String getSetterSrc(String field, String type, String methodName, String template){
		template = template.replace("@returnType", "void");
		template = template.replace("@methodName", methodName);
		template = template.replace("@params", type + " " + field);
		template = template.replace("@methodBody", "this." + field + " = " + field + ";");
		return template;
	}
	
	private static String getMethodAntSrc(String columnName, boolean auto_increment, int size, String label){
		String str = "name=\"" + columnName + "\"";
		if(auto_increment){
			 str += ", auto_increment=" + auto_increment;
		}
		str += ", size=" + size;
		if(BaseUtils.isNotBlank(label)){
			str += ", label=\"" + label + "\"";
		}
		return "@AntColumn(" + str + ")";
	}
	
	private static String getGetterSrc(String field, String type, String methodName, String template){
		template = template.replace("@returnType", type);
		template = template.replace("@methodName", methodName);
		template = template.replace("@params", "");
		template = template.replace("@methodBody", "return this." + field + ";");
		return template;
	}
	
	public static String formatNameField(String str) {
		str = formatName(str);
		str= str.substring(0, 1).toLowerCase() + str.substring(1);
		return str;
	}
	
	public static String formatName(String str) {
		String index[] = str.split("_");
		String all = "";
		if(!str.toUpperCase().equals(str) && index != null && index.length == 1){
			all = String.valueOf(index[0].charAt(0)).toUpperCase() + index[0].substring(1);
		} else {
			for (int i = 0; index != null && i < index.length; i++) {
				all += String.valueOf(index[i].charAt(0)).toUpperCase() + index[i].substring(1).toLowerCase();
			}
		}
		return all;
	}
	
	private static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} 
		return conn;
	}
	
	private static void releaseAll(Object... objects) {
		for (int i = 0; objects != null && i < objects.length; i++) {
			try {
				if (objects[i] == null) {
					continue;
				}
				if (objects[i] instanceof ResultSet) {
					ResultSet obj = (ResultSet) objects[i];
					obj.close();
				} else if (objects[i] instanceof Statement) {
					Statement obj = (Statement) objects[i];
					obj.close();
				} else if (objects[i] instanceof PreparedStatement) {
					PreparedStatement obj = (PreparedStatement) objects[i];
					obj.close();
				} else if (objects[i] instanceof CallableStatement) {
					CallableStatement obj = (CallableStatement) objects[i];
					obj.close();
				} else if (objects[i] instanceof Connection) {
					Connection obj = (Connection) objects[i];
					obj.close();
				}
			} catch (Exception e) {
				Log4jLoader.enableLogAndErrOutput();
				e.printStackTrace();
			}
		}
	}
}
