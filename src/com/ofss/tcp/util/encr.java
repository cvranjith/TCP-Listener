package com.ofss.tcp.util;

import com.ofss.tcp.util.TCPUtil;

public class encr
{
  public static void main(String[] args) throws Exception
  {
  byte[] l_seed = { 111, 114, 97, 99, 108, 101, 102, 105, 110, 97, 110, 99, 105, 97, 108, 115, 111, 108, 117, 116, 105, 111, 110, 115 };
  TCPUtil l_tcp_util = new TCPUtil();
  String l_user = args[0];
  l_seed = l_user.getBytes();
  System.out.println("Encrypted string : \n" + l_tcp_util.encrypt(l_user.getBytes(), l_seed, args[1], args[2]));
}
}