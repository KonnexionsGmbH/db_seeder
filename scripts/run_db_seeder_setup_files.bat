@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_files.bat: Setup the database files.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_CUBRID_DATABASE=.\tmp\kxn_db
set DB_SEEDER_DERBY_DATABASE=.\tmp\kxn_db
set DB_SEEDER_FIREBIRD_DATABASE=.\tmp\kxn_db
set DB_SEEDER_H2_DATABASE=.\tmp\kxn_db
set DB_SEEDER_HSQLDB_DATABASE=.\tmp\kxn_db
set DB_SEEDER_IBMDB2_DATABASE=kxn_db
set DB_SEEDER_INFORMIX_DATABASE=kxn_db
set DB_SEEDER_MIMER_DATABASE=kxn_db
set DB_SEEDER_SQLITE_DATABASE=.\tmp\kxn_db

if ["%1"] EQU [""] (
    echo ===========================================
    echo derby_emb   - Apache Derby [embedded]
    echo cubrid      - CUBRID
    echo firebird    - Firebird
    echo h2_emb      - H2 Database Engine [embedded]
    echo hsqldb_emb  - HyperSQL Database [embedded]
    echo ibmdb2      - IBM Db2 Database
    echo informix    - IBM Informix
    echo mimer       - Mimer SQL
    echo sqlite      - SQLite [embedded]
    echo -------------------------------------------
    set /P DB_SEEDER_DBMS="Enter the desired database management system [default: %DB_SEEDER_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_DBMS!"] EQU [""] (
        set DB_SEEDER_DBMS=%DB_SEEDER_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_DBMS=%1
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - cleanup embedded database files.
echo --------------------------------------------------------------------------------
echo DBMS                      : %DB_SEEDER_DBMS%
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DBMS%"] == ["cubrid"] (
    echo CUBRID_DATABASE           : !DB_SEEDER_CUBRID_DATABASE!
    if exist !DB_SEEDER_CUBRID_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_CUBRID_DATABASE!
        if exist !DB_SEEDER_CUBRID_DATABASE! dir !DB_SEEDER_CUBRID_DATABASE!
        rd /q /s !DB_SEEDER_CUBRID_DATABASE! 2>nul
        if exist !DB_SEEDER_CUBRID_DATABASE! del /f /q !DB_SEEDER_CUBRID_DATABASE!
    )    
)    
if ["%DB_SEEDER_DBMS%"] == ["derby_emb"] (
    echo DERBY_DATABASE            : !DB_SEEDER_DERBY_DATABASE!
    if exist !DB_SEEDER_DERBY_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_DERBY_DATABASE!
        rd /q /s !DB_SEEDER_DERBY_DATABASE! 2>nul
        if exist !DB_SEEDER_DERBY_DATABASE! del /f /q !DB_SEEDER_DERBY_DATABASE!
    )    
)
if ["%DB_SEEDER_DBMS%"] == ["firebird"] (
    echo FIREBIRD_DATABASE         : !DB_SEEDER_FIREBIRD_DATABASE!
    if exist !DB_SEEDER_FIREBIRD_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_FIREBIRD_DATABASE!
        if exist !DB_SEEDER_FIREBIRD_DATABASE! dir !DB_SEEDER_FIREBIRD_DATABASE!
        rd /q /s !DB_SEEDER_FIREBIRD_DATABASE! 2>nul
        if exist !DB_SEEDER_FIREBIRD_DATABASE! del /f /q !DB_SEEDER_FIREBIRD_DATABASE!
    )    
)    
if ["%DB_SEEDER_DBMS%"] == ["h2_emb"] (
    echo H2_DATABASE               : !DB_SEEDER_H2_DATABASE!
    if exist !DB_SEEDER_H2_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_H2_DATABASE!
        rd /q /s !DB_SEEDER_H2_DATABASE!.mv.db 2>nul
        if exist !DB_SEEDER_H2_DATABASE!.mv.db del /f /q !DB_SEEDER_H2_DATABASE!.mv.db
    )    
)
if ["%DB_SEEDER_DBMS%"] == ["hsqldb_emb"] (
    echo HSQLDB_DATABASE           : !DB_SEEDER_HSQLDB_DATABASE!
    if exist !DB_SEEDER_HSQLDB_DATABASE!.lobs (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_HSQLDB_DATABASE!.*
        rd /q /s !DB_SEEDER_HSQLDB_DATABASE!.tmp 2>nul
        if exist !DB_SEEDER_HSQLDB_DATABASE!.lck del /f /q !DB_SEEDER_HSQLDB_DATABASE!.lck
        if exist !DB_SEEDER_HSQLDB_DATABASE!.lobs del /f /q !DB_SEEDER_HSQLDB_DATABASE!.lobs
        if exist !DB_SEEDER_HSQLDB_DATABASE!.log del /f /q !DB_SEEDER_HSQLDB_DATABASE!.log
        if exist !DB_SEEDER_HSQLDB_DATABASE!.properties del /f /q !DB_SEEDER_HSQLDB_DATABASE!.properties
        if exist !DB_SEEDER_HSQLDB_DATABASE!.script del /f /q !DB_SEEDER_HSQLDB_DATABASE!.script
    )    
)
if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
    echo IBMDB2_DATABASE           : !DB_SEEDER_IBMDB2_DATABASE!
    if exist !DB_SEEDER_IBMDB2_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_IBMDB2_DATABASE!
        if exist !DB_SEEDER_IBMDB2_DATABASE! dir !DB_SEEDER_IBMDB2_DATABASE!
        rd /q /s !DB_SEEDER_IBMDB2_DATABASE! 2>nul
        if exist !DB_SEEDER_IBMDB2_DATABASE! del /f /q !DB_SEEDER_IBMDB2_DATABASE!
    )    
)
if ["%DB_SEEDER_DBMS%"] == ["sqlite"] (
    echo SQLITE_DATABASE           : !DB_SEEDER_SQLITE_DATABASE!
    if exist !DB_SEEDER_SQLITE_DATABASE! (
        echo.
        echo ............................................................ before:
        dir !DB_SEEDER_SQLITE_DATABASE!
        if exist !DB_SEEDER_SQLITE_DATABASE! dir !DB_SEEDER_SQLITE_DATABASE!
        rd /q /s !DB_SEEDER_SQLITE_DATABASE! 2>nul
        if exist !DB_SEEDER_SQLITE_DATABASE! del /f /q !DB_SEEDER_SQLITE_DATABASE!
    )    
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
