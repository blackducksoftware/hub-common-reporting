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
    public PDPage currentPage;
    private PDPageContentStream contentStream;

    public static final PDFont DEFAULT_FONT = PDType1Font.HELVETICA;
    public static final PDFont DEFAULT_FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    public static final float DEFAULT_FONT_SIZE = 10;
    public static final Color DEFAULT_COLOR = Color.BLACK;

    public PDFBoxManager(final File outputFile, final PDDocument document) throws IOException {
        this.outputFile = outputFile;
        this.document = document;
        this.currentPage = new PDPage();
        document.addPage(currentPage);
        contentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, true, false);
    }

    public PDRectangle drawRectangle(final float x, final float y, final float width, final float height, final Color color) throws IOException {
        final float startingY = checkYAndSwitchPage(y, height);
        contentStream.setNonStrokingColor(color);
        contentStream.addRect(x, startingY, width, height);
        contentStream.fill();
        return new PDRectangle(x, startingY, width, height);
    }

    public PDRectangle drawImage(final float x, final float y, final float width, final float height, final String resourceImageName) throws IOException, URISyntaxException {
        final float startingY = checkYAndSwitchPage(y, height);
        final URL imageURL = getClass().getResource(resourceImageName);
        final URI imageUri = imageURL.toURI();
        final File imageFile = new File(imageUri);
        final PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(imageFile, document);
        contentStream.drawImage(pdImage, x, startingY, width, height);
        return new PDRectangle(x, startingY, width, height);
    }

    public PDRectangle writeTextCentered(final float x, final float y, final String text, final PDFont font, final float fontSize, final Color textColor) throws IOException {
        final float textLength = StringManager.getStringWidth(font, fontSize, text);
        return writeText(x - (textLength / 2), y, text, font, fontSize, textColor);
    }

    public PDRectangle writeTextCentered(final float x, final float y, final String text) throws IOException {
        final float textLength = StringManager.getStringWidth(DEFAULT_FONT, DEFAULT_FONT_SIZE, text);
        return writeText(x - (textLength / 2), y, text, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_COLOR);
    }

    public PDRectangle writeText(final float x, final float y, final String text) throws IOException {
        return writeText(x, y, text, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_COLOR);
    }

    public PDRectangle writeText(final float x, final float y, final String text, final PDFont font, final float fontSize, final Color textColor) throws IOException {
        final float startingY = checkYAndSwitchPage(y, fontSize);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(textColor);
        contentStream.newLineAtOffset(x, startingY);
        contentStream.showText(text);
        contentStream.endText();
        return new PDRectangle(x, startingY, StringManager.getStringWidth(font, fontSize, text), fontSize);
    }

    public PDRectangle writeWrappedText(final float x, final float y, final float width, final String text) throws IOException {
        return writeWrappedText(x, y, width, text, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_COLOR);
    }

    public PDRectangle writeWrappedCenteredText(final float x, final float y, final float width, final String text, final PDFont font, final float fontSize, final Color color) throws IOException {
        final List<String> textLines = StringManager.wrapToCombinedList(font, fontSize, text, Math.round(width));
        return writeWrappedCenteredText(x, y, width, textLines, font, fontSize, color);
    }

    public PDRectangle writeWrappedCenteredText(final float x, final float y, final float width, final List<String> textLines, final PDFont font, final float fontSize, final Color color) throws IOException {
        final float startingY = checkYAndSwitchPage(y, fontSize);
        final int numOfLines = textLines.size();
        float actualWidth = width;
        float approximateHeight = 0F;
        for (int i = 0; i < numOfLines; i++) {
            final float textLength = StringManager.getStringWidth(DEFAULT_FONT, DEFAULT_FONT_SIZE, textLines.get(i));
            final PDRectangle rectangle = writeText(x - (textLength / 2), startingY - (i * fontSize), textLines.get(i), font, fontSize, color);
            if (numOfLines == 1) {
                actualWidth = rectangle.getWidth();
            }
            approximateHeight += rectangle.getHeight();
        }
        return new PDRectangle(x, startingY, actualWidth, approximateHeight);
    }

    public PDRectangle writeWrappedText(final float x, final float y, final float width, final String text, final PDFont font, final float fontSize, final Color color) throws IOException {
        final List<String> textLines = StringManager.wrapToCombinedList(font, fontSize, text, Math.round(width));
        return writeWrappedText(x, y, width, textLines, font, fontSize, color);
    }

    public PDRectangle writeWrappedText(final float x, final float y, final float width, final List<String> textLines) throws IOException {
        return writeWrappedText(x, y, width, textLines, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_COLOR);
    }

    public PDRectangle writeWrappedText(final float x, final float y, final float width, final List<String> textLines, final PDFont font, final float fontSize, final Color color) throws IOException {
        final float startingY = checkYAndSwitchPage(y, fontSize);
        final int numOfLines = textLines.size();
        float actualWidth = width;
        float approximateHeight = 0F;
        for (int i = 0; i < numOfLines; i++) {
            final PDRectangle rectangle = writeText(x, startingY - (i * fontSize), textLines.get(i), font, fontSize, color);
            if (numOfLines == 1) {
                actualWidth = rectangle.getWidth();
            }
            approximateHeight += rectangle.getHeight();
        }
        return new PDRectangle(x, startingY, actualWidth, approximateHeight);
    }

    public PDRectangle writeLink(final float x, final float y, final String linkText, final String linkURL, final PDFont font, final float fontSize) throws IOException {
        final PDRectangle rectangle = writeText(x, y, linkText, font, fontSize, Color.decode("#46759E"));
        addAnnotationLinkRectangle(rectangle.getLowerLeftX(), rectangle.getLowerLeftY(), rectangle.getWidth(), rectangle.getHeight(), linkURL);
        return rectangle;
    }

    public PDRectangle writeWrappedLink(final float x, final float y, final float width, final String linkText, final String linkURL, final PDFont font, final float fontSize) throws IOException {
        return writeWrappedLink(x, y, width, linkText, linkURL, font, fontSize, Color.decode("#46759E"));
    }

    public PDRectangle writeWrappedLink(final float x, final float y, final float width, final String linkText, final String linkURL, final PDFont font, final float fontSize, final Color color) throws IOException {
        final PDRectangle rectangle = writeWrappedText(x, y, width, linkText, font, fontSize, color);
        addAnnotationLinkRectangle(rectangle.getLowerLeftX(), rectangle.getLowerLeftY(), rectangle.getWidth(), rectangle.getHeight(), linkURL);
        return rectangle;
    }

    public PDRectangle writeWrappedLink(final float x, final float y, final float width, final List<String> linkTextLines, final String linkURL, final PDFont font, final float fontSize) throws IOException {
        return writeWrappedLink(x, y, width, linkTextLines, linkURL, font, fontSize, Color.decode("#46759E"));
    }

    public PDRectangle writeWrappedCenteredLink(final float x, final float y, final float width, final List<String> linkTextLines, final String linkURL, final Color color) throws IOException {
        return writeWrappedCenteredLink(x, y, width, linkTextLines, linkURL, DEFAULT_FONT, DEFAULT_FONT_SIZE, color);
    }

    public PDRectangle writeWrappedCenteredLink(final float x, final float y, final float width, final List<String> linkTextLines, final String linkURL, final PDFont font, final float fontSize, final Color color) throws IOException {
        final PDRectangle rectangle = writeWrappedCenteredText(x, y, width, linkTextLines, font, fontSize, color);
        addAnnotationLinkRectangle(rectangle.getLowerLeftX(), rectangle.getLowerLeftY(), rectangle.getWidth(), rectangle.getHeight(), linkURL);
        return rectangle;
    }

    public PDRectangle writeWrappedLink(final float x, final float y, final float width, final List<String> linkTextLines, final String linkURL, final Color color) throws IOException {
        return writeWrappedLink(x, y, width, linkTextLines, linkURL, DEFAULT_FONT, DEFAULT_FONT_SIZE, color);
    }

    public PDRectangle writeWrappedLink(final float x, final float y, final float width, final List<String> linkTextLines, final String linkURL, final PDFont font, final float fontSize, final Color color) throws IOException {
        final PDRectangle rectangle = writeWrappedText(x, y, width, linkTextLines, font, fontSize, color);
        addAnnotationLinkRectangle(rectangle.getLowerLeftX(), rectangle.getLowerLeftY(), rectangle.getWidth(), rectangle.getHeight(), linkURL);
        return rectangle;
    }

    private PDRectangle addAnnotationLinkRectangle(final float x, final float y, final float width, final float height, final String linkURL) throws IOException {
        final float startingY = checkYAndSwitchPage(y, height);
        final PDAnnotationLink txtLink = new PDAnnotationLink();
        final PDRectangle position = new PDRectangle();
        position.setLowerLeftX(x);
        position.setLowerLeftY(startingY);
        position.setUpperRightX(x + width);
        position.setUpperRightY(startingY + height);
        txtLink.setRectangle(position);

        final PDActionURI action = new PDActionURI();
        action.setURI(linkURL);
        txtLink.setAction(action);

        currentPage.getAnnotations().add(txtLink);
        return new PDRectangle(x, startingY, width, height);
    }

    private float checkYAndSwitchPage(final float y, final float height) throws IOException {
        if (y < 0) {
            contentStream.close();
            this.currentPage = new PDPage();
            document.addPage(currentPage);
            contentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, true, false);
            return currentPage.getMediaBox().getHeight() - 20 - height;
        }
        return y;
    }

    public float getApproximateWrappedStringHeight(final int numberOfTextLines, final float fontSize) {
        return numberOfTextLines * fontSize + fontSize;
    }

    @Override
    public void close() throws IOException {
        contentStream.close();
        document.save(outputFile);
        document.close();
    }
}
