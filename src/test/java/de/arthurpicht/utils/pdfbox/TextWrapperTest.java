package de.arthurpicht.utils.pdfbox;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextWrapperTest {

    @Test
    public void plausibility() throws UtilsPdfboxException {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                new PDType1Font(Standard14Fonts.FontName.COURIER),
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
    public void plausibilityLongWords() throws UtilsPdfboxException {
        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                new PDType1Font(Standard14Fonts.FontName.COURIER),
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
    public void plausibilityWhitespace() throws UtilsPdfboxException {
        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                new PDType1Font(Standard14Fonts.FontName.COURIER),
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

    @Test
    public void plausibilitySlashBefore() throws UtilsPdfboxException {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                new PDType1Font(Standard14Fonts.FontName.COURIER),
                12,
                100,
                '/',
                BreakType.BREAK_BEFORE,
                0,
                IndentType.NONE);

        String text = "Lorem/ipsum/dolor/sit/amet,/consetetur/sadipscing/elitr,/sed/diam/nonumy/eirmod/tempor" +
                      "/invidunt/ut/labore/et/dolore/magna/aliquyam/erat";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        List<String> expected = Arrays.asList(
                "Lorem/ipsum",
                "/dolor/sit",
                "/amet,",
                "/consetetur",
                "/sadipscing",
                "/elitr,/sed",
                "/diam/nonumy",
                "/eirmod"
                ,"/tempor",
                "/invidunt/ut",
                "/labore/et",
                "/dolore/magna",
                "/aliquyam/erat"
        );

        wrappedText.forEach(System.out::println);

        assertEquals(expected, wrappedText);
    }

    @Test
    public void plausibilitySlashAfter() throws UtilsPdfboxException {

        TextWrapperConfig textWrapperConfig = new TextWrapperConfig(
                new PDType1Font(Standard14Fonts.FontName.COURIER),
                12,
                100,
                '/',
                BreakType.BREAK_AFTER,
                0,
                IndentType.NONE);

        String text = "Lorem/ipsum/dolor/sit/amet,/consetetur/sadipscing/elitr,/sed/diam/nonumy/eirmod/tempor" +
                "/invidunt/ut/labore/et/dolore/magna/aliquyam/erat";

        List<String> wrappedText = TextWrapper.wrap(textWrapperConfig, text);

        List<String> expected = Arrays.asList(
                "Lorem/ipsum/",
                "dolor/sit/",
                "amet,/",
                "consetetur/",
                "sadipscing/",
                "elitr,/sed/",
                "diam/nonumy/",
                "eirmod/",
                "tempor/",
                "invidunt/ut/",
                "labore/et/",
                "dolore/magna/",
                "aliquyam/erat"
        );

        wrappedText.forEach(System.out::println);

        assertEquals(expected, wrappedText);
    }


    @Test
    void applyWordBreaks() {

        String string = "ABCXDERRRRRRRRRR";

        assertEquals("ABCX", TextWrapper.applyWordBreaks(string, 10, BreakType.BREAK_AFTER, 'X'));
        assertEquals("ABC", TextWrapper.applyWordBreaks(string, 10, BreakType.BREAK_BEFORE, 'X'));
        assertEquals("ABCX", TextWrapper.applyWordBreaks(string, 10, BreakType.OMIT_CHAR, 'X'));

        assertEquals("ABCXDERRRRR", TextWrapper.applyWordBreaks(string, 10, BreakType.OMIT_CHAR, ' '));
    }

    @Test
    void isWordEnd() {

        String string = "ABCXDE";

        assertFalse(TextWrapper.isWordEnd(string, 1, BreakType.BREAK_AFTER, 'X'));
        assertFalse(TextWrapper.isWordEnd(string, 2, BreakType.BREAK_AFTER, 'X'));
        assertTrue(TextWrapper.isWordEnd(string, 3, BreakType.BREAK_AFTER, 'X'));
        assertFalse(TextWrapper.isWordEnd(string, 4, BreakType.BREAK_AFTER, 'X'));

        assertFalse(TextWrapper.isWordEnd(string, 1, BreakType.BREAK_BEFORE, 'X'));
        assertTrue(TextWrapper.isWordEnd(string, 2, BreakType.BREAK_BEFORE, 'X'));
        assertFalse(TextWrapper.isWordEnd(string, 3, BreakType.BREAK_BEFORE, 'X'));
        assertFalse(TextWrapper.isWordEnd(string, 4, BreakType.BREAK_BEFORE, 'X'));

        assertFalse(TextWrapper.isWordEnd(string, 1, BreakType.OMIT_CHAR, 'X'));
        assertTrue(TextWrapper.isWordEnd(string, 2, BreakType.OMIT_CHAR, 'X'));
        assertTrue(TextWrapper.isWordEnd(string, 3, BreakType.OMIT_CHAR, 'X'));
        assertFalse(TextWrapper.isWordEnd(string, 4, BreakType.OMIT_CHAR, 'X'));
    }

}