package pl.piekoszek.mongo;

import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.collections.ByteReader;
import pl.piekoszek.json.Piekson;
import pl.piekoszek.json.UnsupportedElementType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MongoResponse {

    private MongoHeader mongoHeader;
    int responseFlags;
    long cursorId;
    int cursorStartingPoint;
    int numberOfDocumentsReturned;
    ByteBuffer[] documents;

    MongoResponse(InputStream inputStream) {
        try {
            ByteBuffer responseBuffer = new ByteBuffer();
            for (int i = 0; i < 4; i++) {
                int v = inputStream.read();
                if (v == -1) {
                    throw new MongoException();
                }
                responseBuffer.add((byte) v);
            }
            int size = new ByteReader(responseBuffer.getAllBytes()).readInt();

            for (int i = 0; i < size - 4; i++) {
                int v = inputStream.read();
                if (v == -1) {
                    throw new MongoException();
                }
                responseBuffer.add((byte) v);
            }

            ByteReader byteReader = new ByteReader(responseBuffer.getAllBytes());
            var header = new MongoHeader(byteReader);
            responseFlags = byteReader.readInt();
            cursorId = byteReader.readLong();
            cursorStartingPoint = byteReader.readInt();
            numberOfDocumentsReturned = byteReader.readInt();

            documents = new ByteBuffer[numberOfDocumentsReturned];

            for (int i = 0; i < numberOfDocumentsReturned; i++) {
                int documentSize = byteReader.readInt();
                documents[i] = new ByteBuffer();
                documents[i].addLittleEndian(documentSize);
                for (int j = 0; j < documentSize - 4; j++) {
                    documents[i].add(byteReader.readByte());
                }
            }
        } catch (IOException e) {
            throw new MongoException(e);
        }
    }

    List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();

        for (ByteBuffer document : documents) {
            try {
                result.add(Piekson.fromBson(document.getAllBytes()));
            } catch (UnsupportedElementType e) {
                result.add(e.parsedSoFar);
            }
        }
        return result;
    }

    <T> List<T> getAll(Class<T> type) {
        List<T> result = new ArrayList<>();

        for (ByteBuffer document : documents) {
            try {
                T obj = Piekson.fromBson(document.getAllBytes(), type);
                result.add(obj);
            } catch (UnsupportedElementType e) {
                result.add((T) e.parsedObject);
            }
        }
        return result;
    }

}
