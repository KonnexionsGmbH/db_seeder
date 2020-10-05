@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete_embedded.bat: Run all embedded DBMS variations.
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

set DB_SEEDER_DBMS_DERBY_EMB=yes
set DB_SEEDER_DBMS_H2_EMB=yes
set DB_SEEDER_DBMS_HSQLDB_EMB=yes
set DB_SEEDER_DBMS_SQLITE=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete_embedded.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete_embedded.log 2>&1 (

    if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
        set DB_SEEDER_FILE_STATISTICS_NAME=resources\statistics\db_seeder_cmd_embedded_unknown_%DB_SEEDER_RELEASE%.tsv
    )

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo COMPLETE_RUN                    : %DB_SEEDER_COMPLETE_RUN%
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
    echo --------------------------------------------------------------------------------
    echo DBMS_DERBY_EMB                  : %DB_SEEDER_DBMS_DERBY_EMB%
    echo DBMS_H2_EMB                     : %DB_SEEDER_DBMS_H2_EMB%
    echo DBMS_HSQLDB_EMB                 : %DB_SEEDER_DBMS_HSQLDB_EMB%
    echo DBMS_SQLITE                     : %DB_SEEDER_DBMS_SQLITE%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - embedded version.
    rem ------------------------------------------------------------------------------
    
    call run_db_seeder.bat derby_emb yes %DB_SEEDER_NO_CREATE_RUNS%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - embedded version.
    rem ------------------------------------------------------------------------------
    
    call run_db_seeder.bat h2_emb yes %DB_SEEDER_NO_CREATE_RUNS%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - embedded version.
    rem ------------------------------------------------------------------------------
    
    call run_db_seeder.bat hsqldb_emb yes %DB_SEEDER_NO_CREATE_RUNS%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    rem ------------------------------------------------------------------------------
    rem SQLite.
    rem ------------------------------------------------------------------------------
    
    call run_db_seeder.bat sqlite yes %DB_SEEDER_NO_CREATE_RUNS%
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
