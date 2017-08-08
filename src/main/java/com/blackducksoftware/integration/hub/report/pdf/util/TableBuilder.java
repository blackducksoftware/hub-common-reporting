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

public class TableBuilder {

    public Padding tablePadding;
    public Border tableBorder;
    public CellStyle tableCellStyle;
    public TextStyle tableTextStyle;

    private final int width;
    private final int height;

    public List<RowBuilder> rowBuilders = new ArrayList<>();

    public TableBuilder(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public RowBuilder newRowBuilder() {
        final RowBuilder rowBuilder = new RowBuilder();
        rowBuilder.rowPadding = tablePadding;
        rowBuilder.rowBorder = tableBorder;
        rowBuilder.rowCellStyle = tableCellStyle;
        rowBuilder.rowTextStyle = tableTextStyle;
        return rowBuilder;
    }

    public Table buildTable() {
        // FIXME handle paging

        final int numberOfRowBuilders = rowBuilders.size();
        if (numberOfRowBuilders > 0) {
            final int rowHeight = height / numberOfRowBuilders;
            final List<Row> rows = new ArrayList<>();
            for (final RowBuilder rowBuilder : rowBuilders) {
                rows.add(rowBuilder.buildRow(width, rowHeight));
            }

            return new Table(tableBorder, tablePadding, rows, width, height);
        }
        return null;
    }

}
