#!/usr/bin/env bash

./gradlew \
  allDevicesDebugAndroidTest \
  --continue \
  --stacktrace \
  -Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect
