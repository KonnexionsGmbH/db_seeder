@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_database.bat: Setup a database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DATABASE_BRAND_DEFAULT=sqlite
set DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=yes

set DB_SEEDER_VERSION_CRATEDB=4.1.6
set DB_SEEDER_VERSION_IBMDB2=11.5.0.0a
set DB_SEEDER_VERSION_MARIADB=10.4.13
set DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
set DB_SEEDER_VERSION_MYSQL=8.0.20
set DB_SEEDER_VERSION_ORACLE=db_19_3_ee
set DB_SEEDER_VERSION_POSTGRESQL=12.3

if ["%1"] EQU [""] (
    echo ====================================
    echo cratedb     - CrateDB
    echo ibmdb2      - IBM DB2 Database
    echo mariadb     - MariaDB Server
    echo mssqlserver - Microsoft SQL Server
    echo mysql       - MySQL
    echo oracle      - Oracle Database
    echo postgresql  - PostgreSQL Database
    echo sqlite      - SQLite [no Docker image necessary, hence not available]
    echo ------------------------------------
    set /P DB_SEEDER_DATABASE_BRAND="Enter the desired database brand [default: %DB_SEEDER_DATABASE_BRAND_DEFAULT%] "

    if ["!DB_SEEDER_DATABASE_BRAND!"] EQU [""] (
        set DB_SEEDER_DATABASE_BRAND=%DB_SEEDER_DATABASE_BRAND_DEFAULT%
    )
) else (
    set DB_SEEDER_DATABASE_BRAND=%1
)

if ["%2"] EQU [""] (
    set /P DB_SEEDER_DELETE_EXISTING_CONTAINER="Delete the existing Docker container DB_SEEDER_DB (yes/no) [default: %DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%] "

    if ["!DB_SEEDER_DELETE_EXISTING_CONTAINER!"] EQU [""] (
        set DB_SEEDER_DELETE_EXISTING_CONTAINER=%DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT%
    )
) else (
    set DB_SEEDER_DELETE_EXISTING_CONTAINER=%2
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a database Docker container.
echo --------------------------------------------------------------------------------
echo DATABASE_BRAND            : %DB_SEEDER_DATABASE_BRAND%
echo DELETE_EXISTING_CONTAINER : %DB_SEEDER_DELETE_EXISTING_CONTAINER%
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DATABASE_BRAND%"] == ["ibmdb2"] (
    set DB_SEEDER_IBMDB2_DATABASE=kxn_db
    echo IBMDB2_DATABASE           : %DB_SEEDER_IBMDB2_DATABASE%
    echo --------------------------------------------------------------------------------
)
echo:| TIME
echo ================================================================================

if ["%DB_SEEDER_DATABASE_BRAND%"] NEQ ["sqlite"] (
    if NOT ["%DB_SEEDER_DELETE_EXISTING_CONTAINER%"] == ["no"] (
        echo Docker stop/rm db_seeder_db
        docker stop db_seeder_db
        docker rm -f db_seeder_db
    )
    
    lib\Gammadyne\timer.exe /reset
    lib\Gammadyne\timer.exe /q
    
    call scripts\run_db_seeder_setup_%DB_SEEDER_DATABASE_BRAND%.bat
    
    docker ps
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
