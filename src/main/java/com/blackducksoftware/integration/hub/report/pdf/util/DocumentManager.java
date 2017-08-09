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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.blackducksoftware.integration.hub.report.exception.RiskReportException;
import com.blackducksoftware.integration.hub.report.pdf.model.Cell;
import com.blackducksoftware.integration.hub.report.pdf.model.Rectangle;
import com.blackducksoftware.integration.hub.report.pdf.model.Row;
import com.blackducksoftware.integration.hub.report.pdf.model.Table;
import com.blackducksoftware.integration.hub.report.pdf.style.CellStyle;
import com.blackducksoftware.integration.hub.report.pdf.style.TextStyle;

public class DocumentManager implements Closeable {

    PDDocument document;

    Map<PDPage, List<Rectangle>> pages;

    PDPage currentPage;

    File outputFile;

    public DocumentManager(final File outputFile) {
        this.outputFile = outputFile;
        this.document = new PDDocument();
        pages = new HashMap<>();
        currentPage = new PDPage();
        document.addPage(currentPage);
        pages.put(currentPage, new ArrayList<Rectangle>());
    }

    public PDRectangle getPageContentBox() {
        return currentPage.getMediaBox();
    }

    private boolean canWrite(final Rectangle rectangle) {
        for (final Rectangle writtenRectangle : pages.get(currentPage)) {
            if (writtenRectangle.collidesWith(rectangle)) {
                return false;
            }
        }
        return true;
    }

    // TODO handle padding, border, hyperlink, annotations
    public void writeCell(final int xCoord, final int yCoord, final Cell cell) throws RiskReportException {
        try {
            if (cell == null) {
                return;
            }
            if (!canWrite(cell)) {
                throw new RiskReportException("Rectangle collides with a previous written Rectangle.");
            }

            cell.xCoord = xCoord;
            cell.yCoord = yCoord;
            final PDPageContentStream contentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, false, false);
            final CellStyle cellStyle = cell.getCellStyle();
            cell.width = cellStyle.getWidth();
            cell.height = cellStyle.getHeight();
            if (cellStyle.getBackgroundColor() != null) {
                contentStream.setNonStrokingColor(cell.getCellStyle().getBackgroundColor());
                contentStream.addRect(xCoord, yCoord, cell.width, cell.height);
                contentStream.fill();
            }

            if (cell.getImageResourcePath() != null) {
                final PDImageXObject pdImage = PDImageXObject.createFromFile(getClass().getResource(cell.getImageResourcePath()).toURI().getPath(), document);
                contentStream.drawImage(pdImage, xCoord + cellStyle.getPadding().getLeftPadding(), yCoord + cellStyle.getPadding().getBottomPadding(), cell.getImageWidth(), cell.getImageHeight());
            }
            final TextStyle textStyle = cell.getTextStyle();
            if (cell.getText() != null) {
                contentStream.setFont(textStyle.getFont(), textStyle.getFontSize());
                contentStream.setNonStrokingColor(textStyle.getTextColor());
                // TODO padding
                contentStream.beginText();
                contentStream.newLineAtOffset(cell.xCoord, cell.yCoord);
                contentStream.showText("Black Duck Risk Report");
                contentStream.endText();
            }

            contentStream.close();
            pages.get(currentPage).add(cell);
        } catch (final IOException | URISyntaxException e) {
            throw new RiskReportException("Issue writing this Cell : " + e.getMessage(), e);
        }
        // TODO
    }

    public void writeNextCellInRow(final Cell previousCell, final Cell cell, final int rowXCoord, final int rowYCoord) throws RiskReportException {
        if (previousCell == null) {
            writeCell(rowXCoord, rowYCoord, cell);
        } else {
            writeCell(previousCell.xCoord + previousCell.width, previousCell.yCoord, cell);
        }
    }

    public void writeRow(final int xCoord, final int yCoord, final Row row) throws RiskReportException {
        // write the cells
        Cell previousCell = null;
        for (final Cell cell : row.getCells()) {
            writeNextCellInRow(previousCell, cell, xCoord, yCoord);
            previousCell = cell;
        }
    }

    public void writeNextRowInTable(final Row previousRow, final Row row, final int tableXCoord, final int tableYCoord) throws RiskReportException {
        if (previousRow == null) {
            writeRow(tableXCoord, tableYCoord, row);
        } else {
            writeRow(previousRow.xCoord, previousRow.yCoord - previousRow.height, row);
        }
    }

    public void writeTable(final int xCoord, final int yCoord, final Table table) throws RiskReportException {
        // // TODO
    }

    @Override
    public void close() throws IOException {
        document.save(outputFile);
        document.close();
    }

}
