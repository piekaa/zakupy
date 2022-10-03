package pl.piekoszek.json;

import pl.piekoszek.collections.ByteReader;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static pl.piekoszek.json.T.*;

public class Piekson {

    private static final Map<T, T[]> tt;

    static {
        tt = new HashMap<>();
        tt.put(START, new T[]{
                VALUE_BEGIN,
                VALUE_INT_FIRST,
                VALUE_BOOLEAN_FIRST,
                OBJECT_BEGIN,
                ARRAY_BEGIN,
        });
        tt.put(OBJECT_BEGIN, new T[]{
                KEY_BEGIN,
                OBJECT_END,
        });
        tt.put(OBJECT_END, new T[]{
                OBJECT_END,
                VALUES_SEPARATOR,
                ARRAY_END
        });
        tt.put(KEY_BEGIN, new T[]{
                KEY
        });
        tt.put(KEY, new T[]{
                KEY,
                KEY_END
        });
        tt.put(KEY_END, new T[]{
                KEY_VALUE_SEPARATOR
        });
        tt.put(KEY_VALUE_SEPARATOR, new T[]{
                VALUE_BEGIN,
                VALUE_INT_FIRST,
                VALUE_BOOLEAN_FIRST,
                OBJECT_BEGIN,
                ARRAY_BEGIN
        });
        tt.put(VALUE_BEGIN, new T[]{
                VALUE
        });
        tt.put(ARRAY_VALUE_BEGIN, new T[]{
                ARRAY_VALUE
        });
        tt.put(VALUE, new T[]{
                VALUE,
                VALUE_END
        });
        tt.put(ARRAY_VALUE, new T[]{
                ARRAY_VALUE,
                ARRAY_VALUE_END
        });
        tt.put(VALUE_END, new T[]{
                OBJECT_END,
                VALUES_SEPARATOR,
        });
        tt.put(ARRAY_VALUE_END, new T[]{
                ARRAY_END,
                ARRAY_VALUES_SEPARATOR,
        });
        tt.put(VALUE_INT_FIRST, new T[]{
                VALUE_INT,
                VALUE_POINT,
                VALUES_SEPARATOR,
                OBJECT_END,
                ARRAY_END
        });
        tt.put(VALUE_INT, new T[]{
                VALUE_INT,
                VALUE_POINT,
                VALUES_SEPARATOR,
                OBJECT_END,
                ARRAY_END
        });
        tt.put(VALUE_POINT, new T[]{
                VALUE_FLOAT
        });
        tt.put(VALUE_FLOAT, new T[]{
                VALUE_FLOAT,
                VALUES_SEPARATOR,
                OBJECT_END,
                ARRAY_END
        });
        tt.put(VALUES_SEPARATOR, new T[]{
                KEY_BEGIN,
                VALUE_BEGIN,
                VALUE_INT_FIRST,
                VALUE_BOOLEAN_FIRST,
                OBJECT_BEGIN,
                ARRAY_BEGIN
        });
        tt.put(ARRAY_VALUES_SEPARATOR, new T[]{
                ARRAY_VALUE_BEGIN,
                VALUE_INT_FIRST,
                VALUE_BOOLEAN_FIRST,
                OBJECT_BEGIN,
                ARRAY_BEGIN
        });
        tt.put(ARRAY_BEGIN, new T[]{
                ARRAY_VALUE_BEGIN,
                VALUE_INT_FIRST,
                VALUE_BOOLEAN_FIRST,
                OBJECT_BEGIN,
                ARRAY_BEGIN,
                ARRAY_END
        });
        tt.put(ARRAY_END, new T[]{
                OBJECT_END,
                ARRAY_END,
                VALUES_SEPARATOR,
        });
        tt.put(VALUE_BOOLEAN_FIRST, new T[]{
                VALUE_BOOLEAN_R,
                VALUE_BOOLEAN_A
        });
        tt.put(VALUE_BOOLEAN_R, new T[]{
                VALUE_BOOLEAN_U,
        });
        tt.put(VALUE_BOOLEAN_U, new T[]{
                VALUE_BOOLEAN_E,
        });
        tt.put(VALUE_BOOLEAN_A, new T[]{
                VALUE_BOOLEAN_L,
        });
        tt.put(VALUE_BOOLEAN_L, new T[]{
                VALUE_BOOLEAN_S,
        });
        tt.put(VALUE_BOOLEAN_S, new T[]{
                VALUE_BOOLEAN_E,
        });
        tt.put(VALUE_BOOLEAN_E, new T[]{
                VALUES_SEPARATOR,
                OBJECT_END,
                ARRAY_END
        });


    }

    public static Map<String, Object> fromJson(String json) {
        return (Map<String, Object>) parseJson(json);
    }

