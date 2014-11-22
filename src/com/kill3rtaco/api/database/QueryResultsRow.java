package com.kill3rtaco.api.database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Represents a singular row in the results of a query
 * 
 * @author KILL3RTACO
 * @see QueryResults
 *
 */
public class QueryResultsRow {
	
	private Map<String, Object>	_data;
	
	public QueryResultsRow(Map<String, Object> data) {
		_data = data;
	}
	
	/**
	 * Gets a boolean from this row. The following are special cases for when
	 * the result is not instanceof Boolean:
	 * <ul>
	 * <li>If the value is a String, return the result of Boolean.valueOf() on
	 * the string</li>
	 * <li>Otherwise, get the integer value of the column and return true if the
	 * result is equal to 1. Note that this behavior will cause this method to
	 * return true if the value of the column is anywhere between 1 (inclusive)
	 * and 2 (exclusive). For instance, if the column has a value of 1.343, the
	 * integer value will be 1, causing this method to return true. If an
	 * integer value cannot be retrieved, an exception will be thrown</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name in the row
	 * @return true if the value is true, which can be determined from the above
	 *         elaboration
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public boolean getBoolean(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Boolean) {
			return (boolean) obj;
		} else if (obj instanceof String) {
			return Boolean.valueOf((String) obj);
		} else {
			try {
				int i = getInteger(columnName);
				return i == 1;
			} catch (DatabaseException e) {
				throw new DatabaseException("Data in column `" + columnName + "` cannot be  converted/parsed as Boolean");
			}
		}
	}
	
	/**
	 * Get a date from this row. If the column has a data type that results in
	 * String, then Date.valueOf(value) is returned
	 * 
	 * @param columnName
	 *            The name of the column
	 * @return The date value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public Date getDate(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof String) {
			return Date.valueOf((String) obj);
		} else {
			throw new DatabaseException("Data in column `" + columnName + "` is not instanceof Date");
		}
	}
	
	/**
	 * Get a double from this row. The following classes will convert the value
	 * to a double if found:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Double</li>
	 * <li>BigDecimal</li>
	 * <li>Float</li>
	 * <li>Integer</li>
	 * <li>Short</li>
	 * <li>String (Double.parseDouble())</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name
	 * @return The parsed double value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public double getDouble(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Byte) {
			return ((Byte) obj).doubleValue();
		} else if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).doubleValue();
		} else if (obj instanceof Float) {
			return ((Float) obj).doubleValue();
		} else if (obj instanceof Integer) {
			return ((Integer) obj).doubleValue();
		} else if (obj instanceof Short) {
			return ((Short) obj).doubleValue();
		} else if (obj instanceof String) {
			try {
				return Double.parseDouble((String) obj);
			} catch (NumberFormatException e) {
				//continue below and throw exception
			}
		}
		throw new DatabaseException("Data in column `" + columnName + "` cannot be  converted/parsed as a Double");
	}
	
	/**
	 * Get a float from this row. The following classes will convert the value
	 * to a float if found:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Double</li>
	 * <li>BigDecimal</li>
	 * <li>Float</li>
	 * <li>Integer</li>
	 * <li>Short</li>
	 * <li>String (Float.parseFloat())</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name
	 * @return The parsed float value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public float getFloat(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Byte) {
			return ((Byte) obj).floatValue();
		} else if (obj instanceof Double) {
			return ((Double) obj).floatValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).floatValue();
		} else if (obj instanceof Float) {
			return (Float) obj;
		} else if (obj instanceof Integer) {
			return ((Integer) obj).floatValue();
		} else if (obj instanceof Short) {
			return ((Short) obj).floatValue();
		} else if (obj instanceof String) {
			try {
				return Float.parseFloat((String) obj);
			} catch (NumberFormatException e) {
				//continue below and throw exception
			}
		}
		throw new DatabaseException("Data in column `" + columnName + "` cannot be  converted/parsed as a Float");
	}
	
	/**
	 * Get an int (Integer) from this row. The following classes will convert
	 * the value to an int if found:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Double (precision lost)</li>
	 * <li>BigDecimal</li>
	 * <li>Float (precision lost)</li>
	 * <li>Integer</li>
	 * <li>Short</li>
	 * <li>String (Integer.parseInt())</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name
	 * @return The parsed double value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public int getInteger(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Byte) {
			return ((Byte) obj).intValue();
		} else if (obj instanceof Double) {
			return ((Double) obj).intValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).intValue();
		} else if (obj instanceof Float) {
			return ((Float) obj).intValue();
		} else if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof Short) {
			return ((Short) obj).intValue();
		} else if (obj instanceof String) {
			try {
				return Integer.parseInt((String) obj);
			} catch (NumberFormatException e) {
				//continue below and throw exception
			}
		}
		throw new DatabaseException("Data in column `" + columnName + "` cannot be converted/parsed as an Integer");
	}
	
	/**
	 * Get a long from this row. The following classes will convert the value to
	 * a long if found:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Double (precision lost)</li>
	 * <li>BigDecimal</li>
	 * <li>BigInteger</li>
	 * <li>Float (precision lost)</li>
	 * <li>Integer</li>
	 * <li>Long</li>
	 * <li>Short</li>
	 * <li>String (Long.parseLong())</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name
	 * @return The parsed double value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public long getLong(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Byte) {
			return ((Byte) obj).longValue();
		} else if (obj instanceof Double) {
			return ((Double) obj).longValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).longValue();
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).longValue();
		} else if (obj instanceof Float) {
			return ((Float) obj).longValue();
		} else if (obj instanceof Integer) {
			return ((Integer) obj).longValue();
		} else if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof Short) {
			return ((Short) obj).intValue();
		} else if (obj instanceof String) {
			try {
				return Long.parseLong((String) obj);
			} catch (NumberFormatException e) {
				//continue below and throw exception
			}
		}
		throw new DatabaseException("Data in column `" + columnName + "` cannot be converted/parsed as a Long");
	}
	
	/**
	 * Get a short from this row. The following classes will convert the value
	 * to a short if found:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Short</li>
	 * <li>String (Short.parseShort())</li>
	 * </ul>
	 * 
	 * @param columnName
	 *            The column name
	 * @return The parsed double value
	 * @throws DatabaseException
	 *             if the column does not exist or a value cannot be
	 *             assigned/retrieved
	 */
	public short getShort(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		//Cannot convert from Double, BigDecimal, BigInteger, Float, Integer, or Long
		//The MAX_VALUES of these classes far exceeds that of Short.
		if (obj instanceof Byte) {
			return ((Byte) obj).shortValue();
		} else if (obj instanceof Short) {
			return (Short) obj;
		} else if (obj instanceof String) {
			try {
				return Short.parseShort((String) obj);
			} catch (NumberFormatException e) {
				//continue below and throw exception
			}
		}
		throw new DatabaseException("Data in column `" + columnName + "` cannot be converted/parsed as a Short");
	}
	
