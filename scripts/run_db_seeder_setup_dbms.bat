@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_dbms.bat: Setup database files or a Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setting up the DBMS.
echo --------------------------------------------------------------------------------
echo DBMS_DB                   : %DB_SEEDER_DBMS_DB%
echo DBMS_EMBEDDED             : %DB_SEEDER_DBMS_EMBEDDED%
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo VERSION                   : %DB_SEEDER_VERSION%

if ["%DB_SEEDER_DBMS_EMBEDDED%"] == ["no"] (
    echo Docker stop/rm db_seeder_db ................................ before:
    echo "1 docker ps -a"
    docker ps -a
    if ["%DB_SEEDER_DBMS_DB%"] == ["exasol"] (
        docker ps | find "db_seeder_db" && docker exec -ti db_seeder_db dwad_client stop-wait DB1
    )
    docker ps | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    echo ............................................................. after:
    docker ps -a
)

if ["%DB_SEEDER_DBMS_EMBEDDED%"] == ["yes"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS_DB%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS_DB%"] == ["derby"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS_DB%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS_DB%"] == ["h2"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS_DB%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS_DB%"] == ["ibmdb2"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS_DB%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS_EMBEDDED%"] EQU ["no"] (
    lib\Gammadyne\timer.exe /reset
    lib\Gammadyne\timer.exe /q
    
    call scripts\run_db_seeder_setup_%DB_SEEDER_DBMS_DB%.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    docker ps
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
