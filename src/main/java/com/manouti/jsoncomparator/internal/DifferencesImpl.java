package com.manouti.jsoncomparator.internal;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.MapDifference.ValueDifference;
import com.manouti.jsoncomparator.Differences;

public final class DifferencesImpl implements Differences {

    final Map<String, Object> entriesOnlyOnLeft;
    final Map<String, Object> entriesOnlyOnRight;
    final Map<String, ValueDifference<Object>> entriesDiffering;
    final Map<String, Object> ignoredEntriesOnlyOnLeft;
    final Map<String, Object> ignoredEntriesOnlyOnRight;
    final Map<String, ValueDifference<Object>> tolerancesEntriesDiffering;

    DifferencesImpl(Map<String, Object> entriesOnlyOnLeft, Map<String, Object> entriesOnlyOnRight, Map<String, ValueDifference<Object>> entriesDiffering) {
        this.entriesOnlyOnLeft = entriesOnlyOnLeft;
        this.entriesOnlyOnRight = entriesOnlyOnRight;
        this.entriesDiffering = entriesDiffering;
        this.ignoredEntriesOnlyOnLeft = new HashMap<>();
        this.ignoredEntriesOnlyOnRight = new HashMap<>();
        this.tolerancesEntriesDiffering = new HashMap<>();
    }

    @Override
    public Map<String, Object> getEntriesOnlyOnLeft() {
        return Map.copyOf(entriesOnlyOnLeft);
    }

    @Override
    public Map<String, Object> getEntriesOnlyOnRight() {
        return Map.copyOf(entriesOnlyOnRight);
    }

    @Override
    public Map<String, ValueDifference<Object>> getEntriesDiffering() {
        return Map.copyOf(entriesDiffering);
    }

    @Override
    public Map<String, Object> getIgnoredEntriesOnlyOnLeft() {
        return Map.copyOf(ignoredEntriesOnlyOnLeft);
    }

    @Override
    public Map<String, Object> getIgnoredEntriesOnlyOnRight() {
        return Map.copyOf(ignoredEntriesOnlyOnRight);
    }

    @Override
    public Map<String, ValueDifference<Object>> getTolerancesEntriesDiffering() {
        return Map.copyOf(tolerancesEntriesDiffering);
    }


    @Override
    public String toString() {
        return "Differences [entriesOnlyOnLeft=" + entriesOnlyOnLeft + ", entriesOnlyOnRight=" + entriesOnlyOnRight
                + ", entriesDiffering=" + entriesDiffering + "]";
    }

    public static DifferencesImpl of(Map<String, Object> entriesOnlyOnLeft, Map<String, Object> entriesOnlyOnRight, Map<String, ValueDifference<Object>> entriesDiffering) {
        return new DifferencesImpl(entriesOnlyOnLeft, entriesOnlyOnRight, entriesDiffering);
    }

}
