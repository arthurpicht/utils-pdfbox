package de.arthurpicht.utils.pdfbox2;

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
        List<String> result = new ArrayList<>();

        do {
            string = string.trim();
            String chunk = findNextMatchingSubstring(textWrapperConfig, string);
//            System.out.println("found next substring [" + chunk + "]");
            if (!endsWithWordEnd(string, chunk)) {
                chunk = applyWordBreaks(chunk);
//                System.out.println("matching word        [" + chunk + "]");
            }
            result.add(chunk.trim());
            string = string.substring(chunk.length());
        } while (!string.isEmpty());

        return result;
    }

    private static String findNextMatchingSubstring(TextWrapperConfig textWrapperConfig, String string) throws UtilsPdfbox2Exception {
        for (int i = 1; i < string.length(); i++) {
            String probe = string.substring(0, i);
            float width = getWidth(textWrapperConfig, probe);
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

    private static boolean endsWithWordEnd(String string, String substring) {
        if (!string.startsWith(substring)) throw new IllegalArgumentException("Not a substring.");
        if (string.equals(substring)) return true;
        return string.charAt(substring.length() - 1) == ' ' || string.charAt(substring.length()) == ' ';
    }

    private static String applyWordBreaks(String string) {
        if (string.endsWith(" ")) return string;
        if (!string.contains(" ")) return string;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) == ' ') {
                if (i > 0) {
                    return string.substring(0, i);
                } else {
                    return " ";
                }
            }
        }
        return string;
    }

    private static float getWidth(TextWrapperConfig textWrapperConfig, String string) throws UtilsPdfbox2Exception {
        try {
            return textWrapperConfig.getPdFont().getStringWidth(string) / 1000 * textWrapperConfig.getFontSize();
        } catch (IOException e) {
            throw new UtilsPdfbox2Exception(e.getMessage(), e);
        }
    }

}
