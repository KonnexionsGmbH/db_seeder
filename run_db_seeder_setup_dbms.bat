@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_dbms.bat: Setup a database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DBMS_DEFAULT=sqlite
set DB_SEEDER_DBMS_EMBEDDED=no
set DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=yes

set DB_SEEDER_VERSION_CRATEDB=4.1.6
set DB_SEEDER_VERSION_CUBRID=10.2
set DB_SEEDER_VERSION_DERBY=10.15.2.0
set DB_SEEDER_VERSION_FIREBIRD=3.0.5
set DB_SEEDER_VERSION_H2=1.4.200
set DB_SEEDER_VERSION_HSQLDB=2.5.1
set DB_SEEDER_VERSION_IBMDB2=11.5.0.0a

set DB_SEEDER_VERSION_MARIADB=10.4.13
set DB_SEEDER_VERSION_MARIADB=10.5.3
set DB_SEEDER_VERSION_MARIADB=10.5.4

set DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
set DB_SEEDER_VERSION_MYSQL=8.0.20

set DB_SEEDER_VERSION_ORACLE=db_12_2_ee
set DB_SEEDER_VERSION_ORACLE=db_18_3_ee
set DB_SEEDER_VERSION_ORACLE=db_19_3_ee

set DB_SEEDER_VERSION_POSTGRESQL=12.3-alpine

if ["%1"] EQU [""] (
    echo ===========================================
    echo derby       - Apache Derby [client]
    echo derby_emb   - Apache Derby [embedded]
    echo cratedb     - CrateDB
    echo cubrid      - CUBRID
    echo firebird    - Firebird
    echo h2          - H2 Database Engine [client]
    echo h2_emb      - H2 Database Engine [embedded]
    echo hsqldb      - HyperSQL Database [client]
    echo hsqldb_emb  - HyperSQL Database [embedded]
    echo ibmdb2      - IBM Db2 Database
    echo mariadb     - MariaDB Server
    echo mssqlserver - Microsoft SQL Server
    echo mysql       - MySQL
    echo oracle      - Oracle Database
    echo postgresql  - PostgreSQL Database
    echo sqlite      - SQLite [embedded]
    echo -------------------------------------------
    set /P DB_SEEDER_DBMS="Enter the desired database management system [default: %DB_SEEDER_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_DBMS!"] EQU [""] (
        set DB_SEEDER_DBMS=%DB_SEEDER_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_DBMS=%1
)

if ["%DB_SEEDER_DBMS%"] == ["derby_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)
if ["%DB_SEEDER_DBMS%"] == ["h2_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)
if ["%DB_SEEDER_DBMS%"] == ["hsqldb_emb"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)
if ["%DB_SEEDER_DBMS%"] == ["sqlite"] (
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS_EMBEDDED%"] == ["yes"] (
    set DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=no
)

if ["%2"] EQU [""] (
    set /P DB_SEEDER_DELETE_EXISTING_CONTAINER="Delete the existing Docker container DB_SEEDER_DB (yes/no) [default: %DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%] "

    if ["!DB_SEEDER_DELETE_EXISTING_CONTAINER!"] EQU [""] (
        set DB_SEEDER_DELETE_EXISTING_CONTAINER=%DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%
    )
) else (
    set DB_SEEDER_DELETE_EXISTING_CONTAINER=%2
)

if NOT ["%DB_SEEDER_DELETE_EXISTING_CONTAINER%"] == ["no"] (
    echo Docker stop/rm db_seeder_db
    docker stop db_seeder_db
    docker rm -f db_seeder_db
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a database Docker container.
echo --------------------------------------------------------------------------------
echo DBMS                      : %DB_SEEDER_DBMS%
echo DBMS_EMBEDDED             : %DB_SEEDER_DBMS_EMBEDDED%
if ["%DB_SEEDER_DBMS_EMBEDDED%"] EQU ["no"] (
    echo DELETE_EXISTING_CONTAINER : %DB_SEEDER_DELETE_EXISTING_CONTAINER%
)    
   
call run_db_seeder_setup_files.bat %DB_SEEDER_DBMS%

if ["%DB_SEEDER_DBMS_EMBEDDED%"] EQU ["no"] (
    lib\Gammadyne\timer.exe /reset
    lib\Gammadyne\timer.exe /q
    
    call scripts\run_db_seeder_setup_%DB_SEEDER_DBMS%.bat
    
    docker ps
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
