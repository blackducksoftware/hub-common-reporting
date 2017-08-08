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

public class Rectangle {

    public int width;
    public int height;

    // Should only be set by the DocumentManager
    public int xCoord;
    // Should only be set by the DocumentManager
    public int yCoord;

    // IMPORTANT : PDBox treats the bottom left of the page as the origin
    public boolean collidesWith(final Rectangle rectangle) {
        return collidesWith(rectangle.xCoord, rectangle.yCoord, rectangle.width, rectangle.height);
    }

    public boolean collidesWith(final int xCoord, final int yCoord, final int width, final int height) {
        if (containsPoint(xCoord, yCoord) || containsPoint(xCoord + width, yCoord - height)) {
            return true;
        }
        if (xCoord <= this.xCoord && (xCoord + width) >= this.xCoord && yCoord >= this.yCoord && (yCoord - height) <= this.yCoord) {
            return true;
        }

        return false;
    }

    public boolean containsPoint(final int xCoord, final int yCoord) {
        if (this.xCoord < xCoord && (this.xCoord + width) > xCoord && this.yCoord > yCoord && (this.yCoord - height) < yCoord) {
            return true;
        }
        return false;
    }

}
