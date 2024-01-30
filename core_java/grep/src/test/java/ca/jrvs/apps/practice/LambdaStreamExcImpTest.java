package ca.jrvs.apps.practice;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LambdaStreamExcImpTest {

    private LambdaStreamExc lambdaStreamExc = new LambdaStreamExcImp();

    @Test
    public void testCreateStrStream() {
        Stream<String> result = lambdaStreamExc.createStrStream("apple", "banana", "orange");
        List<String> resultList = result.collect(Collectors.toList());
        assertEquals(Arrays.asList("apple", "banana", "orange"), resultList);
    }

    @Test
    public void testToUpperCase() {
        Stream<String> result = lambdaStreamExc.toUpperCase("apple", "banana", "orange");
        List<String> resultList = result.collect(Collectors.toList());
        assertEquals(Arrays.asList("APPLE", "BANANA", "ORANGE"), resultList);
    }

    @Test
    public void testFilter() {
        Stream<String> input = Stream.of("apple", "kiwi", "orange");
        Stream<String> result = lambdaStreamExc.filter(input, "a");
        List<String> resultList = result.collect(Collectors.toList());
        assertEquals(Arrays.asList("kiwi"), resultList);
    }

    @Test
    public void testCreateIntStream() {
        int[] arr = { 1, 2, 3, 4, 5 };
        IntStream result = lambdaStreamExc.createIntStream(arr);
        int[] resultArray = result.toArray();
        assertTrue(Arrays.equals(arr, resultArray));
    }

    @Test
    public void testToListWithGenericStream() {
        Stream<String> input = Stream.of("apple", "banana", "orange");
        List<String> result = lambdaStreamExc.toList(input);
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @Test
    public void testToListWithIntStream() {
        IntStream input = IntStream.of(1, 2, 3, 4, 5);
        List<Integer> result = lambdaStreamExc.toList(input);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    public void testCreateIntStreamRange() {
        IntStream result = lambdaStreamExc.createIntStream(1, 5);
        int[] resultArray = result.toArray();
        assertTrue(Arrays.equals(new int[] { 1, 2, 3, 4, 5 }, resultArray));
    }

    @Test
    public void testSquareRootIntStream() {
        IntStream input = IntStream.of(1, 4, 9, 16);
        Stream<Double> result = lambdaStreamExc.squareRootIntStream(input).boxed();
        List<Double> resultList = result.collect(Collectors.toList());
        assertTrue(Arrays.asList(1.0, 2.0, 3.0, 4.0).containsAll(resultList));
    }

    @Test
    public void testGetOdd() {
        IntStream input = IntStream.of(1, 2, 3, 4, 5);
        IntStream result = lambdaStreamExc.getOdd(input);
        int[] resultArray = result.toArray();
        assertTrue(Arrays.equals(new int[] { 1, 3, 5 }, resultArray));
    }

    @Test
    public void testGetLambdaPrinter() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            Consumer<String> printer = lambdaStreamExc.getLambdaPrinter("Start>", "<End");
            printer.accept("Message");

            // Assert the captured output
            assertEquals("Start>Message<End", outContent.toString().trim());
        } finally {
            // Reset System.out to its original state
            System.setOut(System.out);
        }
    }

    @Test
    public void testPrintMessages() {
        String[] messages = { "a", "b", "c" };
        StringBuilder sb = new StringBuilder();
        Consumer<String> printer = lambdaStreamExc.getLambdaPrinter("msg:", "!");
        lambdaStreamExc.printMessages(messages, msg -> {
            sb.append(msg);
            printer.accept(msg);
        });
        assertEquals("msg:a!msg:b!msg:c!", sb.toString());
    }

    @Test
    public void testPrintOdd() {
        IntStream input = IntStream.of(1, 2, 3, 4, 5);
        StringBuilder sb = new StringBuilder();
        Consumer<String> printer = s -> sb.append(s).append(" ");
        lambdaStreamExc.printOdd(input, printer);
        assertEquals("odd number:1! odd number:3! odd number:5! ", sb.toString());

    }

    @Test
    public void testFlatNestedInt() {
        Stream<List<Integer>> nestedInts = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        Stream<Integer> result = lambdaStreamExc.flatNestedInt(nestedInts);
        List<Integer> resultList = result.collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 4, 9, 16, 25), resultList);
    }
}
