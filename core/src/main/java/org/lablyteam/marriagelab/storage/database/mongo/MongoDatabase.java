package org.lablyteam.marriagelab.storage.database.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.lablyteam.marriagelab.storage.database.Database;

import java.util.Arrays;

public class MongoDatabase extends Database {

    private MongoClient client;

    public MongoDatabase(String hostname, int port, String username, String password, String database, boolean ssl) {
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

            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyToSslSettings(builder -> builder.enabled(ssl))
                    .applyToClusterSettings(builder ->
                            builder.hosts(Arrays.asList(new ServerAddress(hostname, port))))
                    .build();

            this.client = MongoClients.create(settings);
        } else {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyToSslSettings(builder -> builder.enabled(ssl))
                    .applyToClusterSettings(builder ->
                            builder.hosts(Arrays.asList(new ServerAddress(hostname, port))))
                    .build();

            this.client = MongoClients.create(settings);
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
