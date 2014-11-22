package com.kill3rtaco.api.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents the results of query sent to the MySQL server
 * 
 * @author KILL3RTACO
 *
 */
public class QueryResults implements Iterable<QueryResultsRow> {
	
	private ArrayList<QueryResultsRow>	values;
	
	public QueryResults(ResultSet set) {
		values = new ArrayList<QueryResultsRow>();
		ArrayList<String> columns = new ArrayList<String>();
		try {
			for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
				columns.add(set.getMetaData().getColumnName(i));
			}
			while (set.next()) {
				Map<String, Object> data = new HashMap<String, Object>();
				for (String column : columns) {
					data.put(column, set.getObject(column));
				}
				values.add(new QueryResultsRow(data));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a boolean from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the boolean found
	 * @throws DatabaseException
	 */
	public boolean getBoolean(int index, String columnName) throws DatabaseException {
		return values.get(index).getBoolean(columnName);
	}
	
	/**
	 * Gets a double from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the double found
	 * @throws DatabaseException
	 */
	public double getDouble(int index, String columnName) throws DatabaseException {
		return values.get(index).getDouble(columnName);
	}
	
	/**
	 * Gets a float from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the float found
	 * @throws DatabaseException
	 */
	public float getFloat(int index, String columnName) throws DatabaseException {
		return values.get(index).getFloat(columnName);
	}
	
	/**
	 * Gets an int from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the int found
	 * @throws DatabaseException
	 */
	public int getInteger(int index, String columnName) throws DatabaseException {
		return values.get(index).getInteger(columnName);
	}
	
	/**
	 * Gets a long from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the long found
	 * @throws DatabaseException
	 */
	public long getLong(int index, String columnName) throws DatabaseException {
		return values.get(index).getLong(columnName);
	}
	
	/**
	 * Gets an Object from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the Object found
	 * @throws DatabaseException
	 */
	public Object getObject(int index, String columnName) throws DatabaseException {
		return values.get(index).getObject(columnName);
	}
	
	/**
	 * Gets a short from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the short found
	 * @throws DatabaseException
	 */
	public short getShort(int index, String columnName) throws DatabaseException {
		return values.get(index).getShort(columnName);
	}
	
	/**
	 * Gets a String from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the String found
	 * @throws DatabaseException
	 */
	public String getString(int index, String columnName) throws DatabaseException {
		return values.get(index).getString(columnName);
	}
	
	/**
	 * Gets a Timestamp from the query.
	 * 
	 * @param index
	 *            the row
	 * @param columnName
	 *            the column
	 * @return the String found
	 * @throws DatabaseException
	 */
	public Timestamp getTimestamp(int index, String columnName) throws DatabaseException {
		return values.get(index).getTimestamp(columnName);
	}
	
	/**
	 * Test whether this query has rows or not
	 * 
	 * @return true if the row count is > 0
	 */
	public boolean hasRows() {
		return rowCount() > 0;
	}
	
	/**
	 * Gets the amount of rows in the query
	 * 
	 * @return the amount of rows
	 */
	public int rowCount() {
		return values.size();
	}
	
	@Override
	public Iterator<QueryResultsRow> iterator() {
		return values.iterator();
	}
	
}
