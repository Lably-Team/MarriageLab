package org.lablyteam.marriagelab.storage.database.mongo;

import com.mongodb.*;
import org.lablyteam.marriagelab.storage.database.Database;

public class MongoDatabase extends Database {

    private MongoClient client;

    public MongoDatabase(String hostname, int port, String username, String password, String database) {
        super(hostname, port, username, password, database);
        connect();
    }

    @Override
    public void connect() {
        if (password != null && !password.trim().isEmpty()) {
            MongoCredential credential = MongoCredential.createCredential(
                    username,
                    database,
                    password.toCharArray()
            );

            this.client = new MongoClient(
                    new ServerAddress(hostname, port),
                    credential,
                    MongoClientOptions.builder().build()
            );
        } else {
            this.client = new MongoClient(
                    new ServerAddress(hostname, port),
                    MongoClientOptions.builder().build()
            );
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
