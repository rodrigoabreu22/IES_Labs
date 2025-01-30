package ie.deti.ua.com.lab5_3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String nMec;
    private String generatedNumber;
    private String type;

    public Message() {}

    @JsonCreator
    public Message(
            @JsonProperty("nMec") String nMec,
            @JsonProperty("generatedNumber") String generatedNumber,
            @JsonProperty("type") String type
    ) {
        this.nMec = nMec;
        this.generatedNumber = generatedNumber;
        this.type = type;
    }

    public String getNMec() {
        return nMec;
    }

    public void setNMec(String nMec) {
        this.nMec = nMec;
    }

    public String getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(String generatedNumber) {
        this.generatedNumber = generatedNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "From: " + this.getNMec() + ", generatedNumber: " + this.getGeneratedNumber() + ", type: " + this.getType();
    }
}
