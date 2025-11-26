#!/usr/bin/env bash

# https://github.com/actions/runner-images/issues/2840#issuecomment-790492173

getAvailableSpace() { echo $(df -a $1 | awk 'NR > 1 {avail+=$4} END {print avail}'); }
formatByteCount() { echo $(numfmt --to=iec-i --suffix=B --padding=7 $1'000'); }

BEFORE=$(getAvailableSpace)

which php

echo "user local:"
ls /usr/local

echo "user share:"
ls /usr/share

echo "opt"
ls /opt/microsoft

UNUSED_TOOLS=(
  "/usr/share/dotnet"      # .NET
  "/usr/share/mercurial"   # Mercurial
  "/usr/share/php*"        # PHP
  "/usr/share/swift"       # Swift
  "/opt/ghc"               # Haskell
  "/usr/local/.ghcup"      # Haskell
  "/opt/microsoft"         # Microsoft
  "/usr/local/share/boost" # Boost C++
)

rm -rf "${UNUSED_TOOLS[@]}"

sudo swapoff -a
sudo rm -f /mnt/swapfile
free -h

AFTER=$(getAvailableSpace)
SAVED=$((AFTER-BEFORE))

echo "Saved: $(formatByteCount $SAVED)"
