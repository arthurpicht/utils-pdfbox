package de.arthurpicht.utils.pdfbox2;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class TextWrapperConfig {

    private final PDFont pdFont;
    private final int fontSize;
    private final float width;

    public TextWrapperConfig(PDFont pdFont, int fontSize, float width) {
        this.pdFont = pdFont;
        this.fontSize = fontSize;
        this.width = width;
    }

    public PDFont getPdFont() {
        return pdFont;
    }

    public int getFontSize() {
        return fontSize;
    }

    public float getWidth() {
        return width;
    }

}
