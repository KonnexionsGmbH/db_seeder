@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete.bat: Run all DBMS variations.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DBMS_CRATEDB=yes
set DB_SEEDER_DBMS_CUBRID=yes
set DB_SEEDER_DBMS_DERBY=yes
set DB_SEEDER_DBMS_DERBY_EMB=yes
set DB_SEEDER_DBMS_FIREBIRD=yes
set DB_SEEDER_DBMS_H2=yes
set DB_SEEDER_DBMS_H2_EMB=yes
set DB_SEEDER_DBMS_HSQLDB=yes
set DB_SEEDER_DBMS_HSQLDB_EMB=yes
set DB_SEEDER_DBMS_IBMDB2=yes
set DB_SEEDER_DBMS_INFORMIX=yes
set DB_SEEDER_DBMS_MARIADB=yes
set DB_SEEDER_DBMS_MIMER=yes
set DB_SEEDER_DBMS_MSSQLSERVER=yes
set DB_SEEDER_DBMS_MYSQL=yes
set DB_SEEDER_DBMS_ORACLE=yes
set DB_SEEDER_DBMS_POSTGRESQL=yes
set DB_SEEDER_DBMS_SQLITE=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete.log 2>&1 (

    set DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local_cmd.tsv

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo DBMS_CRATEDB                    : %DB_SEEDER_DBMS_CRATEDB%
    echo DBMS_CUBRID                     : %DB_SEEDER_DBMS_CUBRID%
    echo DBMS_DERBY                      : %DB_SEEDER_DBMS_DERBY%
    echo DBMS_DERBY_EMB                  : %DB_SEEDER_DBMS_DERBY_EMB%
    echo DBMS_FIREBIRD                   : %DB_SEEDER_DBMS_FIREBIRD%
    echo DBMS_H2                         : %DB_SEEDER_DBMS_H2%
    echo DBMS_H2_EMB                     : %DB_SEEDER_DBMS_H2_EMB%
    echo DBMS_HSQLDB                     : %DB_SEEDER_DBMS_HSQLDB%
    echo DBMS_HSQLDB_EMB                 : %DB_SEEDER_DBMS_HSQLDB_EMB%
    echo DBMS_IBMDB2                     : %DB_SEEDER_DBMS_IBMDB2%
    echo DBMS_INFORMIX                   : %DB_SEEDER_DBMS_INFORMIX%
    echo DBMS_MARIADB                    : %DB_SEEDER_DBMS_MARIADB%
    echo DBMS_MIMER                      : %DB_SEEDER_DBMS_MIMER%
    echo DBMS_MSSQLSERVER                : %DB_SEEDER_DBMS_MSSQLSERVER%
    echo DBMS_MYSQL                      : %DB_SEEDER_DBMS_MYSQL%
    echo DBMS_ORACLE                     : %DB_SEEDER_DBMS_ORACLE%
    echo DBMS_POSTGRESQL                 : %DB_SEEDER_DBMS_POSTGRESQL%
    echo DBMS_SQLITE                     : %DB_SEEDER_DBMS_SQLITE%
    echo --------------------------------------------------------------------------------
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    rem ------------------------------------------------------------------------------
    rem CrateDB.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_CRATEDB%"] EQU ["yes"] (
        call run_db_seeder.bat cratedb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem CUBRID.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_CUBRID%"] EQU ["yes"] (
        call run_db_seeder.bat cubrid yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_DERBY%"] EQU ["yes"] (
        call run_db_seeder.bat derby yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_DERBY_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat derby_emb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Firebird - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_FIREBIRD%"] EQU ["yes"] (
        call run_db_seeder.bat firebird yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_H2%"] EQU ["yes"] (
        call run_db_seeder.bat h2 yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_H2_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat h2_emb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_HSQLDB%"] EQU ["yes"] (
        call run_db_seeder.bat hsqldb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_HSQLDB_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat hsqldb_emb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem IBM Db2 Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_IBMDB2%"] EQU ["yes"] (
        call run_db_seeder.bat ibmdb2 yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem IBM Informix.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_INFORMIX%"] EQU ["yes"] (
        call run_db_seeder.bat informix yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem MariaDB Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MARIADB%"] EQU ["yes"] (
        call run_db_seeder.bat mariadb yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Microsoft SQL Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MSSQLSERVER%"] EQU ["yes"] (
        call run_db_seeder.bat mssqlserver yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Mimer SQL.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MIMER%"] EQU ["yes"] (
        call run_db_seeder.bat mimer yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem MySQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MYSQL%"] EQU ["yes"] (
        call run_db_seeder.bat mysql yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem Oracle Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_ORACLE%"] EQU ["yes"] (
        call run_db_seeder.bat oracle yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem PostgreSQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql yes 2
    )
    
    rem ------------------------------------------------------------------------------
    rem SQLite.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_SQLITE%"] EQU ["yes"] (
        call run_db_seeder.bat sqlite yes 2
    )
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
