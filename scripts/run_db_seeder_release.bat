@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_release.bat: Release run for Windows 10.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

rem echo.
rem echo Script %0 is now running
rem echo.
rem echo You can find the run log in the file run_db_seeder_release.log
rem echo.
rem echo Please wait ...
rem echo.

rem > run_db_seeder_release.log 2>&1 (

    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DBSeeder - Release run for Windows 10.
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================

    call gradle copyJarToLib
    if ERRORLEVEL 1 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    del /f /q db_seeder.log

    echo --------------------------------------------------------------------------------
    echo Generator.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company_5400.json

    call scripts\run_db_seeder_generate_schema

    echo --------------------------------------------------------------------------------
    echo Constraints included.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_DROP_CONSTRAINTS=no

    set DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT=resources\statistics\db_seeder_cmd_complete_company_9.9.9_win10.tsv
    if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
        set DB_SEEDER_FILE_STATISTICS_NAME=%DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT%
    )

    del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%

    call run_db_seeder agens            yes 1
    call run_db_seeder cockroach        yes 1
    call run_db_seeder cratedb          yes 1
    call run_db_seeder cubrid           yes 1
    call run_db_seeder derby            yes 1
    call run_db_seeder derby_emb        yes 1
    call run_db_seeder exasol           yes 1
    call run_db_seeder firebird         yes 1
    call run_db_seeder h2               yes 1
    call run_db_seeder h2_emb           yes 1
    rem faulty:
    call run_db_seeder heavy            yes 0
    call run_db_seeder hsqldb           yes 1
    call run_db_seeder hsqldb_emb       yes 1
    call run_db_seeder ibmdb2           yes 1
    call run_db_seeder informix         yes 1
    call run_db_seeder mariadb          yes 1
    call run_db_seeder mimer            yes 1
    rem faulty:
    call run_db_seeder monetdb          yes 0
    call run_db_seeder mysql            yes 1
    call run_db_seeder mysql_trino      yes 1
    call run_db_seeder oracle           yes 1
    call run_db_seeder oracle_trino     yes 1
    call run_db_seeder percona          yes 1
    call run_db_seeder postgresql       yes 1
    call run_db_seeder postgresql_trino yes 1
    call run_db_seeder sqlite           yes 1
    call run_db_seeder sqlserver        yes 1
    call run_db_seeder sqlserver_trino  yes 1
    call run_db_seeder timescale        yes 1
    rem faulty (Java 15):
    call run_db_seeder voltdb           yes 0
    call run_db_seeder yugabyte         yes 1

    echo --------------------------------------------------------------------------------
    echo Constraints excluded.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_DROP_CONSTRAINTS=yes

    call run_db_seeder agens            yes 1
    call run_db_seeder cockroach        yes 1
    call run_db_seeder cubrid           yes 1
    call run_db_seeder derby            yes 1
    call run_db_seeder derby_emb        yes 1
    call run_db_seeder exasol           yes 1
    call run_db_seeder firebird         yes 1
    call run_db_seeder hsqldb           yes 1
    rem faulty (java.sql.SQLSyntaxErrorException: user lacks privilege or object not found: SYS_IDX_KXN_26_10100):
    call run_db_seeder hsqldb_emb       yes 0
    call run_db_seeder ibmdb2           yes 1
    call run_db_seeder informix         yes 1
    call run_db_seeder mariadb          yes 1
    call run_db_seeder mimer            yes 1
    rem faulty:
    call run_db_seeder monetdb          yes 0
    call run_db_seeder mysql            yes 1
    call run_db_seeder oracle           yes 1
    call run_db_seeder percona          yes 1
    call run_db_seeder postgresql       yes 1
    call run_db_seeder sqlserver        yes 1
    call run_db_seeder timescale        yes 1
    call run_db_seeder yugabyte         yes 1

    rem call run_db_seeder agens            yes 1
    rem call run_db_seeder cockroach        yes 1
    rem call run_db_seeder cratedb          no  1
    rem call run_db_seeder cubrid           yes 1
    rem call run_db_seeder derby            yes 1
    rem call run_db_seeder derby_emb        yes 1
    rem call run_db_seeder exasol           yes 1
    rem call run_db_seeder firebird         yes 1
    rem call run_db_seeder h2               no  1
    rem call run_db_seeder h2_emb           no  1
    rem call run_db_seeder heavy            no  1
    rem call run_db_seeder hsqldb           yes 1
    rem call run_db_seeder hsqldb_emb       yes 1
    rem call run_db_seeder ibmdb2           yes 1
    rem call run_db_seeder informix         yes 1
    rem call run_db_seeder mariadb          yes 1
    rem call run_db_seeder mimer            yes 1
    rem call run_db_seeder monetdb          yes 1
    rem call run_db_seeder mysql            yes 1
    rem call run_db_seeder mysql_trino      no  1
    rem call run_db_seeder oracle           yes 1
    rem call run_db_seeder oracle_trino     no  1
    rem call run_db_seeder percona          yes 1
    rem call run_db_seeder postgresql       yes 1
    rem call run_db_seeder postgresql_trino no  1
    rem call run_db_seeder sqlite           no  1
    rem call run_db_seeder sqlserver        yes 1
    rem call run_db_seeder sqlserver_trino  no  1
    rem call run_db_seeder timescale        yes 1
    rem call run_db_seeder voltdb           no  1
    rem call run_db_seeder yugabyte         yes 1

    call scripts\run_db_seeder_compute_improvement %DB_SEEDER_FILE_STATISTICS_NAME%

    call scripts\run_db_seeder_create_summary

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
rem )
