package de.arthurpicht.utils.pdfbox2;

import de.arthurpicht.utils.core.strings.Strings;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static List<String> cutInParagraphs(String string) {
        List<String> paragraphs = new ArrayList<>();
        String work = string;
        while (work.contains("\n")) {
            String[] substrings = Strings.splitAtDelimiter(work, "\n");
            paragraphs.add(substrings[0]);
            work = substrings[1];
        }
        paragraphs.add(work);
        return paragraphs;
    }

}
