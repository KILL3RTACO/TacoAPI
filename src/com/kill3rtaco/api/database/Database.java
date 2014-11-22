package com.kill3rtaco.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Represents a MySQL database. This database system is the only thing that
 * still resembles anything of DeityAPI
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Database 2.0
 *
 */
public class Database {
	
	private Connection	conn;
	private String		_sAdd, _dbName, _dbUsr, _dbPass;
	private int			_sPort;
	
	/**
	 * Construct a database object. The required parameters are used to connect
	 * to the actual MySQL database.
	 * 
	 * @param serverAdress
	 *            The MySQL server address
	 * @param serverPort
	 *            The MySQL server port
	 * @param dbName
	 *            The name of the database
	 * @param dbUser
	 *            MySQL username
	 * @param dbPass
	 *            MySQL password
	 * @since TacoAPI/Database 3.0
	 */
	public Database(String serverAdress, int serverPort, String dbName,
			String dbUser, String dbPass) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			_sAdd = serverAdress;
			_sPort = serverPort;
			_dbName = dbName;
			_dbUsr = dbUser;
			_dbPass = dbPass;
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void connect() throws SQLException {
		conn = DriverManager.getConnection(getConnectionString());
	}
	
	private void ensureConnection() {
		try {
			if (!this.conn.isValid(5)) {
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getConnectionString() {
		return "jdbc:mysql://" + _sAdd + ":" + _sPort + "/" + _dbName + "?user=" + _dbUsr + "&password=" + _dbPass;
	}
	
	private PreparedStatement prepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement stmt = this.conn.prepareStatement(sql);
		int counter = 1;
		for (Object param : params) {
			if (param instanceof Integer) {
				stmt.setInt(counter++, (Integer) param);
			} else if (param instanceof Short) {
				stmt.setShort(counter++, (Short) param);
			} else if (param instanceof Long) {
				stmt.setLong(counter++, (Long) param);
			} else if (param instanceof Double) {
				stmt.setDouble(counter++, (Double) param);
			} else if (param instanceof String) {
				stmt.setString(counter++, (String) param);
			} else if (param == null) {
				stmt.setNull(counter++, Types.NULL);
			} else if (param instanceof Object) {
				stmt.setObject(counter++, param);
			} else {
				System.out.printf("Database -> Unsupported data type %s", param.getClass().getSimpleName());
			}
		}
		return stmt;
	}
	
	/**
	 * Read from the database
	 * 
	 * @param sql
	 *            the query to send
	 * @param params
	 *            the parameters to be used
	 * @return a QueryResults object representing the data received
	 * @since TacoAPI/Database 2.0
	 */
	public QueryResults read(String sql, Object... params) {
		ensureConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		QueryResults results = null;
		try {
			stmt = prepareStatement(sql, params);
			rs = stmt.executeQuery();
			if (rs != null) {
				results = new QueryResults(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}
	
	/**
	 * Write to the database
	 * 
	 * @param sql
	 *            the query to send
	 * @param params
	 *            the parameters to be used
	 */
	public void write(String sql, Object... params) {
		try {
			ensureConnection();
			PreparedStatement stmt = prepareStatement(sql, params);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
