package com.j.tiimi.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reference extends AbstractPersistable<Long> {

    @OneToMany
    @Valid
    private List<Attribute> attributes;

    @NotBlank(message = "Type is required.")
    private String type;

    @Column(unique = true)
    @NotBlank
    private String identifier;

    public Reference() {
    }

    public Reference (String type, String identifier, List<Attribute> attributes) {
        this.type = type;
        this.identifier = identifier;
        this.attributes = attributes;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new ArrayList();
        }

        Attribute attribute = new Attribute();
        attribute.setKey(key);
        attribute.setValue(value);
        attributes.add(attribute);
    }

    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList();
        }

        attributes.add(attribute);
    }

    @Override
    public String toString() {
        String presentation = "type: " + type + "\n";
        for (Attribute a : attributes) {
            presentation += "key: " + a.getKey() + " value: " + a.getValue() + "\n";
        }
        return presentation;
    }
}
