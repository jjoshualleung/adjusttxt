package edu.gatech.seclass.adjusttxt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    // Empty Main class for compiling Individual Project
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    public static void main(String[] args) {
        // if argument is empty --> print error message
        if (args.length == 0) {
            usage();
        }

        // if not empty --> Create an array list to store the args
        ArrayList<String> argsList = new ArrayList<>();
        // Create the options map
        HashMap<String, List<String>> optionsMap = optionsTable();
        // Add each of the argument to the list
        for (String arg : args) {
            // Verify the existent of the options with the corresponding parameter string
            if (!verifyOptionsLookUp(optionsMap, arg)) {
                usage();
                return;
            }
            argsList.add(arg);
        }

        // Check -x and -w are not presemt at the same time
        // if present should throw error message
        if (argsList.contains("-x") && argsList.contains("-w")) {
            usage();
        }

        // Verify the last argument is a valid text file
        File filePath = new File(argsList.get(-1));

        if (!filePath.exists()) {
            usage();
            return;
        }

        if (filePath.length() == 0) {
            return;
        }

        boolean skipEven = false;
        boolean skipOdd = false;
        boolean removeLeading = false;
        boolean removeTrailing = false;
        boolean removeAll = false;
        boolean removeEmptyLine = false;
        boolean reverseWords = false;
        boolean reverseText = false;
        boolean addPrfix = false;

        for (int i = 0; i < argsList.size(); i++) {
            String arg = argsList.get(i);
            if (arg.equals("-s") && i + 1 < argsList.size() && argsList.get(i + 1).equals("0")) {
                skipEven = true;
                // move the next argument
                i++;
            } else {
                skipOdd = true;
                // move the next argument
                i++;
            }

            if (arg.equals("-x") && i + 1 < argsList.size()) {
                if (argsList.get(i + 1).equals("leading")) {
                    removeLeading = true;
                    i++;
                } else if (argsList.get(i + 1).equals("trailing")) {
                    removeTrailing = true;
                    i++;
                } else {
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
                    reverseWords = true;
                } else if (argsList.get(i + 1).equals("text")) {
                    reverseText = true;
                }
            }

            if (arg.equals("-p") && i + 1 < argsList.size()) {
                addPrfix = true;
                i++;
            }
        }
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
