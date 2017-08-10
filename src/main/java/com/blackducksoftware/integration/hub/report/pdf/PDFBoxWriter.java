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
package com.blackducksoftware.integration.hub.report.pdf;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.blackducksoftware.integration.hub.report.exception.RiskReportException;
import com.blackducksoftware.integration.hub.report.pdf.util.PDFBoxManager;
import com.blackducksoftware.integration.log.IntLogger;

public class PDFBoxWriter {

    private final PDFont font = PDType1Font.HELVETICA;

    private final IntLogger logger;

    public PDFBoxWriter(final IntLogger logger) {
        this.logger = logger;
    }

    public File createPDFReportFile(final File outputDirectory, final ReportData report) throws RiskReportException {
        final File pdfFile = new File(outputDirectory, "testRiskReport.pdf");
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        try (PDFBoxManager pdfManager = new PDFBoxManager(pdfFile, new PDDocument())) {
            final PDRectangle pageBox = pdfManager.currentPage.getMediaBox();
            final float pageWidth = pageBox.getWidth();
            final float pageHeight = pageBox.getHeight();

            final float bottomOfHeader = writeHeader(pdfManager, pageWidth, pageHeight);
            final float bottomOfProjectInfo = writeProjectInformation(pdfManager, pageWidth, bottomOfHeader, report);

            return pdfFile;
        } catch (final IOException | URISyntaxException e) {
            throw new RiskReportException("Couldn't create the report: " + e.getMessage(), e);
        }
    }

    private float writeHeader(final PDFBoxManager pdfManager, final float width, final float startingHeight) throws IOException, URISyntaxException {
        pdfManager.drawRectangle(0, startingHeight - 100, width, 100, Color.BLACK);
        pdfManager.drawImage(width - 220, startingHeight - 72.5F, 203, 45, "/riskreport/web/images/Hub_BD_logo.png");
        pdfManager.writeText(5, startingHeight - 60, "Black Duck Risk Report", font, 20, Color.WHITE);
        return startingHeight - 100;
    }

    private float writeProjectInformation(final PDFBoxManager pdfManager, final float width, final float startingHeight, final ReportData reportData) throws IOException {
        final float height = startingHeight - 18;
        pdfManager.writeLink(5, height, reportData.getProjectName(), reportData.getProjectURL(), font, 18);
        final String dash = " - ";
        pdfManager.writeText(5 + StringManager.getStringWidth(font, 18, reportData.getProjectName()), height, dash, font, 18, Color.BLACK);
        pdfManager.writeLink(5 + StringManager.getStringWidth(font, 18, reportData.getProjectName() + dash), height, reportData.getProjectVersion(), reportData.getProjectVersionURL(), font, 18);

        final String projectAttributesString = "Phase:  " + reportData.getPhase() + "    |    Distribution:  " + reportData.getDistribution();
        pdfManager.writeText(5, height - 18, projectAttributesString, font, 12, Color.BLACK);

        return height - 18 - 12;
    }

}
