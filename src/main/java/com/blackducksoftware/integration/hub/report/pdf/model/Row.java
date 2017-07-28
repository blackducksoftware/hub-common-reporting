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

import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.RowStyle;

public class Row {
	
    List<Cell> cells;
    RowStyle rowStyle;
    Padding rowPadding;
    
    int maxWidth;
    int maxHeight;
    
    public Row () {
    	
    }
    
    public void addCells(Cell...cellsToAdd) {
		cells.addAll(Arrays.asList(cellsToAdd));
	}

	public void removeCells(Cell...cellsToRemove) {
		cells.removeAll(Arrays.asList(cellsToRemove));
	}
    
    public Cell mergeCells (Cell...cells){
    	return null;
    }
    
}
