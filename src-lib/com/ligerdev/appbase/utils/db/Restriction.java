package com.ligerdev.appbase.utils.db;

public class Restriction {

	public static class Compare {
		public static final String DF = "!=";
		public static final String EQ = "=";
		public static final String LT = "<";				 
		public static final String LT_EQ = "<=";		 
		public static final String GT = ">";		 
		public static final String GT_EQ = ">=";		 
		public static final String LK = "LIKE";			 
		public static final String IN = "IN";			 
	}
	
	private String column;
	private String compare;
	private Object value;

	public Restriction(String column, String compare, Object value) {
		super();
		this.column = column;
		this.value = value;
		this.compare = compare;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getCompare() {
		return compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}
}
