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
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.blackducksoftware.integration.hub.report.pdf.PDFBoxWriter;
import com.blackducksoftware.integration.log.LogLevel;
import com.blackducksoftware.integration.log.PrintStreamIntLogger;
import com.google.gson.Gson;

public class PDFBoxTest {

    @Ignore
    @Test
    public void testCreatePDFReportFile() throws Exception {
        final String reportDataString = IOUtils.toString(getClass().getResourceAsStream("/TestReportData.json"), StandardCharsets.UTF_8);
        final Gson gson = new Gson();
        final ReportData reportData = gson.fromJson(reportDataString, ReportData.class);
        final PDFBoxWriter writer = new PDFBoxWriter(new PrintStreamIntLogger(System.out, LogLevel.DEBUG));
        writer.createPDFReportFile(new File("."), reportData);
    }
}
