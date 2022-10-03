package pl.piekoszek.json;

import pl.piekoszek.collections.ByteBuffer;

import java.util.Stack;

class Bson {

    static final byte INT = 0x10;
    static final byte LONG = 0x12;
    static final byte DOUBLE = 0x01;
    static final byte STRING = 0x02;
    static final byte OBJECT = 0x03;
    static final byte BOOLEAN = 0x08;
    static final byte OBJECT_END = 0x00;
    static final byte ARRAY = 0x04;
    static final byte NULL = 0x0A;

    private ByteBuffer byteBuffer = new ByteBuffer();

    private Stack<Integer> arrays = new Stack<>();

    private boolean isArray;

    Bson(boolean isArray) {
        this.isArray = isArray;

        if (isArray) {
            arrays.push(0);
        }
    }

    Bson() {
    }

    Bson addNull(String name) {
        byteBuffer.add(NULL).addCString(name);
        return this;
    }

    Bson add(String name, int value) {
        byteBuffer.add(INT).addCString(name).addLittleEndian(value);
        return this;
    }

    Bson add(String name, long value) {
        byteBuffer.add(LONG).addCString(name).addLittleEndian(value);
        return this;
    }

    Bson add(String name, double value) {
        byteBuffer.add(DOUBLE).addCString(name).addLittleEndian(value);
        return this;
    }

    Bson add(String name, String value) {
        byteBuffer.add(STRING).addCString(name).addLengthAndString(value);
        return this;
    }

    Bson add(String name, boolean value) {
        byteBuffer.add(BOOLEAN).addCString(name).addByte((byte) (value ? 1 : 0));
        return this;
    }

    Bson addObject(String name, Bson object) {
        byteBuffer.add(OBJECT).addCString(name).add(object.get());
        return this;
    }

    Bson addArray(String name, Bson array) {
        byteBuffer.add(ARRAY).addCString(name).add(array.get());
        return this;
    }

    Bson endObject() {
        byteBuffer.add(OBJECT_END);
        return this;
    }

    Bson endArray() {
        return endObject();
    }

    Bson addArrayItem(int value) {
        byteBuffer.add((byte) INT).addCString(arrays.peek().toString()).addLittleEndian(value);
        arrays.push(arrays.pop() + 1);
        return this;
    }

    Bson addArrayItem(long value) {
        byteBuffer.add((byte) LONG).addCString(arrays.peek().toString()).addLittleEndian(value);
        arrays.push(arrays.pop() + 1);
        return this;
    }

    Bson addArrayItem(double value) {
        byteBuffer.add((byte) DOUBLE).addCString(arrays.peek().toString()).addLittleEndian(value);
        arrays.push(arrays.pop() + 1);
        return this;
    }

    Bson addArrayItem(String value) {
        byteBuffer.add(STRING).addCString(arrays.peek().toString()).addLengthAndString(value);
        arrays.push(arrays.pop() + 1);
        return this;
    }

    Bson addObjectToArray(Bson object) {
        byteBuffer.add(OBJECT).addCString(arrays.peek().toString()).add(object.get());
        arrays.push(arrays.pop() + 1);
        return this;
    }

    Bson addArrayToArray(Bson array) {
        byteBuffer.add(ARRAY).addCString(arrays.peek().toString()).add(array.get());
        arrays.push(arrays.pop() + 1);
        return this;
    }

    byte[] get() {
        byte[] bytes = byteBuffer.getAllBytes();
        return new ByteBuffer().addLittleEndian(bytes.length + 5)
                .add(bytes).add((byte) 0).getAllBytes();
    }

}
