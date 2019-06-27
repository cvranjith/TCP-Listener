package com.ofss.tcp.util;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import com.ofss.tcp.util.TCPLogger;

public class TCPUtil{

	private static Properties props = new Properties();
	private static PoolDataSource pds;

	public static byte[] do_crypt(byte[] in_str, byte[] in_seed, int operation, String algorithm, String enc_method) throws Exception {
		Cipher l_cipher = Cipher.getInstance(algorithm);
		SecretKeySpec skspec = new SecretKeySpec(in_seed, enc_method);
		byte[] ivBytes = { 111, 114, 97, 99, 108, 101, 102, 105 };
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		l_cipher.init(operation, skspec, iv);
		byte[] ret_array = l_cipher.doFinal(in_str);
		l_cipher = null;
		skspec = null;
		return ret_array;
	}
	public String encrypt(byte[] in_str, byte[] in_seed, String algorithm, String enc_method) throws Exception {
		return new String(Base64.encodeBase64(do_crypt(in_str, in_seed, 1, algorithm, enc_method)));
	}
	private static String decrypt(byte[] in_str, byte[] in_seed, String algorithm, String enc_method) throws Exception {
		return new String(do_crypt(Base64.decodeBase64(in_str), in_seed, 2, algorithm, enc_method));
	}
	private static void createConnectionPool () throws Exception {
		byte[] arrayOfByte = { 111, 114, 97, 99, 108, 101, 102, 105, 110, 97, 110, 99, 105, 97, 108, 115, 111, 108, 117, 116, 105, 111, 110, 115 };
		String encAlgorithm = val("ENC_ALGORITHM");
		String encMethod = val("ENC_METHOD");
		pds = PoolDataSourceFactory.getPoolDataSource();
		pds.setConnectionFactoryClassName(val("DRIVER_CLASS"));
		pds.setURL(decrypt(val("UCP_URL").getBytes(), arrayOfByte, encAlgorithm, encMethod));
		pds.setUser(decrypt(val("DB_USER").getBytes(), arrayOfByte, encAlgorithm, encMethod));
		pds.setPassword(decrypt(val("DB_PWD").getBytes(), arrayOfByte, encAlgorithm, encMethod));
		pds.setInitialPoolSize(Integer.parseInt(val("INITIAL_POOL_SIZE")));
		pds.setMinPoolSize(Integer.parseInt(val("MIN_POOL_SIZE")));
		pds.setMaxPoolSize(Integer.parseInt(val("MAX_POOL_SIZE")));
		TCPLogger.log("createConnectionPool successfully done");
	}
	public static String processDB(String inputStr, String metaData) throws Exception {
		Connection conn = conn = pds.getConnection();
		CallableStatement callStmt = null;
		String outputStr = null;
		callStmt = conn.prepareCall("{call ifpks_msg_router_custom.pr_process(?,?,?)}");
		callStmt.setString(1, inputStr);
		callStmt.setString(2, metaData);
		callStmt.registerOutParameter(3, 12);
		callStmt.executeQuery();
		outputStr = callStmt.getString(3);
		callStmt.close();
		conn.close();
		return outputStr;
	}
	public static void pr_init (String propFile) throws Exception {
		props.load(new BufferedInputStream(new FileInputStream(new File(propFile))));
		TCPLogger.initLog(val("FILE_LOG_REQD"),val("LOG_FILE_PATH"),val("CONSOLE_LOG_REQD"));
		createConnectionPool();
	}
	public static String val(String param) throws Exception {
		return (String)props.get(param);
	}
	public static void closeConnectionPool() throws Exception {
		UniversalConnectionPoolManager mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
		mgr.destroyConnectionPool(pds.getConnectionPoolName());
		TCPLogger.log("Closed PoolDataSource");
	}
}