package com.blackducksoftware.integration.hub.report.pdf.style;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Border extends Style {

    public enum BorderSide {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }

    private final Set<BorderSide> sides;
    private Color color;
    private int width = 0;

    public Border(final Border borderToCopy) {
        sides = borderToCopy.getSides();
        color = borderToCopy.getColor();
        width = borderToCopy.getWidth();
    }

    public Border() {
        this(Color.BLACK, 0);
    }

    public Border(final Color color) {
        this(color, 0);
    }

    public Border(final int width) {
        this(Color.BLACK, width);
    }

    public Border(final Color color, final int width) {
        sides = new HashSet<>();
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
