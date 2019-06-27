call setenv.bat


cd %PD%\src

%JH%\javac %PD%\src\com\ofss\tcp\util\TCPLogger.java
%JH%\javac %PD%\src\com\ofss\tcp\util\TCPUtil.java
%JH%\javac %PD%\src\com\ofss\tcp\util\encr.java
%JH%\javac %PD%\src\com\ofss\tcp\util\TCPClient.java
%JH%\javac %PD%\src\com\ofss\tcp\handler\MsgRequestHandler.java
%JH%\javac %PD%\src\com\ofss\tcp\server\TCPServer.java
%JH%\javac %PD%\src\com\ofss\tcp\server\StopTCPServer.java



cd %pwd1%

