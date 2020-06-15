# db_seeder - Creation of Dummy Data in a Variety of Database Brands.

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/1.5.0.svg)

----

## 1. Introduction

`db_seeder` allows the generation of dummy data in different databases. 
Currently the following databases are supported:
- [CrateDB](https://crate.io/) (relational Database / tested: version 4.1.6) 
- [IBM DB2 Database](https://www.ibm.com/products/db2-database) (relational Database / tested: version 11.5.1.0) 
- [MariaDB Server](https://mariadb.com/) (relational Database / tested: version 10.4.13) 
- [Microsoft SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-2019) (relational Database / tested: version 2019) 
- [MySQL Database](https://www.mysql.com/) (relational Database / tested: version 8.0.20) 
- [Oracle Database](https://www.oracle.com/database/) (relational Database / tested: version 19c)
- [PostgreSQL Database](https://www.postgresql.org/) (relational Database / tested: version 12.3)

The names of the database, the schema and the user can be freely chosen, unless the respective database software contains restrictions. 
If the selected database, schema or user already exists, it is deleted with all including data. 
`db_seeder` then creates the selected database, schema or user and generates the desired dummy data.
A maximum of 2 147 483 647 rows can be generated per database table.

## 2. Data Model

The underlying data model is quite simple and is shown here in the relational version.
The 5 database tables CITY, COMPANY, COUNTRY, COUNTRY_STATE, and TIMEZONE form a simple hierarchical structure and are therefore connected in the relational model via corresponding foreign keys.  

The abbreviations in the following illustration (created with Toad Data Modeler) mean:

- (AK1) - alternate key (unique key)
- FK    - foreign key
- NN    - not null
- PK    - primary key

![](.README_images/Data_Model.png)

## 3. Installation

The easiest way is to download a current release of `db_seeder`.
You can find the necessary link [here](https://github.com/KonnexionsGmbH/db_seeder).
The system requirements are described in the respective release notes. 

## 4. Operating Instructions 

Using the Konnexions development docker image from DockerHub (see [here](https://hub.docker.com/repository/docker/konnexionsgmbh/kxn_dev)) saves the effort of installing the latest Java version. 
To run `db_seeder`, only the libraries in the `lib` directory and the appropriate batch script of `run_db_seeder` are required. 
All parameters used in `db_seeder` can be adjusted in the batch script to suit your needs.

### 4.1 Control Parameters - Basics

The flow control parameters for `db_seeder` are stored in the properties file `src/main/resources/db_seeder.properties` and can all be overridden by the environment variables defined in the batch script.
The following control parameters are currently supported:

```
db_seeder.cratedb.connection.port=5432
db_seeder.cratedb.connection.prefix=crate://
db_seeder.cratedb.password=cratedb
db_seeder.cratedb.user=kxn_user

db_seeder.ibmdb2.connection.port=50000
db_seeder.ibmdb2.connection.prefix=jdbc:db2://
db_seeder.ibmdb2.database=kxn_db
db_seeder.ibmdb2.password=ibmdb2
db_seeder.ibmdb2.schema=kxn_schema

db_seeder.jdbc.connection.host=localhost

db_seeder.mariadb.connection.port=3306
db_seeder.mariadb.connection.prefix=jdbc:mariadb://
db_seeder.mariadb.database=kxn_db
db_seeder.mariadb.password.sys=mariadb
db_seeder.mariadb.password=mariadb
db_seeder.mariadb.user=kxn_user

db_seeder.max.row.city=1800
db_seeder.max.row.company=5400
db_seeder.max.row.country=200
db_seeder.max.row.country_state=600
db_seeder.max.row.timezone=11

db_seeder.mssqlserver.connection.port=1433
db_seeder.mssqlserver.connection.prefix=jdbc:sqlserver://
db_seeder.mssqlserver.database=kxn_db
db_seeder.mssqlserver.password.sys=mssqlserver_2019
db_seeder.mssqlserver.password=mssqlserver_2019
db_seeder.mssqlserver.schema=kxn_schema
db_seeder.mssqlserver.user=kxn_user

db_seeder.mysql.connection.port=3306
db_seeder.mysql.connection.prefix=jdbc:mysql://
db_seeder.mysql.connection.suffix=?serverTimezone=UTC
db_seeder.mysql.database=kxn_db
db_seeder.mysql.password.sys=mysql
db_seeder.mysql.password=mysql
db_seeder.mysql.user=kxn_user

db_seeder.oracle.connection.port=1521
db_seeder.oracle.connection.prefix=jdbc:oracle:thin:@//
db_seeder.oracle.connection.service=orclpdb1
db_seeder.oracle.password.sys=oracle
db_seeder.oracle.password=oracle
db_seeder.oracle.user=kxn_user

db_seeder.postgresql.connection.port=5432
db_seeder.postgresql.connection.prefix=jdbc:postgresql://
db_seeder.postgresql.database=kxn_db
db_seeder.postgresql.password.sys=postgresql
db_seeder.postgresql.password=postgresql
db_seeder.postgresql.user=kxn_user

```

### 4.2 Control Parameters - Detailled

| Property incl. Default Value [db.seeder.] | Environment Variable [DB_SEEDER_] | Used By | Description |
| --- | --- | --- | --- |
| cratedb.connection.port=5432 | CRATEDB_CONNECTION_PORT | CrateDB | port number of the database server |
| cratedb.connection.prefix=crate:// | CRATEDB_CONNECTION_PREFIX | CrateDB | prefix of the database connection string |
| cratedb.password=cratedb | CRATEDB_PASSWORD | CrateDB | password of the normal user |
| cratedb.user=kxn_user | CRATEDB_USER | CrateDB | name of the normal user |
|     |     |     |     |
| ibmdb2.connection.port=50000 | IBMDB2_CONNECTION_PORT | IBM DB2 | port number of the database server |
| ibmdb2.connection.prefix=jdbc:db2:// | IBMDB2_CONNECTION_PREFIX | IBM DB2 | prefix of the database connection string |
| ibmdb2.database=kxn_db | IBMDB2_DATABASE | IBM DB2 | database name |
| ibmdb2.password=ibmdb2 | IBMDB2_PASSWORD | IBM DB2 | password of the user |
| ibmdb2.schema=kxn_db | IBMDB2_SCHEMA | IBM DB2 | schema name |
|     |     |     |     |
| jdbc.connection.host=localhost | JDBC_CONNECTION_HOST | Relational DB | name or ip address of the database server |
|     |     |     |     |
| mariadb.connection.port=3306 | MARIADB_CONNECTION_PORT | MariaDB | port number of the database server |
| mariadb.connection.prefix=jdbc:mariadb:// | MARIADB_CONNECTION_PREFIX | MariaDB | prefix of the database connection string |
| mariadb.database=kxn_db | MARIADB_DATABASE | MariaDB | database name |
| mariadb.password.sys=mariadb | MARIADB_PASSWORD_SYS | MariaDB | password of the privileged user |
| mariadb.password=mariadb | MARIADB_PASSWORD | MariaDB | password of the normal user |
| mariadb.user=kxn_user | MARIADB_USER | MariaDB | name of the normal user |
|     |     |     |     |
| max.row.t...t=9...9 | MAX_ROW_T...T | Relational DB | number of rows to be generated (per database table t...t) |
|     |     |     |     |
| mssqlserver.connection.port=1433 | MSSQLSERVER_CONNECTION_PORT | Microsoft SQL Server | port number of the database server |
| mssqlserver.connection.prefix=jdbc:sqlserver:// | MSSQLSERVER_CONNECTION_PREFIX | Microsoft SQL Server | prefix of the database connection string |
| mssqlserver.database=kxn_db | MSSQLSERVER_DATABASE | Microsoft SQL Server | database name |
| mssqlserver.password.sys=mssqlserver | MSSQLSERVER_PASSWORD_SYS | Microsoft SQL Server | password of the privileged user |
| mssqlserver.password=mssqlserver | MSSQLSERVER_PASSWORD | Microsoft SQL Server | password of the normal user |
| mssqlserver.schema=kxn_schema | MSSQLSERVER_SCHEMA | Microsoft SQL Server | schema name |
| mssqlserver.user=kxn_user | MSSQLSERVER_USER | Microsoft SQL Server | name of the normal user |
|     |     |     |     |
| mysql.connection.port=3306 | MYSQL_CONNECTION_PORT | MySQL | port number of the database server |
| mysql.connection.prefix=jdbc:mysql:// | MYSQL_CONNECTION_PREFIX | MySQL | prefix of the database connection string |
| mysql.connection.suffix=?serverTimezone=UTC | MYSQL_CONNECTION_SUFFIX | MySQL | suffix of the database connection string |
| mysql.database=kxn_db | MYSQL_DATABASE | MySQL | database name |
| mysql.password.sys=mysql | MYSQL_PASSWORD_SYS | MySQL | password of the privileged user |
| mysql.password=mysql | MYSQL_PASSWORD | MySQL | password of the normal user |
| mysql.user=kxn_user | MYSQL_USER | MySQL | name of the normal user |
|     |     |     |     |
| oracle.connection.port=1521 | ORACLE_CONNECTION_PORT | Oracle | port number of the database server |
| oracle.connection.prefix=jdbc:oracle:thin:@// | ORACLE_CONNECTION_PREFIX | Oracle | prefix of the database connection string |
| oracle.connection.service=orclpdb1 | ORACLE_CONNECTION_SERVICE | Oracle | database service name |
| oracle.password.sys=oracle | ORACLE_PASSWORD_SYS | Oracle | password of the privileged user |
| oracle.password=oracle | ORACLE_PASSWORD | Oracle | password of the normal user |
| oracle.user=kxn_user | ORACLE_USER | Oracle | name of the normal user |
|     |     |     |     |
| mssqlserver.connection.port=1433 | MSSQLSERVER_CONNECTION_PORT | Microsoft SQL Server | port number of the database server |
| mssqlserver.connection.prefix=jdbc:sqlserver:// | MSSQLSERVER_CONNECTION_PREFIX | Microsoft SQL Server | prefix of the database connection string |
| mssqlserver.database=kxn_db | MSSQLSERVER_DATABASE | Microsoft SQL Server | database name |
| mssqlserver.password.sys=mssqlserver | MSSQLSERVER_PASSWORD_SYS | Microsoft SQL Server | password of the privileged user |
| mssqlserver.password=mssqlserver | MSSQLSERVER_PASSWORD | Microsoft SQL Server | password of the normal user |
| mssqlserver.user=kxn_user | MSSQLSERVER_USER | Microsoft SQL Server | name of the normal user |
|     |     |     |     |

## 4. Database Brand Specifica

### 4.1 CrateDB

- database driver version 2.6.0
  - JFrog Bintray repository: [here](https://bintray.com/crate/crate/crate-jdbc/2.6.0)
- database image version 4.1.6: [here](https://hub.docker.com/_/crate)
- data definition hierarchy: only user
- privileged database / user: n/a / crate
- restrictions:
  - no constraints (e.g. foreign keys or unique keys)
  - no transaction concept
  - no triggers 
  - only a very proprietary BLOB implementation
- data types used:

| Data Type | CrateDB Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | n/a |
| character large object | TEXT |
| string | TEXT |
| timestamp | TIMESTAMP |

### 4.2 IBM DB2 Database

- database driver version 11.5.0.0 
  - Maven repository: [here](https://mvnrepository.com/artifact/com.ibm.db2/jcc/11.5.0.0)
- database image version 11.5.0.0a: [here](https://hub.docker.com/r/ibmcom/db2)
- data definition hierarchy: only schema
- privileged database / user: n/a / db2inst1
- restrictions:
  - the IBM DB2 database only accepts operating system accounts as database users 
- data types used:

| Data Type | IBM DB2 Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | BLOB |
| character large object | CLOB |
| string | VARCHAR |
| timestamp | TIMESTAMP |

### 4.3 MariaDB Server

- database driver version 2.6.0 
  - Maven repository: [here](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client)
- database image version 10.4.13: [here](https://hub.docker.com/_/mariadb)
- data definition hierarchy: database and user
- privileged database / user: mysql / root
- data types used:

| Data Type | MariaDB Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | LONGBLOB |
| character large object | LONGTEXT |
| string | VARCHAR |
| timestamp | DATETIME |

### 4.4 Microsoft SQL Server

- database driver version 8.31 
  - Maven Repository: [here](https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc)
- database image version 2019: [here](https://hub.docker.com/_/microsoft-mssql-server)
- data definition hierarchy: database, schema and user
- privileged database / user: master / sa
- data types used:

| Data Type | Microsoft SQL Server Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | VARBINARY (MAX) |
| character large object | VARCHAR (MAX) |
| string | VARCHAR |
| timestamp | DATETIME2 |

### 4.5 MySQL Database

- database driver version 8.0.20 
  - Maven repository: [here](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- database image version 8.0.20: [here](https://hub.docker.com/_/mysql)
- data definition hierarchy: database and user
- privileged database / user: sys / root
- data types used:

| Data Type | MySQL Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | LONGBLOB |
| character large object | LONGTEXT |
| string | VARCHAR |
| timestamp | DATETIME |

### 4.6 Oracle Database

- database driver version 
  - Maven repository 19.3.0.0: [here](https://mvnrepository.com/artifact/com.oracle.ojdbc/ojdbc8)
  - Software 19.6.0.0.0: [here](https://www.oracle.com/database/technologies/instant-client/downloads.html)
- database image version 19c
- data definition hierarchy: user
- privileged database / user: orclpdb1 / sys AS SYSDBA
- data types used:

| Data Type | Oracle Type |
| --- | --- |
| big integer | NUMBER |
| binary large object | BLOB |
| character large object | CLOB |
| string | VARCHAR2 |
| timestamp | TIMESTAMP |

### 4.7 PostgreSQL Database

- database driver version 42.2.13
  - Maven repository: [here](https://mvnrepository.com/artifact/org.postgresql/postgresql)
- database image version 12.3: [here](https://hub.docker.com/_/postgres)
- data definition hierarchy: database, schema and user
- privileged database / user: kxn_db_sys / kxn_user_sys
- data types used:

| Data Type | PostgreSQL Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | BYTEA |
| character large object | TEXT |
| string | VARCHAR |
| timestamp | TIMESTAMP |

## 5. Contributing 

In case of software changes we strongly recommend you to respect the license terms.

1. Fork it
1. Create your feature branch (git checkout -b my-new-feature)
1. Commit your changes (git commit -am 'Add some feature')
1. Push to the branch (git push origin my-new-feature)
1. Create a new Pull Request
1. Action points to be considered when adding a new database:
    1. lib/<database_driver>.jar
    1. scripts/run_db_seeder_setup_<database>.bat
    1. src/main/java/ch/konnexions/db_seeder/Config.java
    1. src/main/java/ch/konnexions/db_seeder/DatabaseSeeder.java
    1. src/main/java/ch/konnexions/db_seeder/jdbc/<database>/<Database>Seeder.java
    1. src/main/resources/db_seeder.properties
    1. .travis.yml
    1. build.gradle
    1. README.md
    1. Release-Notes.md
    1. run_db_seeder.bat
    1. run_db_seeder.sh
    1. run_db_seeder_setup_database.bat
    1. run_db_seeder_setup_database.sh
