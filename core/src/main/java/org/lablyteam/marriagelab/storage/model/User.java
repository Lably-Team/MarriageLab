package org.lablyteam.marriagelab.storage.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.lablyteam.marriagelab.gender.Gender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity("User") @SerializableAs("User")
public class User implements ConfigurationSerializable {

    @Id
    private final UUID uuid;
    private Gender gender;
    private String partner;

    public User() {
        this.uuid = UUID.randomUUID();
        this.gender = Gender.UNSPECIFIED;
        this.partner = "";
    }

    public User(UUID uuid) {
        this.uuid = uuid;
        this.gender = Gender.UNSPECIFIED;
        this.partner = "";
    }

    public User(UUID uuid, Gender gender) {
        this.uuid = uuid;
        this.gender = gender;
        this.partner = "";
    }

    public User(UUID uuid, Gender gender, String partner) {
        this.uuid = uuid;
        this.gender = gender;
        this.partner = partner;
    }

    public User(Map<String, Object> serial) {
        this(
                UUID.fromString(serial.get("uuid").toString()),
                Gender.valueOf(serial.get("gender").toString()),
                serial.get("partner").toString()
        );
    }

    public UUID getUUID() {
        return uuid;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPartner() {
        return partner;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serial = new HashMap<>();
        serial.put("uuid", uuid.toString());
        serial.put("gender", gender.name().toUpperCase());
        serial.put("partner", partner);
        return serial;
    }

    public static User deserialize(Map<String, Object> serial) {
        return new User(serial);
    }

    public static User valueOf(Map<String, Object> serial) {
        return new User(serial);
    }
}
