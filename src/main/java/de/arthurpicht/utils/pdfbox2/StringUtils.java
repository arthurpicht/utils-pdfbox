package de.arthurpicht.utils.pdfbox2;

import de.arthurpicht.utils.core.assertion.MethodPreconditions;
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

    public static String trim(String string, char trimChar) {
        MethodPreconditions.assertArgumentNotNull("string", string);
        if (string.isEmpty()) return string;

        int start = 0;
        int end = string.length() - 1;

        while (start <= end && string.charAt(start) == trimChar) {
            start++;
        }

        while (end >= start && string.charAt(end) == trimChar) {
            end--;
        }

        return string.substring(start, end + 1);
    }

}
