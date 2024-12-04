package de.arthurpicht.utils.pdfbox2.demo;

import de.arthurpicht.utils.pdfbox2.BreakType;
import de.arthurpicht.utils.pdfbox2.DynamicTextBox;
import de.arthurpicht.utils.pdfbox2.IndentType;
import de.arthurpicht.utils.pdfbox2.UtilsPdfboxException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo {

    @Test
    public void demo1() throws IOException, UtilsPdfboxException {

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

        dynamicTextBox.renderWithFirstBaselineAsReference(50, 750, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 750, pdPageContentStream);

        dynamicTextBox.renderWithUpperLeftCornerAsReference(300, 750, pdPageContentStream);
        dynamicTextBox.renderMatrixWithUpperLeftCornerAsReference(300, 750, pdPageContentStream);

        String textBrokenBySlashes = "Lorem/ipsum/dolor/sit/amet,/consetetur.../sadipscing/elitr,/sed/diam/nonumy/eirmod/tempor" +
                "/invidunt/ut/labore/et/dolore/magna/aliquyam/erat";

        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textBrokenBySlashes)
                .withWidth(100)
                .withLineBreakCharacter('/')
                .withBreakType(BreakType.BREAK_BEFORE)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(50, 600, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 600, pdPageContentStream);

        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textBrokenBySlashes)
                .withWidth(100)
                .withLineBreakCharacter('/')
                .withBreakType(BreakType.BREAK_AFTER)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(200, 600, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(200, 600, pdPageContentStream);

        String textTooLong = "this_will_break_the_line_without_any_occurrence_of_any_line_breaking_character" +
                "_what_so_ever_at_any_place_and_any_time";
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(textTooLong)
                .withWidth(100)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(350, 600, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(350, 600, pdPageContentStream);

        // indent
        dynamicTextBox = new DynamicTextBox.Builder()
                .withText(loremIpsum)
                .withWidth(150)
                .withIndent(40)
                .withIndentType(IndentType.ALL_EXCEPT_FIRST_LINE)
                .build();
        dynamicTextBox.renderWithFirstBaselineAsReference(50, 350, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 350, pdPageContentStream);

        pdPageContentStream.close();

        document.save(demoPdfFile.toFile());
        document.close();;
    }

}
