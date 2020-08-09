@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_single.bat: Run a single DBMS variation.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

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

if ["%DB_SEEDER_DBMS%"] EQU ["mssqlserver_presto"] (
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["mysql_presto"] (
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle_presto"] (
    set DB_SEEDER_DBMS_PRESTO=yes
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql_presto"] (
    set DB_SEEDER_DBMS_PRESTO=yes
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Run a single DBMS variation.
echo --------------------------------------------------------------------------------
echo DBMS                            : %DB_SEEDER_DBMS%
echo DBMS_EMBEDDED                   : %DB_SEEDER_DBMS_EMBEDDED%
echo DBMS_PRESTO                     : %DB_SEEDER_DBMS_PRESTO%
echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
echo SETUP_DBMS                      : %DB_SEEDER_SETUP_DBMS%
echo --------------------------------------------------------------------------------
echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================
    
if ["%DB_SEEDER_SETUP_DBMS%"] EQU ["yes"] (
    call scripts\run_db_seeder_setup_dbms.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)
    
if ["%DB_SEEDER_DBMS_PRESTO%"] EQU ["yes"] (
    call scripts\run_db_seeder_setup_presto.bat
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["1"] (
    call scripts\run_db_seeder_create_data.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)
    
if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["2"] (
    call scripts\run_db_seeder_create_data.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
    
    call scripts\run_db_seeder_create_data.bat %DB_SEEDER_DBMS%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
