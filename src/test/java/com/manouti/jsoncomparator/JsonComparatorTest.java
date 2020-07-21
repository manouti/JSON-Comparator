package com.manouti.jsoncomparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.manouti.jsoncomparator.Differences;
import com.manouti.jsoncomparator.JsonComparator;

public class JsonComparatorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testCompare() throws IOException, URISyntaxException {
        URL sourceUrl = this.getClass().getResource("/nestedSource.json");
        URL targetUrl = this.getClass().getResource("/nestedTarget.json");
        JsonComparator jsonComparator = new JsonComparator(Paths.get(sourceUrl.toURI()), Paths.get(targetUrl.toURI()));
        Differences differences = jsonComparator.execute();
        Assert.assertTrue(differences.getEntriesOnlyOnLeft().isEmpty());
        Assert.assertEquals(1, differences.getEntriesOnlyOnRight().size());
        Assert.assertEquals(3, differences.getEntriesDiffering().size());
    }

    @Test
    public void testCompareWithTolerance() throws IOException, URISyntaxException {
        URL sourceUrl = this.getClass().getResource("/nestedSource.json");
        URL targetUrl = this.getClass().getResource("/nestedTarget.json");
        URL tolerancesUrl = this.getClass().getResource("/tolerances.json");
        JsonComparator jsonComparator = new JsonComparator(Paths.get(sourceUrl.toURI()), Paths.get(targetUrl.toURI()));
        jsonComparator.setTolerancesFile(Paths.get(tolerancesUrl.toURI()));
        Differences differences = jsonComparator.execute();
        Assert.assertTrue(differences.getEntriesOnlyOnLeft().isEmpty());
        Assert.assertEquals(1, differences.getEntriesOnlyOnRight().size());
        Assert.assertEquals(1, differences.getEntriesDiffering().size());
    }

    @Test
    public void testCompareWithNestedTolerance() throws IOException, URISyntaxException {
        URL sourceUrl = this.getClass().getResource("/nestedSource.json");
        URL targetUrl = this.getClass().getResource("/nestedTarget.json");
        URL tolerancesUrl = this.getClass().getResource("/nestedTolerances.json");
        JsonComparator jsonComparator = new JsonComparator(Paths.get(sourceUrl.toURI()), Paths.get(targetUrl.toURI()));
        jsonComparator.setTolerancesFile(Paths.get(tolerancesUrl.toURI()));
        Differences differences = jsonComparator.execute();
        Assert.assertTrue(differences.getEntriesOnlyOnLeft().isEmpty());
        Assert.assertTrue(differences.getEntriesOnlyOnRight().isEmpty());
        Assert.assertEquals(1, differences.getEntriesDiffering().size());
    }

}
