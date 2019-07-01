package com.pseuco.np19.project.slug.tree.block;

import com.pseuco.np19.project.launcher.formatter.InlineFormatter;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.slug.tree.inline.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link BlockElement} representing a paragraph.
 */
public class Paragraph extends BlockElement implements ParagraphBuilder {
    private List<InlineElement> elements = new ArrayList<>();

    /**
     * @return Returns the inline elements making up the paragraph.
     */
    public List<InlineElement> getElements() {
        return this.elements;
    }

    public List<Item<Renderable>> format(InlineFormatter formatter) {
        final List<Item<Renderable>> items = new ArrayList<>();
        for (InlineElement element : this.elements) {
            element.format(items::add, formatter);
        }
        formatter.endParagraph(items::add);
        return items;
    }

    @Override
    public void appendSyllable(Position position, String syllable) {
        this.elements.add(new Syllable(syllable));
    }

    @Override
    public void appendSpecial(Position position, String special) {
        this.elements.add(new Special(special));
    }

    @Override
    public void appendHyphen(Position position) {
        this.elements.add(new Hyphen());
    }

    @Override
    public void appendSpace(Position position) {
        this.elements.add(new Space());
    }

    @Override
    public void finish() {
        this.elements = Collections.unmodifiableList(this.elements);
    }

    @Override
    public void accept(IBlockVisitor visitor) {
        visitor.visit(this);
    }
}
