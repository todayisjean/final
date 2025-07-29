package util;

import java.util.HashMap;

public class ContextManager {
    private static HashMap<String, Object> data = new HashMap<>();
    public ContextManager() {

    }

    public static Object get(String key) {
        return data.get(key);
    }

    public static void set(String key, String value) {
        data.put(key, value);
    }
}
