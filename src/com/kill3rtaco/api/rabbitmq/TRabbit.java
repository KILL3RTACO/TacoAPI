package com.kill3rtaco.api.rabbitmq;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.api.bukkit.util.ChatUtils;
import com.kill3rtaco.api.command.TCommandManager;
import com.kill3rtaco.api.util.LibraryLoader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class TRabbit {
	
	public static final String					COMMAND_TRACK		= "tacomq-command";
	public static final String					RESPONSE_TRACK		= "tacomq-response";
	private static final List<TCommandManager>	_commandManagers	= new ArrayList<TCommandManager>();
	private static ConnectionFactory			_factory;
	
	public static void init(File rabbitClientJar, final String host) {
		if (rabbitClientJar != null && rabbitClientJar.exists())
			LibraryLoader.addJarToClassPath(rabbitClientJar);
		_factory = new ConnectionFactory();
		_factory.setHost(host);
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				Connection conn = null;
				Channel channel = null;
				QueueingConsumer consumer;
				try {
					conn = _factory.newConnection();
					channel = conn.createChannel();
					channel.queueDeclare(COMMAND_TRACK, false, false, false, null);
					
					consumer = new QueueingConsumer(channel);
					channel.basicConsume(COMMAND_TRACK, true, consumer);
				} catch (Exception e) {
					e.printStackTrace();
					closeConnections(conn, channel);
					return;
				}
				
				//main loop
				while (true) {
					QueueingConsumer.Delivery delivery;
					try {
						delivery = consumer.nextDelivery();
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
					String message = new String(delivery.getBody());
					
//					System.out.println("[TacoAPI/RabbitMQ/CommandTrack] [x] Message received: " + message);
					
					String[] split = message.split("\\s+");
					if (split.length == 0)
						return;
					
					split = ChatUtils.removeFirstArg(split);
					
					if (split.length == 0)
						return;
					
					String cmd = split[0];
					String[] args = ChatUtils.removeFirstArg(split);
					dispatchCommand(cmd, args);
					
				}
				closeConnections(conn, channel);
			}
			
		})).start();
	}
	
	private static void closeConnections(Connection conn, Channel channel) {
		try {
			if (channel != null)
				channel.close();
			if (conn != null)
				conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a message to the RabbitMQ server. Note that the message is sent on
	 * the command queue.
	 * 
	 * @param message
	 *            the message (command) to send
	 * @since TacoAPI/RabbitMQ 1.0
	 */
	public static void sendCommandMessage(String message) {
		Connection conn = null;
		Channel channel = null;
		try {
			conn = _factory.newConnection();
			channel = conn.createChannel();
			channel.queueDeclare(COMMAND_TRACK, false, false, false, null);
			channel.basicPublish("", COMMAND_TRACK, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnections(conn, channel);
		}
		
	}
	
	public static void registerCommandManager(TCommandManager manager) {
		_commandManagers.add(manager);
	}
	
	private static void dispatchCommand(String cmd, String[] args) {
		for (TCommandManager m : _commandManagers) {
			if (m.dispatchCommand(cmd, args))
				return;
		}
	}
	
}
