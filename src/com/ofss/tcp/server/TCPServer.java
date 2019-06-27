package com.ofss.tcp.server;

import java.net.ServerSocket;
import java.net.Socket;
import com.ofss.tcp.util.TCPLogger;
import com.ofss.tcp.util.TCPUtil;
import com.ofss.tcp.handler.MsgRequestHandler;

public class TCPServer {
	private static String running;
	private static ServerSocket listenSocket;

	public static void main(String[] args) throws Exception{
		System.out.println("tcp_server start");
		TCPUtil.pr_init(args[0]);
		TCPLogger.log("Starting Server");
		listenSocket = new ServerSocket(Integer.parseInt(TCPUtil.val("LISTEN_PORT")), 25);
		TCPLogger.log("Server is now listening");
		running = "Y";
		MsgRequestHandler msgRequestHandler;
		for (;;)
		{
			Socket clientSocket = listenSocket.accept();
			TCPLogger.log("Listen socket " + running);
			if (running.equalsIgnoreCase("N")) {
				TCPLogger.log("Stopping Server");
				listenSocket.close();
				TCPUtil.closeConnectionPool();
				break;
			}
			msgRequestHandler = new MsgRequestHandler(clientSocket);
		}
	}
	public static void stopServer() throws Exception {
		TCPLogger.log("Shutdown Signal received");
		running = "N";
	}
}
