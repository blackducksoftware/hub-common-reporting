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

import static java.awt.Color.decode;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.blackducksoftware.integration.hub.report.exception.RiskReportException;
import com.blackducksoftware.integration.hub.report.pdf.util.PDFBoxManager;
import com.blackducksoftware.integration.log.IntLogger;

public class PDFBoxWriter {
    private final IntLogger logger;

    private final String HIGH_RISK = "High Risk";
    private final String MED_RISK = "Medium Risk";
    private final String LOW_RISK = "Low Risk";
    private final String NO_RISK = "No Risk";

    private PDFBoxManager pdfManager;

    public PDFBoxWriter(final IntLogger logger) {
        this.logger = logger;
    }

    public File createPDFReportFile(final File outputDirectory, final ReportData report) throws RiskReportException {
        final File pdfFile = new File(outputDirectory, "testRiskReport.pdf");
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        try (PDFBoxManager pdfManager = new PDFBoxManager(pdfFile, new PDDocument())) {
            this.pdfManager = pdfManager;
            final PDRectangle pageBox = pdfManager.currentPage.getMediaBox();
            final float pageWidth = pageBox.getWidth();
            final float pageHeight = pageBox.getHeight();

            final float bottomOfHeader = writeHeader(pageWidth, pageHeight);
            final float bottomOfProjectInfo = writeProjectInformation(bottomOfHeader, report);
            final float bottomOfSummaryTables = writeSummaryTables(pageWidth, bottomOfProjectInfo, report);

            return pdfFile;
        } catch (final IOException | URISyntaxException e) {
            throw new RiskReportException("Couldn't create the report: " + e.getMessage(), e);
        }
    }

    private float writeHeader(final float width, final float startingHeight) throws IOException, URISyntaxException {
        pdfManager.drawRectangle(0, startingHeight - 100, width, 100, Color.BLACK);
        pdfManager.drawImage(width - 220, startingHeight - 72.5F, 203, 45, "/riskreport/web/images/Hub_BD_logo.png");
        pdfManager.writeText(5, startingHeight - 60, "Black Duck Risk Report", PDFBoxManager.DEFAULT_FONT_BOLD, 20, Color.WHITE);
        return startingHeight - 100;
    }

    private float writeProjectInformation(final float startingHeight, final ReportData reportData) throws IOException {
        final float height = startingHeight - 18;
        pdfManager.writeLink(5, height, reportData.getProjectName(), reportData.getProjectURL(), PDFBoxManager.DEFAULT_FONT, 18);
        final String dash = " - ";
        pdfManager.writeText(5 + StringManager.getStringWidth(PDFBoxManager.DEFAULT_FONT, 18, reportData.getProjectName()), height, dash, PDFBoxManager.DEFAULT_FONT, 18, Color.BLACK);
        pdfManager.writeLink(5 + StringManager.getStringWidth(PDFBoxManager.DEFAULT_FONT, 18, reportData.getProjectName() + dash), height, reportData.getProjectVersion(), reportData.getProjectVersionURL(), PDFBoxManager.DEFAULT_FONT, 18);

        final String projectAttributesString = "Phase:  " + reportData.getPhase() + "    |    Distribution:  " + reportData.getDistribution();
        pdfManager.writeText(5, height - 18, projectAttributesString);

        return height - 18 - 12;
    }

    private float writeSummaryTables(final float width, final float startingHeight, final ReportData reportData) throws IOException {

        final float center = width / 2;

        final float height = startingHeight - 20;
        writeSummaryTable(center - 180, height, "Security Risk", reportData.getVulnerabilityRiskHighCount(), reportData.getVulnerabilityRiskMediumCount(), reportData.getVulnerabilityRiskLowCount(),
                reportData.getVulnerabilityRiskNoneCount());
        writeSummaryTable(center, height, "License Risk", reportData.getLicenseRiskHighCount(), reportData.getLicenseRiskMediumCount(), reportData.getLicenseRiskLowCount(), reportData.getLicenseRiskNoneCount());
        final float bottomOfSummaryTables = writeSummaryTable(center + 180, height, "Operational Risk", reportData.getOperationalRiskHighCount(), reportData.getOperationalRiskMediumCount(), reportData.getOperationalRiskLowCount(),
                reportData.getOperationalRiskNoneCount());
        return bottomOfSummaryTables;
    }

    private float writeSummaryTable(final float centerX, final float y, final String title, final int highCount, final int mediumCount, final int lowCount, final int noneCount) throws IOException {
        pdfManager.writeTextCentered(centerX, y, title, PDFBoxManager.DEFAULT_FONT_BOLD, 14, Color.BLACK);

        final float highRowY = y - 16;
        final float mediumRowY = y - 30;
        final float lowRowY = y - 44;
        final float noneRowY = y - 58;
        final float totalCount = highCount + mediumCount + lowCount + noneCount;

        writeSummaryTableRow(centerX, highRowY, HIGH_RISK, highCount, totalCount, decode("#b52b24"));
        writeSummaryTableRow(centerX, mediumRowY, MED_RISK, mediumCount, totalCount, decode("#eca4a0"));
        writeSummaryTableRow(centerX, lowRowY, LOW_RISK, lowCount, totalCount, new Color(153, 153, 153));
        writeSummaryTableRow(centerX, noneRowY, NO_RISK, noneCount, totalCount, new Color(221, 221, 221));

        return y - 58;
    }

    private void writeSummaryTableRow(final float centerX, final float rowY, final String rowTitle, final int count, final float totalCount, final Color barColor) throws IOException {
        final float rowTitleX = centerX - 95;
        pdfManager.writeText(rowTitleX, rowY, rowTitle);

        final String countString = String.valueOf(count);
        pdfManager.writeTextCentered(centerX, rowY, countString);

        final float barX = centerX + 20;
        pdfManager.drawRectangle(barX, rowY, (count / totalCount) * 60, 10, barColor);
    }

    private float writeComponentTable(final float startingHeight, final ReportData reportData) {
        // TODO write BOM Entry count
        // TODO header row
        // TODO component rows
        return 0F;
    }

}
