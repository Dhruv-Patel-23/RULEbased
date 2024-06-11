package org.example;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    public String disclaimer;
    public String terms;
    public String license;
    public String last_updated;
    public Results results;
}

