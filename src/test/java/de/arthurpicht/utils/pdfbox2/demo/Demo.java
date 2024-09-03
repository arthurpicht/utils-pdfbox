package de.arthurpicht.utils.pdfbox2.demo;

import de.arthurpicht.utils.pdfbox2.DynamicTextBox;
import de.arthurpicht.utils.pdfbox2.UtilsPdfbox2Exception;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo {

    @Test
    public void demo1() throws IOException, UtilsPdfbox2Exception {

        Path demoPdfFile = Paths.get("src/test/temp/demo1.pdf");
        Files.createDirectories(demoPdfFile.getParent());

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(document, page);

        DynamicTextBox dynamicTextBox = new DynamicTextBox(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor" +
                        " invidunt ut labore et dolore magna aliquyam erat",
                new PDType1Font(Standard14Fonts.FontName.COURIER),
                12,
                14.5f,
                200
        );

        System.out.println("Precalculated height: " + dynamicTextBox.getTextBoxDimensions().getHeight());

        dynamicTextBox.renderWithFirstBaselineAsReference(50, 500, pdPageContentStream);
        dynamicTextBox.renderMatrixWithFirstBaselineAsReference(50, 500, pdPageContentStream);

        dynamicTextBox.renderWithUpperLeftCornerAsReference(300, 500, pdPageContentStream);
        dynamicTextBox.renderMatrixWithUpperLeftCornerAsReference(300, 500, pdPageContentStream);

        pdPageContentStream.close();

        document.save(demoPdfFile.toFile());
        document.close();;
    }

}
