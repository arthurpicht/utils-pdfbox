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
    public void whitespace() throws UtilsPdfbox2Exception {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                PDType1Font.COURIER,
                12,
                100);

        String text = "    Lorem                             ipsum    dolor sit amet, consetetur sadipscing elitr," +
                " sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua." +
                " At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata" +
                " sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr," +
                " sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua." +
                " At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata" +
                " sanctus est Lorem ipsum dolor sit amet.";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        wrappedText.forEach(System.out::println);
    }

    @Test
    public void longWords() throws UtilsPdfbox2Exception {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                PDType1Font.COURIER,
                12,
                100);

        String text = " Lorem ipsum    dolor_sit_amet,_consetetur_sadipscing_elitr, sed diam nonumy eirmod tempor" +
                " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et" +
                " justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum" +
                " dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod" +
                " tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam" +
                " et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem" +
                " ipsum dolor sit amet.";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        wrappedText.forEach(System.out::println);
    }

}