package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondProcessor implements Processor {

    private final DataTimeProvider dataTimeProvider;

    public EvenSecondProcessor(DataTimeProvider dataTimeProvider) {
        this.dataTimeProvider = dataTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dataTimeProvider.getTime().getSecond() % 2 == 0) {
            throw new RuntimeException("Even seccond");
        }
        return message;
    }
}
