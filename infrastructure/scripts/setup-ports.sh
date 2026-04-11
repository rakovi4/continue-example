#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/../.env"

if [ -f "$ENV_FILE" ]; then
    echo "Port configuration already exists at $ENV_FILE"
    cat "$ENV_FILE"
    exit 0
fi

REPO_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
REPO_INDEX=0

if [ -n "$1" ]; then
    REPO_INDEX=$1
fi

BACKEND_PORT=$((8080 + REPO_INDEX))
FRONTEND_PORT=$((5173 + REPO_INDEX))
MAILHOG_SMTP_PORT=$((1025 + REPO_INDEX))
MAILHOG_HTTP_PORT=$((8025 + REPO_INDEX))

cat > "$ENV_FILE" <<EOF
REPO_INDEX=$REPO_INDEX
BACKEND_PORT=$BACKEND_PORT
FRONTEND_PORT=$FRONTEND_PORT
MAILHOG_SMTP_PORT=$MAILHOG_SMTP_PORT
MAILHOG_HTTP_PORT=$MAILHOG_HTTP_PORT
BACKEND_URL=http://localhost:$BACKEND_PORT
EOF

echo "Port configuration written to $ENV_FILE"
cat "$ENV_FILE"
