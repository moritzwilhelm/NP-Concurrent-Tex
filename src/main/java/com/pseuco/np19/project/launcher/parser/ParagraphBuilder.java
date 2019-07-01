package com.pseuco.np19.project.launcher.parser;

/**
 * A {@link ParagraphBuilder} is used by the parser to build a paragraph.
 */
public interface ParagraphBuilder {
    /**
     * Appends a syllable to the paragraph.
     *
     * @param position The position of the syllable in the <i>input</i> document.
     * @param syllable The syllable to append to the paragraph.
     */
    void appendSyllable(Position position, String syllable);

    /**
     * Appends a sequence of <i>special characters</i> (non-word characters) to the paragraph.
     *
     * Examples for special characters are punctuation symbols or quotation marks.
     *
     * @param position The position of the special characters in the <i>input</i> document.
     * @param special The special characters to append to the paragraph.
     */
    void appendSpecial(Position position, String special);

    /**
     * Appends a hyphen to the paragraph.
     *
     * @param position The position of the hyphen in the <i>input</i> document.
     */
    void appendHyphen(Position position);

    /**
     * Appends a space to the paragraph.
     *
     * @param position The position of the space in the <i>input</i> document.
     */
    void appendSpace(Position position);

    /**
     * Called after every element has been appended to the paragraph.
     */
    void finish();
}
