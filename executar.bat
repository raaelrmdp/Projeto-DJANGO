@echo off
rem ====================================================================
rem  Sobe o HelpDesk sem Eclipse (Windows).
rem  Precisa de: Java 21+, internet na primeira vez e MySQL ligado.
rem  Baixa Maven e Tomcat 11 para a pasta .ferramentas (dentro do projeto).
rem ====================================================================
setlocal EnableExtensions
cd /d "%~dp0"

set "MAVEN_VER=3.9.9"
set "TOMCAT_VER=11.0.4"
set "FERR=%~dp0.ferramentas"
if not exist "%FERR%" mkdir "%FERR%"

rem ---- 1. Java 21+ ----
where java >nul 2>nul
if errorlevel 1 goto sem_java

set "JV="
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do if not defined JV set "JV=%%v"
set "JV=%JV:"=%"
for /f "delims=." %%m in ("%JV%") do set "JMAJOR=%%m"
if %JMAJOR% LSS 21 goto java_antigo

if defined JAVA_HOME goto java_ok
for /f "delims=" %%i in ('where java') do if not defined JAVA_EXE set "JAVA_EXE=%%i"
for %%i in ("%JAVA_EXE%") do set "JAVA_HOME=%%~dpi.."
:java_ok

rem ---- 2. Maven ----
where mvn >nul 2>nul
if not errorlevel 1 (
    set "MVN=mvn"
    goto maven_ok
)
set "MVN=%FERR%\apache-maven-%MAVEN_VER%\bin\mvn.cmd"
if exist "%MVN%" goto maven_ok
echo Baixando Maven %MAVEN_VER%...
powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VER%/binaries/apache-maven-%MAVEN_VER%-bin.zip' -OutFile '%FERR%\maven.zip'"
if errorlevel 1 goto falha_download
powershell -NoProfile -Command "Expand-Archive -LiteralPath '%FERR%\maven.zip' -DestinationPath '%FERR%' -Force"
del "%FERR%\maven.zip"
:maven_ok

rem ---- 3. Tomcat 11 ----
if defined CATALINA_HOME if exist "%CATALINA_HOME%\bin\catalina.bat" (
    set "TOMCAT=%CATALINA_HOME%"
    goto tomcat_ok
)
set "TOMCAT=%FERR%\apache-tomcat-%TOMCAT_VER%"
if exist "%TOMCAT%\bin\catalina.bat" goto tomcat_ok
echo Baixando Tomcat %TOMCAT_VER%...
powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/tomcat/tomcat-11/v%TOMCAT_VER%/bin/apache-tomcat-%TOMCAT_VER%.zip' -OutFile '%FERR%\tomcat.zip'"
if errorlevel 1 goto falha_download
powershell -NoProfile -Command "Expand-Archive -LiteralPath '%FERR%\tomcat.zip' -DestinationPath '%FERR%' -Force"
del "%FERR%\tomcat.zip"
:tomcat_ok

rem ---- 4. Aviso se o MySQL nao responde ----
powershell -NoProfile -Command "if (-not (Test-NetConnection -ComputerName 127.0.0.1 -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue)) { exit 1 }" >nul 2>nul
if errorlevel 1 (
    echo.
    echo AVISO: nada respondendo na porta 3306. Ligue o MySQL e rode sql\schema.sql.
    echo        Sem o banco, o login vai falhar.
    echo.
)

rem ---- 5. Compila e publica ----
echo Compilando...
call "%MVN%" -q -DskipTests clean package
if errorlevel 1 goto falha_build

if exist "%TOMCAT%\webapps\HelpDesk" rmdir /s /q "%TOMCAT%\webapps\HelpDesk"
if exist "%TOMCAT%\webapps\HelpDesk.war" del "%TOMCAT%\webapps\HelpDesk.war"
copy /y "target\HelpDesk.war" "%TOMCAT%\webapps\" >nul

echo.
echo Abrindo http://localhost:8080/HelpDesk/ em instantes.
echo Para parar o servidor: feche esta janela ou aperte Ctrl+C.
echo.
start "" cmd /c "timeout /t 10 >nul & start http://localhost:8080/HelpDesk/"
call "%TOMCAT%\bin\catalina.bat" run
goto fim

:sem_java
echo Java nao encontrado. Instale o JDK 21 e tente de novo.
goto fim
:java_antigo
echo Java %JV% encontrado, mas o projeto precisa do Java 21 ou mais novo.
goto fim
:falha_download
echo Nao foi possivel baixar (sem internet ou bloqueio da rede).
echo Baixe Maven e Tomcat 11 manualmente e veja o LEIAME.txt.
goto fim
:falha_build
echo A compilacao falhou. Veja as mensagens acima.
goto fim

:fim
pause
endlocal
