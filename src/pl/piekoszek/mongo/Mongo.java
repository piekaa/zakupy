package pl.piekoszek.mongo;

import pl.piekoszek.backend.tcp.Connection;
import pl.piekoszek.json.Piekson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Mongo {

    private Connection connection;
    private String db;

    private int requestId;

    public Mongo(Connection connection, String db) {
        this.connection = connection;
        this.db = db;
    }

    public synchronized List<Map<String, Object>> queryAll(String collection, String jsonQuery) {
        return executeQuery(jsonQuery, db, collection).getAll();
    }

    public synchronized <T> List<T> queryAll(String collection, String jsonQuery, Class<T> type) {
        return executeQuery(jsonQuery, db, collection).getAll(type);
    }

    /**
     *
     * @param id
     * @param collection
     * @param type
     * @return Item or null
     * @param <T>
     */
    public synchronized <T> T getById(String id, String collection, Class<T> type) {
        var all = executeQuery(byIdString(id), db, collection).getAll(type);
        if (all.size() == 0) {
            return null;
        }
        return executeQuery(byIdString(id), db, collection).getAll(type).get(0);
    }

    public synchronized void insert(String collection, Object toInsert) {
        MongoInsert mongoInsert = new MongoInsert(requestId++, db, collection, toInsert);
        try {
            connection.outputStream.write(mongoInsert.bytes);
        } catch (IOException e) {
            throw new MongoException(e);
        }
    }

    public synchronized void update(String collection, Object item) {
        try {
            var id = item.getClass().getField("_id").get(item);
            var query = byIdString(id.toString());
            var update = Map.of("$set", item);
            update(collection, query, Piekson.toJson(update));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void update(String collection, String query, String insertQuery) {
        MongoUpdate mongoUpdate = new MongoUpdate(requestId++, db, collection, query, insertQuery);
        try {
            connection.outputStream.write(mongoUpdate.bytes);
        } catch (IOException e) {
            throw new MongoException(e);
        }
    }

    public synchronized void deleteById(String collection, String id) {
        delete(collection, byIdString(id));
    }

    private String byIdString(String id) {
        return "{\"_id\": \"%s\"}".formatted(id);
    }

    public synchronized void delete(String collection, String query) {
        var delete = new MongoDelete(requestId++, db, collection, query);
        try {
            connection.outputStream.write(delete.bytes);
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
