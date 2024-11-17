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

        String prefix = null;

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

            List<String> validParams = optionsMap.get(arg);
            // Check expected parameter for the option
            if (validParams != null) {
                j++;

                // Verify parameter exists
                if (j >= argsList.size() || !validParams.contains(argsList.get(j))) {
                    usage();
                    return;
                }
                j++;
            }
        }

        for (int i = 0; i < argsList.size(); i++) {
            String arg = argsList.get(i);
            if (arg.equals("-s") && i + 1 < argsList.size() && argsList.get(i + 1).equals("0")) {
                skipOdd = false;
                skipEven = true;
                // move the next argument
                i++;
            } else {
                skipEven = false;
                skipOdd = true;
                // move the next argument
                i++;
            }

            if (arg.equals("-x") && i + 1 < argsList.size()) {
                if (argsList.get(i + 1).equals("leading")) {
                    removeTrailing = false;
                    removeAll = false;
                    removeLeading = true;
                    i++;
                } else if (argsList.get(i + 1).equals("trailing")) {
                    removeAll = false;
                    removeLeading = false;
                    removeTrailing = true;
                    i++;
                } else {
                    removeLeading = false;
                    removeTrailing = false;
                    removeAll = true;
                    i++;
                }
            }

            if (arg.equals("-w") && i + 1 < argsList.size()) {
                removeEmptyLine = true;
                i++;
            }

            if (arg.equals("-r") && i + 1 < argsList.size()) {
                if (argsList.get(i + 1).equals("words")) {
                    reverseText = false;
                    reverseWords = true;
                } else if (argsList.get(i + 1).equals("text")) {
                    reverseWords = false;
                    reverseText = true;
                }
            }

            if (arg.equals("-p") && i + 1 < argsList.size()) {
                addPrfix = true;
                prefix = argsList.get(i + 1);
                i++;
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
            outputLines.add(line.stripLeading());
        }
        return result;
    }

    public static List<String> removeTrailingSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            outputLines.add(line.stripTrailing());
        }
        return result;
    }

    public static List<String> removeAllSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            // if true then proceed
                // Remove all the space
                outputLines.add(line.replace(" ", ""));
            }

        return result;
    }

    public static List<String> removeEmptyLineMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            // Check trimmed line is empty afterward
            if (line.trim().isEmpty()) {
                outputLines.add(line);
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
            }
            outputLines.add(reversedLine.toString());
        }
        return result;
    }

    public static List<String> reverseTextMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        for (String line : outputLines) {
            StringBuilder reversedLine = new StringBuilder(line);
            String reversedText = reversedLine.reverse().toString();
            outputLines.add(reversedText);
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
        optionsMap.put("-x", emptyLineParam);
        optionsMap.put("-r", reverseParam);
        optionsMap.put("-p", null);

        return optionsMap;
    }

    private static void usage() {
        System.err.println(
                "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
    }
}
