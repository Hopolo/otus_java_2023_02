package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import java.io.IOException;
import ru.otus.protobuf.service.IntStreamServiceImpl;
import ru.otus.protobuf.service.RemoteIntStreamServiceImpl;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var intService = new IntStreamServiceImpl();
        var remoteIntStreamService = new RemoteIntStreamServiceImpl(intService);

        var server = ServerBuilder
            .forPort(SERVER_PORT)
            .addService(remoteIntStreamService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
