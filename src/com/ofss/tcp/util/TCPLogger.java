package com.ofss.tcp.util;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.Date;
import java.util.logging.Level;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

public class TCPLogger {
	private static final Logger logger = Logger.getLogger("");
	private static String dbgMode = "N";
	private static String fileLog = "N";
	private static String consoleLog = "N";
	public static void initLog (String fileLogReqd, String logPath, String consoleLogReqd) throws Exception{
		fileLog = fileLogReqd;
		consoleLog = consoleLogReqd;
		if (fileLogReqd.equalsIgnoreCase("Y")){
			Handler[] handlers = logger.getHandlers();
			for(Handler handler : handlers) {
				logger.removeHandler(handler);
			}
			FileHandler handler = new FileHandler(logPath + "TCPLog_" + new SimpleDateFormat("YYYY.MMMM.dd.HH.mm.ss").format(new Date()) + ".log");
			logger.addHandler(handler);
			handler.setFormatter(new LogFormatter());
			dbgMode = "Y";
		}
		if (consoleLogReqd.equalsIgnoreCase("Y")){
			dbgMode = "Y";
		}
	}
	private static class LogFormatter extends Formatter {
		public String format(LogRecord record) {
			StringBuffer sb = new StringBuffer();
			sb.append(record.getMessage() + "\n");
			return sb.toString();
		}
	}
	public static void log (String msg, Exception ex){
		try {
			if (dbgMode.equalsIgnoreCase("Y")) {
				String stackTrace = "";
				if (ex != null) {
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					ex.printStackTrace(printWriter);
					stackTrace = "\n" + writer.toString();
				}
				String tmpMsg = "[" + new SimpleDateFormat("YYYY.MMMM.dd.HH:mm:ss").format(new Date()) + "] " + msg + stackTrace;
				if (fileLog.equalsIgnoreCase("Y")) {
					logger.info(tmpMsg);
				}
				if (consoleLog.equalsIgnoreCase("Y")) {
					System.out.println(tmpMsg);
				}
			}
		}catch (Exception e) {e.printStackTrace();}
	}
	public static void log (String msg){
		log(msg,null);
	}
}
