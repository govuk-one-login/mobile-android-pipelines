#!/usr/bin/env bash

./gradlew \
  check \
  --continue \
  --stacktrace \
  -Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect
