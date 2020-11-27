@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_generate_schema.bat: Generation of database schema.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties

rem set DB_SEEDER_FILE_JSON_NAME=resources\json\db_seeder_schema.syntax.json

set DB_SEEDER_JAVA_CLASSPATH=".;lib/*;JAVA_HOME/lib"
set DB_SEEDER_RELEASE=2.6.2

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Generation of database schema.
echo --------------------------------------------------------------------------------
echo FILE_CONFIGURATION_NAME : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo FILE_JSON_NAME          : %DB_SEEDER_FILE_JSON_NAME%
echo HOME_ECLIPSE            : %HOME_ECLIPSE%
echo JAVA_CLASSPATH          : %DB_SEEDER_JAVA_CLASSPATH%
echo RELEASE                 : %DB_SEEDER_RELEASE%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

java -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.SchemaBuilder %DB_SEEDER_RELEASE%
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

if exist eclipse_workspace\ rmdir /q /s eclipse_workspace

mkdir eclipse_workspace

%HOME_ECLIPSE%\eclipse -nosplash ^
                       -data eclipse_workspace ^
                       -application org.eclipse.jdt.core.JavaCodeFormatter ^
                       -config src\main\resources\org.eclipse.jdt.core.prefs ^
                       -quiet src\main\java\ch\konnexions\db_seeder\generated\ ^
                       -vmargs -Dfile.encoding=UTF-8
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

call gradle init
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

call gradle clean
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

call gradle copyJarToLib
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
