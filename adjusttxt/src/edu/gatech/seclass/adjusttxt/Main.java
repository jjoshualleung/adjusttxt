package edu.gatech.seclass.adjusttxt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Main {
    // Empty Main class for compiling Individual Project
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    public static void main(String[] args) {

        boolean skipEven = false;
        boolean skipOdd = false;
        boolean removeLeading = false;
        boolean removeTrailing = false;
        boolean removeAll = false;
        boolean removeEmptyLine = false;
        boolean reverseWords = false;
        boolean reverseText = false;
        boolean addPrfix = false;

        String prefix = "";

        // if argument is empty --> print error message
        if (args.length == 0) {
            usage();
            return;
        }

        // Convert args to a list
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        // Create the options map
        HashMap<String, List<String>> optionsMap = optionsTable();

        // Extract the last element of the list as text file
        String filePath = argsList.removeLast();
        File file = new File(filePath);

        // Verify the file path exist
        if (!file.exists() || !file.isFile()) {
            usage();
            return;
        }

        // Verify file is empty content
        if (file.length() == 0) {
            return;
        }

        // Verify the file end with new line
        if (!checkNewLineAtEnd(file)) {
            usage();
            return;
        };

        // Check -x and -w are not present at the same time
        if (argsList.contains("-x") && argsList.contains("-w")) {
            usage();
            return;
        }

        int j = 0;
        while (j < argsList.size()) {
            String arg = argsList.get(j);
            // Verify the existent of the options with the corresponding parameter string
            if (!verifyOptionsLookUp(optionsMap, arg)) {
                usage();
                return;
            }

            if (argsList.contains("-x")) {
                int index = argsList.indexOf("-x");
                // if -x is not the last arg
                if (index != argsList.size() - 1) {
                    if (!argsList.get(index + 1).startsWith("-")) {
                        usage();
                        return;
                    }
                }
            }

            List<String> validParams = optionsMap.get(arg);
            // Check expected parameter for the option
            if (validParams != null) {
                j++;
                if (j >= argsList.size()) {
                    usage();
                    return;
                }
                String param = argsList.get(j);
                // If not empty, check if parameter is valid
                if (!validParams.isEmpty() && !validParams.contains(param)) {
                    usage();
                    return;
                }
            }
            j++;
        }

        /* ------------------------------- Implement options functionality ------------------------------- */
        for (int i = 0; i < argsList.size(); i++) {
            String arg = argsList.get(i);
            switch (arg) {
                case "-s":
                    if (i + 1 < argsList.size()) {
                        String paramsArg = argsList.get(++i);
                        // Reset skip option in case there multiple skip option
                        skipEven = false;
                        skipOdd = false;
                        if (paramsArg.equals("0")) {
                            skipEven = true;
                        } else if (paramsArg.equals("1")) {
                            skipOdd = true;
                        }
                    }
                    break;
                case "-w":
                    if (i + 1 < argsList.size()) {
                        String wArg = argsList.get(++i);
                        removeTrailing = false;
                        removeAll = false;
                        removeLeading = false;
                        if (wArg.equals("leading")) {
                            removeLeading = true;
                        } else if (wArg.equals("trailing")) {
                            removeTrailing = true;
                        } else if (wArg.equals("all")) {
                            removeAll = true;
                        } else {
                            usage();
                            return;
                        }
                    }
                    break;
                case "-x":
                    removeEmptyLine = true;
                    break;
                case "-r":
                    if (i + 1 < argsList.size()) {
                        String rArg = argsList.get(++i);
                        reverseText = false;
                        reverseWords = false;
                        if (rArg.equals("words")) {
                            reverseWords = true;
                        } else if (rArg.equals("text")) {
                            reverseText = true;
                        } else {
                            usage();
                            return;
                        }
                    }
                    break;

                case "-p":
                    if (i + 1 < argsList.size()) {
                        addPrfix = true;
                        prefix = argsList.get(++i);
                    } else {
                        usage();
                        return;
                    }
                    break;
            }
        }

        List<String> outputLines;
        try {
            outputLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            usage();
            return;
        }

        if (outputLines.isEmpty()) {
            return;
        }

        if (skipEven) {
            outputLines = skipEvenMethod(outputLines);
        }
        if (skipOdd) {
            outputLines = skipOddMethod(outputLines);
        }
        if (removeLeading) {
            outputLines = removeLeadingSpaceMethod(outputLines);
        }
        if (removeTrailing) {
            outputLines = removeTrailingSpaceMethod(outputLines);
        }
        if (removeAll) {
            outputLines = removeAllSpaceMethod(outputLines);
        }
        if (removeEmptyLine) {
            outputLines = removeEmptyLineMethod(outputLines);
        }
        if (reverseWords) {
            outputLines = reverseWordsMethod(outputLines);
        }
        if (reverseText) {
            outputLines = reverseTextMethod(outputLines);
        }
        if (addPrfix) {
            outputLines = addPrefixMethod(outputLines, prefix);
        }


        // Output lines to stdout
        for (String line : outputLines) {
            System.out.println(line);
        }
    }

    public static List<String> skipEvenMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < outputLines.size(); i++) {
            // skip even line
            if (i % 2 == 0) {
                result.add(outputLines.get(i));
            }
            // add line to output file
        }
        return result;
    }


    public static List<String> skipOddMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < outputLines.size(); i++) {
            // skip odd line
            if (i % 2 != 0) {
                result.add(outputLines.get(i));
            }
        }
        return result;
    }

    public static List<String> removeLeadingSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            // if true then proceed
            result.add(line.stripLeading());
        }
        return result;
    }

    public static List<String> removeTrailingSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            result.add(line.stripTrailing());
        }
        return result;
    }

    public static List<String> removeAllSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            // if true then proceed
            // Remove all the space
            result.add(line.replaceAll("\\s+", ""));
        }
        return result;
    }

    public static List<String> removeEmptyLineMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            // Check trimmed line is empty afterward
            if (!line.trim().isEmpty()) {
                result.add(line);
            }
        }
        return result;
    }

    public static List<String> reverseWordsMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            String[] words = line.split(" ");
            StringBuilder reversedLine = new StringBuilder();
            for (int word = words.length - 1; word >= 0; word--) {
                reversedLine.append((words[word]));
                if (word != 0) {
                    reversedLine.append(" ");
                }
            }
            result.add(reversedLine.toString());
        }
        return result;
    }

    public static List<String> reverseTextMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            StringBuilder reversedLine = new StringBuilder(line);
            String reversedText = reversedLine.reverse().toString();
            result.add(reversedText);
        }
        return result;
    }

    public static List<String> addPrefixMethod(List<String> outputLines, String prefix) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            result.add(prefix + line);
        }
        return result;
    }

    private static boolean verifyOptionsLookUp(HashMap<String, List<String>> optionsMap, String option) {
        return optionsMap.containsKey(option);
    }

    /**
     * Create a hashmap
     * @return optionsMap
     */
    public static HashMap<String, List<String>> optionsTable() {
        // Create a hashmap
        HashMap<String, List<String>> optionsMap = new HashMap<>();

        List<String> skipParam = Arrays.asList("0", "1");
        List<String> whitespaceParam = Arrays.asList("leading", "trailing", "all");
        List<String> reverseParam = Arrays.asList("words", "text");
        List<String> emptyLineParam = Collections.emptyList();

        // Map option to its parameters
        optionsMap.put("-s", skipParam);
        optionsMap.put("-w", whitespaceParam);
        optionsMap.put("-x", null);
        optionsMap.put("-r", reverseParam);
        optionsMap.put("-p", new ArrayList<>());

        return optionsMap;
    }

    private static boolean checkNewLineAtEnd(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            return content.endsWith(System.lineSeparator());
        } catch (IOException e) {
            return false;
        }
    }

    private static void usage() {
        System.err.println(
                "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
    }
}
