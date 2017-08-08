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
package com.blackducksoftware.integration.hub.report.pdf.model;

import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class Cell extends Rectangle {

    private final Padding padding;
    private final Border border;
    private final TextStyle textStyle;
    private final CellStyle cellStyle;
    private final String imageResourcePath;
    private final String text;
    private final String annotation;
    private final String hyperlink;
    private final String hyperlinkKey;

    private final int colSpan;

    public Cell(final Padding padding, final Border border, final TextStyle textStyle, final CellStyle cellStyle, final String imageResourcePath, final String text, final String annotation, final String hyperlink, final String hyperlinkKey,
            final int width, final int height, final int colSpan) {
        this.padding = padding;
        this.border = border;
        this.textStyle = textStyle;
        this.cellStyle = cellStyle;
        this.imageResourcePath = imageResourcePath;
        this.text = text;
        this.annotation = annotation;
        this.hyperlink = hyperlink;
        this.hyperlinkKey = hyperlinkKey;
        this.width = width;
        this.height = height;
        this.colSpan = colSpan;
    }

    public Padding getPadding() {
        return padding;
    }

    public Border getBorder() {
        return border;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public String getImageResourcePath() {
        return imageResourcePath;
    }

    public String getText() {
        return text;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public String getHyperlinkKey() {
        return hyperlinkKey;
    }

    public int getColSpan() {
        return colSpan;
    }

}
