package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<File>();
        File[] files = new File(rootDir).listFiles();
        if (files == null) {
            return null;
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                } else if (file.isDirectory()) {
                    fileList.addAll(listFiles(file.getAbsolutePath()));
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            logger.error("Error: File not found", e);
        }

        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return Pattern.matches(getRegex(), line);
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        String filePath = getOutFile();
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            logger.error("Error: Unable to write to file", e);
        }
    }
}
