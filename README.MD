// Detekt tips
```bash
  gradle check
```

```bash
  gradle detekt
```

```bash
  gradle detekt --auto-correct
```

### Klint
```bash
  ./gradlew ktlintFormat
```

```bash
  ./gradlew ktlintCheck
```

### Show local addresses
```bash
  ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}'
```

### Show local open ports v4≠≠
```bash
  lsof -Pn -i4 | grep LISTEN
```

