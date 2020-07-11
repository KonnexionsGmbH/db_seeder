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
set DB_SEEDER_DBMS_DERBY_EMB=
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
    echo:| TIME
    echo ================================================================================
    
    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    rem ------------------------------------------------------------------------------
    rem CrateDB.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_CRATEDB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=cratedb
        call scripts\run_db_seeder_single.bat
    )
    
    rem ------------------------------------------------------------------------------
    rem CUBRID.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_CUBRID%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=cubrid
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_DERBY%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=derby
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_DERBY_EMB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=derby_emb
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem Firebird - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_FIREBIRD%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=firebird
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_H2%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=h2
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_H2_EMB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=h2_emb
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - client version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_HSQLDB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=hsqldb
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_HSQLDB_EMB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=hsqldb_emb
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem IBM Db2 Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_IBMDB2%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=ibmdb2
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem IBM Informix.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_INFORMIX%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=informix
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem MariaDB Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MARIADB%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=mariadb
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem Microsoft SQL Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MSSQLSERVER%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=mssqlserver
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem Mimer SQL.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MIMER%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=mimer
        call scripts\run_db_seeder_single.bat mimer
    )
    
    rem ------------------------------------------------------------------------------
    rem MySQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MYSQL%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=mysql
        call scripts\run_db_seeder_single.bat mysql
    )
    
    rem ------------------------------------------------------------------------------
    rem Oracle Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_ORACLE%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=oracle
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem PostgreSQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=postgresql
        call scripts\run_db_seeder_single.bat 
    )
    
    rem ------------------------------------------------------------------------------
    rem SQLite.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_SQLITE%"] EQU ["yes"] (
        set DB_SEEDER_DBMS=sqlite
        call scripts\run_db_seeder_single.bat 
    )
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
