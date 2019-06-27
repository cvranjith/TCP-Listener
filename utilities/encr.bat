call setenv.bat


cd %PD%\src

set /p str="Enter string: "

java -cp %CLASSPATH% com.ofss.tcp.util.encr %str% "DESede/CBC/PKCS5Padding" "DESede"

cd %pwd1%
