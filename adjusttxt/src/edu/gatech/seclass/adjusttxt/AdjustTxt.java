package edu.gatech.seclass.adjusttxt;

import java.io.File;
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
}
