/**
 * Hub Common Reporting
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.blackduck.report;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.synopsys.integration.blackduck.report.api.ReportData;
import com.synopsys.integration.blackduck.report.pdf.RiskReportPdfWriter;
import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.log.PrintStreamIntLogger;

public class PDFBoxTest {

    @Ignore
    @Test
    public void testCreatePDFReportFile() throws Exception {
        final String reportDataString = IOUtils.toString(getClass().getResourceAsStream("/TestReportData.json"), StandardCharsets.UTF_8);
        final Gson gson = new Gson();
        final ReportData reportData = gson.fromJson(reportDataString, ReportData.class);
        final RiskReportPdfWriter writer = new RiskReportPdfWriter(new PrintStreamIntLogger(System.out, LogLevel.DEBUG));
        writer.createPDFReportFile(new File("."), reportData);
    }

    @Ignore
    @Test
    public void testCreateWeirdPDFReportFile() throws Exception {
        final String reportDataString = IOUtils.toString(getClass().getResourceAsStream("/TestReportDataWeird.json"), StandardCharsets.UTF_8);
        final Gson gson = new Gson();
        final ReportData reportData = gson.fromJson(reportDataString, ReportData.class);
        final RiskReportPdfWriter writer = new RiskReportPdfWriter(new PrintStreamIntLogger(System.out, LogLevel.DEBUG));
        writer.createPDFReportFile(new File("."), reportData);
    }
}
