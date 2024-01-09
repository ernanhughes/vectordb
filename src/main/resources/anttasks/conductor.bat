SET CONDUCTOR_VER=3.11.2
SET PORT=8888
if not exist "conductor-server-%CONDUCTOR_VER%-boot.jar" (
   curl https://repo1.maven.org/maven2/com/netflix/conductor/conductor-server/%CONDUCTOR_VER%/conductor-server-%CONDUCTOR_VER%-boot.jar --output conductor-server-%CONDUCTOR_VER%-boot.jar 
)
java -jar conductor-server-%CONDUCTOR_VER%-boot.jar --server.port=%PORT%
