package de.arthurpicht.utils.pdfbox2;

import de.arthurpicht.utils.pdfbox2.TextWrapperConfig.BreakType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Functionality for breaking strings in parts matching a specified column width.
 */
@SuppressWarnings("ALL")
public class TextWrapper {

    /**
     * Cuts a string in a list of substrings, so that every substring is as long as possible and does not exceed the
     * specified width. When breaking the string, words will be kept intact.
     *
     * @param textWrapperConfig
     * @param string string to be wrapped
     * @return list of strings
     * @throws UtilsPdfbox2Exception
     */
    public static List<String> wrap(TextWrapperConfig textWrapperConfig, String string) throws UtilsPdfbox2Exception {

        BreakType breakType = textWrapperConfig.getBreakType();
        char lineBreakChar = textWrapperConfig.getLineBreakChar();
        List<String> result = new ArrayList<>();

        do {
            String chunk = findNextMatchingSubstring(textWrapperConfig, string, result.size());
//            System.out.println("width matching substring : [" + chunk + "]");

            chunk = applyWordBreaks(string, chunk.length() - 1, breakType, lineBreakChar);
//            System.out.println("broken substring         : [" + chunk + "]");

            string = string.substring(chunk.length());
            if (textWrapperConfig.getBreakType() == BreakType.OMIT_CHAR) {
                chunk = StringUtils.trim(chunk, textWrapperConfig.getLineBreakChar());
                string = StringUtils.trim(string, textWrapperConfig.getLineBreakChar());
            }
            result.add(chunk);
//            System.out.println("add to result            : [" + chunk + "]");
//            System.out.println("remaining string         : [" + string + "]");
//            System.out.println("---");
        } while (!string.isEmpty());

        return result;
    }

    private static String findNextMatchingSubstring(TextWrapperConfig textWrapperConfig, String string, int lineNumber)
            throws UtilsPdfbox2Exception {

        for (int i = 1; i < string.length(); i++) {
            String probe = string.substring(0, i);
            float width;
            if (lineNumber == 0) {
                width = getWidth(textWrapperConfig, probe);
            } else {
                width = textWrapperConfig.getIndent() + getWidth(textWrapperConfig, probe);
            }
            if (width > textWrapperConfig.getWidth()) {
                if (i > 1) {
                    return string.substring(0, i - 1);
                } else {
                    throw new UtilsPdfbox2Exception("width too small.");
                }
            }
        }
        return string;
    }

    static String applyWordBreaks(String string, int index, BreakType breakType, char lineBreakChar) {
        for (int i = index; i >= 0; i--) {
            if (isWordEnd(string, i, breakType, lineBreakChar)) return string.substring(0, i + 1);
        }
        return string.substring(0, index + 1);
    }

    static boolean isWordEnd(String string, int index, BreakType breakType, char lineBreakChar) {

//        System.out.println("isWordEnd? string [" + string + "], index [" + index + "]");

        if (index >= string.length()) throw new IllegalArgumentException("Index out of bounds.");
        if (string.length() == index + 1) return true;

        if (breakType == BreakType.BREAK_BEFORE) {
            return string.charAt(index + 1) == lineBreakChar;
        } else if (breakType == BreakType.BREAK_AFTER) {
            return string.charAt(index) == lineBreakChar;
        } else {
            return string.charAt(index + 1) == lineBreakChar
                   || string.charAt(index) == lineBreakChar;
        }
    }

    private static float getWidth(TextWrapperConfig textWrapperConfig, String string) throws UtilsPdfbox2Exception {
        try {
            return PdfUtils.getWidth(textWrapperConfig.getPdFont(), textWrapperConfig.getFontSize(), string);
//            return textWrapperConfig.getPdFont().getStringWidth(string) / 1000 * textWrapperConfig.getFontSize();
        } catch (IOException e) {
            throw new UtilsPdfbox2Exception(e.getMessage(), e);
        }
    }

}
