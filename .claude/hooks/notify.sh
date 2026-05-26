#!/usr/bin/env bash
# Linux-native desktop notification (libnotify + freedesktop sound theme).
#
# Usage:
#   notify.sh [--title TITLE] [--message MSG] [--type info|warning|error] [--duration SECONDS]
#
# Reads JSON from stdin if available (Claude Code hook payload) and uses its
# `message` field when --message is not given.

set -u

title="Claude Code"
message=""
type="info"
duration=8

while [[ $# -gt 0 ]]; do
    case "$1" in
        --title)    title="$2";    shift 2 ;;
        --message)  message="$2";  shift 2 ;;
        --type)     type="$2";     shift 2 ;;
        --duration) duration="$2"; shift 2 ;;
        -h|--help)
            sed -n '2,9p' "$0" | sed 's/^# \{0,1\}//'
            exit 0
            ;;
        *) echo "notify.sh: unknown arg '$1'" >&2; exit 2 ;;
    esac
done

if [[ -z "$message" && ! -t 0 ]]; then
    payload="$(cat)"
    if command -v jq >/dev/null 2>&1; then
        parsed="$(printf '%s' "$payload" | jq -r '.message // empty' 2>/dev/null || true)"
        [[ -n "$parsed" ]] && message="$parsed"
    fi
    [[ -z "$message" ]] && message="$payload"
fi
[[ -z "$message" ]] && message="Notification"

case "$type" in
    warning) urgency="normal";   icon="dialog-warning";     sound="dialog-warning" ;;
    error)   urgency="critical"; icon="dialog-error";       sound="dialog-error" ;;
    info|*)  urgency="normal";   icon="dialog-information"; sound="dialog-information" ;;
esac

expire_ms=$(( duration * 1000 ))

notify-send \
    --app-name="Claude Code" \
    --urgency="$urgency" \
    --icon="$icon" \
    --expire-time="$expire_ms" \
    -- "$title" "$message" &

if command -v canberra-gtk-play >/dev/null 2>&1; then
    canberra-gtk-play --id="$sound" >/dev/null 2>&1 &
fi

wait
