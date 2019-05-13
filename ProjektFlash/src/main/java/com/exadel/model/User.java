package com.exadel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "user")
public class User {
    private String id;
    private String login;
    private String pass;
    private boolean activate;
    private String[] role;
    private Object contact;

}
