# Introduction

This Java Grep application mirrors Linux grep command, a tool that searches for a specified pattern within files and outputs lines containing that pattern. In this version, you are able to run the JAR from command line specifying the pattern you want to match, the input folder/files and the location you want the matched text to be outputted into. Testing was done using jUnit as well as manually in using the main method. The app was also dockerized for distribution.

# Quick Start

To use the app in command line using Jar file:

`java -jar [path/to/jar] [regex] [rootPath] [outFile]`

To attain Docker Image

`docker pull jaytran/grep`

To use Docker Image

`` docker run --rm -v`pwd`/data -v `pwd`/log ${docker_user}/grep [regex] [rootPath] [outFile]  ``

# Implemenation

## Pseudocode

We want to create an object to store matchedLines. We will then recursively go through the folder we have our data in, read each line in the files and whatever lines contain the pattern we want, we will add it to our matchedLines object. After going through everything we will write matchedLines to our output file.

```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue

There are a lot of memory issues that can come up when running the app. For example if you have a file that exceeds the heap memory, it will thorw an OutOfMemoryError. This is because the app loads the entire file into its heap memory and then performs the process.

# Test

Testing was done via the alteration of each input in the command line all yielding expected results

`regex` - Different regular expressions were tested to ensure the application correctly identified and matched the specified patterns.

`rootPath` - Various root paths were examined, encompassing scenarios such as empty folders, empty files, and directories with multiple files. This comprehensive testing validated the application's ability to navigate and search in diverse directory structures.

`outputfile` - Tested if existing files were overwritten, as well as evaluating the application's ability to automatically append the date and time of file creations.

# Deployment

A docker image was built locally after creating a dockerfile and packaging the java app. The image was then uploaded to the Docker Hub and is accessible via the command

`docker pull jaytran/grep`

# Improvement

- Memory is a big issue when dealing with larger files as the app loads the entire file into its heap memory.

  - One improvement to this would be to add functionality that reads the lines from the inputfiles without loading the entire file into memory.

- Using via command line can be tedious`

  - Another Improvement would be to create an interface to input regex and select input/output folders for ease of use.

- For Loops are not as efficient as using Streams and Lambdas

  - To improve on performance, replacing code with Streams and Lambdas can help and make code more concise and readable.
