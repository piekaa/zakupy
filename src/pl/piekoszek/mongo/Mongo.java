package pl.piekoszek.mongo;

import pl.piekoszek.backend.tcp.Connection;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Mongo {

    private Connection connection;

    private int requestId;

    public Mongo(Connection connection) {
        this.connection = connection;
    }

    public List<Map<String, Object>> queryAll(String jsonQuery, String db, String collection) {
        return executeQuery(jsonQuery, db, collection).getAll();
    }

    public <T> List<T> queryAll(String jsonQuery, String db, String collection, Class<T> type) {
        return executeQuery(jsonQuery, db, collection).getAll(type);
    }

    public void insert(String db, String collection, Object toInsert) {
        MongoInsert mongoInsert = new MongoInsert(requestId++, db, collection, toInsert);
        try {
            connection.outputStream.write(mongoInsert.bytes);
        } catch (IOException e) {
            throw new MongoException(e);
        }
    }

    private MongoResponse executeQuery(String jsonQuery, String db, String collection) {
        MongoQuery mongoQuery = new MongoQuery(requestId++, jsonQuery, db, collection);
        try {
            connection.outputStream.write(mongoQuery.bytes);
            MongoResponse mongoResponse = new MongoResponse(connection.inputStream);
            return mongoResponse;
        } catch (IOException e) {
            throw new MongoException(e);
        }
    }

}
