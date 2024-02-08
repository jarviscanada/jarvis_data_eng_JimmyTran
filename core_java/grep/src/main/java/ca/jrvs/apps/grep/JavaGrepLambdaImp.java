package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        // creating JavaGrepLambdaImp instead of JavaGrepImp
        // JavaGrepLambdaImp inherits all methods except two override methods
        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            // try calling parent method,
            // but it will call override method (in this class)
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<String>();
        for (File file : listFiles(getRootPath())) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    // https://howtodoinjava.com/java8/java-8-list-all-files-example/
    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(rootDir))) {
            fileList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    // https://mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    // @Override
    // public List<File> listFiles(String rootDir) {
    // List<File> fileList = new ArrayList<File>();
    // File[] files = new File(rootDir).listFiles();
    // if (files == null) {
    // return null;
    // } else {
    // for (File file : files) {
    // if (file.isFile()) {
    // fileList.add(file);
    // } else if (file.isDirectory()) {
    // fileList.addAll(listFiles(file.getAbsolutePath()));
    // }
    // }
    // }
    // return fileList;
    // }

    // @Override
    // public List<String> readLines(File inputFile) {
    // List<String> lines = new ArrayList<String>();

    // try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

    // String line;

    // while ((line = reader.readLine()) != null) {
    // lines.add(line);
    // }
    // reader.close();
    // } catch (IOException e) {
    // logger.error("Error: File not found", e);
    // }

    // return lines;
    // }
}
