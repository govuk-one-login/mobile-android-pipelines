#!/usr/bin/env bash

./gradlew \
  testDebugUnitTest \
  --continue \
  --stacktrace \
  -Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect
