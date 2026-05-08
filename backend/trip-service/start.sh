#!/bin/sh
set -e

if [ -n "$DATABASE_URL" ] && [ -z "$SPRING_DATASOURCE_URL" ]; then
  export SPRING_DATASOURCE_URL="$(printf '%s' "$DATABASE_URL" | sed -E 's#^postgres(ql)?://[^@]+@([^/]+)/(.+)$#jdbc:postgresql://\2/\3#')"
fi

exec java -jar app.jar

