package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class EvenSecondProcessorTest {
    Message msg = new Message.Builder(1).build();

    @Test
    @DisplayName("Проверяем на четной секунде")
    void processEven() {
        var evenSecondProcessor = new EvenSecondProcessor(() -> LocalDateTime.of(2023, 5, 20, 18, 23, 22));
        assertThrows(RuntimeException.class, () -> evenSecondProcessor.process(msg));
    }

    @Test
    @DisplayName("Проверяем на нечетной секунде")
    void processOdd() {
        var evenSecondProcessor = new EvenSecondProcessor(() -> LocalDateTime.of(2023, 5, 20, 18, 23, 21));
        assertDoesNotThrow(() -> evenSecondProcessor.process(msg));
    }
}