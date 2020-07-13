#!/bin/bash

# ------------------------------------------------------------------------------
#
# run_travis_push_to_github.sh: db_seeder - push statistics bach to GitHub.
#
# ------------------------------------------------------------------------------

setup_git() {
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis CI"
}

commit_statistics_file() {
  cp statistics/$DB_SEEDER_FILE_STATISTICS_NAME /tmp
  cd /tmp || exit 255
  git clone --branch=master https://github.com/KonnexionsGmbH/db_seeder.git
  mv /tmp/$DB_SEEDER_FILE_STATISTICS_NAME db_seeder/statistics/
  cd db_seeder || exit 255
  # Current month and year, e.g: Apr 2018
  dateAndMonth=$(date "+%b %Y")
  # Stage the modified files in dist/output
  git add -f statistics/$DB_SEEDER_FILE_STATISTICS_NAME
  # Create a new commit with a custom build message
  # with "[skip ci]" to avoid a build loop
  # and Travis build number for reference
  git commit -m "Travis update: $dateAndMonth (Build $TRAVIS_BUILD_NUMBER)" -m "[skip ci]"
}

upload_file() {
  # Remove existing "origin"
  git remote rm origin
  # Add new "origin" with access token in the git URL for authentication
  git remote add origin https://KonnexionsGmbH:"${ORA_BENCH_TOKEN}"@github.com/KonnexionsGmbH/db_seeder.git > /dev/null 2>&1
  git push origin master
}

setup_git

# Attempt to commit to git only if "git commit" succeeded
ls -ll
if commit_statistics_file; then
  echo "A new commit with changed statistics file exists. Uploading to GitHub"
  upload_file
else
  echo "No changes in statistics file. Nothing to do"
fi
