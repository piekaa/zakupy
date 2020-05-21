package pl.piekoszek.mongo;


import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.json.Piekson;

class MongoQuery {

    byte[] bytes;

    public MongoQuery(int requestId, String query, String db, String collection) {
        MongoHeader mongoHeader = new MongoHeader();
        mongoHeader.requestId = requestId;
        mongoHeader.opCode = 2004;

        ByteBuffer buffer = new ByteBuffer();

        buffer.addLittleEndian(0); //flags
        buffer.addCString(db + "." + collection);

        buffer.addLittleEndian(0); // number of documents to skip
        buffer.addLittleEndian(100); // number of documents to return

        //BSON
        buffer.add(Piekson.jsonToBson(query));

        byte[] bufferBytes = buffer.getAllBytes();
        mongoHeader.messageLength = bufferBytes.length + 16;

        bytes = new ByteBuffer().add(mongoHeader.bytes()).add(bufferBytes).getAllBytes();
    }

}
