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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.blackducksoftware.integration.hub.report.exception.RiskReportException;
import com.blackducksoftware.integration.hub.report.pdf.model.Row;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;
import com.blackducksoftware.integration.hub.report.pdf.util.CellBuilder;
import com.blackducksoftware.integration.hub.report.pdf.util.DocumentManager;
import com.blackducksoftware.integration.hub.report.pdf.util.RowBuilder;
import com.blackducksoftware.integration.log.IntLogger;

public class PDFBoxWriter {

    IntLogger logger;

    public PDFBoxWriter(final IntLogger logger) {
        this.logger = logger;
    }

    public File createPDFReportFile(final File outputDirectory, final ReportData report) throws RiskReportException {
        // /final File pdfFile = getOriginalPDFBoxPDF(outputDirectory);

        final File pdfFile = new File(outputDirectory, "testRiskReport2.pdf");
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        try (DocumentManager docManager = new DocumentManager(pdfFile)) {
            final int width = Math.round(docManager.getPageContentBox().getWidth());
            final int height = Math.round(docManager.getPageContentBox().getHeight());

            final Row headerRow = getHeaderRow(width);

            docManager.writeRow(0, height - 100, headerRow);

        } catch (final IOException e) {
            throw new RiskReportException("Couldn't create the report: " + e.getMessage(), e);
        }
        return pdfFile;
    }

    private Row getHeaderRow(final int width) {
        final CellStyle cellStyle = new CellStyle();
        cellStyle.setBackgroundColor(Color.BLACK).setHeight(100);
        final TextStyle textStyle = new TextStyle();
        textStyle.setFontSize(20).setTextColor(Color.WHITE);

        final RowBuilder rowBuilder = new RowBuilder();
        rowBuilder.rowCellStyle = cellStyle;
        rowBuilder.rowTextStyle = textStyle;

        final CellBuilder riskReportTitleCell = rowBuilder.newCellBuilder();
        riskReportTitleCell.text = "Black Duck Risk Report";
        riskReportTitleCell.cellStyle.setWidth(250);
        final CellBuilder blankCell = rowBuilder.newCellBuilder();
        blankCell.cellStyle.setWidth(112);
        final CellBuilder logoCell = rowBuilder.newCellBuilder();
        logoCell.cellStyle.setWidth(250);
        logoCell.imageResourcePath = "/riskreport/web/images/Hub_BD_logo.png";
        logoCell.imageWidth = 240;
        logoCell.imageHeight = 60;

        return rowBuilder.buildRow(width, 100);
    }

    private File getOriginalPDFBoxPDF(final File outputDirectory) throws RiskReportException {
        try (PDDocument document = new PDDocument()) {
            final PDPage page = new PDPage();
            document.addPage(page);

            final PDRectangle pageBox = page.getMediaBox();

            final PDFont font = PDType1Font.HELVETICA;

            final PDPageContentStream contents = new PDPageContentStream(document, page);
            contents.setNonStrokingColor(Color.BLACK);
            contents.addRect(0, pageBox.getHeight() - 100, pageBox.getWidth(), 100);
            contents.fill();
            final URL imageURL = getClass().getResource("/riskreport/web/images/Hub_BD_logo.png");
            final URI imageUri = imageURL.toURI();
            final PDImageXObject pdImage = PDImageXObject.createFromFile(imageUri.getPath(), document);
            contents.drawImage(pdImage, pageBox.getWidth() - (pdImage.getWidth() - 40), pageBox.getHeight() - (pdImage.getHeight() + 20), 240, 60);

            contents.beginText();
            contents.setFont(font, 20);
            contents.setNonStrokingColor(Color.WHITE);
            contents.newLineAtOffset(20, pageBox.getHeight() - 60);
            contents.showText("Black Duck Risk Report");
            contents.endText();

            contents.beginText();
            contents.setNonStrokingColor(Color.BLACK);
            contents.newLineAtOffset(20, pageBox.getHeight() - 200);
            contents.showText("Test");
            contents.endText();
            contents.close();

            final File pdfFile = new File(outputDirectory, "testRiskReport.pdf");
            document.save(pdfFile);
            return pdfFile;
        } catch (final IOException | URISyntaxException e) {
            throw new RiskReportException("Couldn't create the report: " + e.getMessage(), e);
        }
    }
}
