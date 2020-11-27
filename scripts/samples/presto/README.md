# Brief operating instructions for the Presto demonstration programs 

## 1. Available Files

- `src/ch/konnexions/db_seeder/samples/presto/Sample[mysql|oracle|postgresql|sqlserver].java`
- `Samples_Presto.md` (this file)
- `scripts/samples/presto/run_sample_[mysql|oracle|postgresql|sqlserver].[bat/sh]`

## 2. Compiling

The best way to compile is with Gradle and the statement `build copyJarToLib`. 
This creates the JAR file `db_seeder.jar` in the directory `lib`.
Java must be installed at least in version 15.

## 3. Running

Prerequisite for this is an executable Presto version and the successful installation of the corresponding DBMS.

The Presto catalog properties can be generated with the script `scripts/run_db_seeder_presto_environment`.

Presto and the DBMS can be made available with the script `run_db_seeder [mysql|oracle|postgresql|sqlserver]_presto yes 0`, whereby corresponding Docker container are created in Windows and Linux.

The **`db_seeder`** demonstration program for Presto can then be executed with the DBMS specific script `scripts/samples/presto/run_sample_[mysql|oracle|postgresql|sqlserver]`. 

## 4. Sample run for the PostgreSQL Connector

Menu items 1 and 2 (Run the demo with the original JDBC driver) and 3 and 4 (Run the demo with the Presto JDBC driver) differ only in the selection of the driver. 
By default, 1000 rows are inserted in the following database table (PostgreSQL version):

    CREATE TABLE issue_table (
        column_pk        BIGINT         NOT NULL
                                        PRIMARY KEY,
        column_blob      BYTEA,
        column_clob      TEXT,
        column_timestamp TIMESTAMP      NOT NULL,
        column_varchar   VARCHAR(100)   NOT NULL
                                        UNIQUE
    )

The Connector proprties files are located in the directory `resources/docker/presto/catalog`.  If adjustments are necessary, the scripts `scripts/run_db_seeder_presto_environment` and `scripts/run_db_seeder_setup_presto`must be run again. 

All driver and SQL relevant events are logged at the console.

