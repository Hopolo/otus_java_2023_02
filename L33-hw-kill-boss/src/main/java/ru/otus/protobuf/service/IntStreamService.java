package ru.otus.protobuf.service;

import java.util.List;

public interface IntStreamService {
    List<Integer> intStream(
        int first,
        int last
    );
}
