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

import java.util.List;

import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;

public class Row extends Rectangle {

    private final Border border;
    private final Padding padding;

    private final List<Cell> cells;

    public Row(final Border border, final Padding padding, final List<Cell> cells, final int width, final int height) {
        this.border = border;
        this.padding = padding;
        this.cells = cells;
        this.width = width;
        this.height = height;
    }

    public Border getBorder() {
        return border;
    }

    public Padding getPadding() {
        return padding;
    }

    public List<Cell> getCells() {
        return cells;
    }

}
