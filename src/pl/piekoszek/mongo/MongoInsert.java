package pl.piekoszek.mongo;

import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.json.Piekson;

class MongoInsert {

    byte[] bytes;

    MongoInsert(int requestId, String db, String collection, Object toInsert) {
        MongoHeader mongoHeader = new MongoHeader();
        mongoHeader.requestId = requestId;
        mongoHeader.opCode = 2002;

        ByteBuffer buffer = new ByteBuffer();

        buffer.addLittleEndian(0); //flags
        buffer.add((db + "." + collection).getBytes());
        buffer.add((byte) 0); // 0 after collection

        buffer.add(Piekson.toBson(toInsert));

        byte[] bufferBytes = buffer.getAllBytes();
        mongoHeader.messageLength = bufferBytes.length + 16;

        bytes = new ByteBuffer().add(mongoHeader.bytes()).add(bufferBytes).getAllBytes();
    }

}
