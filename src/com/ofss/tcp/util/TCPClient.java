package com.ofss.tcp.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

public class TCPClient
{
  public static void main(String[] args) throws Exception
  {
    Socket s = null;
      s = new Socket(args[0], Integer.parseInt(args[1]));
	  System.out.println("new socket");
	  
	  
	  
	File fileDir = new File(args[2]);
	BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
	String str;
	str = fin.readLine();
	fin.close();
	
	str = str + "\n";
	
      DataInputStream in = new DataInputStream(s.getInputStream());
      DataOutputStream out = new DataOutputStream(s.getOutputStream());
	  System.out.println("Sending ");
      //out.writeUTF(args[2]);
	  //out.writeUTF(str);
	  
	  
	ByteArrayOutputStream bop = new ByteArrayOutputStream();
    bop.write( str.getBytes());
    out.write( bop.toByteArray(), 0, str.getBytes().length);
    out.flush();
	
	  
      //String data = in.readUTF();
	  
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    while (true) {
      int b = in.read();
      if (b == 0x0A) {
        break;
      }
      buffer.write(b);
    }
    String data = new String(buffer.toByteArray(), "UTF-8");

	
      System.out.println("Received: " + data); 
	  s.close();
	  return;
  }
}
