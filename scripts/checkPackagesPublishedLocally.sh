#!/usr/bin/env bash

IFS="," read -r -a PACKAGES <<< "${EXPECTED_PACKAGES//.//}"
echo Packages: "${PACKAGES[@]}"
cd ~/.m2/repository || exit
for PACKAGE in "${PACKAGES[@]}"
do
  echo "$PACKAGE"
  ls "$PACKAGE" || exit
done
