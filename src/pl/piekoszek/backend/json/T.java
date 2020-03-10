package pl.piekoszek.backend.json;

import java.util.regex.Pattern;

enum T {
    START(Pattern.compile(""), true, false, new Idle()),
    OBJECT_BEGIN(Pattern.compile("\\{"), true, false, new CreateObject()),
    OBJECT_END(Pattern.compile("}"), true, false, new SetValueIfExistsAndNotArray()),
    ARRAY_BEGIN(Pattern.compile("\\["), true, false, new Push()),
    ARRAY_END(Pattern.compile("]"), true, false, new SetArrayValue()),
    VALUE_BEGIN(Pattern.compile("\""), false, true, new Push()),
    ARRAY_VALUE_BEGIN(Pattern.compile("\""), false, true, new Push()),
    VALUE(Pattern.compile("[^\\\\\"]"), false, true, new Append()),
    ARRAY_VALUE(Pattern.compile("[^\\\\\"]"), false, true, new Append()),
    VALUE_INT_FIRST(Pattern.compile("[0-9-]"), false, false, new PushAndAppend()),
    VALUE_POINT(Pattern.compile("\\."), false, false, new Append()),
    VALUE_INT(Pattern.compile("[0-9]"), true, false, new Append()),
    VALUE_FLOAT(Pattern.compile("[0-9]"), true, false, new Append()),
    VALUE_END(Pattern.compile("\""), true, false, new SetValueIfExistsAndNotArray()), // think about arrays
    ARRAY_VALUE_END(Pattern.compile("\""), true, false, new SetValueIfExistsAndNotArray()), // think about arrays
    KEY_BEGIN(Pattern.compile("\""), false, false, new Push()),
    KEY(Pattern.compile("[a-zA-Z0-9]"), false, false, new Append()),
    KEY_END(Pattern.compile("\""), true, false, new Idle()),
    KEY_VALUE_SEPARATOR(Pattern.compile(":"), true, false, new Idle()),
    VALUES_SEPARATOR(Pattern.compile(","), true, false, new SetIfNotArray()),
    ARRAY_VALUES_SEPARATOR(Pattern.compile(","), true, false, new SetIfNotArray()),
    VALUE_BOOLEAN_FIRST(Pattern.compile("[tf]"), false, false, new PushAndAppend()),
    VALUE_BOOLEAN_R(Pattern.compile("r"), false, false, new Append()),
    VALUE_BOOLEAN_U(Pattern.compile("u"), false, false, new Append()),
    VALUE_BOOLEAN_A(Pattern.compile("a"), false, false, new Append()),
    VALUE_BOOLEAN_L(Pattern.compile("l"), false, false, new Append()),
    VALUE_BOOLEAN_S(Pattern.compile("s"), false, false, new Append()),
    VALUE_BOOLEAN_E(Pattern.compile("e"), true, false, new Append());

    Pattern p;
    boolean skipBlank;
    boolean escapable;
    StackFunction stackFunction;

    T(Pattern p, boolean skipBlank, boolean escapable, StackFunction stackFunction) {
        this.p = p;
        this.skipBlank = skipBlank;
        this.escapable = escapable;
        this.stackFunction = stackFunction;
    }
}
