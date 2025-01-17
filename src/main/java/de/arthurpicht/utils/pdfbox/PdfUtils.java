package de.arthurpicht.utils.pdfbox;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public class PdfUtils {

    public static float getWidth(PDFont pdFont, float fontSize, String string) throws IOException {
        return pdFont.getStringWidth(string) / 1000 * fontSize;
    }

    public static float getFontHeight(PDFont font, float fontSize) {
        return (font.getFontDescriptor().getCapHeight()) / 1000 * fontSize;
    }

}
