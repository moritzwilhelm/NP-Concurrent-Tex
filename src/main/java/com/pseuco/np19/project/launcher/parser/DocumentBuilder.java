package com.pseuco.np19.project.launcher.parser;

/**
 * A {@link DocumentBuilder} is used by the {@link Parser} to build a document.
 */
public interface DocumentBuilder {
    /**
     * Appends a forced-page-break to the document.
     *
     * @param position The position of the page break in the <i>input</i> document.
     */
    void appendForcedPageBreak(Position position);

    /**
     * Appends a paragraph to the document.
     *
     * @param position The position of the first character of the paragraph.
     *
     * @return A {@link ParagraphBuilder} to build the paragraph with.
     */
    ParagraphBuilder appendParagraph(Position position);

    /**
     * Called after every element has been appended to the document.
     */
    void finish();
}
