package pl.piekoszek.json;

import java.lang.reflect.Array;
import java.util.*;

abstract class StackFunction {
    abstract void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c);

    void setValue(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, X key, X value) {
        if (value.t == T.VALUE_INT_FIRST || value.t == T.VALUE_BEGIN || value.t == T.VALUE_BOOLEAN_FIRST) {
            mapStack.peek().put(key.value, decodeValue(value, mapStack));
            stack.remove(stack.size() - 1);
            stack.remove(stack.size() - 1);
        }
    }

    Object decodeValue(X value, Stack<Map<String, Object>> mapStack) {
        if (value.t == T.VALUE_INT_FIRST) {
            if (value.value.contains(".")) {
                return Double.parseDouble(value.value);
            } else {
                return Long.parseLong(value.value);
            }
        } else if (value.t == T.VALUE_BEGIN) {
            return value.value;
        } else if (value.t == T.VALUE_BOOLEAN_FIRST) {
            return Boolean.parseBoolean(value.value);
        } else if (value.t == T.OBJECT_BEGIN) {
            return mapStack.pop();
        }
        return value.value;
    }

}

class CreateObject extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {
        stack.add(new X(t));
        mapStack.push(new HashMap<>());
    }
}

class Push extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {
        stack.add(new X(t));
    }
}

class Append extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {
        stack.get(stack.size() - 1).value += c;
    }
}

class PushAndAppend extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {
        stack.add(new X(t, c));
    }
}

class SetValueIfExistsAndNotArray extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {

        X key = stack.get(stack.size() - 2);
        X value = stack.get(stack.size() - 1);

        setValue(stack, mapStack, key, value);

        if (t == T.OBJECT_END) {
            key = stack.get(stack.size() - 2);
            if (key.t != T.KEY_BEGIN) { //in case it's array
                return;
            }
            Map<String, Object> obj = mapStack.pop();
            mapStack.peek().put(key.value, obj);
            stack.remove(stack.size() - 1);
            stack.remove(stack.size() - 1);
        }
    }
}


class SetIfNotArray extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {

        if (stack.size() < 2) {
            return;
        }

        X key = stack.get(stack.size() - 2);
        X value = stack.get(stack.size() - 1);

        if (key.t != T.KEY_BEGIN) {
            return;
        }

        setValue(stack, mapStack, key, value);
    }
}

class SetArrayValue extends StackFunction {

    @Override
    void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {
        List<Object> array = new ArrayList<>();
        Class<?> valueClass = Object.class;
        while (stack.get(stack.size() - 1).t != T.ARRAY_BEGIN) {
            X value = stack.get(stack.size() - 1);
            Object decodedValue = decodeValue(value, mapStack);
            valueClass = decodedValue.getClass();
            array.add(decodedValue);
            stack.remove(stack.size() - 1);
        }
        Object[] resultArray = (Object[]) Array.newInstance(valueClass, array.size());
        for (int i = 0; i < array.size(); i++) {
            resultArray[i] = array.get(array.size() - 1 - i);
        }
        stack.remove(stack.size() - 1);
        X key = stack.get(stack.size() - 1);
        mapStack.peek().put(key.value, resultArray);
        stack.remove(stack.size() - 1);
    }
}

class Idle extends StackFunction {
    @Override
    public void handle(ArrayList<X> stack, Stack<Map<String, Object>> mapStack, T t, char c) {

    }
}