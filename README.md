# db_seeder - Creation of Dummy Data in a Variety of Database Brands.

## 1. Introduction

`db_seeder` allows the generation of dummy data in different databases. 
Currently the following databases are supported:
- MySQL database (relational Database / tested: version 8.0.20) 
- Oracle database (relational Database / tested: version 19c)


The name of the database schema or the database user can be freely chosen. 
If the selected schema or user already exists in the database, it is deleted with all existing data. 
`db_seeder` then creates the selected schema or user and generates the desired dummy data.
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
You can find the necessary link here: https://github.com/KonnexionsGmbH/db_seeder.
The system requirements are described in the respective release notes. 

## 4. Operating Instructions 

Using the connection development docker image saves the effort of installing the latest Java version. 
To run `db_seeder`, only the libraries in the `lib` directory and the appropriate batch script of `run_db_seeder` are required. 
All parameters used in `db_seeder` can be adjusted in the batch script to suit your needs.

### 4.1 Control Parameters - Basics

The flow control parameters for 'db_seeder' are stored in the properties file 'rc/main/resources/db_seeder.properties' and can all be overridden by the environment variables defined in the batch script.
The following control parameters are currently supported:

```
db_seeder.jdbc.connection.host=localhost

db_seeder.max.row.city=1800
db_seeder.max.row.company=5400
db_seeder.max.row.country=200
db_seeder.max.row.country_state=600
db_seeder.max.row.timezone=11

db_seeder.mysql.connection.database=kxn_db
db_seeder.mysql.connection.port=3306
db_seeder.mysql.connection.prefix=jdbc:mysql://
db_seeder.mysql.connection.suffix=?serverTimezone=UTC
db_seeder.mysql.password.sys=mysql
db_seeder.mysql.password=mysql
db_seeder.mysql.user=kxn_user
db_seeder.mysql.user.sys=root

db_seeder.oracle.connection.port=1521
db_seeder.oracle.connection.prefix=jdbc:oracle:thin:@//
db_seeder.oracle.connection.service=orclpdb1
db_seeder.oracle.password.sys=oracle
db_seeder.oracle.password=oracle
db_seeder.oracle.user=kxn_user
db_seeder.oracle.user.sys=sys
```

### 4.2 Control Parameters - Detailled

| Property incl. Default Value [db.seeder.] | Environment Variable [DB_SEEDER_] | Used By | Description |
| --- | --- | --- | --- |
| jdbc.connection.host=localhost | JDBC_CONNECTION_HOST | Relational DB | name or ip address of the database server |
|     |     |     |     |
| max.row.t...t=9...9 | MAX_ROW_T...T | Relational DB | number of rows to be generated (per database table t...t) |
|     |     |     |     |
| mysql.connection.database=kxn_db | MYSQL_CONNECTION_DATABASE | MySQL | schema name |
| mysql.connection.port=3306 | MYSQL_CONNECTION_PORT | MySQL | port number of the database server |
| mysql.connection.prefix=jdbc:mysql:// | MYSQL_CONNECTION_PREFIX | MySQL | prefix of the database connection string |
| mysql.connection.suffix=?serverTimezone=UTC | MYSQL_CONNECTION_SUFFIX | MySQL | suffix of the database connection string |
| mysql.password.sys=mysql | MYSQL_PASSWORD | MySQL | password of the normal user |
| mysql.password=mysql | MYSQL_PASSWORD_SYS | MySQL | password of the privileged user |
| mysql.user=kxn_user | MYSQL_USER | MySQL | name of the normal user |
| mysql.user.sys=root | MYSQL_USER_SYS | MySQL | name of the privileged user |
|     |     |     |     |
| oracle.connection.port=1521 | ORACLE_CONNECTION_PORT | Oracle | port number of the database server |
| oracle.connection.prefix=jdbc:oracle:thin:@// | ORACLE_CONNECTION_PREFIX | Oracle | prefix of the database connection string |
| oracle.connection.service=orclpdb1 | ORACLE_CONNECTION_SERVICE | Oracle | database service name |
| oracle.password.sys=oracle | ORACLE_PASSWORD | Oracle | password of the normal user |
| oracle.password=oracle | ORACLE_PASSWORD_SYS | Oracle | password of the privileged user |
| oracle.user=kxn_user | ORACLE_USER | Oracle | name of the normal user |
| oracle.user.sys=root | ORACLE_USER_SYS | Oracle | name of the privileged user |
|     |     |     |     |

## 4. Database Brand Specifica

### 4.1 MySQL Database

- the MySQL Database differentiates between database (schema) and user
- the used data types:

| Data Type | MySQL Type |
| --- | --- |
| big integer | BIGINT |
| binary large object | LONGBLOB |
| characterr large object | LONGTEXT |
| string | VARCHAR |
| timestamp | DATETIME |

### 4.2 Oracle Database

- the Oracle database only knows the database user that is identical with the schema
- the used data types:

| Data Type | Oracle Type |
| --- | --- |
| big integer | NUMBER |
| binary large object | BLOB |
| characterr large object | CLOB |
| string | VARCHAR2 |
| timestamp | TIMESTAMP |

## 5. Contributing 

In case of software changes we strongly recommend you to respect the license terms.

1. Fork it
1. Create your feature branch (git checkout -b my-new-feature)
1. Commit your changes (git commit -am 'Add some feature')
1. Push to the branch (git push origin my-new-feature)
1. Create a new Pull Request
