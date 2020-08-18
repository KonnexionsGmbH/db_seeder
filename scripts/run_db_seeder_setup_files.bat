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
echo DATABASE                  : %DB_SEEDER_DATABASE%
echo DBMS                      : %DB_SEEDER_DBMS%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------

set DB_SEEDER_DATABASE_INTERN=

if ["%DB_SEEDER_DBMS%"] == ["derby"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["derby_emb"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["h2"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["h2_emb"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["hsqldb"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)
if ["%DB_SEEDER_DBMS%"] == ["hsqldb_emb"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["ibmdb2"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)

if ["%DB_SEEDER_DBMS%"] == ["sqlite"] (
    set DB_SEEDER_DATABASE_INTERN=%DB_SEEDER_DATABASE%
)

if NOT ["%DB_SEEDER_DATABASE_INTERN%" == ""] (
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
        md %dirname%
    )
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
