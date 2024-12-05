package de.arthurpicht.utils.pdfbox.demo;

import de.arthurpicht.utils.pdfbox.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("NewClassNamingConvention")
public class Demo {

    @Test
    public void demo1() throws IOException, UtilsPdfboxException {

        PDFont pdFont = new PDType1Font(Standard14Fonts.FontName.COURIER);
        float fontSizeHeader = 14;
        float leadingHeader = 16;

        Path demoPdfFile = Paths.get("src/test/temp/demo1.pdf");
        Files.createDirectories(demoPdfFile.getParent());

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(document, page);

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor" +
                " invidunt ut labore et dolore magna aliquyam erat";

        DynamicTextBox dynamicTextBox = new DynamicTextBox.Builder()
                .withText(loremIpsum)
                .build();
        System.out.println("Precalculated height: " + dynamicTextBox.getTextBoxDimensions().getHeight());

        PdfRenderer.renderLineOfText(
                "default at baseline",
                50,
                750,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox.renderWithFirstBaselineAsReference(50, 730, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 730, pdPageContentStream);

        PdfRenderer.renderLineOfText(
                "default at left corner",
                300,
                750,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox.renderWithUpperLeftCornerAsReference(300, 730, pdPageContentStream);
        dynamicTextBox.renderMatrixWithUpperLeftCornerAsReference(300, 730, pdPageContentStream);

        String textBrokenBySlashes = "Lorem/ipsum/dolor/sit/amet,/consetetur.../sadipscing/elitr,/sed/diam/nonumy/eirmod/tempor" +
                "/invidunt/ut/labore/et/dolore/magna/aliquyam/erat";

        PdfRenderer.renderLineOfText(
                "break before \"/\"",
                50,
                600,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textBrokenBySlashes)
                .withWidth(100)
                .withLineBreakCharacter('/')
                .withBreakType(BreakType.BREAK_BEFORE)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(50, 580, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 580, pdPageContentStream);

        PdfRenderer.renderLineOfText(
                "break after \"/\"",
                230,
                600,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textBrokenBySlashes)
                .withWidth(100)
                .withLineBreakCharacter('/')
                .withBreakType(BreakType.BREAK_AFTER)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(230, 580, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(230, 580, pdPageContentStream);

        PdfRenderer.renderLineOfText(
                "too long",
                400,
                600,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        String textTooLong = "this_will_break_the_line_without_any_occurrence_of_any_line_breaking_character" +
                "_what_so_ever_at_any_place_and_any_time";
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textTooLong)
                .withWidth(100)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(400, 580, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(400, 580, pdPageContentStream);

        PdfRenderer.renderLineOfText(
                "indent ALL_EXCEPT_FIRST_LINE",
                50,
                350,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(loremIpsum)
                .withWidth(150)
                .withIndent(40)
                .withIndentType(IndentType.ALL_EXCEPT_FIRST_LINE)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(50, 330, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 330, pdPageContentStream);

        PdfRenderer.renderLineOfText(
                "indent FIRST_LINE_ONLY",
                320,
                350,
                pdFont,
                fontSizeHeader,
                leadingHeader,
                pdPageContentStream
        );
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(loremIpsum)
                .withWidth(150)
                .withIndent(40)
                .withIndentType(IndentType.FIRST_LINE_ONLY)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(320, 330, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(320, 330, pdPageContentStream);

        pdPageContentStream.close();

        document.save(demoPdfFile.toFile());
        document.close();
    }



}
