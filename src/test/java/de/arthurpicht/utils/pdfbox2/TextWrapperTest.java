package de.arthurpicht.utils.pdfbox2;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextWrapperTest {

    @Test
    public void plausibility() throws UtilsPdfbox2Exception {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                PDType1Font.COURIER,
                12,
                100);

        String text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor" +
                " invidunt ut labore et dolore magna aliquyam erat";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        List<String> expected = Arrays.asList(
                "Lorem ipsum",
                "dolor sit",
                "amet,",
                "consetetur",
                "sadipscing",
                "elitr, sed",
                "diam nonumy",
                "eirmod tempor",
                "invidunt ut",
                "labore et",
                "dolore magna",
                "aliquyam erat"
        );

        wrappedText.forEach(System.out::println);

        assertEquals(expected, wrappedText);
    }

    @Test
    public void plausibilityLongWords() throws UtilsPdfbox2Exception {
        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                PDType1Font.COURIER,
                12,
                100);

        String text = "Lorem_ipsum dolor_sit_amet, consetetur_sadipscing_elitr, sed diam nonumy eirmod tempor" +
                " invidunt ut labore et dolore magna aliquyam erat";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        List<String> expected = Arrays.asList(
                "Lorem_ipsum",
                "dolor_sit_ame",
                "t,",
                "consetetur_sa",
                "dipscing_elit",
                "r, sed diam",
                "nonumy eirmod",
                "tempor",
                "invidunt ut",
                "labore et",
                "dolore magna",
                "aliquyam erat"
        );

        wrappedText.forEach(System.out::println);

        assertEquals(expected, wrappedText);
    }

    @Test
    public void plausibilityWhitespace() throws UtilsPdfbox2Exception {
        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                PDType1Font.COURIER,
                12,
                100);

        String text = "Lorem       ipsum               dolor                 sit               amet,                " +
                "consetetur               sadipscing                 elitr,                  sed                    " +
                "diam              nonumy                 eirmod                  tempor                            " +
                "invidunt                ut                  labore                 et            dolore            " +
                "magna                 aliquyam                  erat";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        List<String> expected = Arrays.asList(
                "Lorem",
                "ipsum",
                "dolor",
                "sit",
                "amet,",
                "consetetur",
                "sadipscing",
                "elitr,",
                "sed",
                "diam",
                "nonumy",
                "eirmod",
                "tempor",
                "invidunt",
                "ut",
                "labore",
                "et",
                "dolore",
                "magna",
                "aliquyam",
                "erat"
        );

        wrappedText.forEach(System.out::println);

        assertEquals(expected, wrappedText);
    }

}