package ru.otus.protobuf.service;

import java.util.List;
import java.util.stream.IntStream;

public class IntStreamServiceImpl implements IntStreamService {

    @Override
    public List<Integer> intStream(
        int first,
        int last
    ) {
        System.out.println("IntStreamServiceImpl::intStream");
        return IntStream.range(first, last + 1).boxed().toList();
    }
}
