package com.pseuco.np19.project.slug.tree.inline;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.formatter.InlineFormatter;
import com.pseuco.np19.project.launcher.render.Renderable;

import java.util.function.Consumer;

/**
 * An {@link InlineElement} representing a space.
 */
public class Space extends InlineElement {
    @Override
    public void format(Consumer<Item<Renderable>> consumer, InlineFormatter formatter) {
        formatter.pushSpace(consumer);
    }

    @Override
    public String getValue() {
        return " ";
    }
}
