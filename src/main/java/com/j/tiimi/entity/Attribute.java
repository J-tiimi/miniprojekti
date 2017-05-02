package com.j.tiimi.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Attribute extends AbstractPersistable<Long> {

    @NotBlank
    private String key;

    @NotBlank
    private String value;

    public Attribute(){}

    public Attribute (String key, String value) {
        this.key = key;
        this.value = value;
    }

    //for testing purposes REDO jos jaksaa
    public Attribute setAttributes(String key, String value) {
        this.key = key;
        this.value = value;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
