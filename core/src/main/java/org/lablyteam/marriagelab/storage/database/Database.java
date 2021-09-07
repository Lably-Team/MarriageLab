package org.lablyteam.marriagelab.storage.database;

public abstract class Database {

    protected String hostname, username, password, uri, database;
    protected int port;

    public Database(String hostname, int port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public Database(String uri) {
        this.uri = uri;
    }

    public abstract void connect();
    public abstract void disconnect();
}
