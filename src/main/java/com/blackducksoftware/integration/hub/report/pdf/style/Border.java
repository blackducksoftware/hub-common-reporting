package com.blackducksoftware.integration.hub.report.pdf.style;

import java.awt.Color;

public class Border {

    public enum BORDER_SIDE {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }

    public BORDER_SIDE[] sides = BORDER_SIDE.values();
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

    @Override
    public String toString() {
        return "Border [color=" + color + ", width=" + width + "]";
    }

}
