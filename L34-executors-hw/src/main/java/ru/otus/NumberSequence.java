package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequence {
    private static final Logger logger = LoggerFactory.getLogger(NumberSequence.class);
    private int lastThreadId = 2;

    public static void main(String[] args) {
        var numberSequence = new NumberSequence();
        new Thread(() -> numberSequence.reversingStreamInt(1, 1, 10)).start();
        new Thread(() -> numberSequence.reversingStreamInt(2, 1, 10)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void intOutput(
        int threadId,
        Integer curNumber
    ) {
        try {
            while (lastThreadId == threadId) {
                this.wait();
            }

            logger.info("current number: {}", curNumber);
            lastThreadId = threadId;
            sleep();
            notifyAll();
            logger.info("after notify");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void reversingStreamInt(
        int id,
        int start,
        int end
    ) {
        int i = start;
        boolean up = true;
        while (true) {
            if (i < end && up) {
                this.intOutput(id, i);
                i++;
                if (i == end) {
                    up = false;
                }
            } else {
                this.intOutput(id, i);
                i--;
                if (i == start) {
                    up = true;
                }
            }
        }
    }
}
