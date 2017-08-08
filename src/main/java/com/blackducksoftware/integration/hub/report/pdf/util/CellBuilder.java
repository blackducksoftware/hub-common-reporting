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

import java.util.List;

import com.blackducksoftware.integration.hub.report.pdf.model.Cell;
import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class CellBuilder {

    Padding cellPadding;
    Border cellBorder;
    CellStyle cellStyle;
    TextStyle textStyle;

    int width;
    int height;

    int colSpan;

    String imageResourcePath;
    List<String> textSections;
    String annotation;
    String hyperlink;
    String hyperlinkKey;

    public Cell buildCell(final int xCoord, final int yCoord, final int maxWidth, final int maxHeight) {
        if (width > maxWidth) {
            width = maxWidth;
        }
        if (height > maxHeight) {
            height = maxHeight;
        }
        return new Cell(cellPadding, cellBorder, textStyle, cellStyle, imageResourcePath, textSections, annotation, hyperlink, hyperlinkKey, width, height, colSpan, xCoord, yCoord);
    }

}
