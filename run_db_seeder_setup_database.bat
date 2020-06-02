@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_database.bat: Setup a database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle
set DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=no

set DB_SEEDER_VERSION_MYSQL=8.0.20
set DB_SEEDER_VERSION_ORACLE=db_19_3_ee

if ["%1"] EQU [""] (
    set /P DB_SEEDER_DATABASE_BRAND="Enter the desired database brand (mysql/oracle) [default: %DB_SEEDER_DATABASE_BRAND_DEFAULT%] "

    if ["%DB_SEEDER_DATABASE_BRAND%"] EQU [""] (
        set DB_SEEDER_DATABASE_BRAND=%DB_SEEDER_DATABASE_BRAND_DEFAULT%
    )
) else (
    set DB_SEEDER_DATABASE_BRAND=%1
)

if ["%2"] EQU [""] (
    set /P DB_SEEDER_DELETE_EXISTING_CONTAINER="Delete the existing Docker container DB_SEEDER_DB (yes/no) [default: %DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%] "

    if ["%DB_SEEDER_DELETE_EXISTING_CONTAINER%"] EQU [""] (
        set DB_SEEDER_DELETE_EXISTING_CONTAINER=%DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%
    )
) else (
    set DB_SEEDER_DELETE_EXISTING_CONTAINER=%2
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a database Docker container.
echo --------------------------------------------------------------------------------
echo DATABASE_BRAND            : %DB_SEEDER_DATABASE_BRAND%
echo DELETE_EXISTING_CONTAINER : %DB_SEEDER_DELETE_EXISTING_CONTAINER%
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DATABASE_BRAND%" == "mysql"] (
    echo VERSION_MYSQL             : %DB_SEEDER_VERSION_MYSQL%
)
if ["%DB_SEEDER_DATABASE_BRAND%" == "oracle"] (
    echo VERSION_ORACLE            : %DB_SEEDER_VERSION_ORACLE%
)
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

if ["%DB_SEEDER_DELETE_EXISTING_CONTAINER%" NEQ "no"] (
    echo Docker stop/rm db_seeder_db
    docker stop db_seeder_db
    docker rm -f db_seeder_db
)

lib\Gammadyne\timer.exe /reset
lib\Gammadyne\timer.exe /q

rem ------------------------------------------------------------------------------
rem MySQL Database                                  https://hub.docker.com/_/mysql
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DATABASE_BRAND%" == "mysql"] (
    echo MySQL Database
    echo --------------------------------------------------------------------------------
    echo Docker create db_seeder_db (MySQL %DB_SEEDER_VERSION_MYSQL%)
    docker create -e MYSQL_ROOT_PASSWORD=mysql --name db_seeder_db -p 3306:3306 mysql:%DB_SEEDER_VERSION_MYSQL%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )

    echo Docker start db_seeder_db (MySQL %DB_SEEDER_VERSION_MYSQL%) ...
    docker start db_seeder_db
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )

    for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
    echo DOCKER MySQL Database was ready in %CONSUMED%
)

rem ------------------------------------------------------------------------------
rem Oracle Database.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DATABASE_BRAND%" == "oracle"] (
    echo Oracle Database
    echo --------------------------------------------------------------------------------
    echo Docker create db_seeder_db (Oracle %DB_SEEDER_VERSION_ORACLE%)
    docker create -e ORACLE_PWD=oracle --name db_seeder_db -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/%DB_SEEDER_VERSION_ORACLE%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )

    echo Docker start db_seeder_db (Oracle %DB_SEEDER_VERSION_ORACLE%) ...
    docker start db_seeder_db
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )

    for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
    echo DOCKER Oracle Database was ready in %CONSUMED%

    :check_health_status:
    mkdir tmp >nul 2>&1
    docker inspect -f {{.State.Health.Status}} db_seeder_db > tmp\docker_health_status.txt
    set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
    if ["%DOCKER_HEALTH_STATUS%" NEQ "healthy"] (
        docker ps --filter "name=db_seeder_db"
        ping -n 60 127.0.0.1 >nul
        goto :check_health_status
    )
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================

exit %ERRORLEVEL%
