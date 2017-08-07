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

import java.util.Arrays;
import java.util.List;

import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;

public class Table {

    Border border;
    Padding padding;
    List<Row> rows;

    public void addRows(final Row... rowsToAdd) {
        rows.addAll(Arrays.asList(rowsToAdd));
    }

    public void addRow(final int index, final Row row) {
        rows.add(index, row);
    }

    public void removeRows(final Row... rowsToRemove) {
        rows.removeAll(Arrays.asList(rowsToRemove));
    }

    public void removeRow(final int index) {
        rows.remove(index);
    }

    public Row mergeRows(final Row... rows) {
        return null;
    }
}
