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
package com.blackducksoftware.integration.hub.report;

import java.io.File;

import org.junit.Test;

import com.blackducksoftware.integration.hub.report.pdf.PDFBoxWriter;

public class PDFBoxTest {

    @Test
    public void testCreatePDFReportFile() throws Exception {
        final PDFBoxWriter writer = new PDFBoxWriter();
        writer.createPDFReportFile(new File("."), null);
    }
}
