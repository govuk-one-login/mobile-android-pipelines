#!/usr/bin/env bash
# Example: `EXPECTED_PACKAGES=uk.gov.my.app,uk.gov.publishing.your-app scripts/checkPackagesPublishedLocally.sh`

# Converting comma separated string input `EXPECTED_PACKAGES` into a list of strings,
# replacing "." with "/" to make it a list of directory path.
IFS="," read -r -a PACKAGES <<< "${EXPECTED_PACKAGES//.//}"
echo Packages: "${PACKAGES[@]}"

# Navigate to directory where publishToMavenLocal publishes to in filesystem. If this local
# Maven repository doesn't exist, exit with error.
cd ~/.m2/repository || exit

# For each package, echo out the package name and check the contents inside the corresponding
# directory for that package. If the directory does not exist, exit with error.
for PACKAGE in "${PACKAGES[@]}"
do
  echo "$PACKAGE"
  ls "$PACKAGE" || exit
done
