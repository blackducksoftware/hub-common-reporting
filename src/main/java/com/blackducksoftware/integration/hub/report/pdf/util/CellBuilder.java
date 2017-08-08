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

public class CellBuilder {

    public Padding cellPadding;
    public Border cellBorder;
    public CellStyle cellStyle;
    public TextStyle textStyle;

    public int colSpan = 1;

    public String imageResourcePath;
    public String text;
    public String annotation;
    public String hyperlink;
    public String hyperlinkKey;

    public Cell buildCell(final int maxWidth, final int maxHeight) {
        if (cellStyle.getWidth() > maxWidth) {
            cellStyle.setWidth(maxWidth);
        }
        if (cellStyle.getHeight() > maxHeight) {
            cellStyle.setHeight(maxHeight);
        }
        return new Cell(cellPadding, cellBorder, textStyle, cellStyle, imageResourcePath, text, annotation, hyperlink, hyperlinkKey, colSpan);
    }

}
