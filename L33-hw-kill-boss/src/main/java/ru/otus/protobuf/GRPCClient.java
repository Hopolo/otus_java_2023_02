package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import ru.otus.protobuf.generated.IntStreamElement;
import ru.otus.protobuf.generated.RemoteIntStreamServiceGrpc;
import ru.otus.protobuf.generated.StartStreamMessage;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final AtomicLong currentValue = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
            .usePlaintext()
            .build();

        var latch = new CountDownLatch(1);
        var stub = RemoteIntStreamServiceGrpc.newStub(channel);

        var counter = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long val = currentValue.incrementAndGet();
                System.out.println("currentValue: " + val);
            }
        });

        stub.intStream(StartStreamMessage.newBuilder().setFirst(0).setLast(30).build(), new StreamObserver<IntStreamElement>() {
            @Override
            public void onNext(IntStreamElement value) {
                if (value.getElement() == 0) {
                    System.out.println("Starting!");
                    counter.start();
                }
                System.out.println("Value from server: " + value.getElement());
                currentValue.addAndGet(value.getElement());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\n\nЯ все!");
                latch.countDown();
            }
        });

        latch.await();

        channel.shutdown();
    }
}
