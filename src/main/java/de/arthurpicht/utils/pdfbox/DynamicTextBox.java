package de.arthurpicht.utils.pdfbox;

import de.arthurpicht.utils.core.strings.Strings;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

@SuppressWarnings("JavadocDeclaration")
public class DynamicTextBox {

    private final PDFont pdFont;
    private final float fontSize;
    private final float leading;
    private final float width;
    private final List<String> wrappedText;
    private final char lineBreakCharacter;
    private final BreakType breakType;
    private final TextBoxDimensions textBoxDimensions;
    private final float indent;
    private final IndentType indentType;

    @SuppressWarnings("unused")
    public static class Builder {

        private PDFont pdFont = new PDType1Font(Standard14Fonts.FontName.COURIER);
        private float fontSize = 12;
        private float leading = 14.5f;
        private float width = 200;
        private String text = "";
        private char lineBreakCharacter = ' ';
        private BreakType breakType = BreakType.OMIT_CHAR;
        private float indent;
        private IndentType indentType = IndentType.NONE;

        public Builder withFont(PDFont pdFont) {
            this.pdFont = pdFont;
            return this;
        }

        public Builder withFontSize(float fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder withLeading(float leading) {
            this.leading = leading;
            return this;
        }

        public Builder withWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withLineBreakCharacter(char lineBreakCharacter) {
            this.lineBreakCharacter = lineBreakCharacter;
            return this;
        }

        public Builder withBreakType(BreakType breakType) {
            this.breakType = breakType;
            return this;
        }

        public Builder withIndent(float indent) {
            this.indent = indent;
            return this;
        }

        public Builder withIndentType(IndentType indentType) {
            this.indentType = indentType;
            return this;
        }

        public DynamicTextBox build() throws UtilsPdfboxException {
            return new DynamicTextBox(
                    this.text,
                    this.pdFont,
                    this.fontSize,
                    this.leading,
                    this.width,
                    this.lineBreakCharacter,
                    this.breakType,
                    this.indent,
                    this.indentType
            );
        }

    }

    private DynamicTextBox(
            String text,
            PDFont pdFont,
            float fontSize,
            float leading,
            float width,
            char lineBreakCharacter,
            BreakType breakType,
            float indent,
            IndentType indentType)
            throws UtilsPdfboxException {

        assertArgumentNotNull("text", text);
        assertArgumentNotNull("pdFont", pdFont);
        if (indent >= width) throw new IllegalArgumentException("Indent exceeding width.");

        this.pdFont = pdFont;
        this.fontSize = fontSize;
        this.leading = leading;
        this.width = width;
        this.lineBreakCharacter = lineBreakCharacter;
        this.breakType = breakType;
        this.indent = indent;
        this.indentType = indentType;

        List<String> paragraphs = StringUtils.cutInParagraphs(text);
        assertNoControlCharacters(paragraphs);

        this.wrappedText = Collections.unmodifiableList(wrapText(paragraphs));
        this.textBoxDimensions = calculateDimensions();
    }

    /**
     * Returns the dimensions of the resulting text box.
     *
     * @return an instance of TextBoxDimensions
     */
    public TextBoxDimensions getTextBoxDimensions() {
        return this.textBoxDimensions;
    }

    /**
     * Writes text with specified position as the beginning of the first baseline.
     *
     * @param x x-coordinate
     * @param y y-coordinate of the beginning of the first baseline
     * @param pdPageContentStream
     * @throws UtilsPdfboxException
     */
    public void renderWithFirstBaselineAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfboxException {

        PdfRenderer.renderParagraphWithFirstBaselineAsReference(
                this.wrappedText,
                x,
                y,
                this.pdFont,
                this.fontSize,
                this.leading,
                this.indent,
                this.indentType,
                pdPageContentStream
        );
    }

