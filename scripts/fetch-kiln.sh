#!/usr/bin/env bash
# Clone liberation-data/kiln next to this repo's build (not committed).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="${KILN_HOME:-$ROOT/vendor/kiln}"
PIN="${KILN_PIN:-main}"

if [[ -d "$DEST/.git" ]]; then
  echo "Updating $DEST @ $PIN"
  git -C "$DEST" fetch --depth 1 origin "$PIN"
  git -C "$DEST" checkout --detach "origin/$PIN" 2>/dev/null || git -C "$DEST" checkout "$PIN"
else
  echo "Cloning liberation-data/kiln → $DEST"
  mkdir -p "$(dirname "$DEST")"
  git clone --depth 1 --branch "$PIN" https://github.com/liberation-data/kiln.git "$DEST"
fi

git -C "$DEST" rev-parse --short HEAD
echo "Next: (cd \"$DEST\" && native/build.sh vulkan)   # Halo / AMD Vulkan"
echo "Then:  ./gradlew run --args='<model-id> \"your prompt\"'"
