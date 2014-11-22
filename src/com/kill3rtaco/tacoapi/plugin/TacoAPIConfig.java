package com.kill3rtaco.tacoapi.plugin;

import java.io.File;
import java.util.UUID;

import com.kill3rtaco.api.config.yml.YamlDocument;

public class TacoAPIConfig extends YamlDocument {
	
	private static final String		MYSQL_SERVER_ADDRESS		= "mysql.server.adress";
	private static final String		MYSQL_SERVER_PORT			= "mysql.server.port";
	private static final String		MYSQL_DB_NAME				= "mysql.database.name";
	private static final String		MYSQL_DB_USR				= "mysql.database.user";
	private static final String		MYSQL_DB_PASS				= "mysql.database.pass";
	private static final String		RABBITMQ_HOST				= "rabbitmq.host";
	private static final String		SERVER_ID					= "server-id";
	
	private static final String		USE_METRICS					= "metrics.use";
	
	private static final String		DEF_MYSQL_SERVER_ADDRESS	= "localhost";
	private static final int		DEF_MYSQL_SERVER_PORT		= 3306;
	private static final String		DEF_MYSQL_DB_NAME			= "minecraft";
	private static final String		DEF_MYSQL_DB_USER			= "root";
	private static final String		DEF_MYSQL_DB_PASS			= "root";
	private static final String		DEF_RABBITMQ_HOST			= "localhost";
//	private static final String		DEF_SERVER_ID				= "main";
	
	private static final boolean	DEF_USE_METRICS				= true;
	
	public TacoAPIConfig() {
		super(new File(TacoAPIPlugin.plugin.getDataFolder() + "/config.yml"));
		setDefault(MYSQL_SERVER_ADDRESS, DEF_MYSQL_SERVER_ADDRESS);
		setDefault(MYSQL_SERVER_PORT, DEF_MYSQL_SERVER_PORT);
		setDefault(MYSQL_DB_NAME, DEF_MYSQL_DB_NAME);
		setDefault(MYSQL_DB_USR, DEF_MYSQL_DB_USER);
		setDefault(MYSQL_DB_PASS, DEF_MYSQL_DB_PASS);
		setDefault(USE_METRICS, DEF_USE_METRICS);
		setDefault(RABBITMQ_HOST, DEF_RABBITMQ_HOST);
		setDefault(SERVER_ID, UUID.randomUUID().toString());
	}
	
	public String getServerId() {
		return getString(SERVER_ID);
	}
	
	public String getMySqlServerAddress() {
		return getString(MYSQL_SERVER_ADDRESS);
	}
	
	public int getMySqlServerPort() {
		return getInt(MYSQL_SERVER_PORT);
	}
	
	public String getDatabaseName() {
		return getString(MYSQL_DB_NAME);
	}
	
	public String getDatabaseUsername() {
		return getString(MYSQL_DB_USR);
	}
	
	public String getDatabasePassword() {
		return getString(MYSQL_DB_PASS);
	}
	
	public String getRabbitMqHost() {
		return getString(RABBITMQ_HOST);
	}
	
	public boolean useMetrics() {
		return getBoolean(USE_METRICS);
	}
}
