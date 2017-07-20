/**
// * Hub Common Reporting
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
package com.blackducksoftware.integration.hub.report.PDFBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import com.blackducksoftware.integration.hub.report.api.BomComponent;
import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.planbase.pdf.layoutmanager.BorderStyle;
import com.planbase.pdf.layoutmanager.Cell;
import com.planbase.pdf.layoutmanager.CellStyle;
import com.planbase.pdf.layoutmanager.LineStyle;
import com.planbase.pdf.layoutmanager.LogicalPage;
import com.planbase.pdf.layoutmanager.Padding;
import com.planbase.pdf.layoutmanager.PdfLayoutMgr;
import com.planbase.pdf.layoutmanager.ScaledJpeg;
import com.planbase.pdf.layoutmanager.ScaledPng;
import com.planbase.pdf.layoutmanager.TableBuilder;
import com.planbase.pdf.layoutmanager.TablePart;
import com.planbase.pdf.layoutmanager.TableRowBuilder;
import com.planbase.pdf.layoutmanager.TextStyle;
import com.planbase.pdf.layoutmanager.XyDim;
import com.planbase.pdf.layoutmanager.XyOffset;
import static com.planbase.pdf.layoutmanager.CellStyle.Align.*;
import static java.awt.Color.*;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class PDFBuilder {
	
	public float mainTitleFontSize = 20f;
	public float cellTitleFontSize = 12f;
	public float bodyFontSize = 10f;
	public float headerCellSize = 80f;
	public float numberCellSize = 60f;
	float componentVersionCellSize = 150f;
	float componentLicenseCellSize = 200f;
	float componentNameCellSize = 150f;
	public Color bodyCellBackgroundColor = Color.WHITE;
	public Color bodyCellFontColor = Color.BLACK;
	public PDType1Font bodyFont = PDType1Font.HELVETICA;
	public float padding = 20f;

	public void makePDF(ReportData report) throws IOException {	
		PdfLayoutMgr pageMgr = PdfLayoutMgr.newRgbPageMgr();
		LogicalPage lp = pageMgr.logicalPageStart();
		
		TableBuilder titleTable = lp.tableBuilder(XyOffset.of(0, lp.yPageTop()));
		titleTable = titleTable(titleTable, report, lp.pageWidth());
		XyOffset titleTableXYOffset = titleTable.buildTable();
		
		TableBuilder projectInformationTable = lp.tableBuilder(XyOffset.of(0, titleTableXYOffset.y() - padding / 2));
		projectInformationTable = projectInformationTable(projectInformationTable, report);
		XyOffset projectInformationTableXYOffset = projectInformationTable.buildTable();
		
		
		float startingtb1X = (lp.pageWidth() / 2) - (((3 * (numberCellSize + headerCellSize)) + (2 * padding)) / 2);
		float startingtb1Y = projectInformationTableXYOffset.y() - padding;
		TableBuilder tb1 = lp.tableBuilder(XyOffset.of(startingtb1X, startingtb1Y));
		tb1 = aggregateSecurityRiskTable(tb1, report);
		XyOffset tb1XYOffset = tb1.buildTable();

		TableBuilder tb2 = lp.tableBuilder(XyOffset.of(tb1XYOffset.x() + padding, startingtb1Y));
		tb2 = aggregateLicenseRiskTable(tb2, report);
		XyOffset tb2XYOffset = tb2.buildTable();

		TableBuilder tb3 = lp.tableBuilder(XyOffset.of(tb2XYOffset.x() + padding, startingtb1Y));
		tb3 = aggregateOperationalRiskTable(tb3, report);
		XyOffset tb3XYOffset = tb3.buildTable();

		
		float startingtb4X = (lp.pageWidth() / 2) - ((componentVersionCellSize + componentLicenseCellSize + componentNameCellSize + 5 * 50f) / 2);	
		TableBuilder tb4 = lp.tableBuilder(XyOffset.of(startingtb4X, tb3XYOffset.y() - padding));
		tb4 = componentTable(tb4, report);
		XyOffset tb4XYOffset = tb4.buildTable();

		lp.commit();
		OutputStream os = new FileOutputStream("test.pdf");
		pageMgr.save(os);
	}

	public TableBuilder aggregateSecurityRiskTable(TableBuilder tb, ReportData report) {

		tb.addCellWidths(vec(headerCellSize, numberCellSize))
		//title rows
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, cellTitleFontSize, BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(1), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_RIGHT).addStrs("Security").buildCell()
		.cellBuilder().align(MIDDLE_LEFT).addStrs("Risk").buildCell()
		.buildRow()
		.buildPart()

		//high risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("High Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskHighCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//medium risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Medium Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskMediumCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//low risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Low Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskLowCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//no risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("No Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskNoneCount()+"").buildCell()
		.buildRow()
		.buildPart();

		return tb;
	}

	public TableBuilder aggregateLicenseRiskTable(TableBuilder tb, ReportData report) {
		tb.addCellWidths(vec(headerCellSize, numberCellSize))
		//title rows
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, cellTitleFontSize, BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(1), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_RIGHT).addStrs("License").buildCell()
		.cellBuilder().align(MIDDLE_LEFT).addStrs("Risk").buildCell()
		.buildRow()
		.buildPart()

		//high risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("High Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskHighCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//medium risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Medium Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskMediumCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//low risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Low Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskLowCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//no risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("No Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskNoneCount()+"").buildCell()
		.buildRow()
		.buildPart();

		return tb;
	}

	public TableBuilder aggregateOperationalRiskTable(TableBuilder tb, ReportData report) {
		tb.addCellWidths(vec(headerCellSize, numberCellSize))

		//title rows
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, cellTitleFontSize, BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(1), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_RIGHT).addStrs("Operational").buildCell()
		.cellBuilder().align(MIDDLE_LEFT).addStrs("Risk").buildCell()
		.buildRow()
		.buildPart()

		//high risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("High Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskHighCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//medium risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Medium Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskMediumCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//low risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Low Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskLowCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//no risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("No Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskNoneCount()+"").buildCell()
		.buildRow()
		.buildPart();

		return tb;
	}

	public TableBuilder projectInformationTable(TableBuilder tb, ReportData report) {
		tb.addCellWidths(vec(componentNameCellSize * 2))
		.textStyle(TextStyle.of(bodyFont, mainTitleFontSize, decode("#46759E")))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_LEFT, Padding.of(2), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().addStrs(report.getProjectName() + " - " + report.getProjectVersion()).buildCell()
		.buildRow()
		.buildPart()
		//phase and distrib
		.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_LEFT, Padding.of(3), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().addStrs("Phase:    " + report.getPhase() + "    |    Distribution:    " + report.getDistribution()).buildCell()
		.buildRow()
		.buildPart();
		
		return tb;
	}
	
	public TableBuilder titleTable(TableBuilder tb, ReportData report, float pageWidth) throws IOException {
		
		tb.addCellWidths(vec(pageWidth / 2, pageWidth / 2))
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, mainTitleFontSize, WHITE))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), Color.BLACK, BorderStyle.of(BLACK)))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_LEFT).addStrs("Black Duck Risk Report").buildCell()
		.cellBuilder().align(MIDDLE_RIGHT).add(ScaledPng.of(ImageIO.read(new File("src/main/resources/riskreport/web/images/Hub_BD_logo.png")), 203, 45)).buildCell()
		.buildRow()
		.buildPart();
		
		return tb;
	}
	
	public TableBuilder componentTable(TableBuilder tb, ReportData report) {
		float numberCellSize = 50f;

		tb.addCellWidths(vec(componentNameCellSize, componentVersionCellSize, componentLicenseCellSize, numberCellSize, numberCellSize, numberCellSize, numberCellSize, numberCellSize));
		
		tb.textStyle(TextStyle.of(bodyFont, bodyFontSize, Color.BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder().addTextCells("BOM Entries " + report.getTotalComponents()).buildRow()
		.buildPart();

		//title rows
		tb.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD_OBLIQUE, cellTitleFontSize, BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#f5f5f5"), BorderStyle.of(BLACK)))
		.rowBuilder().addTextCells("Component", "Version", "License", "H", "M", "L", "Lic R", "Opt R").buildRow()
		.buildPart();

		//component details rows
		boolean row = false;
		for (BomComponent b : report.getComponents()) {
			int h = b.getLicenseRiskHighCount() + b.getOperationalRiskHighCount() + b.getSecurityRiskHighCount();
			int m = b.getLicenseRiskMediumCount() + b.getOperationalRiskMediumCount() + b.getSecurityRiskMediumCount();
			int l = b.getLicenseRiskLowCount() + b.getOperationalRiskLowCount() + b.getSecurityRiskLowCount();
			String licR = licenseRisk(b);
			String optR = operationalRisk(b);
			Color bodyCellBackgroundColor = (row) ? decode("#f9f9f9") : decode("#ffffff");
			Color licRColor = (licR.equals("H")) ? decode("#b52b24") : (licR.equals("M")) ? decode("#eca4a0") : (licR.equals("L")) ? new Color(153,153,153) : bodyCellBackgroundColor;
			Color optRColor = (optR.equals("H")) ? decode("#b52b24") : (optR.equals("M")) ? decode("#eca4a0") : (optR.equals("L")) ? new Color(153,153,153) : bodyCellBackgroundColor;
			
			tb.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.of(DARK_GRAY)))
			.textStyle(TextStyle.of(bodyFont, bodyFontSize, bodyCellFontColor))
			.rowBuilder().cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(b.getComponentName())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(b.getComponentVersion())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(b.getLicense())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(h+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(m+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(l+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(licR).cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.NO_PADDING, licRColor, BorderStyle.of(DARK_GRAY))).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(optR).cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.NO_PADDING, optRColor, BorderStyle.of(DARK_GRAY))).buildCell()
			.buildRow()
			.buildPart();

			row = !row;
		}
		return tb;
	}

	

	
	public String licenseRisk(BomComponent b) {
		if (b.getLicenseRiskHighCount() > 0) {
			return "H";
		} else if (b.getLicenseRiskMediumCount() > 0) {
			return "M";
		} else if (b.getLicenseRiskLowCount() > 0) {
			return "L";
		} else {
			return "-";
		}
	}

	public String operationalRisk(BomComponent b) {
		if (b.getOperationalRiskHighCount() > 0) {
			return "H";
		} else if (b.getOperationalRiskMediumCount() > 0) {
			return "M";
		} else if (b.getOperationalRiskLowCount() > 0) {
			return "L";
		} else {
			return "-";
		}
	}

	private static <T> List<T> vec(T... ts) { return Arrays.asList(ts); }

	public static void main(String[] args) throws IOException {
		ReportData report = new ReportData();

		BomComponent bc1 = new BomComponent();
		bc1.setComponentName("Manager Manager Manager");
		//		bc1.setComponentVersion("kkkkkkkkkkk kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"); //moves out of box at 26 #'s - wraps at 45 , K overruns at 22, k at 28
		bc1.setComponentVersion("EEEEEEEEEE RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR KKKKKKKKKK-KKKKKKKKKKK-KKKKKKKKKKKKKKKKKK ihoirwanovirwbvabrl heihoi"); //moves out of box at 26 #'s - wraps at 45 , K overruns at 22, k at 28
		bc1.setLicense("BSD 3-clause New or Revised License");
		bc1.setSecurityRiskHighCount(0);
		bc1.setSecurityRiskMediumCount(2);
		bc1.setSecurityRiskLowCount(3);

		bc1.setLicenseRiskHighCount(0);
		bc1.setLicenseRiskMediumCount(1);
		bc1.setLicenseRiskLowCount(2);

		bc1.setOperationalRiskHighCount(0);
		bc1.setOperationalRiskMediumCount(0);
		bc1.setOperationalRiskLowCount(9);

		BomComponent bc2 = new BomComponent();
		bc2.setComponentName("ComponentName");
		bc2.setComponentVersion("This is a super long version name");
		bc2.setLicense("mit license mit license mit license mit license mit license mit license mit license mit license mit license mit license mit licbgbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbense");
		bc2.setSecurityRiskHighCount(0);
		bc2.setSecurityRiskMediumCount(0);
		bc2.setSecurityRiskLowCount(3);

		bc2.setLicenseRiskHighCount(1);
		bc2.setLicenseRiskMediumCount(0);
		bc2.setLicenseRiskLowCount(2);

		bc2.setOperationalRiskHighCount(0);
		bc2.setOperationalRiskMediumCount(0);
		bc2.setOperationalRiskLowCount(9);

		ArrayList<BomComponent> list = new ArrayList<BomComponent>();
		list.add(bc1);
		list.add(bc2);

		report.setComponents(list);
		report.setDistribution("Internal");
		report.setPhase("In Development");
		report.setProjectName("This is the Project Name");
		report.setProjectVersion("1.0.0");

		PDFBuilder builder = new PDFBuilder();
		builder.makePDF(report);
	}

}
