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

    }

    @Override
    public void setLineToSkip(LineToSkip lineToSkip) {

    }

    @Override
    public void setRemoveSpaces(RemoveSpaces removeSpaces) {

    }

    @Override
    public void setRemoveEmptyLines(boolean removeEmptyLines) {

    }

    @Override
    public void setReverseLine(ReverseLine reverseLine) {

    }

    @Override
    public void setPrefix(String prefix) {

    }

    @Override
    public void adjusttxt() throws AdjustTxtException {

    }
}
