package edu.gatech.seclass.adjusttxt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class AdjustTxt implements AdjustTxtInterface{

    private LineToSkip lineToSkip;
    private RemoveSpaces removeSpaces;
    private ReverseLine reverseLine;
    private String filepath;
    private String prefix;
    private boolean removeEmptyLines;

    public AdjustTxt(){
    }
    @Override
    public void reset() {
        lineToSkip = null;
        removeSpaces = null;
        reverseLine = null;
        filepath = null;
        prefix = null;
        removeEmptyLines = false;
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void setLineToSkip(LineToSkip lineToSkip) {
        this.lineToSkip = lineToSkip;
    }

    @Override
    public void setRemoveSpaces(RemoveSpaces removeSpaces) {
        this.removeSpaces = removeSpaces;
    }

    @Override
    public void setRemoveEmptyLines(boolean removeEmptyLines) {
        this.removeEmptyLines = removeEmptyLines;
    }

    @Override
    public void setReverseLine(ReverseLine reverseLine) {
        this.reverseLine = reverseLine;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void adjusttxt() throws AdjustTxtException {
        if (filepath == null){
            throw new AdjustTxtException();
        }

        // Verify the file path exist
        if (!getfile(filepath).exists() || !getfile(filepath).isFile()) {
            throw new AdjustTxtException();
        }

        // Verify file is empty content
        if (getfile(filepath).length() == 0) {
            System.out.println();
        }

        if (!checkNewLineAtEnd(getfile(filepath))) {
            throw new AdjustTxtException();
        }

        // Check -x and -w are not present at the same time
        if (removeEmptyLines && removeSpaces != null) {
            throw new AdjustTxtException();
        }

        List<String> outputLines;
        try {
            outputLines = Files.readAllLines(getfile(filepath).toPath());
        } catch (IOException e) {
            throw new AdjustTxtException();
        }

        if (outputLines.isEmpty()) {
            throw new AdjustTxtException();
        }

        if (lineToSkip == LineToSkip.even) {
            outputLines = skipEvenMethod(outputLines);
        } else if (lineToSkip == lineToSkip.odd) {
            outputLines = skipOddMethod(outputLines);
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

    /**
     *
     * @param filepath
     * @return text file
     */
    public File getfile(String filepath) {
        File file = new File(filepath);
        return file;
    }

    private static boolean verifyOptionsLookUp(HashMap<String, List<String>> optionsMap, String option) {
        return optionsMap.containsKey(option);
    }

    public static HashMap<String, List<String>> optionsTable() {
        // Create a hashmap
        HashMap<String, List<String>> optionsMap = new HashMap<>();

        List<String> skipParam = Arrays.asList("0", "1");
        List<String> whitespaceParam = Arrays.asList("leading", "trailing", "all");
        List<String> reverseParam = Arrays.asList("words", "text");

        // Map option to its parameters
        optionsMap.put("-s", skipParam);
        optionsMap.put("-w", whitespaceParam);
        optionsMap.put("-x", null);
        optionsMap.put("-r", reverseParam);
        optionsMap.put("-p", new ArrayList<>());

        return optionsMap;
    }

    /**
     * Check new line exist in text file
     * @param file
     * @return
     */
    private static boolean checkNewLineAtEnd(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            return content.endsWith(System.lineSeparator());
        } catch (IOException e) {
            return false;
        }
    }
}
