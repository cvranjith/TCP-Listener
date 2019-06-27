package com.ofss.tcp.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import com.ofss.tcp.util.TCPUtil;
import com.ofss.tcp.util.TCPLogger;
import com.ofss.tcp.server.TCPServer;


public class MsgRequestHandler extends Thread{
	Socket clientSocket;
	public MsgRequestHandler(Socket aClientSocket) throws Exception{
		TCPLogger.log("MsgRequestHandler");
		this.clientSocket = aClientSocket;
		start();
	}
	public void run() {
		try{
			TCPLogger.log("MsgRequestHandler Thread");
			DataInputStream in = new DataInputStream(this.clientSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());

			String metaData = this.clientSocket.getInetAddress() + ":" + this.clientSocket.getPort();
			TCPLogger.log("Client Address :" + metaData);
			for (;;){
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				while (true) {
					int b = in.read();
					if (b < 0) {
						TCPLogger.log("Data truncated");
						throw new SocketException();
					}
					if (b == 0x0A) {
						break;
					}
					buffer.write(b);
				}
				String inputStr = new String(buffer.toByteArray(), "UTF-8");
				String outputStr = null;
				TCPLogger.log("Client sent :" + inputStr + ":");
				if (inputStr.equalsIgnoreCase("SHUTDOWN")) {
					TCPLogger.log("Sending Shutdown Signal");
					TCPServer.stopServer();
				}
				else {
					try {
						outputStr = TCPUtil.processDB(inputStr, metaData);
					}
					catch (Exception e) {
						outputStr = "Error in DB processing :" + e.getMessage();
						TCPLogger.log(outputStr,e);
					}
				}
				TCPLogger.log("Output :" + outputStr + ":"); 
				outputStr = outputStr + "\n";
				out.write(outputStr.getBytes());
			}
		}
		catch (SocketException e){
			TCPLogger.log("MsgRequestHandler Client closed the socket"); 
		}
		catch (Exception e){
			TCPLogger.log("MsgRequestHandler Thread Error :" + e.getMessage(),e); 
		}
		finally{
			try{
				TCPLogger.log("Finising message Handler"); 
				if (this.clientSocket != null){
					TCPLogger.log("closing client socket"); 
					this.clientSocket.close();
					this.clientSocket = null;
				}
			}
			catch (Exception e){
				TCPLogger.log("MsgRequestHandler Socket Close failed :" + e.getMessage(),e); 
			}
		}
	}
}
