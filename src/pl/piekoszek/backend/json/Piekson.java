package pl.piekoszek.backend.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static pl.piekoszek.backend.json.T.*;

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

        if (result.getClass().isArray()) {
            return (T) createArray(type.getComponentType(), result);
        }

        return (T) result;
    }

    private static Object parseObject(Class objectsClass, Map<String, Object> map) {

        Object instance = null;
        try {
            instance = objectsClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new PieksonException(e);
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
                    Map<String, Object>[] arr = (Map<String, Object>[]) v;
                    List collectionToSet = new ArrayList();
                    for (int i = 0; i < arr.length; i++) {
                        collectionToSet.add(parseObject((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], arr[i]));
                    }
                    if (field.getType() == Set.class) {
                        setValue(instance, field, new HashSet(collectionToSet));
                    } else if (field.getType() == List.class) {
                        setValue(instance, field, collectionToSet);
                    }
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

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (t.skipBlank && Character.isWhitespace(c)) {
                continue;
            }
            T nt = Arrays.stream(tt.get(t)).filter(e -> e.p.matcher("" + c).matches())
                    .findFirst()
                    .orElse(null);
            if (nt == null) {
                throw new PieksonException("Error parsing json at " + json.substring(0, i) + " expected any of " + Arrays.toString(tt.get(t)));
            }

            nt.stackFunction.handle(stack, mapStack, nt, c);

            t = nt;
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

        Field[] fields = object.getClass().getFields();
        StringBuilder result = new StringBuilder("{");
        try {
            for (Field field : fields) {
                Object value = field.get(object);
                if (value != null) {
                    result.append(result.length() > 1 ? "," : "");
                    result.append("\"").append(field.getName()).append("\":");
                    if (value.getClass().isArray()) {
                        result.append("[");
                        for (int i = 0; i < Array.getLength(value); i++) {
                            result.append(i > 0 ? "," : "");
                            result.append(encodeValue(Array.get(value, i)));
                        }
                        result.append("]");
                    } else if (value instanceof Collection) {
                        result.append("[");
                        int i = 0;
                        for (Object item : (Collection) value) {
                            result.append(i > 0 ? "," : "");
                            result.append(encodeValue(item));
                            i++;
                        }
                        result.append("]");
                    } else {
                        result.append(encodeValue(value));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new PieksonException(e);
        }
        return result + "}";
    }

    private static String encodeValue(Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof String) {
            return ("\"") + (value) + ("\"");
        } else {
            return toJson(value);
        }
    }

}
