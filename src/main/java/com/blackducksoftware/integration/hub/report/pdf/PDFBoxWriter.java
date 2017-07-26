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

public class PDFBoxWriter {

    public File createPDFReportFile(final File outputDirectory, final ReportData report) throws RiskReportException {
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
