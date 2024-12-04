package de.arthurpicht.utils.pdfbox2;

public class IndentResolver {

    private final IndentType indentType;

    public IndentResolver(IndentType indentType) {
        this.indentType = indentType;
    }

    public boolean applyIndent(int lineIndex) {
        if (this.indentType == IndentType.ALL) {
            return true;
        } else if (this.indentType == IndentType.FIRST_LINE_ONLY) {
            return lineIndex == 0;
        } else if (this.indentType == IndentType.ALL_EXCEPT_FIRST_LINE) {
            return lineIndex > 0;
        } else if (this.indentType == IndentType.NONE) {
            return false;
        }
        throw new IllegalArgumentException("Unknown indent type: " + this.indentType);
    }

}
