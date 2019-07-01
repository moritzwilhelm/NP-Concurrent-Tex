package com.pseuco.np19.project.slug.tree.inline;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.formatter.InlineFormatter;
import com.pseuco.np19.project.launcher.render.Renderable;

import java.util.function.Consumer;

/**
 * An {@link InlineElement} representing a syllable.
 */
public class Syllable extends ValueInlineElement {
    public Syllable(String value) {
        super(value);
    }

    @Override
    public void format(Consumer<Item<Renderable>> consumer, InlineFormatter formatter) {
        formatter.pushSyllable(consumer, this.getValue());
    }
}
