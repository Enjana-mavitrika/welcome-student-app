@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  geosparql-fuseki startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and GEOSPARQL_FUSEKI_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\geosparql-fuseki-1.1.2.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.26.jar;%APP_HOME%\lib\geosparql-jena-1.1.2.jar;%APP_HOME%\lib\rdf-tables-1.0.4.jar;%APP_HOME%\lib\jena-tdb-3.10.0.jar;%APP_HOME%\lib\jena-tdb2-3.10.0.jar;%APP_HOME%\lib\jena-dboe-trans-data-3.10.0.jar;%APP_HOME%\lib\jena-dboe-transaction-3.10.0.jar;%APP_HOME%\lib\jena-dboe-index-3.10.0.jar;%APP_HOME%\lib\jena-dboe-base-3.10.0.jar;%APP_HOME%\lib\jena-arq-3.11.0.jar;%APP_HOME%\lib\jena-fuseki-main-3.11.0.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\jena-core-3.11.0.jar;%APP_HOME%\lib\jena-base-3.11.0.jar;%APP_HOME%\lib\jena-shaded-guava-3.11.0.jar;%APP_HOME%\lib\libthrift-0.12.0.jar;%APP_HOME%\lib\jsonld-java-0.12.3.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.26.jar;%APP_HOME%\lib\jena-fuseki-access-3.11.0.jar;%APP_HOME%\lib\jena-cmds-3.11.0.jar;%APP_HOME%\lib\expiring-map-1.0.2.jar;%APP_HOME%\lib\jena-iri-3.11.0.jar;%APP_HOME%\lib\jena-fuseki-core-3.11.0.jar;%APP_HOME%\lib\slf4j-api-1.7.26.jar;%APP_HOME%\lib\jcommander-1.72.jar;%APP_HOME%\lib\commons-collections4-4.2.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\httpclient-cache-4.5.5.jar;%APP_HOME%\lib\httpclient-4.5.6.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\opencsv-3.9.jar;%APP_HOME%\lib\commons-lang3-3.5.jar;%APP_HOME%\lib\jetty-xml-9.4.12.v20180830.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\sis-referencing-0.8.jar;%APP_HOME%\lib\jts-core-1.16.1.jar;%APP_HOME%\lib\jdom2-2.0.6.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\commons-cli-1.4.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\httpcore-4.4.10.jar;%APP_HOME%\lib\commons-fileupload-1.4.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\jetty-servlets-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-servlet-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-security-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-server-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-http-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-io-9.4.12.v20180830.jar;%APP_HOME%\lib\jetty-util-9.4.12.v20180830.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\sis-metadata-0.8.jar;%APP_HOME%\lib\sis-utility-0.8.jar;%APP_HOME%\lib\geoapi-3.0.1.jar;%APP_HOME%\lib\unit-api-1.0.jar;%APP_HOME%\lib\commons-beanutils-1.9.3.jar;%APP_HOME%\lib\commons-csv-1.5.jar;%APP_HOME%\lib\commons-compress-1.18.jar;%APP_HOME%\lib\collection-0.7.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\micrometer-registry-prometheus-1.1.3.jar;%APP_HOME%\lib\micrometer-core-1.1.3.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\jetty-continuation-9.4.12.v20180830.jar;%APP_HOME%\lib\HdrHistogram-2.1.9.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\simpleclient_common-0.5.0.jar;%APP_HOME%\lib\simpleclient-0.5.0.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar

@rem Execute geosparql-fuseki
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GEOSPARQL_FUSEKI_OPTS%  -classpath "%CLASSPATH%" io.github.galbiston.geosparql_fuseki.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GEOSPARQL_FUSEKI_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%GEOSPARQL_FUSEKI_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
