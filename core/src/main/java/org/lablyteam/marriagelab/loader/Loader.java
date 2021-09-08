package org.lablyteam.marriagelab.loader;

public interface Loader {

    void load();
    default void unload() {}
}
