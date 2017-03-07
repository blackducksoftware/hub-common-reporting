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
package com.blackducksoftware.integration.hub.report;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RiskReportCopierTest {

    private static final String RISK_REPORT_DIR = "risk_report_dir";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testRiskReportCopier() throws Exception {
        folder.create();
        final File dirToWriteTo = folder.newFolder();
        final File riskReportDir = new File(dirToWriteTo, RISK_REPORT_DIR);
        final RiskReportResourceCopier publisher = new RiskReportResourceCopier(riskReportDir.getCanonicalPath());
        final List<File> writtenFiles = publisher.copy();
        for (final File file : writtenFiles) {
            System.out.println(file.getCanonicalPath());
        }

        assertFalse(writtenFiles.isEmpty());
    }
}