    /**
     * Writes text with specified position as the upper left corner of the virtual border box.
     *
     * @param x x-coordinate
     * @param y y-coordinate of the beginning of the first baseline
     * @param pdPageContentStream
     * @throws UtilsPdfboxException
     */
    public void renderWithUpperLeftCornerAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfboxException {

        PdfRenderer.renderParagraphWithUpperLeftCornerAsReference(
                this.wrappedText,
                x,
                y,
                this.pdFont,
                this.fontSize,
                this.leading,
                this.indent,
                this.indentType,
                pdPageContentStream
        );
    }

    /**
     * Renders border box and baselines. For debug purposes.
     *
     * @param x x coordinate
     * @param y y coordinate of the beginning first baseline
     * @param pdPageContentStream initialized PdPageContentStream
     * @throws UtilsPdfboxException
     */
    public void renderMatrixWithFirstBaselineAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfboxException {

        renderMatrix(x, y, pdPageContentStream);
    }

    /**
     * Renders border box and baselines. For debug purposes.
     *
     * @param x x-coordinate
     * @param y y-coordinate of upper left corner of border box
     * @param pdPageContentStream initialized PdPageContentStream
     * @throws UtilsPdfboxException
     */
    public void renderMatrixWithUpperLeftCornerAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfboxException {

        y = y - PdfUtils.getFontHeight(this.pdFont, this.fontSize);
        renderMatrix(x, y, pdPageContentStream);
    }

    private void renderMatrix(float x, float y, PDPageContentStream pdPageContentStream) throws UtilsPdfboxException {
        float fontHeight = PdfUtils.getFontHeight(this.pdFont, this.fontSize);

        try {
            pdPageContentStream.setLineWidth(1f);
            PDColor red = new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE);
            pdPageContentStream.setStrokingColor(red);

            float y_upper = y + fontHeight;
            float height = this.textBoxDimensions.getHeight();
            float y_lower = y_upper - height;

            pdPageContentStream.addRect(x, y_lower, this.width, height);
            pdPageContentStream.addRect(0,0, 611, 791);
            pdPageContentStream.stroke();

            for (int i = 0; i < this.textBoxDimensions.getNrOfTextLines(); i++) {
                float yDistance = this.textBoxDimensions.getDistanceBaselineToTop(i);
                float yEndpoint = y_upper - yDistance;
                pdPageContentStream.moveTo(x, yEndpoint);
                pdPageContentStream.lineTo(x + this.width, yEndpoint);
                pdPageContentStream.stroke();
            }
        } catch (IOException e) {
            throw new UtilsPdfboxException(e.getMessage(), e);
        }
    }

    private void assertNoControlCharacters(List<String> paragraphs) {
        for (String paragraph : paragraphs) {
            if (Strings.containsControlCharacter(paragraph))
                throw new IllegalArgumentException("Specified text contains at least one control character.");
        }
    }

    private List<String> wrapText(List<String> paragraphs) throws UtilsPdfboxException {
        TextWrapperConfig textWrapperConfig =
                new TextWrapperConfig(
                        this.pdFont,
                        this.fontSize,
                        this.width,
                        this.lineBreakCharacter,
                        this.breakType,
                        this.indent,
                        this.indentType);
        List<String> wrappedText = new ArrayList<>();
        for (String paragraph : paragraphs) {
            List<String> wrappedParagraph = TextWrapper.wrap(textWrapperConfig, paragraph);
            wrappedText.addAll(wrappedParagraph);
        }
        return wrappedText;
    }

    private TextBoxDimensions calculateDimensions() {
        float fontHeight = PdfUtils.getFontHeight(this.pdFont, this.fontSize);
        float lineHeight = this.leading;
        float height = fontHeight + ((this.wrappedText.size() -1) * lineHeight);
        List<Float> distanceBaseLineToTopList = new ArrayList<>();
        for (int i = 0; i < this.wrappedText.size(); i++) {
            if (i == 0) {
                distanceBaseLineToTopList.add(fontHeight);
            } else {
                distanceBaseLineToTopList.add(fontHeight + (i * lineHeight));
            }
        }
        return new TextBoxDimensions(
                this.width,
                height,
                distanceBaseLineToTopList
        );
    }

}
