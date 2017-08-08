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

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TextStyle {

    PDFont font = PDType1Font.HELVETICA;
    float fontSize = 12f;
    Color textColor = Color.BLACK;
    Color highlightColor;

    public TextStyle setFont(final PDFont font) {
        this.font = font;
        return this;
    }

    public TextStyle setFontSize(final float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextStyle setTextColor(final Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public TextStyle setHighlightColor(final Color highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

}
