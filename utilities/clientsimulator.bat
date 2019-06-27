call setenv.bat


cd %PD%\src

set /p ip="Enter ip: "
set /p port="Enter port: "
set /p str="Enter string: "

java -cp %CLASSPATH% -Dfile.encoding=UTF-8 com.ofss.tcp.util.TCPClient %ip% %port% %str%


cd %pwd1%
