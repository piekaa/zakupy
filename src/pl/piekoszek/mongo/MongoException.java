package pl.piekoszek.mongo;

public class MongoException extends RuntimeException{

    public MongoException() {
    }

    public MongoException(Throwable cause) {
        super(cause);
    }
}
