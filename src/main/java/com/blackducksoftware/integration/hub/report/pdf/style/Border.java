package com.blackducksoftware.integration.hub.report.pdf.style;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Border {

    public enum BorderSide {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }

    public Set<BorderSide> sides = new HashSet<>();
    public Color color = Color.BLACK;
    public int width = 1;

    public Border() {
    }

    public Border(final Color color) {
        this.color = color;
    }

    public Border(final int width) {
        this.width = width;
    }

    public Border(final Color color, final int width) {
        this.color = color;
        this.width = width;
    }

    public Border addSide(final BorderSide side) {
        sides.add(side);
        return this;
    }

    @Override
    public String toString() {
        return "Border [color=" + color + ", width=" + width + "]";
    }

}
