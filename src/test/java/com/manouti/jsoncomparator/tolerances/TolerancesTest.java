package com.manouti.jsoncomparator.tolerances;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.MapDifference.ValueDifference;
import com.manouti.jsoncomparator.internal.DifferencesImpl;
import com.manouti.jsoncomparator.internal.Tolerances;


public class TolerancesTest {

    @Test
    public void testTolerate() throws IOException, URISyntaxException {
        Tolerances tolerances = Tolerances.fromFile(Paths.get(this.getClass().getResource("/tolerances.json").toURI()));
        Map<String, Object> entriesOnlyOnLeft = new HashMap<>();
        entriesOnlyOnLeft.put("/name", "John");
        Map<String, Object> entriesOnlyOnRight = new HashMap<>();
        entriesOnlyOnRight.put("/name", "Jane");
        Map<String, ValueDifference<Object>> entriesDiffering = new HashMap<>();
        ValueDifference<Object> valueDifference = Mockito.mock(ValueDifference.class);
        Mockito.when(valueDifference.leftValue()).thenReturn(1.1);
        Mockito.when(valueDifference.rightValue()).thenReturn(1);
        entriesDiffering.put("/score", valueDifference);
        DifferencesImpl differences = DifferencesImpl.of(entriesOnlyOnLeft, entriesOnlyOnRight, entriesDiffering);
        tolerances.apply(differences);

        assertTrue(differences.getEntriesOnlyOnLeft().isEmpty());
        assertTrue(differences.getEntriesOnlyOnRight().isEmpty());
        assertTrue(differences.getEntriesDiffering().isEmpty());
        assertEquals(1, differences.getIgnoredEntriesOnlyOnLeft().size());
        assertEquals(1, differences.getIgnoredEntriesOnlyOnRight().size());
        assertEquals(1, differences.getTolerancesEntriesDiffering().size());
    }

    @Test
    public void testToleranceBelow() throws IOException, URISyntaxException {
        Tolerances tolerances = Tolerances.fromFile(Paths.get(this.getClass().getResource("/tolerances.json").toURI()));
        Map<String, Object> entriesOnlyOnLeft = new HashMap<>();
        entriesOnlyOnLeft.put("/name", "John");
        Map<String, Object> entriesOnlyOnRight = new HashMap<>();
        entriesOnlyOnRight.put("/name", "Jane");
        Map<String, ValueDifference<Object>> entriesDiffering = new HashMap<>();
        ValueDifference<Object> valueDifference = Mockito.mock(ValueDifference.class);
        Mockito.when(valueDifference.leftValue()).thenReturn(1.5);
        Mockito.when(valueDifference.rightValue()).thenReturn(1);
        entriesDiffering.put("/score", valueDifference);
        DifferencesImpl differences = DifferencesImpl.of(entriesOnlyOnLeft, entriesOnlyOnRight, entriesDiffering);
        tolerances.apply(differences);

        assertTrue(differences.getEntriesOnlyOnLeft().isEmpty());
        assertTrue(differences.getEntriesOnlyOnRight().isEmpty());
        assertEquals(1, differences.getEntriesDiffering().size());
        assertEquals(1, differences.getIgnoredEntriesOnlyOnLeft().size());
        assertEquals(1, differences.getIgnoredEntriesOnlyOnRight().size());
        assertTrue(differences.getTolerancesEntriesDiffering().isEmpty());
    }

}
