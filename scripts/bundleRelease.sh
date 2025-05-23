#!/usr/bin/env bash
# Example: `MODULES_TO_BUNDLE=app,my/app,your:app INPUT_FLAVORS=build,dev scripts/bundleRelease.sh`

# Converting comma separated string input into a list of strings, replacing ":" with "/" for
# module paths.
IFS="," read -r -a MODULE_PATHS <<< "${MODULES_TO_BUNDLE//://}"
echo Module paths: "${MODULE_PATHS[@]}"

# Converting comma separated string input into a list of strings for input flavors.
if [ -z "${INPUT_FLAVORS}" ]
then
  EMPTY_STRING=""
  FLAVORS=("$EMPTY_STRING")
else
  IFS="," read -r -a FLAVORS <<< "${INPUT_FLAVORS}"
  echo Flavors: "${FLAVORS[@]}"
fi

# Converting comma separated string input into a list of strings, replacing "/" with ":" for tasks.
IFS="," read -r -a MODULE_TASKS <<< "${MODULES_TO_BUNDLE////:}"
echo Modules to run gradle task: "${MODULE_TASKS[@]}"


# Requires one argument for the module to bundle release to run bundle release task.
# The argument cannot be an empty string.
if [ "${#MODULE_PATHS[@]}" -lt 1 ] || [ "${MODULE_PATHS[0]}" == "" ]
then
  echo "  - Requires one argument provided for module to bundle release!"
  exit 11
else
  # For each module to be bundled as an argument, the bundle release task is run.
  for MODULE_TASK in "${MODULE_TASKS[@]}"
  do
    ./gradlew \
      --no-build-cache \
      --no-configuration-cache \
      "$MODULE_TASK":bundleRelease \
      -PversionCode="${VERSION_CODE}"
  done
fi

OUTPUT=""

for MODULE in "${MODULE_PATHS[@]}"
do
  MODULE_SUFFIX="${MODULE##*/}"
  for FLAVOR in "${FLAVORS[@]}"
  do
    if [ "${FLAVOR}" == "" ]
    then
      PACKAGE_NAME_INDICATOR="production"
      RELEASE_FOLDER="release"
      AAB_NAME_HYPHEN=""
    else
      PACKAGE_NAME_INDICATOR="${FLAVOR}"
      RELEASE_FOLDER="Release"
      AAB_NAME_HYPHEN="-"
    fi
    OUTPUT+="${PACKAGE_NAME_INDICATOR}:${MODULE}/build/outputs/bundle/${FLAVOR}${RELEASE_FOLDER}/${MODULE_SUFFIX}${AAB_NAME_HYPHEN}${FLAVOR}-release.aab;"
  done
done
echo "aab paths = ${OUTPUT}"
echo "aab-paths=${OUTPUT}" >> "$GITHUB_OUTPUT"