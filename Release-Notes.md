# Release Notes

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/1.15.0.svg)

----------

## Version 1.15.0

Release Date: 12.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- IBM Informix

  - Sporadically during Informix startup: "shared memory not initialized for INFORMIXSERVER 'informix'" (see [here](https://community.ibm.com/community/user/hybriddatamanagement/communities/community-home/digestviewer?communitykey=cf5a1f39-c21f-4bc4-9ec2-7ca108f0a365&tab=digestviewer&SuccessMsg=Thank+you+for+contributing+to+the+IBM+Community.+Your+message+is+in+queue+and+will+be+reviewed+soon.).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203).

#### New Features

- New batch script: `run_db_seeder_complete`: Run all DBMS variations
- New DBMS: Mimer SQL

#### Modified Features

n/a

#### Deleted Features

n/a


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

