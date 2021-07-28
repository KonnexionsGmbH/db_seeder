@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_multiple.bat: Run multiple databases.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete_client.log
echo.
echo Please wait ...
echo.

> run_db_seeder_multiple.log 2>&1 (

    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DBSeeder - Run multiple databases.
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    echo --------------------------------------------------------------------------------
    echo Generator.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company_50.json

    call scripts\run_db_seeder_generate_schema

    echo --------------------------------------------------------------------------------
    echo Constraints included.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_DROP_CONSTRAINTS=no

    call run_db_seeder agens            yes 2
    call run_db_seeder cockroach        yes 2
    call run_db_seeder cratedb          yes 2
    call run_db_seeder cubrid           yes 2
    call run_db_seeder derby            yes 2
    call run_db_seeder derby_emb        yes 2
    call run_db_seeder exasol           yes 2
    call run_db_seeder firebird         yes 2
    call run_db_seeder h2               yes 2
    call run_db_seeder h2_emb           yes 2
    call run_db_seeder hsqldb           yes 2
    call run_db_seeder hsqldb_emb       yes 2
    call run_db_seeder ibmdb2           yes 2
    call run_db_seeder informix         yes 2
    call run_db_seeder mariadb          yes 2
    call run_db_seeder mimer            yes 2
    call run_db_seeder monetdb          yes 2
    call run_db_seeder mysql            yes 2
    call run_db_seeder mysql_trino      no  0
    call run_db_seeder omnisci          yes 1
    call run_db_seeder oracle           yes 2
    call run_db_seeder oracle_trino     no  0
    call run_db_seeder percona          yes 2
    call run_db_seeder postgresql       yes 2
    call run_db_seeder postgresql_trino no  0
    call run_db_seeder sqlite           yes 2
    call run_db_seeder sqlserver        yes 2
    call run_db_seeder sqlserver_trino  no  0
    call run_db_seeder timescale        yes 2
    call run_db_seeder voltdb           yes 2
    call run_db_seeder yugabyte         yes 2
    
    echo --------------------------------------------------------------------------------
    echo Constraints excluded.
    echo --------------------------------------------------------------------------------
    set DB_SEEDER_DROP_CONSTRAINTS=yes

    call run_db_seeder agens            yes 2
    call run_db_seeder cockroach        no  1
    call run_db_seeder cratedb          no  1
    call run_db_seeder cubrid           yes 2
    call run_db_seeder derby            yes 2
    call run_db_seeder derby_emb        yes 2
    call run_db_seeder exasol           yes 2
    call run_db_seeder firebird         no  1
    call run_db_seeder h2               no  1
    call run_db_seeder h2_emb           no  1
    call run_db_seeder hsqldb           no  1
    call run_db_seeder hsqldb_emb       no  1
    call run_db_seeder ibmdb2           yes 2
    call run_db_seeder informix         yes 2
    call run_db_seeder mariadb          yes 2
    call run_db_seeder mimer            yes 2
    call run_db_seeder monetdb          yes 2
    call run_db_seeder mysql            yes 2
    call run_db_seeder mysql_trino      no  1
    call run_db_seeder omnisci          no  1
    call run_db_seeder oracle           yes 2
    call run_db_seeder oracle_trino     no  1
    call run_db_seeder percona          yes 2
    call run_db_seeder postgresql       yes 2
    call run_db_seeder postgresql_trino no  1
    call run_db_seeder sqlite           no  1
    call run_db_seeder sqlserver        yes 2
    call run_db_seeder sqlserver_trino  no  1
    call run_db_seeder timescale        yes 2
    call run_db_seeder voltdb           yes 1
    call run_db_seeder yugabyte         no  1
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
