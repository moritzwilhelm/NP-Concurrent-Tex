package com.pseuco.np19.project.slug.tree.inline;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.formatter.InlineFormatter;
import com.pseuco.np19.project.launcher.render.Renderable;

import java.util.function.Consumer;

/**
 * An {@link InlineElement} representing a special (non-word) character sequence.
 */
public class Special extends ValueInlineElement {
    public Special(String value) {
        super(value);
    }

    @Override
    public void format(Consumer<Item<Renderable>> consumer, InlineFormatter formatter) {
        formatter.pushSpecial(consumer, this.getValue());
    }
}
