# DBSeeder - Contributing Guide

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/3.0.7.svg)

----

## 1. License

In case of software changes we strongly recommend you to respect the license terms.

## 2. Process

In case of software changes we strongly recommend you to respect the license terms.

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
6. Action points to be considered when adding a new database:
    
   - `lib/<database_driver>.jar`
   - `resources/docker/trino/catalog/db_seeder_<ticker_symbol>.properties`
   
   - `scripts`
     - `run_db_seeder_complete[client|emb|trino].[bat|sh]`
     - `run_db_seeder_generate_schema.[bat|sh]`
     - `run_db_seeder_setup_<ticker_symbol>.bat`
     - `run_db_seeder_setup_dbms.[bat|sh]`
     - `run_db_seeder_setup_files.[bat|sh]`
     - `run_db_seeder_single.[bat|sh]`
     - `run_db_seeder_trino_environment.[bat|sh]`
   
   - `src/main/java`
     - `ch/konnexions/db_seeder/AbstractDatabaseSeeder.java`
     - `ch/konnexions/db_seeder/DatabaseSeeder.java`
     - `ch/konnexions/db_seeder/TrinoEnvironment.java`
     - `ch/konnexions/db_seeder/generator/GenerateSchema.java`
     - `ch/konnexions/db_seeder/jdbc/<ticker_symbol>/<Database>Seeder.java`
     - `ch/konnexions/db_seeder/jdbc/AbstractJdbcSeeder.java`
