package org.lablyteam.marriagelab.storage.database.mongo;

import com.mongodb.*;
import org.lablyteam.marriagelab.storage.database.Database;

public class MongoDatabase extends Database {

    private MongoClient client;

    public MongoDatabase(String hostname, int port, String username, String password, String database) {
        super(hostname, port, username, password, database);
        connect();
    }

    public MongoDatabase(String uri) {
        super(uri);
        connect();
    }

    @Override
    public void connect() {
        if (uri != null && !uri.trim().isEmpty()) {
            client = new MongoClient(new MongoClientURI(uri));
            return;
        }

        ServerAddress address = new ServerAddress(hostname, port);
        MongoClientOptions clientOptions = MongoClientOptions.builder().build();

        if (password != null && !password.trim().isEmpty()) {
            MongoCredential credential = MongoCredential.createCredential(
                    username,
                    database,
                    password.toCharArray()
            );

            this.client = new MongoClient(
                    address,
                    credential,
                    clientOptions
            );
        } else {
            this.client = new MongoClient(address, clientOptions);
        }
    }

    @Override
    public void disconnect() {
        client.close();
    }

    public MongoClient getClient() {
        return client;
    }
}
