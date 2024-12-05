package de.arthurpicht.utils.pdfbox;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.List;

public class PdfRenderer {

    public static void renderLineOfText(
            String string,
            float x,
            float y,
            PDFont pdFont,
            float fontSize,
            float leading,
            PDPageContentStream pdPageContentStream) throws UtilsPdfboxException {

        try {
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(pdFont, fontSize);
            pdPageContentStream.setLeading(leading);
            pdPageContentStream.newLineAtOffset(x, y);
            pdPageContentStream.showText(string);
            pdPageContentStream.endText();
        } catch (IOException e) {
            throw new UtilsPdfboxException(e.getMessage(), e);
        }
    }

    public static void renderParagraph(
            List<String> text,
            float x,
            float y,
            PDFont pdFont,
            float fontSize,
            float leading,
            float indent,
            IndentType indentType,
            PDPageContentStream pdPageContentStream) throws UtilsPdfboxException {

        float curY = y;
        int linesProcessed = 0;
        IndentResolver indentResolver = new IndentResolver(indentType);
        for (String string : text) {
            float curX = indentResolver.applyIndent(linesProcessed) ? x + indent : x;
            PdfRenderer.renderLineOfText(
                    string,
                    curX,
                    curY,
                    pdFont,
                    fontSize,
                    leading,
                    pdPageContentStream);
            curY = curY - leading;
            linesProcessed++;
        }
    }

    public static void renderParagraphWithFirstBaselineAsReference(
            List<String> text,
            float x,
            float y,
            PDFont pdFont,
            float fontSize,
            float leading,
            float indent,
            IndentType indentType,
            PDPageContentStream pdPageContentStream) throws UtilsPdfboxException {

        renderParagraph(
                text,
                x,
                y,
                pdFont,
                fontSize,
                leading,
                indent,
                indentType,
                pdPageContentStream
        );
    }

    public static void renderParagraphWithUpperLeftCornerAsReference(
            List<String> text,
            float x,
            float y,
            PDFont pdFont,
            float fontSize,
            float leading,
            float indent,
            IndentType indentType,
            PDPageContentStream pdPageContentStream) throws UtilsPdfboxException {

        y = y - PdfUtils.getFontHeight(pdFont, fontSize);
        renderParagraph(
                text,
                x,
                y,
                pdFont,
                fontSize,
                leading,
                indent,
                indentType,
                pdPageContentStream
        );
    }

}
