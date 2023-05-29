package ru.otus.dataprocessor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

class ResourcesFileLoaderTest {

    private String fileNameInput;
    private List<Measurement> expected;

    @BeforeEach
    void init() {
        fileNameInput = "inputData.json";
        expected = Arrays.asList(new Measurement("val1", 0.0), new Measurement("val1", 1.0), new Measurement("val1", 2.0),
                                 new Measurement("val2", 0.0), new Measurement("val2", 10.0), new Measurement("val2", 20.0),
                                 new Measurement("val3", 10.0), new Measurement("val3", 11.0), new Measurement("val3", 12.0)
        );
    }

    @Test
    void load() {
        List<Measurement> actual = new ResourcesFileLoader(fileNameInput).load();
//        assertEquals(expected, actual);
    }

    @Test
    void load1() {
        assertDoesNotThrow(() -> new ResourcesFileLoader(fileNameInput).load(), "Ошибка чтения json");
    }
}