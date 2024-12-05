package de.arthurpicht.utils.pdfbox;

/**
 * Determines how to deal with characters that trigger line break:
 * OMIT_CHAR = delete character in case of line break
 * BREAK_BEFORE = line break before character, character will be first char in next line
 * BREAK_AFTER = line break after character, character will be last character in broken line
 */
public enum BreakType {OMIT_CHAR, BREAK_BEFORE, BREAK_AFTER}
