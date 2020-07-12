@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_files.bat: Setup the database files.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - cleanup embedded database files.
echo --------------------------------------------------------------------------------
echo DBMS                      : %DB_SEEDER_DBMS%
echo --------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS%"] == ["derby"] (
    echo DERBY_VERSION             : %DB_SEEDER_DERBY_VERSION%
    echo DERBY_DATABASE            : %DB_SEEDER_DERBY_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_DERBY_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["derby_emb"] (
    echo DERBY_VERSION             : %DB_SEEDER_DERBY_VERSION%
    echo DERBY_DATABASE            : %DB_SEEDER_DERBY_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_DERBY_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["h2"] (
    echo H2_VERSION                : %DB_SEEDER_H2_VERSION%
    echo H2_DATABASE               : %DB_SEEDER_H2_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_H2_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["h2_emb"] (
    echo H2_VERSION                : %DB_SEEDER_H2_VERSION%
    echo H2_DATABASE               : %DB_SEEDER_H2_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_H2_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["hsqldb"] (
    echo HSQLDB_VERSION            : %DB_SEEDER_HSQLDB_VERSION%
    echo HSQLDB_DATABASE           : %DB_SEEDER_HSQLDB_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_HSQLDB_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["hsqldb_emb"] (
    echo HSQLDB_VERSION            : %DB_SEEDER_HSQLDB_VERSION%
    echo HSQLDB_DATABASE           : %DB_SEEDER_HSQLDB_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_HSQLDB_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
    echo IBMDB2_VERSION            : %DB_SEEDER_IBMDB2_VERSION%
    echo IBMDB2_DATABASE           : %DB_SEEDER_IBMDB2_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_IBMDB2_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["sqlite"] (
    echo SQLITE_VERSION            : %DB_SEEDER_SQLITE_VERSION%
    echo SQLITE_DATABASE           : %DB_SEEDER_SQLITE_DATABASE%
    set DB_SEEDER_DATABASE=%DB_SEEDER_SQLITE_DATABASE%
)

if NOT ["%DB_SEEDER_DATABASE%" == ""] (
    if EXIST %DB_SEEDER_DATABASE%\nul ( 
        echo. 
        echo ............................................................ before:
        dir %DB_SEEDER_DATABASE%
        rd /q /s %DB_SEEDER_DATABASE% 2>nul
    )    

    if EXIST %DB_SEEDER_DATABASE%.tmp\nul ( 
        echo. 
        echo ............................................................ before:
        dir %DB_SEEDER_DATABASE%.tmp
        rd /q /s %DB_SEEDER_DATABASE%.tmp 2>nul
    )    

    if EXIST %DB_SEEDER_DATABASE% ( 
        echo. 
        echo ............................................................ before:
        dir %DB_SEEDER_DATABASE%
        del /f /q %DB_SEEDER_DATABASE%
    )    
    
    if EXIST %DB_SEEDER_DATABASE%.* ( 
        echo. 
        echo ............................................................ before:
        dir %DB_SEEDER_DATABASE%.*
        del /f /q %DB_SEEDER_DATABASE%.*
    )    
    
    if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
        md %DB_SEEDER_DATABASE%
    ) else (
        for %%F in ("%DB_SEEDER_DATABASE%") do set dirname=%%~dpF
        md dirname
    )
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
