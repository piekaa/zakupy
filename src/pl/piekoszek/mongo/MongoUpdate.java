package pl.piekoszek.mongo;

import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.json.Piekson;

class MongoUpdate {

    byte[] bytes;

    MongoUpdate(int requestId, String db, String collection, String query, String updateQuery) {
        MongoHeader mongoHeader = new MongoHeader();
        mongoHeader.requestId = requestId;
        mongoHeader.opCode = 2001;

        ByteBuffer buffer = new ByteBuffer();

        buffer.addLittleEndian(0); // ZERO
        buffer.addCString(db + "." + collection);
        //todo can be set as upsert
        buffer.addLittleEndian(0); //flags

        buffer.add(Piekson.jsonToBson(query));
        buffer.add(Piekson.jsonToBson(updateQuery));

        byte[] bufferBytes = buffer.getAllBytes();
        mongoHeader.messageLength = bufferBytes.length + 16;

        bytes = new ByteBuffer().add(mongoHeader.bytes()).add(bufferBytes).getAllBytes();
    }

}
