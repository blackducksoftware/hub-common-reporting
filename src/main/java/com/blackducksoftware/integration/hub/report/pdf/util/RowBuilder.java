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

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.integration.hub.report.pdf.model.Cell;
import com.blackducksoftware.integration.hub.report.pdf.model.Row;
import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class RowBuilder {

    public Padding rowPadding;
    public Border rowBorder;
    public CellStyle rowCellStyle;
    public TextStyle rowTextStyle;

    public int height;

    public List<CellBuilder> cellBuilders = new ArrayList<>();

    public CellBuilder newCellBuilder() {
        final CellBuilder cellBuilder = new CellBuilder();
        cellBuilder.cellPadding = rowPadding;
        cellBuilder.cellBorder = rowBorder;
        cellBuilder.cellStyle = rowCellStyle;
        cellBuilder.textStyle = rowTextStyle;
        return cellBuilder;
    }

    public Row buildRow(final int maxWidth, final int maxHeight) {
        if (height > maxHeight) {
            height = maxHeight;
        }
        final int numberOfCellBuilders = cellBuilders.size();
        if (numberOfCellBuilders > 0) {
            final int width = maxWidth / numberOfCellBuilders;
            final List<Cell> cells = new ArrayList<>();
            for (final CellBuilder cellBuilder : cellBuilders) {
                cells.add(cellBuilder.buildCell(width, height));
            }

            return new Row(rowBorder, rowPadding, cells, maxWidth, height);
        }
        return null;
    }

}
