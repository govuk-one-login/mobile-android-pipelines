#!/usr/bin/env bash

# https://github.com/actions/runner-images/issues/2840#issuecomment-790492173

unused_tools=(
  "/usr/share/dotnet"      # .NET
  "/opt/ghc"               # Haskell
  "/usr/local/share/boost" # Boost C++
)

rm -rf "${unused_tools[@]}"
