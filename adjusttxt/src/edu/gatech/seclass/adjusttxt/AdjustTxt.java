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
        reset();
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
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        File file = getfile(filepath);

        // Verify the file path exist
        if (!file.exists() || !file.isFile()) {
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        // Verify file is empty content
        if (file.length() == 0) {
            return;
        }

        if (!checkNewLineAtEnd(file)) {
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        // Check -x and -w are not present at the same time
        if (removeEmptyLines && removeSpaces != null) {
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        List<String> outputLines;
        try {
            outputLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        if (outputLines.isEmpty()) {
            throw new AdjustTxtException("Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
        }

        if (lineToSkip == LineToSkip.even) {
            outputLines = skipEvenMethod(outputLines);
        } else if (lineToSkip == lineToSkip.odd) {
            outputLines = skipOddMethod(outputLines);
        }

        if (removeSpaces == RemoveSpaces.leading) {
            outputLines = removeLeadingSpaceMethod(outputLines);
        }
        if (removeSpaces == removeSpaces.trailing) {
            outputLines = removeTrailingSpaceMethod(outputLines);
        }
        if (removeSpaces == removeSpaces.all) {
            outputLines = removeAllSpaceMethod(outputLines);
        }
        if (removeEmptyLines) {
            outputLines = removeEmptyLineMethod(outputLines);
        }

        if (reverseLine == reverseLine.words) {
            outputLines = reverseWordsMethod(outputLines);
        }
        if (reverseLine == reverseLine.text) {
            outputLines = reverseTextMethod(outputLines);
        }
        if (prefix != null) {
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
            if (line == null) {
                // Replace null lines with empty strings
                result.add("");
            } else {
                // if true then proceed
                result.add(line.stripLeading());
            }
        }
        return result;
    }

    public static List<String> removeTrailingSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            if (line == null) {
                // Replace null lines with empty strings
                result.add("");
            } else {
                result.add(line.stripTrailing());
            }
        }
        return result;
    }

    public static List<String> removeAllSpaceMethod(List<String> outputLines) {
        List<String> result = new ArrayList<>();

        // for each line in the file
        for (String line : outputLines) {
            if (line == null) {
                // Replace null lines with empty strings
                result.add("");
            } else {
                // Remove all the space
                result.add(line.replaceAll("\\s+", "").trim());
            }
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

    /**
     *
     * @param filepath
     * @return text file
     */
    public File getfile(String filepath) {
        File file = new File(filepath);
        return file;
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