    public static <T> T fromJson(String json, Class<T> type) {

        Object result = parseJson(json);
        if (result instanceof Map) {
            return (T) parseObject(type, (Map<String, Object>) result);
        }
        if (result instanceof Long && type.isAssignableFrom(Integer.class)) {
            return (T) (Integer) ((Number) result).intValue();
        }

        if (result instanceof Double && type.isAssignableFrom(Float.class)) {
            return (T) (Float) ((Number) result).floatValue();
        }

        if (type.isArray()) {
            return (T) createArray(type.getComponentType(), result);
        }

        if (Collection.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("for collections use fromJson(String json, Class<T> type, Class<T> elementType)");
        }

        return (T) result;
    }

    public static <T, Y> T fromJson(String json, Class<T> type, Class<Y> elementType) {
        if (!Collection.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Only collections");
        }
        Object result = parseJson(json);
        return (T) createCollection(type, elementType, result);
    }

    private static Object parseObject(Class objectsClass, Map<String, Object> map) {

        Object instance = null;
        try {
            instance = objectsClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PieksonException(e);
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            try {
                Field field = instance.getClass().getField(k);
                if (v instanceof Map) {
                    setValue(instance, field, parseObject(field.getType(), (Map<String, Object>) v));
                } else if (field.getType().isArray()) {
                    setValue(instance, field, createArray(field.getType().getComponentType(), v));
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    Object collection = createCollection(field.getType(), (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], v);
                    setValue(instance, field, collection);
                } else {
                    setValue(instance, field, v);
                }
            } catch (NoSuchFieldException e) {
                // it's ok
            }
        }
        return instance;
    }

    static Object createArray(Class<?> componentType, Object array) {
        Object arrayToSet = Array.newInstance(componentType, Array.getLength(array));
        Object[] arr = (Object[]) array;
        for (int i = 0; i < arr.length; i++) {
            if (componentType == int.class) {
                Array.setInt(arrayToSet, i, ((Number) arr[i]).intValue());
            } else if (componentType == long.class) {
                Array.setLong(arrayToSet, i, (long) arr[i]);
            } else if (componentType == double.class) {
                Array.setDouble(arrayToSet, i, (double) arr[i]);
            } else if (componentType == float.class) {
                Array.setFloat(arrayToSet, i, ((Number) arr[i]).floatValue());
            } else if (componentType == boolean.class) {
                Array.setBoolean(arrayToSet, i, (boolean) arr[i]);
            } else if (componentType == Integer.class) {
                Array.set(arrayToSet, i, ((Long) arr[i]).intValue());
            } else if (componentType == Long.class) {
                Array.set(arrayToSet, i, arr[i]);
            } else if (componentType == Double.class) {
                Array.set(arrayToSet, i, arr[i]);
            } else if (componentType == Float.class) {
                Array.set(arrayToSet, i, ((Double) arr[i]).floatValue());
            } else if (componentType == Boolean.class) {
                Array.set(arrayToSet, i, arr[i]);
            } else if (array instanceof HashMap[]) {
                Array.set(arrayToSet, i, parseObject(componentType, (Map<String, Object>) arr[i]));
            } else {
                Array.set(arrayToSet, i, arr[i]);
            }
        }
        return arrayToSet;
    }

    private static Object createCollection(Class<?> type, Class<?> componentType, Object collection) {

        List collectionToSet = new ArrayList();
        if (collection instanceof Map[]) {
            Map<String, Object>[] arr = (Map<String, Object>[]) collection;
            for (int i = 0; i < arr.length; i++) {
                collectionToSet.add(parseObject(componentType, arr[i]));
            }
        } else {
            Object array = createArray(collection.getClass().getComponentType(), collection);
            for (int i = 0; i < Array.getLength(array); i++) {
                collectionToSet.add(Array.get(array, i));
            }
        }
        if (type == Set.class) {
            return new HashSet(collectionToSet);
        }
        return collectionToSet;
    }

