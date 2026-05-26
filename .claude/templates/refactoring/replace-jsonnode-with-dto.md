# Replace JsonNode Traversal with Typed DTO

## Smell

Code parses a JSON payload by walking an untyped tree (`objectMapper.readTree()` + `.get("field").asText()`, `.path(...)` chains) — or worse, re-parses a structured body as plain text (splitting on `:` line-by-line, regex over an emitted format) to reconstruct fields. Typos in string keys silently break the parser; readers can't see the payload's shape from the code; field accessors don't show up in IDE navigation.

The sibling smell is the outbound direction: `Map<String, Object>` as request body (see `replace-map-with-dto.md`). This template covers the inbound direction.

## Fix

Define a record/DTO that mirrors the payload's structure. Use `@JsonProperty` for snake_case or non-Java field names. Deserialize directly into the type with `ObjectMapper.readValue(json, DTO.class)` and expose named accessors. Callers stop knowing string keys.

## Steps

1. Identify the JSON payload's fields and types
2. Create a `record` (or `@Value` class) with matching fields
3. Add `@JsonProperty("snake_case")` for non-camelCase names
4. Add a `from(source)` factory method that wraps `readValue` and converts checked exceptions into a descriptive `IllegalArgumentException`
5. Replace all tree-traversal / text-parsing call sites with `DTO.from(source).fieldName()`
6. Delete the homegrown parser (helper methods, intermediate `Map<String,String>`, custom splitters)
7. Run tests

## Before — tree traversal

```java
public class TelegramSendMessage {
    private final String chatId;
    private final Map<String, String> textFields;

    public static TelegramSendMessage from(LoggedRequest request) {
        try {
            JsonNode node = MAPPER.readTree(request.getBodyAsString());
            String chatId = node.get("chat_id").asText();
            String text = node.get("text").asText();
            return new TelegramSendMessage(chatId, parseTextFields(text));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Map<String, String> parseTextFields(String text) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (String line : text.split("\n")) {
            int colon = line.indexOf(':');
            if (colon > 0) fields.put(line.substring(0, colon).trim(), line.substring(colon + 1).trim());
        }
        return fields;
    }

    public String getChatId() { return chatId; }
    public String getEmailLine() { return "Email: " + textFields.get("Email"); }
    public String getPhoneLine() { return "Phone: " + textFields.get("Phone"); }
    // ... more per-field accessors that round-trip the emitted format
}
```

## After — typed deserialization

```java
public record TelegramSendMessage(@JsonProperty("chat_id") String chatId, String text) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static TelegramSendMessage from(LoggedRequest request) {
        try {
            return MAPPER.readValue(request.getBodyAsString(), TelegramSendMessage.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Failed to parse Telegram sendMessage body: " + request.getBodyAsString(), e);
        }
    }
}
```

Callers use `sendMessage.chatId()` and `sendMessage.text()` directly. Assertions about content move up to the test layer where they belong.

## When to Apply

- Any non-trivial `JsonNode` traversal in test infrastructure or adapters (≥2 `.get()` or `.path()` calls, or one call followed by `.asText()`/`.asInt()`/`.asBoolean()`)
- Any text re-parsing of a body whose structured form is already available (split on `:`, regex on emitted format, line-by-line key extraction)
- Anywhere the parser has a per-field accessor method that reconstructs the same string format the producer emits — that round-trip means the parser is mirroring production logic and testing nothing

## When NOT to Apply

- Truly unstructured/dynamic payloads where the shape varies per request (an audit log of arbitrary key-value pairs) — `JsonNode` is appropriate
- One-off extraction of a single field from a payload you don't own and can't model — a single `.get("field").asText()` is fine; don't model a 50-field external schema just to read one value
