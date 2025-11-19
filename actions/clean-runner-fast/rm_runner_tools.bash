#!/usr/bin/env bash

# https://github.com/actions/runner-images/issues/2840#issuecomment-790492173
rm -rf /usr/share/dotnet        # .NET
rm -rf /opt/ghc                 # Haskell
rm -rf "/usr/local/share/boost" # Boost C++
