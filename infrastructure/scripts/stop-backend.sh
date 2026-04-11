#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/../.env"

if [ ! -f "$ENV_FILE" ]; then
    echo "No .env file found."
    exit 1
fi

source "$ENV_FILE"

PID=$(netstat -ano 2>/dev/null | grep ":$BACKEND_PORT " | grep LISTENING | awk '{print $NF}' | head -1)

if [ -n "$PID" ]; then
    echo "Stopping backend (PID: $PID) on port $BACKEND_PORT..."
    taskkill //PID $PID //F 2>/dev/null
    echo "Backend stopped."
else
    echo "No process found on port $BACKEND_PORT"
fi
