package com.manouti.jsoncomparator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import com.google.common.collect.MapDifference.ValueDifference;

public interface Differences {

    /**
     * Returns an unmodifiable map containing the entries from the left map whose keys are not present in the right map.
     */
    Map<String, Object> getEntriesOnlyOnLeft();

    /**
     * Returns an unmodifiable map containing the entries from the right map whose keys are not present in the left map.
     */
    Map<String, Object> getEntriesOnlyOnRight();

    /**
     * Returns an unmodifiable map describing keys that appear in both maps, but with different values.
     */
    Map<String, ValueDifference<Object>> getEntriesDiffering();

    /**
     * Returns an unmodifiable map containing the ignored entries from the left map whose keys are not present in the right map.
     */
    Map<String, Object> getIgnoredEntriesOnlyOnLeft();

    /**
     * Returns an unmodifiable map containing the ignored entries from the right map whose keys are not present in the left map.
     */
    Map<String, Object> getIgnoredEntriesOnlyOnRight();

    /**
     * Returns an unmodifiable map describing keys that appear in both maps, but with tolerated differences.
     */
    Map<String, ValueDifference<Object>> getTolerancesEntriesDiffering();

    /**
     * Export the differences to an output stream.
     *
     * @param outputStream where to write the differences
     */
    default void export(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("Entries only on left:");
        getEntriesOnlyOnLeft().entrySet().forEach(e -> writer.println(e.getKey()));
        writer.println();

        writer.println("Entries only on right:");
        getEntriesOnlyOnRight().entrySet().forEach(e -> writer.println(e.getKey()));
        writer.println();

        writer.println("Entries differing:");
        getEntriesDiffering().entrySet().forEach(writer::println);
        writer.flush();

        writer.println("Ignored entries only on left:");
        getIgnoredEntriesOnlyOnLeft().entrySet().forEach(e -> writer.println(e.getKey()));
        writer.println();

        writer.println("Ignored entries only on right:");
        getIgnoredEntriesOnlyOnRight().entrySet().forEach(e -> writer.println(e.getKey()));
        writer.println();

        writer.println("Tolerated differences:");
        getTolerancesEntriesDiffering().entrySet().forEach(writer::println);

        writer.flush();
    }

}
