package pl.piekoszek.json;

class X {
    T t;
    String value;

    X(T t) {
        this.t = t;
        value = "";
    }

    X(T t, char c) {
        this.t = t;
        this.value = "" + c;
    }

    @Override
    public String toString() {
        return "X{" +
                "t=" + t +
                ", value='" + value + '\'' +
                '}';
    }
}
