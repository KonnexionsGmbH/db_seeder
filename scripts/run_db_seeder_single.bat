@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_single.bat: Run a single DBMS variation.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_DBMS_DB=%DB_SEEDER_DBMS%

set DB_SEEDER_DBMS_EMBEDDED=no

if ["%DB_SEEDER_DBMS%"] EQU ["derby_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqldb_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlite"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

set DB_SEEDER_DBMS_PRESTO=no

if ["%DB_SEEDER_DBMS%"] EQU ["mysql_presto"] (
    set DB_SEEDER_DBMS_DB=mysql
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle_presto"] (
    set DB_SEEDER_DBMS_DB=oracle
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql_presto"] (
    set DB_SEEDER_DBMS_DB=postgresql
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlserver_presto"] (
    set DB_SEEDER_DBMS_DB=sqlserver
    set DB_SEEDER_DBMS_PRESTO=yes
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Run a single DBMS variation.
echo "--------------------------------------------------------------------------------
echo COMPLETE_RUN                    : %DB_SEEDER_COMPLETE_RUN%
echo DBMS                            : %DB_SEEDER_DBMS%
echo DBMS_DB                         : %DB_SEEDER_DBMS_DB%
echo DBMS_DEFAULT                    : %DB_SEEDER_DBMS_DEFAULT%
echo DBMS_EMBEDDED                   : %DB_SEEDER_DBMS_EMBEDDED%
echo DBMS_PRESTO                     : %DB_SEEDER_DBMS_PRESTO%
echo DIRECTORY_CATALOG_PROPERTY      : %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%
echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo FILE_JSON_NAME                  : %DB_SEEDER_FILE_JSON_NAME%
echo FILE_STATISTICS_DELIMITER       : %DB_SEEDER_FILE_STATISTICS_DELIMITER%
echo FILE_STATISTICS_HEADER          : %DB_SEEDER_FILE_STATISTICS_HEADER%
echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
echo GLOBAL_CONNECTION_HOST          : %DB_SEEDER_GLOBAL_CONNECTION_HOST%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
echo RELEASE                         : %DB_SEEDER_RELEASE%
echo SETUP_DBMS                      : %DB_SEEDER_SETUP_DBMS%
echo VERSION_PRESTO                  : %DB_SEEDER_VERSION_PRESTO%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST_PRESTO          : %DB_SEEDER_CONNECTION_HOST_PRESTO%
echo CONNECTION_PORT_PRESTO          : %DB_SEEDER_CONNECTION_PORT_PRESTO%
echo --------------------------------------------------------------------------------
echo CATALOG                         : %DB_SEEDER_CATALOG%
echo CATALOG_SYS                     : %DB_SEEDER_CATALOG_SYS%
echo CONNECTION_HOST                 : %DB_SEEDER_CONNECTION_HOST%
echo CONNECTION_PORT                 : %DB_SEEDER_CONNECTION_PORT%
echo CONNECTION_PREFIX               : %DB_SEEDER_CONNECTION_PREFIX%
echo CONNECTION_SERVICE              : %DB_SEEDER_CONNECTION_SERVICE%
echo CONNECTION_SUFFIX               : %DB_SEEDER_CONNECTION_SUFFIX%
echo CONTAINER_PORT                  : %DB_SEEDER_CONTAINER_PORT%
echo DATABASE                        : %DB_SEEDER_DATABASE%
echo DATABASE_SYS                    : %DB_SEEDER_DATABASE_SYS%
echo PASSWORD                        : %DB_SEEDER_PASSWORD%
echo PASSWORD_SYS                    : %DB_SEEDER_PASSWORD_SYS%
echo SCHEMA                          : %DB_SEEDER_SCHEMA%
echo USER                            : %DB_SEEDER_USER%
echo USER_SYS                        : %DB_SEEDER_USER_SYS%
echo VERSION                         : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================
    
if ["%DB_SEEDER_SETUP_DBMS%"] EQU ["yes"] (
    call scripts\run_db_seeder_setup_dbms.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)
    
if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["1"] (
    call scripts\run_db_seeder_create_data.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)
    
if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["2"] (
    call scripts\run_db_seeder_create_data.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    call scripts\run_db_seeder_create_data.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
