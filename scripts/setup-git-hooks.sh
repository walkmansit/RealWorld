#!/bin/bash

# Script to install git hooks from .githooks directory

echo "Setting up git hooks..."

HOOKS_SOURCE_DIR=".githooks"
HOOKS_TARGET_DIR=".git/hooks"

# Check if source directory exists
if [ ! -d "$HOOKS_SOURCE_DIR" ]; then
    echo "Error: $HOOKS_SOURCE_DIR directory not found."
    exit 1
fi

# Create target directory if it doesn't exist
mkdir -p "$HOOKS_TARGET_DIR"

# Install each hook from source directory
for hook in "$HOOKS_SOURCE_DIR"/*; do
    if [ -f "$hook" ]; then
        hook_name=$(basename "$hook")
        target_hook="$HOOKS_TARGET_DIR/$hook_name"

        # Copy hook and make executable
        cp "$hook" "$target_hook"
        chmod +x "$target_hook"

        echo "Installed hook: $hook_name"
    fi
done

echo "Git hooks installed successfully!"
echo "Hooks will run automatically on git events."