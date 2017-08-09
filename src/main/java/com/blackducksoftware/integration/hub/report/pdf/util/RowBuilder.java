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

public class RowBuilder extends Builder {

    public Padding rowPadding = new Padding(0);
    public Border rowBorder = new Border();
    public CellStyle rowCellStyle = new CellStyle();
    public TextStyle rowTextStyle = new TextStyle();

    public int height;

    public List<CellBuilder> cellBuilders = new ArrayList<>();

    public CellBuilder newCellBuilder() {
        final CellBuilder cellBuilder = new CellBuilder();
        cellBuilder.cellPadding = new Padding(rowPadding);
        cellBuilder.cellBorder = new Border(rowBorder);
        cellBuilder.cellStyle = new CellStyle(rowCellStyle);
        cellBuilder.textStyle = new TextStyle(rowTextStyle);
        cellBuilders.add(cellBuilder);
        return cellBuilder;
    }

    public Row buildRow(final int maxWidth, final int maxHeight) {
        if (rowCellStyle.getHeight() > maxHeight) {
            rowCellStyle.setHeight(maxHeight);
        }
        final int numberOfCellBuilders = cellBuilders.size();
        if (numberOfCellBuilders > 0) {
            final List<Cell> cells = new ArrayList<>();
            for (final CellBuilder cellBuilder : cellBuilders) {
                cells.add(cellBuilder.buildCell(maxWidth, rowCellStyle.getHeight()));
            }

            return new Row(rowBorder, rowPadding, cells, maxWidth, rowCellStyle.getHeight());
        }
        return null;
    }

    public Padding getRowPadding() {
        return rowPadding;
    }

    public Border getRowBorder() {
        return rowBorder;
    }

    public CellStyle getRowCellStyle() {
        return rowCellStyle;
    }

    public TextStyle getRowTextStyle() {
        return rowTextStyle;
    }

}