	/**
	 * Get the String value from this row.
	 * 
	 * @param columnName
	 *            The column name
	 * @return The String if the data type is String, otherwise the result of
	 *         .toString()
	 * @throws DatabaseException
	 *             if the column does not exist
	 */
	public String getString(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}
	
	/**
	 * Get a java.sql.Timestamp value from this row. If the column has a data
	 * type that results in String, then Timestamp.valueOf(value) is returned
	 * 
	 * @param columnName
	 * @return A Timestamp value
	 * @throws DatabaseException
	 *             if the column does not exist or the data cannot be
	 *             assigned/retrieved
	 */
	public Timestamp getTimestamp(String columnName) throws DatabaseException {
		Object obj = getObject(columnName);
		if (obj instanceof Timestamp) {
			return (Timestamp) obj;
		} else if (obj instanceof String) {
			return Timestamp.valueOf((String) obj);
		} else {
			throw new DatabaseException("Data in column `" + columnName + "` cannot be converted/parsed as a Timestamp");
		}
	}
	
	/**
	 * Get a value from this row.
	 * 
	 * @param columnName
	 * @return the value at the specified column
	 * @throws DatabaseException
	 *             If the column does not exist.
	 */
	public Object getObject(String columnName) throws DatabaseException {
		if (!_data.containsKey(columnName)) {
			throw new DatabaseException("Column with the name of '" + columnName + "' not found");
		} else {
			return _data.get(columnName);
		}
	}
	
}
