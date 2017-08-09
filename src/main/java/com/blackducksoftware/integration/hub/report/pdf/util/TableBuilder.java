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

import com.blackducksoftware.integration.hub.report.pdf.model.Row;
import com.blackducksoftware.integration.hub.report.pdf.model.Table;
import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class TableBuilder extends Builder {

    public Padding tablePadding = new Padding(0);
    public Border tableBorder = new Border();
    public CellStyle tableCellStyle = new CellStyle();
    public TextStyle tableTextStyle = new TextStyle();

    public List<RowBuilder> rowBuilders = new ArrayList<>();

    public RowBuilder newRowBuilder() {
        final RowBuilder rowBuilder = new RowBuilder();
        rowBuilder.rowPadding = new Padding(tablePadding);
        rowBuilder.rowBorder = new Border(tableBorder);
        rowBuilder.rowCellStyle = new CellStyle(tableCellStyle);
        rowBuilder.rowTextStyle = new TextStyle(tableTextStyle);
        rowBuilders.add(rowBuilder);
        return rowBuilder;
    }

    public Table buildTable(final int width, final int maxRowHeight) {
        // FIXME handle paging

        final int numberOfRowBuilders = rowBuilders.size();
        if (numberOfRowBuilders > 0) {
            final List<Row> rows = new ArrayList<>();
            int height = 0;
            for (final RowBuilder rowBuilder : rowBuilders) {
                final Row row = rowBuilder.buildRow(width, maxRowHeight);
                height += row.height;
                rows.add(row);
            }
            return new Table(tableBorder, tablePadding, rows, width, height);
        }
        return null;
    }

    public Padding getTablePadding() {
        return tablePadding;
    }

    public Border getTableBorder() {
        return tableBorder;
    }

    public CellStyle getTableCellStyle() {
        return tableCellStyle;
    }

    public TextStyle getTableTextStyle() {
        return tableTextStyle;
    }

}
