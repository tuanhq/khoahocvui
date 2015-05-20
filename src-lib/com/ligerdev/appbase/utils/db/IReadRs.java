package com.ligerdev.appbase.utils.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class IReadRs {

	public abstract boolean found(String transid, int readIndex, ResultSet rs) throws SQLException;
	public abstract void notFound(String transid) throws SQLException;
	
}
