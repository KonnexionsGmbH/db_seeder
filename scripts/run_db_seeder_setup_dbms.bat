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
echo DB Seeder - setting up the DBMS.
echo --------------------------------------------------------------------------------
echo DBMS                      : %DB_SEEDER_DBMS%
echo DBMS_EMBEDDED             : %DB_SEEDER_DBMS_EMBEDDED%
   
if ["%DB_SEEDER_DBMS_EMBEDDED%"] == ["no"] (
    echo Docker stop/rm db_seeder_db ................................ before:
    docker ps -a
    docker ps -qa --filter "name=db_seeder_db" | grep -q . && docker stop db_seeder_db && docker rm -fv db_seeder_db
    echo ............................................................. after:
    docker ps -a
)
    
if ["%DB_SEEDER_DBMS_EMBEDDED%"] == ["yes"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS%"] == ["derby"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS%"] == ["h2"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
    call scripts\run_db_seeder_setup_files.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_DBMS_EMBEDDED%"] EQU ["no"] (
    lib\Gammadyne\timer.exe /reset
    lib\Gammadyne\timer.exe /q
    
    call scripts\run_db_seeder_setup_%DB_SEEDER_DBMS%.bat
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
    
    docker ps
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
