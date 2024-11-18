package edu.gatech.seclass.adjusttxt;

/**
 * Interface created for use in Georgia Tech CS6300.
 *
 * <p>IMPORTANT: This interface should NOT be altered in any way.
 */
public interface AdjustTxtInterface {
    enum LineToSkip {
        even,
        odd,
    }

    enum RemoveSpaces {
        leading,
        trailing,
        all
    }

    enum ReverseLine {
        words,
        text
    }

    /** Reset the AdjustTxt object to its initial state, for reuse. */
    void reset();

    /**
     * Sets the path of the input file. This method has to be called before invoking the {@link
     * #adjusttxt()} methods.
     *
     * @param filepath The file path to be set.
     */
    void setFilepath(String filepath);

    /**
     * Set to apply the skipping of lines based upon the supplied parameter, lineToSkip. 0 is
     * considered even and 1 is odd. All files start with line 1. This method has to be called
     * before invoking the {@link #adjusttxt()} method.
     *
     * @param lineToSkip Determine which lines to skip
     */
    void setLineToSkip(LineToSkip lineToSkip);

    /**
     * Set to remove either leading, trailing, or all spaces from the input file. This method has to
     * be called before invoking the {@link #adjusttxt()} method.
     *
     * @param removeSpaces Determine which whitespace to remove
     */
    void setRemoveSpaces(RemoveSpaces removeSpaces);

    /**
     * Set to remove all empty lines from the input file. This method has to be called before
     * invoking the {@link #adjusttxt()} method.
     *
     * @param removeEmptyLines Flag to toggle functionality
     */
    void setRemoveEmptyLines(boolean removeEmptyLines);

    /**
     * Set to reverse the lines of a file. This method has to be called before invoking the {@link
     * #adjusttxt()} method.
     *
     * @param reverseLine Determine how to reverse a line
     */
    void setReverseLine(ReverseLine reverseLine);

    /**
     * Adds prefix at the start of each line. This method has to be called before invoking the
     * {@link #adjusttxt()} method.
     *
     * @param prefix The prefix to be set
     */
    void setPrefix(String prefix);

    /**
     * Outputs a System.lineSeparator() delimited string that contains selected parts of the lines
     * in the file specified using {@link #setFilepath} and according to the current configuration,
     * which is set through calls to the other methods in the interface.
     *
     * <p>It throws a {@link AdjustTxtException} if an error condition occurs (e.g., when the
     * specified file does not exist).
     *
     * @throws AdjustTxtException thrown if an error condition occurs
     */
    void adjusttxt() throws AdjustTxtException;
}
