#!/usr/bin/env bash
set -euo pipefail
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"
mvn package -DskipTests
find target -maxdepth 1 -type f \( -name "*.jar" -o -name "*.pom" \) -print
