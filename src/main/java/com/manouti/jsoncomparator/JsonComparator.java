package com.manouti.jsoncomparator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.manouti.jsoncomparator.internal.DifferencesImpl;
import com.manouti.jsoncomparator.internal.FlatMapUtil;
import com.manouti.jsoncomparator.internal.Tolerances;

/**
 * Entry point for this tool.
 *
 * @author manouti
 *
 */
public final class JsonComparator {

    private static final Logger logger = LogManager.getLogger(JsonComparator.class);

    private final Path leftFilePath;
    private final Path rightFilePath;
    private Path tolerancesFilePath;
    private final ObjectMapper objectMapper;

    public JsonComparator(String leftFilePath, String rightFilePath) {
        this(Paths.get(leftFilePath), Paths.get(rightFilePath));
    }

    public JsonComparator(Path leftFilePath, Path rightFilePath) {
        this.leftFilePath = leftFilePath;
        this.rightFilePath = rightFilePath;
        this.objectMapper = new ObjectMapper();
    }

    public void setTolerancesFile(Path tolerancesFilePath) {
        this.tolerancesFilePath = tolerancesFilePath;
    }

    public Differences execute() throws IOException {
        var typeReference = new TypeReference<HashMap<String, Object>>() {};
        var leftMap = objectMapper.readValue(leftFilePath.toFile(), typeReference);
        var rightMap = objectMapper.readValue(rightFilePath.toFile(), typeReference);
        Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
        Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);
        var difference = Maps.difference(leftFlatMap, rightFlatMap);
        var entriesOnlyOnLeft = new HashMap<>(difference.entriesOnlyOnLeft());
        var entriesOnlyOnRight = new HashMap<>(difference.entriesOnlyOnRight());
        var entriesDiffering = new HashMap<>(difference.entriesDiffering());

        var differences = DifferencesImpl.of(entriesOnlyOnLeft, entriesOnlyOnRight, entriesDiffering);
        if(tolerancesFilePath != null) {
            Tolerances.fromFile(tolerancesFilePath).apply(differences);
        }

        return differences;
    }

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            printUsage();
            System.exit(-1);
        }

        String leftFilePath = args[0];
        String rightFilePath = args[1];
        JsonComparator jsonComparator = new JsonComparator(leftFilePath, rightFilePath);
        if (args.length > 2) {
            jsonComparator.setTolerancesFile(Paths.get(args[2]));
        }
        Differences differences = jsonComparator.execute();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        differences.export(baos);
        logger.info(() -> new String(baos.toByteArray()));
        try(FileOutputStream output = new FileOutputStream("differences.txt")) {
            differences.export(output);
        }
    }

    private static void printUsage() {
        logger.error("Usage: JsonComparator leftFile rightFile [tolerancesFile]");
    }
    
}
