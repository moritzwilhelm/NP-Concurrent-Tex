package com.pseuco.np19.project.slug.tree;

import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.slug.tree.block.BlockElement;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input document.
 */
public class Document implements DocumentBuilder {
    private List<BlockElement> elements = new LinkedList<>();

    /**
     * @return Returns the block elements of the document.
     */
    public List<BlockElement> getElements() {
        return this.elements;
    }

    @Override
    public void appendForcedPageBreak(Position position) {
        this.elements.add(new ForcedPageBreak());
    }

    @Override
    public ParagraphBuilder appendParagraph(Position position) {
        Paragraph paragraph = new Paragraph();
        this.elements.add(paragraph);
        return paragraph;
    }

    @Override
    public void finish() {
        this.elements = Collections.unmodifiableList(this.elements);
    }
}