    private static void setValue(Object object, Field field, Object value) {
        try {
            if (value instanceof Number) {
                if (field.getType() == int.class) {
                    field.set(object, ((Number) value).intValue());
                } else if (field.getType() == long.class) {
                    field.set(object, ((Number) value).longValue());
                } else if (field.getType() == double.class) {
                    field.set(object, ((Number) value).doubleValue());
                } else if (field.getType() == float.class) {
                    field.set(object, ((Number) value).floatValue());
                }
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new PieksonException(e);
        }
    }

    private static Object parseJson(String json) {
        T t = START;

        ArrayList<X> stack = new ArrayList<>();
        Stack<Map<String, Object>> mapStack = new Stack<>();

        stack.add(new X(KEY_BEGIN, 'v'));
        mapStack.push(new HashMap<>());

        boolean escape = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (t.skipBlank && Character.isWhitespace(c)) {
                continue;
            }

            if (t.escapable && c == '\\') {
                escape = true;
                continue;
            }
            T nt;
            if (escape) {
                nt = t;
            } else {
                nt = Arrays.stream(tt.get(t)).filter(e -> e.p.matcher("" + c).matches())
                        .findFirst()
                        .orElse(null);
                if (nt == null) {
                    throw new PieksonException("Error parsing json at " + json.substring(0, i) + " index: " + i + " expected any of " + Arrays.toString(tt.get(t)));
                }
            }

            nt.stackFunction.handle(stack, mapStack, nt, c);

            t = nt;
            escape = false;
        }

        if (!stack.isEmpty()) {
            new SetValueIfExistsAndNotArray().handle(stack, mapStack, stack.get(stack.size() - 1).t, ' ');
        }

        return mapStack.peek().get("v");
    }

