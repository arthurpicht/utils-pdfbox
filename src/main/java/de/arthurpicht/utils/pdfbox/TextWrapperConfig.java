package de.arthurpicht.utils.pdfbox;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class TextWrapperConfig {

    private final PDFont pdFont;
    private final float fontSize;
    private final float width;
    private final char lineBreakChar;
    private final BreakType breakType;
    private final float indent;
    private final IndentType indentType;

    public TextWrapperConfig(PDFont pdFont, float fontSize, float width) {
        this.pdFont = pdFont;
        this.fontSize = fontSize;
        this.width = width;
        this.lineBreakChar = ' ';
        this.breakType = BreakType.OMIT_CHAR;
        this.indent = 0;
        this.indentType = IndentType.NONE;
    }

    /**
     *
     * @param pdFont pdFont
     * @param fontSize font size
     * @param width maximum width of column
     * @param lineBreakChar break line at specified char
     * @param breakType Determines how to deal with characters that trigger line break:
     *      OMIT_CHAR = delete character in case of line break;
     *      BREAK_BEFORE = line break before character, character will be first char in next line;
     *      BREAK_AFTER = line break after character, character will be last character in broken line;
     * @param indent indentation in same unit as width
     */
    public TextWrapperConfig(PDFont pdFont, float fontSize, float width, char lineBreakChar, BreakType breakType, float indent, IndentType indentType) {
        this.pdFont = pdFont;
        this.fontSize = fontSize;
        this.width = width;
        this.lineBreakChar = lineBreakChar;
        this.breakType = breakType;
        this.indent = indent;
        this.indentType = indentType;
    }

    public PDFont getPdFont() {
        return pdFont;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getWidth() {
        return width;
    }

    public char getLineBreakChar() {
        return lineBreakChar;
    }

    public BreakType getBreakType() {
        return breakType;
    }

    public float getIndent() {
        return indent;
    }

    public IndentType getIndentType() {
        return indentType;
    }

}
