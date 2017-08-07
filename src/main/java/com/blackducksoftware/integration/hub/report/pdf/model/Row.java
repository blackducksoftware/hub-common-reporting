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

public class Row {

    Border border;
    Padding padding;

    List<Cell> cells;

    public Row() {

    }

    public void addCells(final Cell... cellsToAdd) {
        cells.addAll(Arrays.asList(cellsToAdd));
    }

    public void addCell(final int index, final Cell cell) {
        cells.add(index, cell);
    }

    public void removeCells(final Cell... cellsToRemove) {
        cells.removeAll(Arrays.asList(cellsToRemove));
    }

    public void removeCell(final int index) {
        cells.remove(index);
    }

    public Cell mergeCells(final Cell... cells) {
        return null;
    }

}
