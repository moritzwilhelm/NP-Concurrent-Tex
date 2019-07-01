package com.pseuco.np19.project.slug.tree.inline;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.formatter.InlineFormatter;
import com.pseuco.np19.project.launcher.render.Renderable;

import java.util.function.Consumer;

public abstract class InlineElement {
    public abstract void format(Consumer<Item<Renderable>> consumer, InlineFormatter formatter);

    public abstract String getValue();
}
