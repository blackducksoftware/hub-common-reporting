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
package com.blackducksoftware.integration.hub.report.pdf.style;

public class Padding {

    final int leftPadding;
    final int rightPadding;
    final int topPadding;
    final int bottomPadding;

    public Padding(final int padding) {
        this.leftPadding = padding;
        this.rightPadding = padding;
        this.topPadding = padding;
        this.bottomPadding = padding;
    }

    public Padding(final int xPadding, final int yPadding) {
        this.leftPadding = xPadding;
        this.rightPadding = xPadding;
        this.topPadding = yPadding;
        this.bottomPadding = yPadding;
    }

    public Padding(final int leftPadding, final int rightPadding, final int topPadding, final int bottomPadding) {
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
    }
}
