package org.thq.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnection {
	Connection makeConnection() throws ClassNotFoundException, SQLException;
}
