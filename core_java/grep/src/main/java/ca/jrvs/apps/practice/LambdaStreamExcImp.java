package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Stream.of(strings);
        // return Arrays.stream(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings).map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> !s.contains(pattern));
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        // return IntStream.of(arr);
        return Arrays.stream(arr);
    }

    // Stream > List
    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    // IntStream > List
    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(num -> Math.sqrt((double) num));
        // return intStream.asDoubleStream().map(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(num -> num % 2 != 0);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        // Consumer<String> printer = message -> System.out.println(prefix + message +
        // suffix);
        // return printer;
        return message -> System.out.println(prefix + message + suffix);

    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        // Stream.of(messages).map(msg -> "msg:" + msg + "!").forEach(printer);
        Stream.of(messages).forEach(msg -> printer.accept("msg:" + msg + "!"));
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        // getOdd(intStream).forEach(num -> getLambdaPrinter("num:", "!"));
        getOdd(intStream).forEach(num -> printer.accept("odd number:" + num + "!"));
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(List::stream).map(i -> i * i);
    }
}
