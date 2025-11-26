#!/usr/bin/env bash

pre_release=$1
dry_run=$2

function grep_version {
  #           ___________________    _____________     _______________
  #         / normal version     \ / pre-release  \  / build          \
  grep -E '^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z.-]+)?(\+[0-9A-Za-z.-]+)?$'
}

if [[ -n "${pre_release:-}" ]]; then
  echo "This is a pre-release: $pre_release" >&2
  cog_get_version_flags=(--include-prereleases)
  cog_bump_flags=(--auto --pre "$pre_release" --skip-untracked)
else
  echo "This is a not a pre-release" >&2
  cog_get_version_flags=()
  cog_bump_flags=(--auto --skip-untracked)
fi

if [[ "$dry_run" == "true" ]]; then
  echo "This is a dry run" >&2
  cog_bump_flags+=(--dry-run)
fi

echo "cog_get_version_flags=${cog_get_version_flags[*]}" >&2
echo "cog_bump_flags=${cog_bump_flags[*]}" >&2

# Capture the current version before bumping
current_version=$(cog -v get-version "${cog_get_version_flags[@]}")

# Bump the version
bump_result=$(cog bump "${cog_bump_flags[@]}")

# Process the output of cog bump because it
# - outputs an error message after a dry-run for a no-op
# - may output the new version with a version prefix
# - returns a success code even when the version isn't bumped
bump_result=$(printf '%s' "$bump_result" | sed 's/^v//' | grep_version)

if [[ "$dry_run" == "true" ]]; then
  echo "bump_result=$bump_result" >&2
  new_version=${bump_result:-$current_version}
else
  # This is needed because bump only outputs the new version for dry-runs.
  new_version=$(cog -v get-version "${cog_get_version_flags[@]}")
fi

# Output is formatted for Github Actions step output
echo "current_version=$current_version"
echo "new_version=$new_version"
