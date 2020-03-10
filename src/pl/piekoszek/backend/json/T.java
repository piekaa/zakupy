package pl.piekoszek.backend.json;

import java.util.regex.Pattern;

enum T {
    START(Pattern.compile(""), true, new Idle()),
    OBJECT_BEGIN(Pattern.compile("\\{"), true, new CreateObject()),
    OBJECT_END(Pattern.compile("}"), true, new SetValueIfExistsAndNotArray()),
    ARRAY_BEGIN(Pattern.compile("\\["), true, new Push()),
    ARRAY_END(Pattern.compile("]"), true, new SetArrayValue()),
    VALUE_BEGIN(Pattern.compile("\""), false, new Push()),
    ARRAY_VALUE_BEGIN(Pattern.compile("\""), false, new Push()),
    VALUE(Pattern.compile("[^\\\\\"]"), false, new Append()),
    ARRAY_VALUE(Pattern.compile("[^\\\\\"]"), false, new Append()),
    VALUE_INT_FIRST(Pattern.compile("[0-9-]"), false, new PushAndAppend()),
    VALUE_POINT(Pattern.compile("\\."), false, new Append()),
    VALUE_INT(Pattern.compile("[0-9]"), true, new Append()),
    VALUE_FLOAT(Pattern.compile("[0-9]"), true, new Append()),
    VALUE_END(Pattern.compile("\""), true, new SetValueIfExistsAndNotArray()), // think about arrays
    ARRAY_VALUE_END(Pattern.compile("\""), true, new SetValueIfExistsAndNotArray()), // think about arrays
    KEY_BEGIN(Pattern.compile("\""), false, new Push()),
    KEY(Pattern.compile("[a-zA-Z0-9]"), false, new Append()),
    KEY_END(Pattern.compile("\""), true, new Idle()),
    KEY_VALUE_SEPARATOR(Pattern.compile(":"), true, new Idle()),
    VALUES_SEPARATOR(Pattern.compile(","), true, new SetIfNotArray()),
    ARRAY_VALUES_SEPARATOR(Pattern.compile(","), true, new SetIfNotArray()),
    VALUE_BOOLEAN_FIRST(Pattern.compile("[tf]"), false, new PushAndAppend()),
    VALUE_BOOLEAN_R(Pattern.compile("r"), false, new Append()),
    VALUE_BOOLEAN_U(Pattern.compile("u"), false, new Append()),
    VALUE_BOOLEAN_A(Pattern.compile("a"), false, new Append()),
    VALUE_BOOLEAN_L(Pattern.compile("l"), false, new Append()),
    VALUE_BOOLEAN_S(Pattern.compile("s"), false, new Append()),
    VALUE_BOOLEAN_E(Pattern.compile("e"), true, new Append());

    Pattern p;
    boolean skipBlank;
    StackFunction stackFunction;

    T(Pattern p, boolean skipBlank, StackFunction stackFunction) {
        this.p = p;
        this.skipBlank = skipBlank;
        this.stackFunction = stackFunction;
    }
}
