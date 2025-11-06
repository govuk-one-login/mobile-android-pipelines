#!/usr/bin/env bash

pre_release=$1

if [[ -n "${pre_release:-}" ]]; then
  echo "This is a pre-release: $pre_release" >&2
  cog_get_version_flags=(--include-prereleases)
  cog_bump_flags=(--auto --pre "$pre_release" --skip-untracked)
else
  echo "This is a not a pre-release" >&2
  cog_get_version_flags=()
  cog_bump_flags=(--auto --skip-untracked)
fi

echo "cog_get_version_flags=${cog_get_version_flags[*]}" >&2
echo "cog_bump_flags=${cog_bump_flags[*]}" >&2

current_version=$(cog -v get-version "${cog_get_version_flags[@]}")
cog bump "${cog_bump_flags[@]}"
new_version=$(cog -v get-version "${cog_get_version_flags[@]}")

# Output is formatted for Github Actions step output
echo "current_version=$current_version"
echo "new_version=$new_version"
