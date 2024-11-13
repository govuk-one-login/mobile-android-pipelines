#!/usr/bin/env bash

# Check the build logic documentation using vale

BUILD_LOGIC_DIR=$1

vale sync \
  --config=${BUILD_LOGIC_DIR}/config/vale/.vale.ini

vale \
  --no-wrap \
  --config=${BUILD_LOGIC_DIR}/config/vale/.vale.ini \
  --glob='!**/{build,.gradle}/**' \
  ${BUILD_LOGIC_DIR}
