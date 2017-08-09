/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.hub.report.pdf.style;

import java.awt.Color;

public class CellStyle extends Style {
    private Color backgroundColor = Color.WHITE;
    private Align contentAlignment = Align.LEFT_CENTER;
    private Padding padding = new Padding(0);
    private Border border;

    private int width;
    private int height;

    public CellStyle(final CellStyle styleToCopy) {
        backgroundColor = styleToCopy.getBackgroundColor();
        contentAlignment = styleToCopy.getContentAlignment();
        padding = styleToCopy.getPadding();
        border = styleToCopy.getBorder();
        width = styleToCopy.getWidth();
        height = styleToCopy.getHeight();
    }

    public CellStyle() {

    }

    public CellStyle setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CellStyle setContentAlignment(final Align contentAlignment) {
        this.contentAlignment = contentAlignment;
        return this;
    }

    public CellStyle setPadding(final Padding padding) {
        this.padding = padding;
        return this;
    }

    public CellStyle setBorder(final Border border) {
        this.border = border;
        return this;
    }

    public CellStyle setWidth(final int width) {
        this.width = width;
        return this;
    }

    public CellStyle setHeight(final int height) {
        this.height = height;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Align getContentAlignment() {
        return contentAlignment;
    }

    public Padding getPadding() {
        return padding;
    }

    public Border getBorder() {
        return border;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
