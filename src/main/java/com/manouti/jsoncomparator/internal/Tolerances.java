package com.manouti.jsoncomparator.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public final class Tolerances {

    private final Tolerance[] tolerances;

    public static Tolerances fromFile(Path path) throws IOException {
        var tolerances = new ObjectMapper().registerModule(new Jdk8Module())
                                           .readValue(Files.readAllBytes(path), Tolerance[].class);
        return new Tolerances(tolerances);
    }

    Tolerances(Tolerance[] tolerances) {
        this.tolerances = tolerances;
    }

    public void apply(DifferencesImpl differences) {
        applyOnLeftoverDifferences(differences, false);
        applyOnLeftoverDifferences(differences, true);
        applyOnDifferences(differences);
    }

    private void applyOnLeftoverDifferences(DifferencesImpl differences, boolean left) {
        var diffMap = left ? differences.entriesOnlyOnLeft : differences.entriesOnlyOnRight;
        var iterator = diffMap.entrySet().iterator();
        while(iterator.hasNext()) {
            var entry = iterator.next();
            Optional<Tolerance> tolerance = Arrays.stream(tolerances)
                                                  .filter(t -> t.getPath().equals(entry.getKey()))
                                                  .findAny();
            if(tolerance.isPresent() && tolerance.get().getType() == Type.EXCLUDE) {
                iterator.remove();
                if(left) {
                    differences.ignoredEntriesOnlyOnLeft.put(entry.getKey(), entry.getValue());
                } else {
                    differences.ignoredEntriesOnlyOnRight.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void applyOnDifferences(DifferencesImpl differences) {
        var iterator = differences.entriesDiffering.entrySet().iterator();
        while(iterator.hasNext()) {
            var entry = iterator.next();
            Optional<Tolerance> matchingTolerances = Arrays.stream(tolerances)
                                                  .filter(t -> t.getPath().equals(entry.getKey()))
                                                  .findAny();
            matchingTolerances.ifPresent(tolerance -> {
                if(tolerance.getType() == Type.EXCLUDE) {
                    iterator.remove();
                    differences.tolerancesEntriesDiffering.put(entry.getKey(), entry.getValue());
                } else if (tolerance.getTolerance().isPresent()) {
                    double toleranceValue = tolerance.getTolerance().get();
                    Object left = entry.getValue().leftValue();
                    Object right = entry.getValue().rightValue();
                    if(left instanceof Number && right instanceof Number) {
                        Number leftNum = (Number) entry.getValue().leftValue();
                        Number rightNum = (Number) entry.getValue().rightValue();
                        if(Math.abs(leftNum.doubleValue() - rightNum.doubleValue()) <= toleranceValue) {
                            iterator.remove();
                            differences.tolerancesEntriesDiffering.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            });
        }
    }
}
