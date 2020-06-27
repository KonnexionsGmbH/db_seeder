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

set DB_SEEDER_CUBRID_DATABASE=kxn_db
set DB_SEEDER_DERBY_DATABASE=kxn_db
set DB_SEEDER_FIREBIRD_DATABASE=kxn_db
set DB_SEEDER_IBMDB2_DATABASE=kxn_db
set DB_SEEDER_SQLITE_DATABASE=kxn_db

set DB_SEEDER_VERSION_CRATEDB=4.1.6
set DB_SEEDER_VERSION_CUBRID=10.2
set DB_SEEDER_VERSION_DERBY=10.15.2.0
set DB_SEEDER_VERSION_FIREBIRD=3.0.5
set DB_SEEDER_VERSION_IBMDB2=11.5.0.0a

set DB_SEEDER_VERSION_MARIADB=10.4.13
set DB_SEEDER_VERSION_MARIADB=10.5.3
set DB_SEEDER_VERSION_MARIADB=10.5.4

set DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
set DB_SEEDER_VERSION_MYSQL=8.0.20

set DB_SEEDER_VERSION_ORACLE=db_12_2_ee
set DB_SEEDER_VERSION_ORACLE=db_18_3_ee
set DB_SEEDER_VERSION_ORACLE=db_19_3_ee

set DB_SEEDER_VERSION_POSTGRESQL=12.3

if ["%1"] EQU [""] (
    echo ====================================
    echo derby       - Apache Derby [client]
    echo derby_emb   - Apache Derby [embedded]
    echo cratedb     - CrateDB
    echo cubrid      - CUBRID
    echo firebird    - Firebird
    echo ibmdb2      - IBM Db2 Database
    echo mariadb     - MariaDB Server
    echo mssqlserver - Microsoft SQL Server
    echo mysql       - MySQL
    echo oracle      - Oracle Database
    echo postgresql  - PostgreSQL Database
    echo sqlite      - SQLite [embedded]
    echo ------------------------------------
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
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DBMS%"] == ["cubrid"] (
    echo CUBRID_DATABASE           : !DB_SEEDER_CUBRID_DATABASE!
    if exist !DB_SEEDER_CUBRID_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_CUBRID_DATABASE!
    )    
    if exist !DB_SEEDER_CUBRID_DATABASE! dir !DB_SEEDER_CUBRID_DATABASE!
    rd /q /s !DB_SEEDER_CUBRID_DATABASE! 2>nul
    if exist !DB_SEEDER_CUBRID_DATABASE! del /f /q !DB_SEEDER_CUBRID_DATABASE!
    echo --------------------------------------------------------------------------------
)    
if ["%DB_SEEDER_DBMS%"] == ["derby_emb"] (
    echo DERBY_DATABASE            : !DB_SEEDER_DERBY_DATABASE!
    if exist !DB_SEEDER_DERBY_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_DERBY_DATABASE!
    )    
    rd /q /s !DB_SEEDER_DERBY_DATABASE! 2>nul
    if exist !DB_SEEDER_DERBY_DATABASE! del /f /q !DB_SEEDER_DERBY_DATABASE!
    echo --------------------------------------------------------------------------------
)
if ["%DB_SEEDER_DBMS%"] == ["firebird"] (
    echo FIREBIRD_DATABASE         : !DB_SEEDER_FIREBIRD_DATABASE!
    if exist !DB_SEEDER_FIREBIRD_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_FIREBIRD_DATABASE!
    )    
    if exist !DB_SEEDER_FIREBIRD_DATABASE! dir !DB_SEEDER_FIREBIRD_DATABASE!
    rd /q /s !DB_SEEDER_FIREBIRD_DATABASE! 2>nul
    if exist !DB_SEEDER_FIREBIRD_DATABASE! del /f /q !DB_SEEDER_FIREBIRD_DATABASE!
    echo --------------------------------------------------------------------------------
)    
if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
    echo IBMDB2_DATABASE           : !DB_SEEDER_IBMDB2_DATABASE!
    if exist !DB_SEEDER_IBMDB2_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_IBMDB2_DATABASE!
    )    
    if exist !DB_SEEDER_IBMDB2_DATABASE! dir !DB_SEEDER_IBMDB2_DATABASE!
    rd /q /s !DB_SEEDER_IBMDB2_DATABASE! 2>nul
    if exist !DB_SEEDER_IBMDB2_DATABASE! del /f /q !DB_SEEDER_IBMDB2_DATABASE!
    echo --------------------------------------------------------------------------------
)
if ["%DB_SEEDER_DBMS%"] == ["sqlite"] (
    echo SQLITE_DATABASE           : !DB_SEEDER_SQLITE_DATABASE!
    if exist !DB_SEEDER_SQLITE_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_SQLITE_DATABASE!
    )    
    if exist !DB_SEEDER_SQLITE_DATABASE! dir !DB_SEEDER_SQLITE_DATABASE!
    rd /q /s !DB_SEEDER_SQLITE_DATABASE! 2>nul
    if exist !DB_SEEDER_SQLITE_DATABASE! del /f /q !DB_SEEDER_SQLITE_DATABASE!
    echo --------------------------------------------------------------------------------
)
echo:| TIME
echo ================================================================================

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
