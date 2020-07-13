## Contributing 

In case of software changes we strongly recommend you to respect the license terms.

1. Fork it
1. Create your feature branch (git checkout -b my-new-feature)
1. Commit your changes (git commit -am 'Add some feature')
1. Push to the branch (git push origin my-new-feature)
1. Create a new Pull Request
1. Action points to be considered when adding a new database:
   - lib
     - <database_driver>.jar
   - scripts
     - run_db_seeder_complete.bat
     - run_db_seeder_complete.sh
     - run_db_seeder_setup_<ticker_symbol>.bat
     - run_db_seeder_setup_dbms.bat
     - run_db_seeder_setup_dbms.sh
     - run_db_seeder_setup_files.bat
     - run_db_seeder_setup_files.sh
   - src/main/java
     - ch/konnexions/db_seeder/AbstractDatabaseSeeder.java
     - ch/konnexions/db_seeder/DatabaseSeeder.java
     - ch/konnexions/db_seeder/jdbc/<ticker_symbol>/<Database>Seeder.java
     - ch/konnexions/db_seeder/utils/Config.java
   - src/main/resources
     - db_seeder.properties
   - .travis.yml
   - README.md
   - Release-Notes.md
   - build.gradle
   - run_db_seeder.bat
   - run_db_seeder.sh
