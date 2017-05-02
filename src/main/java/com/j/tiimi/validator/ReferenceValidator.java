package com.j.tiimi.validator;

import com.j.tiimi.entity.Attribute;
import com.j.tiimi.entity.Reference;
import com.j.tiimi.repository.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by riku on 5.4.2017.
 */
public abstract class ReferenceValidator implements Validator {

    private Set<String> requiredKeys;
    private Set<String> optionalKeys;
    private Map<String, String> aliases;

    private static final Set<String> integerFields = new HashSet<>(Arrays.asList(new String[] {
            "YEAR",
            "CHAPTER"
    }));

    public Set<String> getRequiredKeys() {
        return requiredKeys;
    }

    public void setRequiredKeys(Set<String> requiredKeys) {
        this.requiredKeys = requiredKeys;
    }

    public Set<String> getOptionalKeys() {
        return optionalKeys;
    }

    public void setOptionalKeys(Set<String> optionalKeys) {
        this.optionalKeys = optionalKeys;
    }

    public void setAliases(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    @Autowired
    private ReferenceRepository referenceRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Reference.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reference reference = (Reference) target;

        if (reference.getIdentifier() == null || reference.getIdentifier().trim().isEmpty()) {
            errors.reject(null, "Identifier can't be empty.");
        }

        if (!referenceRepository.findByIdentifier(reference.getIdentifier()).isEmpty()) {
            errors.reject(null, "Identifier should be unique.");
        }

        if (reference.getAttributes() == null) {
            errors.reject(null,reference.getType() + " reference should have attributes.");
            return;
        }

        validateAttributeKeys(reference.getAttributes(), errors);
        validateAttributeValues(reference.getAttributes(), errors);

    }

    private void validateAttributeKeys(List<Attribute> attributes, Errors errors) {
        Set<String> attributeKeys = attributes.stream()
                .map(a -> {
                    if (aliases.containsKey(a.getKey().toUpperCase())) {
                        return aliases.get(a.getKey().toUpperCase());
                    }
                    return a.getKey().toUpperCase();
                }).collect(Collectors.toSet());

        requiredKeys.forEach(requiredKey -> {
            if (!attributeKeys.contains(requiredKey)) {
                errors.reject(null,"Field " + requiredKey + " is required");
            }
        });

        attributeKeys.removeAll(requiredKeys);
        attributeKeys.removeAll(optionalKeys);
        attributeKeys.forEach(a -> errors.reject(null,a + " isn't a valid field."));
    }

    private void validateAttributeValues(List<Attribute> attributes, Errors errors) {
        attributes.stream().forEach(a -> {
            if (a.getValue() == null || a.getValue().trim().isEmpty()) {
               errors.reject(null,a.getKey().toUpperCase() + " can't be empty.");
            }
        });

        List<Attribute> integerAttributes = attributes.stream().filter(a ->
                        integerFields.contains(a.getKey().toUpperCase())
                ).collect(Collectors.toList());

        for (Attribute a : integerAttributes) {
            if (!isInteger(a.getValue())) {
                errors.reject(null, a.getKey() + " should be an integer.");
            }
        }

    }

    private boolean isInteger(String value) {
        try {
            Integer.parseUnsignedInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
