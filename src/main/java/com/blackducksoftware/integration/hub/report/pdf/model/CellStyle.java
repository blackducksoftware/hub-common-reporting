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

import java.awt.Color;

public class CellStyle {
    Color backgroundColor = Color.WHITE;
    Align contentAlignment = Align.LEFT_CENTER;
    Padding padding = new Padding(0);

    int width;
    int height;

}
