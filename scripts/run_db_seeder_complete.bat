@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete.bat: Run all DBMS variations.
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

set DB_SEEDER_DBMS_AGENS=yes
set DB_SEEDER_DBMS_DERBY=yes
set DB_SEEDER_DBMS_DERBY_EMB=yes
set DB_SEEDER_DBMS_COCKROACH=yes
set DB_SEEDER_DBMS_CRATEDB=yes
set DB_SEEDER_DBMS_CUBRID=yes
set DB_SEEDER_DBMS_EXASOL=yes
set DB_SEEDER_DBMS_FIREBIRD=yes
set DB_SEEDER_DBMS_H2=yes
set DB_SEEDER_DBMS_H2_EMB=yes
set DB_SEEDER_DBMS_HEAVY=yes
set DB_SEEDER_DBMS_HSQLDB=yes
set DB_SEEDER_DBMS_HSQLDB_EMB=yes
set DB_SEEDER_DBMS_IBMDB2=yes
set DB_SEEDER_DBMS_INFORMIX=yes
set DB_SEEDER_DBMS_MARIADB=yes
set DB_SEEDER_DBMS_MIMER=yes
set DB_SEEDER_DBMS_MONETDB=yes
set DB_SEEDER_DBMS_MYSQL=yes
set DB_SEEDER_DBMS_MYSQL_TRINO=yes
set DB_SEEDER_DBMS_ORACLE=yes
set DB_SEEDER_DBMS_ORACLE_TRINO=yes
set DB_SEEDER_DBMS_PERCONA=yes
set DB_SEEDER_DBMS_POSTGRESQL=yes
set DB_SEEDER_DBMS_POSTGRESQL_TRINO=yes
set DB_SEEDER_DBMS_SQLSERVER=yes
set DB_SEEDER_DBMS_SQLSERVER_TRINO=yes
set DB_SEEDER_DBMS_SQLITE_EMB=yes
set DB_SEEDER_DBMS_TIMESCALE=yes
set DB_SEEDER_DBMS_VOLTDB=yes
set DB_SEEDER_DBMS_YUGABYTE=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file db_seeder_cmd_complete.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete.log 2>&1 (

    if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
        set DB_SEEDER_FILE_STATISTICS_NAME=resources\statistics\db_seeder_cmd_complete_unknown_%DB_SEEDER_RELEASE%.tsv
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
    echo DBMS_AGENS                      : %DB_SEEDER_DBMS_AGENS%
    echo DBMS_COCKROACH                  : %DB_SEEDER_DBMS_COCKROACH%
    echo DBMS_CRATEDB                    : %DB_SEEDER_DBMS_CRATEDB%
    echo DBMS_CUBRID                     : %DB_SEEDER_DBMS_CUBRID%
    echo DBMS_DERBY                      : %DB_SEEDER_DBMS_DERBY%
    echo DBMS_DERBY_EMB                  : %DB_SEEDER_DBMS_DERBY_EMB%
    echo DBMS_EXASOL                     : %DB_SEEDER_DBMS_EXASOL%
    echo DBMS_FIREBIRD                   : %DB_SEEDER_DBMS_FIREBIRD%
    echo DBMS_H2                         : %DB_SEEDER_DBMS_H2%
    echo DBMS_H2_EMB                     : %DB_SEEDER_DBMS_H2_EMB%
    echo DBMS_HEAVY                      : %DB_SEEDER_DBMS_HEAVY%
    echo DBMS_HSQLDB                     : %DB_SEEDER_DBMS_HSQLDB%
    echo DBMS_HSQLDB_EMB                 : %DB_SEEDER_DBMS_HSQLDB_EMB%
    echo DBMS_IBMDB2                     : %DB_SEEDER_DBMS_IBMDB2%
    echo DBMS_INFORMIX                   : %DB_SEEDER_DBMS_INFORMIX%
    echo DBMS_MARIADB                    : %DB_SEEDER_DBMS_MARIADB%
    echo DBMS_MIMER                      : %DB_SEEDER_DBMS_MIMER%
    echo DBMS_MOMETDB                    : %DB_SEEDER_DBMS_MONETDB%
    echo DBMS_MYSQL                      : %DB_SEEDER_DBMS_MYSQL%
    echo DBMS_MYSQL_TRINO                : %DB_SEEDER_DBMS_MYSQL_TRINO%
    echo DBMS_ORACLE                     : %DB_SEEDER_DBMS_ORACLE%
    echo DBMS_ORACLE_TRINO               : %DB_SEEDER_DBMS_ORACLE_TRINO%
    echo DBMS_PERCONA                    : %DB_SEEDER_DBMS_PERCONA%
    echo DBMS_POSTGRESQL                 : %DB_SEEDER_DBMS_POSTGRESQL%
    echo DBMS_POSTGRESQL_TRINO           : %DB_SEEDER_DBMS_POSTGRESQL_TRINO%
    echo DBMS_SQLITE_EMB                 : %DB_SEEDER_DBMS_SQLITE_EMB%
    echo DBMS_SQLSERVER                  : %DB_SEEDER_DBMS_SQLSERVER%
    echo DBMS_SQLSERVER_TRINO            : %DB_SEEDER_DBMS_SQLSERVER_TRINO%
    echo DBMS_VOLTDB                     : %DB_SEEDER_DBMS_VOLTDB%
    echo DBMS_YUGABYTE                   : %DB_SEEDER_DBMS_YUGABYTE%
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
    rem AgensGraph.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_AGENS%"] EQU ["yes"] (
        call run_db_seeder.bat agens yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem CockroachDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_COCKROACH%"] EQU ["yes"] (
        call run_db_seeder.bat cockroach yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem CrateDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_CRATEDB%"] EQU ["yes"] (
        call run_db_seeder.bat cratedb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem CUBRID.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_CUBRID%"] EQU ["yes"] (
        call run_db_seeder.bat cubrid yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Apache Derby - client version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_DERBY%"] EQU ["yes"] (
        call run_db_seeder.bat derby yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Apache Derby - embedded version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_DERBY_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat derby_emb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Exasol - client version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_EXASOL%"] EQU ["yes"] (
        call run_db_seeder.bat exasol yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Firebird - client version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_FIREBIRD%"] EQU ["yes"] (
        call run_db_seeder.bat firebird yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - client version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_H2%"] EQU ["yes"] (
        call run_db_seeder.bat h2 yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - embedded version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_H2_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat h2_emb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem HeavyDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_HEAVY%"] EQU ["yes"] (
        call run_db_seeder.bat heavy yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem HSQLDB - client version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_HSQLDB%"] EQU ["yes"] (
        call run_db_seeder.bat hsqldb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem HSQLDB - embedded version.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_HSQLDB_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat hsqldb_emb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem IBM Db2 Database.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_IBMDB2%"] EQU ["yes"] (
        call run_db_seeder.bat ibmdb2 yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem IBM Informix.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_INFORMIX%"] EQU ["yes"] (
        call run_db_seeder.bat informix yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem MariaDB Server.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_MARIADB%"] EQU ["yes"] (
        call run_db_seeder.bat mariadb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem Mimer SQL.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_MIMER%"] EQU ["yes"] (
        call run_db_seeder.bat mimer yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem MonetDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_MONETDB%"] EQU ["yes"] (
        call run_db_seeder.bat monetdb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem MySQL Database.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_MYSQL%"] EQU ["yes"] (
        call run_db_seeder.bat mysql yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
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
    rem Oracle Database.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_ORACLE%"] EQU ["yes"] (
        call run_db_seeder.bat oracle yes %DB_SEEDER_NO_CREATE_RUNS%
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
    rem Percona Server for MySQL.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_PERCONA%"] EQU ["yes"] (
        call run_db_seeder.bat percona yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem PostgreSQL.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem PostgreSQL.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_POSTGRESQL_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql_trino yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem SQL Server.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_SQLSERVER%"] EQU ["yes"] (
        call run_db_seeder.bat sqlserver yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem SQL Server.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_SQLSERVER_TRINO%"] EQU ["yes"] (
        call run_db_seeder.bat sqlserver_trino yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem SQLite.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_SQLITE_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat sqlite yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem TimescaleDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_TIMESCALE%"] EQU ["yes"] (
        call run_db_seeder.bat timescale yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem VoltDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_VOLTDB%"] EQU ["yes"] (
        call run_db_seeder.bat voltdb yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    rem ------------------------------------------------------------------------------
    rem YugabyteDB.
    rem ------------------------------------------------------------------------------

    if ["%DB_SEEDER_DBMS_YUGABYTE%"] EQU ["yes"] (
        call run_db_seeder.bat yugabyte yes %DB_SEEDER_NO_CREATE_RUNS%
        if ERRORLEVEL 1 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )

    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
