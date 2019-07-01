package com.pseuco.np19.project.slug.tree.inline;

public abstract class ValueInlineElement extends InlineElement {
    private final String value;

    ValueInlineElement(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