**Windows example:**

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>docker start db_seeder_presto
    db_seeder_presto

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>docker start db_seeder_db
    db_seeder_db

    D:\SoftDevelopment\Projects\db_seeder>docker ps
    CONTAINER ID        IMAGE                     COMMAND                  CREATED             STATUS              PORTS                                            NAMES
    fe106d6782fa        postgres:latest           "docker-entrypoint.s…"   2 minutes ago       Up 2 minutes        0.0.0.0:5432->5432/tcp                           db_seeder_db
    9bfe33d2c490        prestosql/presto:latest   "/usr/lib/presto/bin…"   About an hour ago   Up About an hour    0.0.0.0:8080->8080/tcp                           db_seeder_presto
    3259f5bb6c0e        portainer/portainer-ce    "/portainer"             6 weeks ago         Up About an hour    0.0.0.0:8000->8000/tcp, 0.0.0.0:9000->9000/tcp   portainer

    D:\SoftDevelopment\Projects\db_seeder>scripts\samples\presto\run_sample_postgresql
    ================================================================================
    Start scripts\samples\presto\run_sample_postgresql
    --------------------------------------------------------------------------------
    DB Seeder - Demonstration Issues Presto PostgreSQL Connector.
    --------------------------------------------------------------------------------
    The current time is: 17:03:09.92
    Enter the new time:
    --------------------------------------------------------------------------------
    2020-11-27 17:03:10,270 [SamplePostgresql.java] INFO  Start SamplePostgresql
    2020-11-27 17:03:10,277 [SamplePostgresql.java] INFO
    2020-11-27 17:03:10,279 [SamplePostgresql.java] INFO  ----------------------------------------------
    2020-11-27 17:03:10,280 [SamplePostgresql.java] INFO  1 - Run the demo with the original JDBC driver
    2020-11-27 17:03:10,281 [SamplePostgresql.java] INFO  2 - Run the demo with the original JDBC driver - with batch operations
    2020-11-27 17:03:10,283 [SamplePostgresql.java] INFO  3 - Run the demo with the Presto JDBC driver
    2020-11-27 17:03:10,286 [SamplePostgresql.java] INFO  4 - Run the demo with the Presto JDBC driver - with batch operations
    2020-11-27 17:03:10,288 [SamplePostgresql.java] INFO  8 - Run all demos
    2020-11-27 17:03:10,292 [SamplePostgresql.java] INFO  9 - Terminate the processing
    2020-11-27 17:03:10,294 [SamplePostgresql.java] INFO  ----------------------------------------------
    2020-11-27 17:03:10,295 [SamplePostgresql.java] INFO
    2020-11-27 17:03:10,296 [SamplePostgresql.java] INFO  Please make a choice:
    8
    2020-11-27 17:03:14,004 [SamplePostgresql.java] INFO  Run the demo with the original JDBC driver
    2020-11-27 17:03:14,005 [SamplePostgresql.java] INFO
    2020-11-27 17:03:14,007 [SamplePostgresql.java] INFO  Reset the database
    2020-11-27 17:03:14,008 [SamplePostgresql.java] INFO
    2020-11-27 17:03:14,766 [SamplePostgresql.java] INFO  Setup the database
    2020-11-27 17:03:14,769 [SamplePostgresql.java] INFO
    2020-11-27 17:03:15,128 [SamplePostgresql.java] INFO     100 rows inserted so far - milliseconds:  244
    2020-11-27 17:03:15,364 [SamplePostgresql.java] INFO     200 rows inserted so far - milliseconds:  235
    2020-11-27 17:03:15,603 [SamplePostgresql.java] INFO     300 rows inserted so far - milliseconds:  236
    2020-11-27 17:03:15,829 [SamplePostgresql.java] INFO     400 rows inserted so far - milliseconds:  224
    2020-11-27 17:03:16,048 [SamplePostgresql.java] INFO     500 rows inserted so far - milliseconds:  216
    2020-11-27 17:03:16,312 [SamplePostgresql.java] INFO     600 rows inserted so far - milliseconds:  261
    2020-11-27 17:03:16,554 [SamplePostgresql.java] INFO     700 rows inserted so far - milliseconds:  239
    2020-11-27 17:03:16,762 [SamplePostgresql.java] INFO     800 rows inserted so far - milliseconds:  206
    2020-11-27 17:03:16,973 [SamplePostgresql.java] INFO     900 rows inserted so far - milliseconds:  210
    2020-11-27 17:03:17,190 [SamplePostgresql.java] INFO    1000 rows inserted so far - milliseconds:  214
    2020-11-27 17:03:17,192 [SamplePostgresql.java] INFO
    2020-11-27 17:03:17,195 [SamplePostgresql.java] INFO    1000 rows inserted totally - duration in seconds:          2
    2020-11-27 17:03:17,197 [SamplePostgresql.java] INFO
    2020-11-27 17:03:17,199 [SamplePostgresql.java] INFO  Run the demo with the original JDBC driver - with batch operations
    2020-11-27 17:03:17,200 [SamplePostgresql.java] INFO
    2020-11-27 17:03:17,202 [SamplePostgresql.java] INFO  Reset the database
    2020-11-27 17:03:17,203 [SamplePostgresql.java] INFO
    2020-11-27 17:03:17,236 [SamplePostgresql.java] INFO  Setup the database
    2020-11-27 17:03:17,238 [SamplePostgresql.java] INFO
    2020-11-27 17:03:17,409 [SamplePostgresql.java] INFO     100 rows inserted so far - milliseconds:   66
    2020-11-27 17:03:17,489 [SamplePostgresql.java] INFO     200 rows inserted so far - milliseconds:   78
    2020-11-27 17:03:17,574 [SamplePostgresql.java] INFO     300 rows inserted so far - milliseconds:   82
    2020-11-27 17:03:17,649 [SamplePostgresql.java] INFO     400 rows inserted so far - milliseconds:   73
    2020-11-27 17:03:17,738 [SamplePostgresql.java] INFO     500 rows inserted so far - milliseconds:   86
    2020-11-27 17:03:17,817 [SamplePostgresql.java] INFO     600 rows inserted so far - milliseconds:   78
    2020-11-27 17:03:17,898 [SamplePostgresql.java] INFO     700 rows inserted so far - milliseconds:   78
    2020-11-27 17:03:17,978 [SamplePostgresql.java] INFO     800 rows inserted so far - milliseconds:   77
    2020-11-27 17:03:18,065 [SamplePostgresql.java] INFO     900 rows inserted so far - milliseconds:   84
    2020-11-27 17:03:18,145 [SamplePostgresql.java] INFO    1000 rows inserted so far - milliseconds:   77
    2020-11-27 17:03:18,147 [SamplePostgresql.java] INFO
    2020-11-27 17:03:18,148 [SamplePostgresql.java] INFO    1000 rows inserted totally - duration in seconds:          0
    2020-11-27 17:03:18,150 [SamplePostgresql.java] INFO
    2020-11-27 17:03:18,151 [SamplePostgresql.java] INFO  Run the demo with the Presto JDBC driver
    2020-11-27 17:03:18,152 [SamplePostgresql.java] INFO
    2020-11-27 17:03:18,154 [SamplePostgresql.java] INFO  Reset the database
    2020-11-27 17:03:18,155 [SamplePostgresql.java] INFO
    2020-11-27 17:03:18,188 [SamplePostgresql.java] INFO  Setup the database
    2020-11-27 17:03:18,190 [SamplePostgresql.java] INFO
    2020-11-27 17:03:26,806 [SamplePostgresql.java] INFO     100 rows inserted so far - milliseconds: 8145
    2020-11-27 17:03:34,768 [SamplePostgresql.java] INFO     200 rows inserted so far - milliseconds: 7961
    2020-11-27 17:03:42,627 [SamplePostgresql.java] INFO     300 rows inserted so far - milliseconds: 7856
    2020-11-27 17:03:50,604 [SamplePostgresql.java] INFO     400 rows inserted so far - milliseconds: 7974
    2020-11-27 17:03:58,697 [SamplePostgresql.java] INFO     500 rows inserted so far - milliseconds: 8090
    2020-11-27 17:04:06,742 [SamplePostgresql.java] INFO     600 rows inserted so far - milliseconds: 8043
    2020-11-27 17:04:14,989 [SamplePostgresql.java] INFO     700 rows inserted so far - milliseconds: 8245
    2020-11-27 17:04:23,088 [SamplePostgresql.java] INFO     800 rows inserted so far - milliseconds: 8096
    2020-11-27 17:04:31,057 [SamplePostgresql.java] INFO     900 rows inserted so far - milliseconds: 7967
    2020-11-27 17:04:39,124 [SamplePostgresql.java] INFO    1000 rows inserted so far - milliseconds: 8064
    2020-11-27 17:04:39,135 [SamplePostgresql.java] INFO
    2020-11-27 17:04:39,137 [SamplePostgresql.java] INFO    1000 rows inserted totally - duration in seconds:         80
    2020-11-27 17:04:39,138 [SamplePostgresql.java] INFO
    2020-11-27 17:04:39,140 [SamplePostgresql.java] INFO  Run the demo with the Presto JDBC driver - with batch operations
    2020-11-27 17:04:39,141 [SamplePostgresql.java] INFO
    2020-11-27 17:04:39,144 [SamplePostgresql.java] INFO  Reset the database
    2020-11-27 17:04:39,146 [SamplePostgresql.java] INFO
    2020-11-27 17:04:39,227 [SamplePostgresql.java] INFO  Setup the database
    2020-11-27 17:04:39,229 [SamplePostgresql.java] INFO
    2020-11-27 17:04:47,520 [SamplePostgresql.java] INFO     100 rows inserted so far - milliseconds: 8177
    2020-11-27 17:04:55,483 [SamplePostgresql.java] INFO     200 rows inserted so far - milliseconds: 7960
    2020-11-27 17:05:03,180 [SamplePostgresql.java] INFO     300 rows inserted so far - milliseconds: 7694
    2020-11-27 17:05:10,937 [SamplePostgresql.java] INFO     400 rows inserted so far - milliseconds: 7753
    2020-11-27 17:05:19,199 [SamplePostgresql.java] INFO     500 rows inserted so far - milliseconds: 8260
    2020-11-27 17:05:27,322 [SamplePostgresql.java] INFO     600 rows inserted so far - milliseconds: 8120
    2020-11-27 17:05:35,668 [SamplePostgresql.java] INFO     700 rows inserted so far - milliseconds: 8343
    2020-11-27 17:05:43,842 [SamplePostgresql.java] INFO     800 rows inserted so far - milliseconds: 8171
    2020-11-27 17:05:52,484 [SamplePostgresql.java] INFO     900 rows inserted so far - milliseconds: 8639
    2020-11-27 17:06:00,481 [SamplePostgresql.java] INFO    1000 rows inserted so far - milliseconds: 7994
    2020-11-27 17:06:00,491 [SamplePostgresql.java] INFO
    2020-11-27 17:06:00,492 [SamplePostgresql.java] INFO    1000 rows inserted totally - duration in seconds:         81
    2020-11-27 17:06:00,493 [SamplePostgresql.java] INFO
    2020-11-27 17:06:00,495 [SamplePostgresql.java] INFO  End   SamplePostgresql
    --------------------------------------------------------------------------------
    The current time is: 17:06:00.57
    Enter the new time:
    --------------------------------------------------------------------------------
    End   scripts\samples\presto\run_sample_postgresql
    ================================================================================
    
    D:\SoftDevelopment\Projects\db_seeder>
    
