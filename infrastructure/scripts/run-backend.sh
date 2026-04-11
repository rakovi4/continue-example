#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/../.env"

if [ ! -f "$ENV_FILE" ]; then
    echo "No .env file found. Running setup-ports.sh..."
    bash "$SCRIPT_DIR/setup-ports.sh"
fi

source "$ENV_FILE"

cd "$SCRIPT_DIR/../.."

export JAVA_HOME="/c/Program Files/OpenJDK/jdk-17.0.2"
export BACKEND_PORT

echo "Starting backend on port $BACKEND_PORT..."
./gradlew :backend:application:bootRun -Dserver.port=$BACKEND_PORT 2>&1 &
echo $! > "$SCRIPT_DIR/../backend.pid"
echo "Backend PID: $(cat "$SCRIPT_DIR/../backend.pid")"
