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

if [[ "$dry_run" == "true" ]]; then
  echo "bump_result=$bump_result" >&2

  # Try to extract the version from the output of cog bump.
  # Also strip the version prefix if present.
  # Note that the raw output may contain an error message if the bump is a no-op.
  new_version=$(printf '%s' "$bump_result" | sed 's/^v//' | grep_version)

  new_version=${new_version:-$current_version}
else
  # This is needed because bump only outputs the new version for dry-runs.
  new_version=$(cog -v get-version "${cog_get_version_flags[@]}")
fi

# Output is formatted for Github Actions step output
echo "current_version=$current_version"
echo "new_version=$new_version"
