package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import java.util.List;
import ru.otus.protobuf.generated.IntStreamElement;
import ru.otus.protobuf.generated.RemoteIntStreamServiceGrpc;
import ru.otus.protobuf.generated.StartStreamMessage;

public class RemoteIntStreamServiceImpl extends RemoteIntStreamServiceGrpc.RemoteIntStreamServiceImplBase {

    private final IntStreamService intStreamService;

    public RemoteIntStreamServiceImpl(IntStreamService intStreamService) {
        this.intStreamService = intStreamService;
    }

    @Override
    public void intStream(
        StartStreamMessage request,
        StreamObserver<IntStreamElement> responseObserver
    ) {
        List<Integer> intStream = intStreamService.intStream(request.getFirst(), request.getLast());
        intStream.forEach(el -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(IntStreamElement.newBuilder().setElement(el).build());
        });
        responseObserver.onCompleted();
    }
}
