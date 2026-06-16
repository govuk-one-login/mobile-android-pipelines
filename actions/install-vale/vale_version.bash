#!/usr/bin/env bash
set -euo pipefail

brew info --json=v2 vale | jq -r '.formulae[0].versions.stable'
