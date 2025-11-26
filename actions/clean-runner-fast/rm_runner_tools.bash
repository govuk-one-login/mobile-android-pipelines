#!/usr/bin/env bash

# https://github.com/actions/runner-images/issues/2840#issuecomment-790492173

getAvailableSpace() { echo $(df -a $1 | awk 'NR > 1 {avail+=$4} END {print avail}'); }
formatByteCount() { echo $(numfmt --to=iec-i --suffix=B --padding=7 $1'000'); }

BEFORE=$(getAvailableSpace)

unused_tools=(
  "/usr/share/dotnet"      # .NET
  "/opt/ghc"               # Haskell
  "/usr/local/.ghcup"      # Haskell
  "/usr/local/share/boost" # Boost C++
)

rm -rf "${unused_tools[@]}"

sudo swapoff -a
sudo rm -f /mnt/swapfile
free -h

AFTER=$(getAvailableSpace)
SAVED=$((AFTER-BEFORE))

echo "Saved: $(formatByteCount $SAVED)"
