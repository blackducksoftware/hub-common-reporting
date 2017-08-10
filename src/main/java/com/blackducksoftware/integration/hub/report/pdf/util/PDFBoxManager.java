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
package com.blackducksoftware.integration.hub.report.pdf.util;

import java.awt.Color;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import com.blackducksoftware.integration.hub.report.pdf.StringManager;

public class PDFBoxManager implements Closeable {
    public final File outputFile;
    public final PDDocument document;
    public final PDPage currentPage;
    private final PDPageContentStream contentStream;

    public PDFBoxManager(final File outputFile, final PDDocument document) throws IOException {
        this.outputFile = outputFile;
        this.document = document;
        this.currentPage = new PDPage();
        document.addPage(currentPage);
        contentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, true, false);
    }

    public void drawRectangle(final float x, final float y, final float width, final float height, final Color color) throws IOException {
        contentStream.setNonStrokingColor(color);
        contentStream.addRect(x, y, width, height);
        contentStream.fill();
    }

    public void drawImage(final float x, final float y, final float width, final float height, final String resourceImageName) throws IOException, URISyntaxException {
        final URL imageURL = getClass().getResource(resourceImageName);
        final URI imageUri = imageURL.toURI();
        final File imageFile = new File(imageUri);
        final PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(imageFile, document);
        contentStream.drawImage(pdImage, x, y, width, height);
    }

    public void writeText(final float x, final float y, final String text) throws IOException {
        writeText(x, y, text, PDType1Font.HELVETICA, 12, Color.BLACK);
    }

    public void writeText(final float x, final float y, final String text, final PDFont font, final float fontSize, final Color textColor) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(textColor);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    public float writeWrappedText(final float x, final float y, final float width, final String text) throws IOException {
        return writeWrappedText(x, y, width, text, PDType1Font.HELVETICA, 12, Color.BLACK);
    }

    public float writeWrappedText(final float x, final float y, final float width, final String text, final PDFont font, final float fontSize, final Color color) throws IOException {
        // TODO
        float approximateHeight = fontSize / 4;
        final List<String> textLines = StringManager.wrapToCombinedList(font, fontSize, text, Math.round(width));
        final int numOfLines = textLines.size();
        approximateHeight += numOfLines * fontSize;
        for (int i = 0; i < numOfLines; i++) {
            writeText(x, y - (i * fontSize), textLines.get(i), font, fontSize, color);
        }
        return approximateHeight;
    }

    public void writeLink(final float x, final float y, final String linkText, final String linkURL, final PDFont font, final float fontSize) throws IOException {
        writeText(x, y, linkText, font, fontSize, Color.decode("#46759E"));
        final float width = StringManager.getStringWidth(font, fontSize, linkText);
        addAnnotationLinkRectangle(x, y, width, fontSize, linkURL);
    }

    public float writeWrappedLink(final float x, final float y, final float width, final String linkText, final String linkURL, final PDFont font, final float fontSize) throws IOException {
        final float height = writeWrappedText(x, y, width, linkText, font, fontSize, Color.decode("#46759E"));
        addAnnotationLinkRectangle(x, y, width, height, linkURL);
        return height;
    }

    private void addAnnotationLinkRectangle(final float x, final float y, final float width, final float height, final String linkURL) throws IOException {
        final PDAnnotationLink txtLink = new PDAnnotationLink();
        final PDRectangle position = new PDRectangle();
        position.setLowerLeftX(x);
        position.setLowerLeftY(y);
        position.setUpperRightX(x + width);
        position.setUpperRightY(y + height);
        txtLink.setRectangle(position);
        // add an action
        final PDActionURI action = new PDActionURI();
        action.setURI(linkURL);
        txtLink.setAction(action);

        currentPage.getAnnotations().add(txtLink);
    }

    @Override
    public void close() throws IOException {
        contentStream.close();
        document.save(outputFile);
        document.close();
    }
}
