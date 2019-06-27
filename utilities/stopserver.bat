call setenv.bat

set /p ip="Enter ip: "
set /p port="Enter port: "

cd %PD%\src

java -cp %CLASSPATH%  com.ofss.tcp.server.StopTCPServer %ip% %port%

cd %pwd1%

