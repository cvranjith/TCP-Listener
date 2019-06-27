package com.ofss.tcp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.SocketException;

public class StopTCPServer
{
  public static void main(String[] args) throws Exception
  {
	sendShutdownSignal (args);
	try {
	sendShutdownSignal (args);
	}
    catch (SocketException e){
		System.out.println("Stopped"); 
	}
  }
  private static void sendShutdownSignal (String[] args) throws Exception
  {
	Socket s = null;
    s = new Socket(args[0], Integer.parseInt(args[1]));
	String str = "SHUTDOWN\n";
	DataInputStream in = new DataInputStream(s.getInputStream());
	DataOutputStream out = new DataOutputStream(s.getOutputStream());
	ByteArrayOutputStream bop = new ByteArrayOutputStream();
    bop.write( str.getBytes());
    out.write( bop.toByteArray(), 0, str.getBytes().length);
    out.flush();
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    while (true) {
      int b = in.read();
      if (b == 0x0A) {
        break;
      }
      buffer.write(b);
    }
    String data = new String(buffer.toByteArray(), "UTF-8");
	//System.out.println("Received: " + data); 
	s.close();
	return;
  }
}
