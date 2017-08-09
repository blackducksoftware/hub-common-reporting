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
package com.blackducksoftware.integration.hub.report.pdf.util;

import com.blackducksoftware.integration.hub.report.pdf.model.Cell;
import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class CellBuilder extends Builder {

    public Padding cellPadding = new Padding(0);
    public Border cellBorder = new Border();
    public CellStyle cellStyle = new CellStyle();
    public TextStyle textStyle = new TextStyle();;

    public int colSpan = 1;

    public String imageResourcePath;
    public int imageWidth = 0;
    public int imageHeight = 0;
    public String text;
    public String annotation;
    public String hyperlink;
    public String hyperlinkKey;

    public Cell buildCell(final int maxWidth, final int maxHeight) {
        if (cellStyle.getWidth() > maxWidth || cellStyle.getWidth() == 0) {
            cellStyle.setWidth(maxWidth);
        }
        if (cellStyle.getHeight() > maxHeight || cellStyle.getHeight() == 0) {
            cellStyle.setHeight(maxHeight);
        }
        return new Cell(cellPadding, cellBorder, textStyle, cellStyle, imageResourcePath, imageWidth, imageHeight, text, annotation, hyperlink, hyperlinkKey, colSpan);
    }

    public Padding getCellPadding() {
        return cellPadding;
    }

    public Border getCellBorder() {
        return cellBorder;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

}
