package edu.gatech.seclass.adjusttxt;

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

    }
}
