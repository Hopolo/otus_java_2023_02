package ru.otus.dataprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

class ProcessorAggregatorTest {

    private Map<String, Double> expected;
    private List<Measurement> testData;

    @BeforeEach
    void init() {
        expected = new HashMap<>();
        expected.put("val1", 3.0);
        expected.put("val2", 30.0);
        expected.put("val3", 33.0);
        testData = Arrays.asList(new Measurement("val1", 0.0), new Measurement("val1", 1.0), new Measurement("val1", 2.0),
                                 new Measurement("val2", 0.0), new Measurement("val2", 10.0), new Measurement("val2", 20.0),
                                 new Measurement("val3", 10.0), new Measurement("val3", 11.0), new Measurement("val3", 12.0)
        );

    }

    @Test
    void process() {
        Map<String, Double> actual = new ProcessorAggregator().process(testData);
        assertEquals(expected, actual);
    }
}