    public static String toJson(Object object) {

        if (object instanceof Number || object instanceof Boolean || object instanceof String) {
            return encodeValue(object);
        }

        if (object instanceof Collection) {
            return encodeCollection((Collection) object);
        }

        if (object.getClass().isArray()) {
            return encodeArray(object);
        }

        if (object instanceof Map) {
            return encodeMap((Map<String, Object>) object);
        }

        Field[] fields = object.getClass().getFields();
        StringBuilder result = new StringBuilder("{");

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                encodeObjectValueAndAppend(result, field.getName(), value);
            }
        } catch (IllegalAccessException e) {
            throw new PieksonException(e);
        }
        return result.append("}").toString();
    }

    private static String encodeMap(Map<String, Object> map) {
        StringBuilder result = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            encodeObjectValueAndAppend(result, entry.getKey(), entry.getValue());
        }
        return result.append("}").toString();
    }

    private static String encodeCollection(Collection collection) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        int i = 0;
        for (Object item : collection) {
            result.append(i > 0 ? "," : "");
            result.append(encodeValue(item));
            i++;
        }
        result.append("]");
        return result.toString();
    }

    private static String encodeArray(Object array) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < Array.getLength(array); i++) {
            result.append(i > 0 ? "," : "");
            result.append(encodeValue(Array.get(array, i)));
        }
        result.append("]");
        return result.toString();
    }

    private static void encodeObjectValueAndAppend(StringBuilder jsonFragment, String key, Object value) {
        if (value != null) {
            jsonFragment.append(jsonFragment.length() > 1 ? "," : "");
            jsonFragment.append("\"").append(key).append("\":");
            if (value.getClass().isArray()) {
                jsonFragment.append(encodeArray(value));
            } else if (value instanceof Collection) {
                jsonFragment.append(encodeCollection((Collection) value));
            } else if (value instanceof Map) {
                jsonFragment.append(encodeMap((Map<String, Object>) value));
            } else {
                jsonFragment.append(encodeValue(value));
            }
        }
    }

    public static byte[] toBson(Map<String, Object> map) {
        return toBsonStep(map).get();
    }

    private static Bson toBsonStep(Map<String, Object> map) {
        Bson bson = new Bson();
        for (Map.Entry<String, Object> kv : map.entrySet()) {
            if (kv.getValue() == null) {
                bson.addNull(kv.getKey());
            } else if (kv.getValue() instanceof Integer) {
                bson.add(kv.getKey(), (Integer) kv.getValue());
            } else if (kv.getValue() instanceof Long) {
                bson.add(kv.getKey(), (Long) kv.getValue());
            } else if (kv.getValue() instanceof Double) {
                bson.add(kv.getKey(), (Double) kv.getValue());
            } else if (kv.getValue() instanceof Boolean) {
                bson.add(kv.getKey(), (Boolean) kv.getValue());
            } else if (kv.getValue() instanceof String) {
                bson.add(kv.getKey(), (String) kv.getValue());
            } else if (kv.getValue() instanceof Map) {
                bson.addObject(kv.getKey(), toBsonStep((Map) kv.getValue()));
            } else if (kv.getValue().getClass().isArray()) {
                bson.addArray(kv.getKey(), toBsonArrayStep((Object[]) kv.getValue()));
            }
        }
        return bson;
    }


    private static Bson toBsonStep(Object obj) {
        Bson bson = new Bson();

        for (Field field : obj.getClass().getFields()) {
            try {
                Object value = field.get(obj);
                String name = field.getName();
                if (value == null) {
                    bson.addNull(field.getName());
                } else if (value instanceof Integer) {
                    bson.add(name, (Integer) value);
                } else if (value instanceof Long) {
                    bson.add(name, (Long) value);
                } else if (value instanceof Double) {
                    bson.add(name, (Double) value);
                } else if (value instanceof Boolean) {
                    bson.add(name, (Boolean) value);
                } else if (value instanceof String) {
                    bson.add(name, (String) value);
                } else if (value instanceof Map) {
                    bson.addObject(name, toBsonStep((Map) value));
                } else if (value.getClass().isArray()) {
                    bson.addArray(name, toBsonArrayStep((Object[]) value));
                } else {
                    bson.addObject(name, toBsonStep(value));
                }
            } catch (IllegalAccessException e) {
                throw new PieksonException(e);
            }
        }

        return bson;
    }


    private static Bson toBsonArrayStep(Object[] array) {
        Bson bson = new Bson(true);

        for (int i = 0; i < Array.getLength(array); i++) {

            Object value = Array.get(array, i);

            if (value instanceof Integer) {
                bson.addArrayItem((Integer) value);
            } else if (value instanceof Long) {
                bson.addArrayItem((Long) value);
            } else if (value instanceof Double) {
                bson.addArrayItem((Double) value);
            } else if (value instanceof String) {
                bson.addArrayItem((String) value);
            } else if (value instanceof Map) {
                bson.addObjectToArray(toBsonStep((Map) value));
            } else if (value.getClass().isArray()) {
                bson.addArrayToArray(toBsonArrayStep((Object[]) value));
            }
        }
        return bson;
    }

    public static Map<String, Object> fromBson(byte[] bson) {
        return fromBsonStep(new ByteReader(bson));
    }

    public static <T> T fromBson(byte[] bson, Class<T> type) {
        try {
            return (T) parseObject(type, fromBsonStep(new ByteReader(bson)));
        } catch (UnsupportedElementType e) {
            e.parsedObject = parseObject(type, e.parsedSoFar);
            throw e;
        }
    }

    private static Map<String, Object> fromBsonStep(ByteReader byteReader) {
        Map<String, Object> result = new HashMap<>();
        int size = byteReader.readInt();
        for (; ; ) {
            byte elementType = byteReader.readByte();
            if (elementType == 0) {
                break;
            }
            if (elementType == Bson.INT) {
                result.put(byteReader.readCString(), byteReader.readInt());
            } else if (elementType == Bson.LONG) {
                result.put(byteReader.readCString(), byteReader.readLong());
            } else if (elementType == Bson.DOUBLE) {
                result.put(byteReader.readCString(), byteReader.readDouble());
            } else if (elementType == Bson.BOOLEAN) {
                result.put(byteReader.readCString(), byteReader.readBoolean());
            } else if (elementType == Bson.STRING) {
                result.put(byteReader.readCString(), byteReader.readString());
            } else if (elementType == Bson.OBJECT) {
                result.put(byteReader.readCString(), fromBsonStep(byteReader));
            } else if (elementType == Bson.ARRAY) {
                result.put(byteReader.readCString(), fromBsonStepArray(byteReader));
            } else {
                throw new UnsupportedElementType("Unsupported element type: " + elementType, result);
            }
        }
        return result;
    }

    private static Object[] fromBsonStepArray(ByteReader byteReader) {
        List<Object> result = new ArrayList<>();
        int size = byteReader.readInt();
        for (; ; ) {
            byte elementType = byteReader.readByte();
            if (elementType == 0) {
                break;
            }
            if (elementType == Bson.INT) {
                byteReader.readCString();
                result.add(byteReader.readInt());
            }
            if (elementType == Bson.LONG) {
                byteReader.readCString();
                result.add(byteReader.readLong());
            }
            if (elementType == Bson.DOUBLE) {
                byteReader.readCString();
                result.add(byteReader.readDouble());
            }
            if (elementType == Bson.BOOLEAN) {
                byteReader.readCString();
                result.add(byteReader.readBoolean());
            }
            if (elementType == Bson.STRING) {
                byteReader.readCString();
                result.add(byteReader.readString());
            }
            if (elementType == Bson.OBJECT) {
                byteReader.readCString();
                result.add(fromBsonStep(byteReader));
            }
            if (elementType == Bson.ARRAY) {
                byteReader.readCString();
                result.add(fromBsonStepArray(byteReader));
            }
        }
        return result.toArray();
    }

    public static byte[] toBson(Object object) {
        return toBsonStep(object).get();
    }

    public static byte[] jsonToBson(String json) {
        return toBson(fromJson(json));
    }

    private static String encodeValue(Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof String) {
            return ("\"") + (((String) value).replaceAll("\"", "\\\\\"") + ("\""));
        } else {
            return toJson(value);
        }
    }

}
