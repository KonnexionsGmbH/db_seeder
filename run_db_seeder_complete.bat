@echo off

rem ------------------------------------------------------------------------------
rem
rem call run_db_seeder_complete.bat: Run all DBMS variations.
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

set DB_SEEDER_MAX_ROW_CITY=
set DB_SEEDER_MAX_ROW_COMPANY=
set DB_SEEDER_MAX_ROW_COUNTRY=
set DB_SEEDER_MAX_ROW_COUNTRY_STATE=
set DB_SEEDER_MAX_ROW_TIMEZONE=

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
echo MAX_ROW_CITY                    : %DB_SEEDER_MAX_ROW_CITY%
echo MAX_ROW_COMPANY                 : %DB_SEEDER_MAX_ROW_COMPANY%
echo MAX_ROW_COUNTRY                 : %DB_SEEDER_MAX_ROW_COUNTRY%
echo MAX_ROW_COUNTRY_STATE           : %DB_SEEDER_MAX_ROW_COUNTRY_STATE%
echo MAX_ROW_TIMEZONE                : %DB_SEEDER_MAX_ROW_TIMEZONE%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

if exist db_seeder.csv del /f /q db_seeder.csv

rem ------------------------------------------------------------------------------
rem CrateDB.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_CRATEDB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms cratedb yes
    call run_db_seeder            cratedb
    call run_db_seeder            cratedb
)

rem ------------------------------------------------------------------------------
rem CUBRID.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_CUBRID%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms cubrid yes
    call run_db_seeder            cubrid
    call run_db_seeder            cubrid
)

rem ------------------------------------------------------------------------------
rem Apache Derby - client version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_DERBY%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms derby yes
    call run_db_seeder            derby
    call run_db_seeder            derby
)

rem ------------------------------------------------------------------------------
rem Apache Derby - embedded version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_DERBY_EMB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms derby_emb no
    call run_db_seeder            derby_emb
    call run_db_seeder            derby_emb
)

rem ------------------------------------------------------------------------------
rem H2 Database Engine - client version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_H2%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms h2 yes
    call run_db_seeder            h2
    call run_db_seeder            h2
)

rem ------------------------------------------------------------------------------
rem H2 Database Engine - embedded version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_H2_EMB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms h2_emb no
    call run_db_seeder            h2_emb
    call run_db_seeder            h2_emb
)

rem ------------------------------------------------------------------------------
rem HyperSQL Database - client version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_HSQLDB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms hsqldb yes
    call run_db_seeder            hsqldb
    call run_db_seeder            hsqldb
)

rem ------------------------------------------------------------------------------
rem HyperSQL Database - embedded version.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_HSQLDB_EMB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms hsqldb_emb no
    call run_db_seeder            hsqldb_emb
    call run_db_seeder            hsqldb_emb
)

rem ------------------------------------------------------------------------------
rem IBM Db2 Database.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_IBMDB2%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms ibmdb2 yes
    call run_db_seeder            ibmdb2
    call run_db_seeder            ibmdb2
)

rem ------------------------------------------------------------------------------
rem IBM Informix.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_INFORMIX%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms informix yes
    call run_db_seeder            informix
    call run_db_seeder            informix
)

rem ------------------------------------------------------------------------------
rem MariaDB Server.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_MARIADB%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms mariadb yes
    call run_db_seeder            mariadb
    call run_db_seeder            mariadb
)

rem ------------------------------------------------------------------------------
rem Microsoft SQL Server.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_MSSQLSERVER%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms mssqlserver yes
    call run_db_seeder            mssqlserver
    call run_db_seeder            mssqlserver
)

rem ------------------------------------------------------------------------------
rem Mimer SQL.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_MIMER%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms mimer yes
    call run_db_seeder            mimer
    call run_db_seeder            mimer
)

rem ------------------------------------------------------------------------------
rem MySQL Database.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_MYSQL%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms mysql yes
    call run_db_seeder            mysql
    call run_db_seeder            mysql
)

rem ------------------------------------------------------------------------------
rem Oracle Database.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_ORACLE%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms oracle yes
    call run_db_seeder            oracle
    call run_db_seeder            oracle
)

rem ------------------------------------------------------------------------------
rem PostgreSQL Database.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms postgresql yes
    call run_db_seeder            postgresql
    call run_db_seeder            postgresql
)

rem ------------------------------------------------------------------------------
rem SQLite.
rem ------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_SQLITE%"] EQU ["yes"] (
    call run_db_seeder_setup_dbms sqlite no
    call run_db_seeder            sqlite
    call run_db_seeder            sqlite
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
