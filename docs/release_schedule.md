# DBSeeder - Release Schedule

To create a new release, the following steps must be performed in the specified order.
It is not allowed to continue with the next step until the current step has been completed successfully, i.e. also without any errors. 

### **1. Manual quality control**.

#### a) Remove all personal markers.

It is good practice to mark experimental source code, such as source code comments or logging messages that are only used for testing, with a uniform personal marker, such as **`wwe`**, so that this source code can be easily discovered and removed for release builds.

#### b) Check all **`TODO`** markers for necessity.

#### c) Check all links in the **`docs`** file directory.

Links should generally contain the following suffix: **`{:target="_blank"}`**. 
This ensures that a web page activated by the link is opened in a new tab.

### **2. Define a new version number**.

The rules of semantic versioning must be applied. 
Given a version number MAJOR.MINOR.PATCH, increment the:

* MAJOR version when you make incompatible API changes
* MINOR version when you add functionality in a backwards compatible manner
* PATCH version when you make backwards compatible bug fixes

See [here](https://semver.org/){:target="_blank"} for details.

#### a) Update the version number in the **IO-AVSTATS** files:

- docs/code_of_conduct.md
- docs/contributing.md
- docs/license.md
- docs/release_notes.md

### **3. Create release candidate branch `<rel_branch>`**.

### **4. Switch to the release candidate branch `<rel_branch> `**.

    git checkout <rel_branch>

### **5. Update the software environment.

#### a. Check the current software versions.

    Dropbox/SoftDevelopment/Software_Release_Management.xlsx

#### b. Create a new version in repository **`docker_images`**.

#### c. Create a new Docker image.

### **6. Run the performance scripts.

#### a. Check the generator settings.

- run **`scripts/run_db_seeder_generate_schema.bat`**
- run **`scripts/run_db_seeder_generate_schema.sh`**

#### b. Update the Windows 10 software.

- run **`scripts/run_db_seeder_reslease.bat`**
- rename the resulting file **`resources/statistics/db_seeder_bash_complete_company_9.9.9_win10.tsv`**
- rename the resulting file **`resources/statistics/db_seeder_bash_improvement_company_9.9.9_win10.tsv`**

#### c. Create a new virtual machine.

- run **`scripts/run_db_seeder_reslease.sh`**
- rename the resulting file **`resources/statistics/db_seeder_bash_complete_company_9.9.9_vmware.tsv`**
- rename the resulting file **`resources/statistics/db_seeder_bash_improvement_company_9.9.9_vmware.tsv`**

#### d. Create a new WSL.

- run **`scripts/run_db_seeder_reslease.sh`**
- rename the resulting file **`resources/statistics/db_seeder_bash_complete_company_9.9.9_wsl2.tsv`**
- rename the resulting file **`resources/statistics/db_seeder_bash_improvement_company_9.9.9_wsl2.tsv`**

#### e. Rename the summary file.

- run **`db_seeder_summary_3.0.3-x.x.x.tsv`**

### **7. Finalise the new release**.

#### a. Create the new release in GitHub:

- Releases --> Create a new release
- Choose a tag ---> **`9.9.9`** ---> + Create a new tag
- Release title ---> **`Release 9.9.9: <headline>`**.
- Describe the release ---> **`see: https://github.com/io-aero/io-avstats/blob/main/docs/release_notes.md`**
- **`x`** This is a pre-release
- **`x`** Create a discussion for this release
- Publish release

### **8. Prepare the next release**.

#### a. Choose a new version number.

#### b. Prepare the following files in the **io-rsaster** repository for the new version:

- docs/release_history.md
- docs/release_notes.md
