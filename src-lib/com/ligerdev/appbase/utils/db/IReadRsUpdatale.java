package com.ligerdev.appbase.utils.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class IReadRsUpdatale {

	public abstract boolean found(String transid, int readIndex, ResultSet rs) throws SQLException;
	public abstract void notFound(String transid) throws SQLException;
	public abstract boolean whereRs(ResultSet rs) throws SQLException;
}
