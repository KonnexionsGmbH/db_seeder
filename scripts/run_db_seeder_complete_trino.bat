@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete_trino.bat: Run all trino DBMS variations.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_COMPLETE_RUN=yes
set DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2

if ["%1"] EQU [""] (
    set /P DB_SEEDER_NO_CREATE_RUNS="Number of data creation runs (0-2) [default: %DB_SEEDER_NO_CREATE_RUNS_DEFAULT%] "

    if ["!DB_SEEDER_NO_CREATE_RUNS!"] EQU [""] (
        set DB_SEEDER_NO_CREATE_RUNS=%DB_SEEDER_NO_CREATE_RUNS_DEFAULT%
    )
) else (
    set DB_SEEDER_NO_CREATE_RUNS=%1
)

set DB_SEEDER_DBMS_MYSQL_TRINO=yes
set DB_SEEDER_DBMS_ORACLE_TRINO=yes
set DB_SEEDER_DBMS_POSTGRESQL_TRINO=yes
set DB_SEEDER_DBMS_SQLSERVER_TRINO=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete_trino.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete_trino.log 2>&1 (

    if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
        set DB_SEEDER_FILE_STATISTICS_NAME=resources\statistics\db_seeder_cmd_trino_unknown_%DB_SEEDER_RELEASE%.tsv
    )

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%

    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DBSeeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo COMPLETE_RUN                    : %DB_SEEDER_COMPLETE_RUN%
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
    echo --------------------------------------------------------------------------------
    echo DBMS_MYSQL_TRINO               : %DB_SEEDER_DBMS_MYSQL_TRINO%
    echo DBMS_ORACLE_TRINO              : %DB_SEEDER_DBMS_ORACLE_TRINO%
    echo DBMS_POSTGRESQL                 : %DB_SEEDER_DBMS_POSTGRESQL_TRINO%
    echo DBMS_SQLSERVER                  : %DB_SEEDER_DBMS_SQLSERVER_TRINO%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================

    call scripts\run_db_seeder_generate_schema.bat
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    call scripts\run_db_seeder_trino_environment.bat complete
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    call scripts\run_db_seeder_setup_trino.bat
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    rem ------------------------------------------------------------------------------
    rem MySQL Database - via trino.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_MYSQL_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat mysql_trino yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Oracle Database - via trino.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_ORACLE_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat oracle_trino yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem PostgreSQL.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_POSTGRESQL_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem SQL Server.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_SQLSERVER_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat sqlserver yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem End of processing.
    rem ------------------------------------------------------------------------------

    start resources\audio\end_of_series.mp3
    if ERRORLEVEL 1 (
        echo Processing of the script: %0 - step: 'start resources\audio\end_of_series.mp3' was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
