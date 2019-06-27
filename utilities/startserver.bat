call setenv.bat


cd %PD%\src

java -cp %CLASSPATH%  -Dfile.encoding=UTF-8 com.ofss.tcp.server.TCPServer %PD%\config\tcp.properties

cd %pwd1%


