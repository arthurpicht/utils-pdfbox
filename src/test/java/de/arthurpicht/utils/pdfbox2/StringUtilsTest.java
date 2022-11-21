package de.arthurpicht.utils.pdfbox2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    public void empty() {
        List<String> paragraphs = StringUtils.cutInParagraphs("");
        assertEquals(1, paragraphs.size());
        assertEquals("", paragraphs.get(0));
    }

    @Test
    public void noLineFeed() {
        List<String> paragraphs = StringUtils.cutInParagraphs("Hello World!");
        assertEquals(1, paragraphs.size());
        assertEquals("Hello World!", paragraphs.get(0));
    }

    @Test
    public void withLineFeed() {
        List<String> paragraphs = StringUtils.cutInParagraphs("Hello World!\nSecond line.");
        assertEquals(2, paragraphs.size());
        assertEquals("Hello World!", paragraphs.get(0));
        assertEquals("Second line.", paragraphs.get(1));
    }

    @Test
    public void withLineFeedAtEndOfLine() {
        List<String> paragraphs = StringUtils.cutInParagraphs("Hello World!\n");
        assertEquals(2, paragraphs.size());
        assertEquals("Hello World!", paragraphs.get(0));
        assertEquals("", paragraphs.get(1));
    }

    @Test
    public void threeLines() {
        List<String> paragraphs = StringUtils.cutInParagraphs("Hello World!\nSecond line.\nSomething else.");
        assertEquals(3, paragraphs.size());
        assertEquals("Hello World!", paragraphs.get(0));
        assertEquals("Second line.", paragraphs.get(1));
        assertEquals("Something else.", paragraphs.get(2));
    }

    @Test
    public void doubleLineFeed() {
        List<String> paragraphs = StringUtils.cutInParagraphs("Hello World!\n\nSomething else.");
        assertEquals(3, paragraphs.size());
        assertEquals("Hello World!", paragraphs.get(0));
        assertEquals("", paragraphs.get(1));
        assertEquals("Something else.", paragraphs.get(2));
    }


}