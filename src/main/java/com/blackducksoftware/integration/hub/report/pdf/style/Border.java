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

    private final Set<BorderSide> sides = new HashSet<>();
    private Color color = Color.BLACK;
    private int width = 1;

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

    public Border addSide(final BorderSide... sides) {
        for (final BorderSide side : sides) {
            this.sides.add(side);
        }
        return this;
    }

    public Set<BorderSide> getSides() {
        return sides;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

}
