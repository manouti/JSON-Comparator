package com.manouti.jsoncomparator.internal;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

enum Type {

    EXCLUDE("exclude", "ignore"), NUMERIC("numeric", "num");

    private String[] keys;

    Type(String... keys) {
        this.keys = keys;
    }

    @JsonCreator
    public static Type fromString(String key) {
        if(key == null) {
            return null;
        }
        Optional<Type> matchedType = EnumSet.allOf(Type.class).stream()
               .filter(type -> Arrays.stream(type.keys).anyMatch(key::equals))
               .findFirst();
        return matchedType.orElseThrow(() -> new IllegalArgumentException("Unknown tolerance type: " + key));
    }

}
