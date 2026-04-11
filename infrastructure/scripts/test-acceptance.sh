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
export BACKEND_URL

TASK_TYPE="${1:-backend}"

if [ "$TASK_TYPE" = "backend" ]; then
    echo "Running backend acceptance tests against $BACKEND_URL..."
    ./gradlew backendTest
elif [ "$TASK_TYPE" = "frontend" ]; then
    echo "Running frontend acceptance tests..."
    ./gradlew frontendTest
else
    echo "Unknown test type: $TASK_TYPE. Use 'backend' or 'frontend'."
    exit 1
fi
