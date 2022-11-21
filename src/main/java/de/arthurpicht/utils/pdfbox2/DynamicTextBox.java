package de.arthurpicht.utils.pdfbox2;

import de.arthurpicht.utils.core.strings.Strings;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
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
    private final int fontSize;
    private final float leading;
    private final float width;
    private final List<String> wrappedText;
    private final TextBoxDimensions textBoxDimensions;

    public DynamicTextBox(
            String text,
            PDFont pdFont,
            int fontSize,
            float leading,
            float width)
            throws UtilsPdfbox2Exception {

        assertArgumentNotNull("text", text);
        assertArgumentNotNull("pdFont", pdFont);

        this.pdFont = pdFont;
        this.fontSize = fontSize;
        this.leading = leading;
        this.width = width;

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
     * @throws UtilsPdfbox2Exception
     */
    public void renderWithFirstBaselineAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfbox2Exception {

        render(x, y, pdPageContentStream);
    }

    /**
     * Writes text with specified position as the upper left corner of the virtual border box.
     *
     * @param x x-coordinate
     * @param y y-coordinate of the beginning of the first baseline
     * @param pdPageContentStream
     * @throws UtilsPdfbox2Exception
     */
    public void renderWithUpperLeftCornerAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfbox2Exception {

        y = y - PdfUtils.getFontHeight(this.pdFont, this.fontSize);
        render(x, y, pdPageContentStream);
    }

    /**
     * Renders border box and baselines. For debug purposes.
     *
     * @param x x coordinate
     * @param y y coordinate of the beginning first baseline
     * @param pdPageContentStream initialized PdPageContentStream
     * @throws UtilsPdfbox2Exception
     */
    public void renderMatrixWithFirstBaselineAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfbox2Exception {

        renderMatrix(x, y, pdPageContentStream);
    }

    /**
     * Renders border box and baselines. For debug purposes.
     *
     * @param x x-coordinate
     * @param y y-coordinate of upper left corner of border box
     * @param pdPageContentStream initialized PdPageContentStream
     * @throws UtilsPdfbox2Exception
     */
    public void renderMatrixWithUpperLeftCornerAsReference(float x, float y, PDPageContentStream pdPageContentStream)
            throws UtilsPdfbox2Exception {

        y = y - PdfUtils.getFontHeight(this.pdFont, this.fontSize);
        renderMatrix(x, y, pdPageContentStream);
    }

    private void renderMatrix(float x, float y, PDPageContentStream pdPageContentStream) throws UtilsPdfbox2Exception {
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
            throw new UtilsPdfbox2Exception(e.getMessage(), e);
        }
    }

    private void render(float x, float y, PDPageContentStream pdPageContentStream) throws UtilsPdfbox2Exception {
        try {
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(this.pdFont, this.fontSize);
            pdPageContentStream.setLeading(this.leading);
            pdPageContentStream.newLineAtOffset(x, y);
            for (String string : this.wrappedText) {
                pdPageContentStream.showText(string);
                pdPageContentStream.newLine();
            }
            pdPageContentStream.endText();
        } catch (IOException e) {
            throw new UtilsPdfbox2Exception(e.getMessage(), e);
        }
    }

    private void assertNoControlCharacters(List<String> paragraphs) {
        for (String paragraph : paragraphs) {
            if (Strings.containsControlCharacter(paragraph))
                throw new IllegalArgumentException("Specified text contains at least one control character.");
        }
    }

    private List<String> wrapText(List<String> paragraphs) throws UtilsPdfbox2Exception {
        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(this.pdFont, this.fontSize, this.width);
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
