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

import com.blackducksoftware.integration.hub.report.pdf.model.Align;
import com.blackducksoftware.integration.hub.report.pdf.model.Cell;
import com.blackducksoftware.integration.hub.report.pdf.style.Border;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.Padding;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class CellBuilder {
	
	CellStyle cellStyle;
	TextStyle textStyle;
	Align contentAlignment = Align.LEFT_CENTER;
    Padding padding = new Padding(0);
    Border border = new Border();
	
	public CellBuilder(CellStyle cellStyle, TextStyle textStyle) {
		this.cellStyle = cellStyle;
		this.textStyle = textStyle;
	}
	
	public CellBuilder addText() {
		return null;
	}
		
	public Cell buildCell() {
		return null;
	}

}
