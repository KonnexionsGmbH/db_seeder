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

set DB_SEEDER_DBMS_TRINO=no

if ["%DB_SEEDER_DBMS%"] EQU ["mysql_trino"] (
    set DB_SEEDER_DBMS_DB=mysql
    set DB_SEEDER_DBMS_TRINO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle_trino"] (
    set DB_SEEDER_DBMS_DB=oracle
    set DB_SEEDER_DBMS_TRINO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql_trino"] (
    set DB_SEEDER_DBMS_DB=postgresql
    set DB_SEEDER_DBMS_TRINO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlserver_trino"] (
    set DB_SEEDER_DBMS_DB=sqlserver
    set DB_SEEDER_DBMS_TRINO=yes
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Run a single DBMS variation.
echo "--------------------------------------------------------------------------------
echo COMPLETE_RUN                    : %DB_SEEDER_COMPLETE_RUN%
echo DBMS                            : %DB_SEEDER_DBMS%
echo DBMS_DB                         : %DB_SEEDER_DBMS_DB%
echo DBMS_DEFAULT                    : %DB_SEEDER_DBMS_DEFAULT%
echo DBMS_EMBEDDED                   : %DB_SEEDER_DBMS_EMBEDDED%
echo DBMS_TRINO                      : %DB_SEEDER_DBMS_TRINO%
echo DIRECTORY_CATALOG_PROPERTY      : %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%
echo DROP_CONSTRAINTS                : %DB_SEEDER_DROP_CONSTRAINTS%
echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo FILE_JSON_NAME                  : %DB_SEEDER_FILE_JSON_NAME%
echo FILE_STATISTICS_DELIMITER       : %DB_SEEDER_FILE_STATISTICS_DELIMITER%
echo FILE_STATISTICS_HEADER          : %DB_SEEDER_FILE_STATISTICS_HEADER%
echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
echo IMAGE                           : %DB_SEEDER_IMAGE%
echo IMAGE_TRINO                     : %DB_SEEDER_IMAGE_TRINO%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
echo RELEASE                         : %DB_SEEDER_RELEASE%
echo SETUP_DBMS                      : %DB_SEEDER_SETUP_DBMS%
echo VERSION_TRINO                   : %DB_SEEDER_VERSION_TRINO%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST_TRINO           : %DB_SEEDER_CONNECTION_HOST_TRINO%
echo CONNECTION_PORT_TRINO           : %DB_SEEDER_CONNECTION_PORT_TRINO%
echo --------------------------------------------------------------------------------
echo CATALOG                         : %DB_SEEDER_CATALOG%
echo CATALOG_SYS                     : %DB_SEEDER_CATALOG_SYS%
echo CHARACTER_SET_SERVER            : %DB_SEEDER_CHARACTER_SET_SERVER%
echo COLLATION_SERVER                : %DB_SEEDER_COLLATION_SERVER%
echo CONNECTION_HOST                 : %DB_SEEDER_CONNECTION_HOST%
echo CONNECTION_PORT                 : %DB_SEEDER_CONNECTION_PORT%
echo CONNECTION_PREFIX               : %DB_SEEDER_CONNECTION_PREFIX%
echo CONNECTION_SERVICE              : %DB_SEEDER_CONNECTION_SERVICE%
echo CONNECTION_SUFFIX               : %DB_SEEDER_CONNECTION_SUFFIX%
echo CONTAINER_PORT                  : %DB_SEEDER_CONTAINER_PORT%
echo DATABASE                        : %DB_SEEDER_DATABASE%
echo DATABASE_SYS                    : %DB_SEEDER_DATABASE_SYS%
echo DROP_CONSTRAINTS                : %DB_SEEDER_DROP_CONSTRAINTS%
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
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["1"] (
    call scripts\run_db_seeder_create_data.bat
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["2"] (
    call scripts\run_db_seeder_create_data.bat
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    call scripts\run_db_seeder_create_data.bat
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS%"] EQU ["ibmdb2"] (
    rd /q /s %DB_SEEDER_DATABASE% || true
)

docker stop db_seeder_db
docker rm --force db_seeder_db
docker ps -a

docker rmi --force %DB_SEEDER_IMAGE%
if ["%DB_SEEDER_DBMS_TRINO%"] EQU ["yes"] (
    docker rmi --force %DB_SEEDER_IMAGE_TRINO%
)
docker images

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
