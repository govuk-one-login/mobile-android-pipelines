#!/usr/bin/env bash

if [ $# -ne 1 ]; then
  echo "Usage: calculate_prerelease.bash <PRE_RELEASE_TYPE>" >&2
  echo "Example: calculate_prerelease.bash alpha" >&2
  exit 1
fi

prerelease_type="$1"

echo "Calculating the next pre-release version" >&2

# Only increment the pre-release if there are changes to release
next_stable_version=$(cog bump --auto --dry-run --skip-untracked)
if [ "$next_stable_version" == "" ]; then
  echo "There's nothing to release" >&2
  exit 0
fi
echo "Next stable version would be $next_stable_version" >&2

# Get the latest pre-release (if any)
cur_prerelease_version=$(
  cog -v get-version --tag --include-prereleases |
    grep "$prerelease_type"
)
echo "Latest pre-release version: ${cur_prerelease_version:-none}" >&2

# Start or increment the pre-release number
if [[ "$cur_prerelease_version" =~ ${prerelease_type}\.([0-9]+) ]]; then
  cur_prerelease_number=${BASH_REMATCH[1]}
  next_prerelease_number=$((cur_prerelease_number + 1))
  echo "Incrementing $prerelease_type: $cur_prerelease_number -> $next_prerelease_number" >&2
else
  echo "Starting a new $prerelease_type sequence" >&2
  next_prerelease_number=1
fi

# Output is formatted for Github Actions step output
# E.g. version=alpha.123
echo "version=$prerelease_type.$next_prerelease_number"
