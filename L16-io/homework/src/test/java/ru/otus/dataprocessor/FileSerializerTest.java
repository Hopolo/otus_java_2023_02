package ru.otus.dataprocessor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSerializerTest {

    private String fileNameOutput;
    private Map<String, Double> testData;

    @BeforeEach
    void init(@TempDir Path tempdir) {
        fileNameOutput = String.format("%s%s%s", tempdir, File.separator, "outputData.json");
        testData = new HashMap<>();
        testData.put("val1", 3.0);
        testData.put("val2", 30.0);
        testData.put("val3", 33.0);
    }

    @Test
    void serialize() throws IOException {
        new FileSerializer(fileNameOutput).serialize(testData);
        var serializedOutput = Files.readString(Paths.get(fileNameOutput));
        //обратите внимание: важен порядок ключей
        assertThat(serializedOutput).isEqualTo("{\"val1\":3.0,\"val2\":30.0,\"val3\":33.0}");
    }
}