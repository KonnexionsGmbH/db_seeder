# Release Notes

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/2.5.1.svg)

## Version 2.5.1

Release Date: dd.09.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

  - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

n/a

#### Modified Features

- CrateDB: DBMS 4.2.4
- CUBRID: JDBC 10.2.2.8874
- Exasol: DBMS 7.0.0
- Presto: DBMS 341
- Presto: JDBC 341

#### Deleted Features

n/a

----------

## Version 2.5.0

Release Date: 30.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

  - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: Exasol

#### Modified Features

- Microsoft SQL Server: JDBC 8.4.1.jre14

----------

## Version 2.4.0

Release Date: 27.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

  - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: AgensGraph
- New DBMS: VolDB

----------

## Version 2.3.0

Release Date: 26.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

  - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: Percona Server for MySQL
- New DBMS: YugabyteDB

#### Modified Features

- CrateDB: DBMS 4.2.3
- PostgreSQL: DBMS 12.4
- PostgreSQL: JDBC 42.2.15

----------

## Version 2.2.0

Release Date: 18.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- MonetDB

  - Exception in thread "main" java.sql.BatchUpdateException.

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### New Features

- New DBMS: MonetDB

#### Modified Features

- the [original image from Presto](https://hub.docker.com/r/prestosql/presto) is now used

----------

## Version 2.1.3

Release Date: 17.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### Modified Features

- PostgreSQL: JDBC 42.2.15
- Presto: use of Docker network
- SQLite: DBMS 3.33.0
- minor script fixes

----------

## Version 2.1.2

Release Date: 13.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - MySQL Connector: Connection failure (see [here](https://github.com/prestosql/presto/issues/4812)).
  - PostgreSQL Connector: Connection failure (see [here](https://github.com/prestosql/presto/issues/4813)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### Modified Features

- minor script fixes.

----------

## Version 2.1.1

Release Date: 13.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - MySQL Connector: JDBC INSERT limited to about 100 rows - observed 98 to 115 (see [here](https://github.com/prestosql/presto/issues/4732)).
  - SQL Server Connector: Issues with BLOB and CLOB (see [here](https://github.com/prestosql/presto/issues/4757)).

#### New Features

- Demonstration programs for the Presto Connectors

#### Modified Features

- Firebird: JDBC 4.0.1.java11
- MariaDB Server: DBMS 10.5.5
- Presto: JDBC 340
- solved: PostgreSQL Connector: Cannot insert BLOB using Presto JDBC (see [here](https://github.com/prestosql/presto/issues/4751)).
- solved: SQL Server Connector: Login failed (see [here](https://github.com/prestosql/presto/issues/4757)).
- solved: gradle warning with http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727)).

----------

## Version 2.1.0

Release Date: 11.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- CUBRID

  - gradle warning with http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - MySQL Connector: JDBC INSERT limited to about 100 rows - observed 98 to 115 (see [here](https://github.com/prestosql/presto/issues/4732)).
  - PostgreSQL Connector: Cannot insert BLOB using Presto JDBC (see [here](https://github.com/prestosql/presto/issues/4751)).
  - SQL Server Connector: Login failed (see [here](https://github.com/prestosql/presto/issues/4757)).

#### New Features

- Adding the JDBC driver of the Presto Distributed Query Enginge (see [Issue #5](https://github.com/KonnexionsGmbH/db_seeder/issues/5)).

#### Modified Features

- MariaDB Server: JDBC 2.6.2

- Microsoft SQL Server: JDBC 8.4.0.jre14

----------

## Version 2.0.0

Release Date: 02.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### New Features

- The underlying database schema can now be freely defined using a JSON-based parameter file (Issues [37](https://github.com/KonnexionsGmbH/db_seeder/issues/37), [38](https://github.com/KonnexionsGmbH/db_seeder/issues/38), [39](https://github.com/KonnexionsGmbH/db_seeder/issues/39) and [50](https://github.com/KonnexionsGmbH/db_seeder/issues/50)).

#### Modified Features

- CrateDB: DBMS 4.1.8

- SQLite: JDBC 3.32.3.2

----------

## Version 1.15.10

Release Date: 14.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### Modified Features

- Config.java: reduce number of properties (issue #36)
- MySQL Database: DBMS & JDBC 8.0.21

----------

## Version 1.15.8

Release Date: 13.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### Modified Features

- Added complete run and upload of statistics data to Travis CI (issue #42)
- Scripts restructured and extended (issue #40)
- Solved IBM Informix issue (issue #41)

----------

## Version 1.15.5

Release Date: 10.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- IBM Informix

  - Sporadically during Informix startup: "shared memory not initialized for INFORMIXSERVER 'informix'" (see [here](https://community.ibm.com/community/user/hybriddatamanagement/communities/community-home/digestviewer/viewthread?GroupId=4147&MessageKey=65106f54-ae71-4c3f-afec-92ce84587989&CommunityKey=cf5a1f39-c21f-4bc4-9ec2-7ca108f0a365&tab=digestviewer&ReturnUrl=%2fcommunity%2fuser%2fhybriddatamanagement%2fcommunities%2fcommunity-home%2fdigestviewer%3fcommunitykey%3dcf5a1f39-c21f-4bc4-9ec2-7ca108f0a365%26tab%3ddigestviewer%26SuccessMsg%3dThank%2byou%2bfor%2bcontributing%2bto%2bthe%2bIBM%2bCommunity.%2bYour%2bmessage%2bis%2bin%2bqueue%2band%2bwill%2bbe%2breviewed%2bsoon.)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### New Features

- New batch script: `run_db_seeder_complete`: Run all DBMS variations
- New DBMS: Mimer SQL

#### Modified Features

- CrateDB: DBMS 4.1.8
- Firebird: DBMS 3.0.6
- IBM Db2 Database: DBMS & JDBC 11.5.4.0
- IBM Informix: JDBC 4.50.4.1
- Oracle Database: JDBC 19.7.0.0

----------

## Version 1.14.0

Release Date: 06.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: IBM Informix

----------

## Version 1.13.0

Release Date: 01.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

## Version 1.12.0

Release Date: 29.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

## Version 1.11.2

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### Modified Features

- introducing encoding support for ISO-8859-1 and UTF-8
- Tested DBMS version: MariaDB Server 10.5.4

----------

## Version 1.11.0

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).
  
- Mimer SQL

  - Previewing BLOB columns with DBeaver gives the error message "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203))  

#### New Features

- New DBMS: Firebird

----------

## Version 1.8.3

Release Date: 21.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- CUBRID

  - It is not possible to construct a valid URL for JDBC (see [here](http://jira.cubrid.org/browse/CBRD-23695)).

#### New Features

- New DBMS: Apache Derby (both client and embedded version)
- New DBMS: CUBRID

#### Modified Features

- Tested DBMS version: MariaDB Server 10.5.3
- Tested DBMS version: Oracle database 12c
- Tested DBMS version: Oracle database 18c

----------

## Version 1.6.0

Release Date: 16.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: SQLite

----------

## Version 1.5.0

Release Date: 15.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: CrateDB

----------

## Version 1.4.0

Release Date: 14.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: IBM Db2 Database

----------

## Version 1.3.0

Release Date: 12.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: MariaDB Server

----------

## Version 1.2.0

Release Date: 10.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: PostgreSQL Database

----------

## Version 1.1.0

Release Date: 06.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- Documentation
- New DBMS: Microsoft SQL Server
- Travis CI Integration
- Verification of the data storage 

#### Modified Features

- Adding BLOB and CLOB support

----------

## Version 1.0.0

Release Date: 01.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: MySQL Database
- New DBMS: Oracle Database

----------

