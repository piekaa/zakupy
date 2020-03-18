package pl.piekoszek.json;

import java.util.Map;

public class UnsupportedElementType extends PieksonException {

    public Map<String, Object> parsedSoFar;
    public Object parsedObject;

    public UnsupportedElementType(String message, Map<String, Object> parsedSoFar) {
        super(message);
        this.parsedSoFar = parsedSoFar;
    }

    public UnsupportedElementType(String message, Object parsedObject) {
        super(message);
        this.parsedObject = parsedObject;
    }
}
