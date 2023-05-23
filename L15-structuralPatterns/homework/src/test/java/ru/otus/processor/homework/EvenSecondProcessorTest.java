package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class EvenSecondProcessorTest {
    @Test
    void process() {
        var evenSecondProcessor = new EvenSecondProcessor(() -> LocalDateTime.of(2023, 5, 20, 18, 23, 22));
        var msg = new Message.Builder(1).build();
        assertThrows(RuntimeException.class, () -> evenSecondProcessor.process(msg));
    }

}