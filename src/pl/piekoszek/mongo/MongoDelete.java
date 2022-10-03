package pl.piekoszek.mongo;

import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.json.Piekson;

class MongoDelete {

    byte[] bytes;

    MongoDelete(int requestId, String db, String collection, String query) {
        MongoHeader mongoHeader = new MongoHeader();
        mongoHeader.requestId = requestId;
        mongoHeader.opCode = 2006;

        ByteBuffer buffer = new ByteBuffer();

        buffer.addLittleEndian(0); // ZERO
        buffer.addCString(db + "." + collection);
        buffer.addLittleEndian(0); //flags

        //BSON
        buffer.add(Piekson.jsonToBson(query));

        byte[] bufferBytes = buffer.getAllBytes();
        mongoHeader.messageLength = bufferBytes.length + 16;

        bytes = new ByteBuffer().add(mongoHeader.bytes()).add(bufferBytes).getAllBytes();
    }

}
