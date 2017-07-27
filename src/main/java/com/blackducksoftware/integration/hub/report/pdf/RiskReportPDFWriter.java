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

package com.blackducksoftware.integration.hub.report.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.blackducksoftware.integration.hub.report.api.BomComponent;
import com.blackducksoftware.integration.hub.report.api.PolicyRule;
import com.blackducksoftware.integration.hub.report.api.ReportData;
import com.blackducksoftware.integration.hub.report.exception.RiskReportException;
import com.planbase.pdf.layoutmanager.BorderStyle;
import com.planbase.pdf.layoutmanager.Cell;
import com.planbase.pdf.layoutmanager.CellStyle;
import com.planbase.pdf.layoutmanager.LogicalPage;
import com.planbase.pdf.layoutmanager.Padding;
import com.planbase.pdf.layoutmanager.PdfLayoutMgr;
import com.planbase.pdf.layoutmanager.Renderable;
import com.planbase.pdf.layoutmanager.ScaledPng;
import com.planbase.pdf.layoutmanager.TableBuilder;
import com.planbase.pdf.layoutmanager.Text;
import com.planbase.pdf.layoutmanager.TextStyle;
import com.planbase.pdf.layoutmanager.XyOffset;
import static com.planbase.pdf.layoutmanager.CellStyle.Align.*;
import static java.awt.Color.*;
import java.awt.Color;

public class RiskReportPDFWriter {

	public float FONT_SIZE_MAIN_TITLE = 20f;
	public float FONT_SIZE_CELL_HEADER = 12f;
	public float FONT_SIZE_CELL_BODY = 10f;
	public final PDType1Font FONT_BODY = PDType1Font.HELVETICA;

	public final float CELL_SIZE_TABLE_HEADER = 80f;
	public final float CELL_SIZE_RISK_TABLE_BODY = 65f;
	public final float CELL_SIZE_NUMBER = 40f;
	public final float CELL_SIZE_COMPONENT_VERSION = 115f;
	public final float CELL_SIZE_COMPONENT_LICENSE = 150f;
	public final float CELL_SIZE_COMPONENT_NAME = 125f;
	public final float CELL_SIZE_HIDDEN_COLUMN = 10f;

	public final Color CELL_COLOR_BACKGROUND_BODY = Color.WHITE;
	public final Color CELL_COLOR_FONT_BODY = Color.BLACK;

	public float PADDING = 20f;

	public final String HIGH_RISK = "High Risk";
	public final String MED_RISK = "Medium Risk";
	public final String LOW_RISK = "Low Risk";
	public final String NO_RISK = "No Risk";

	public File createPDFReportFile(final File outputDirectory, final ReportData report) throws RiskReportException {
		try {
			String fileName = report.getProjectName() + "_BlackDuck_RiskReport.pdf";
			PdfLayoutMgr pageMgr = PdfLayoutMgr.newRgbPageMgr();
			LogicalPage lp = pageMgr.logicalPageStart(LogicalPage.Orientation.PORTRAIT);

			TableBuilder titleTable = lp.tableBuilder(XyOffset.of(0, lp.yPageTop()));
			titleTable = titleTable(titleTable, report, lp.pageWidth());
			XyOffset titleTableXYOffset = titleTable.buildTable();


			TableBuilder projectInformationTable = lp.tableBuilder(XyOffset.of(0, titleTableXYOffset.y() - PADDING / 2));
			projectInformationTable = projectInformationTable(projectInformationTable, lp.pageWidth(), report);
			XyOffset projectInformationTableXYOffset = projectInformationTable.buildTable();

			float riskTableStartingX = (lp.pageWidth() / 2) - (((3 * (CELL_SIZE_NUMBER + CELL_SIZE_NUMBER + CELL_SIZE_RISK_TABLE_BODY)) + (2 * PADDING)) / 2);
			float riskTableStartingY = projectInformationTableXYOffset.y() - PADDING;


			//security risk
			TableBuilder securityRiskTableHeader = lp.tableBuilder(XyOffset.of(riskTableStartingX, riskTableStartingY));
			securityRiskTableHeader = aggregateSecurityRiskTableHeader(securityRiskTableHeader, report);
			XyOffset securityRiskTableHeaderXYOffset = securityRiskTableHeader.buildTable();

			TableBuilder securityRiskTable = lp.tableBuilder(XyOffset.of(riskTableStartingX, securityRiskTableHeaderXYOffset.y()));
			securityRiskTable = aggregateSecurityRiskTable(securityRiskTable, report);
			XyOffset securityRiskTableXYOffset = securityRiskTable.buildTable();


			//license risk
			TableBuilder licenseRiskTableHeader = lp.tableBuilder(XyOffset.of(securityRiskTableXYOffset.x() + PADDING, riskTableStartingY));
			licenseRiskTableHeader = aggregateLicenseRiskTableHeader(licenseRiskTableHeader, report);
			XyOffset licenseRiskTableHeaderXYOffset = licenseRiskTableHeader.buildTable();

			TableBuilder licenseRiskTable = lp.tableBuilder(XyOffset.of(securityRiskTableXYOffset.x() + PADDING, licenseRiskTableHeaderXYOffset.y()));
			licenseRiskTable = aggregateLicenseRiskTable(licenseRiskTable, report);
			XyOffset licenseRiskTableXYOffset = licenseRiskTable.buildTable();


			//operational risk
			TableBuilder operationalRiskTableHeader = lp.tableBuilder(XyOffset.of(licenseRiskTableXYOffset.x() + PADDING, riskTableStartingY));
			operationalRiskTableHeader = aggregateOperationalRiskTableHeader(operationalRiskTableHeader, report);
			XyOffset operationalRiskTableHeaderXYOffset = operationalRiskTableHeader.buildTable();

			TableBuilder operationalRiskTable = lp.tableBuilder(XyOffset.of(licenseRiskTableXYOffset.x() + PADDING, operationalRiskTableHeaderXYOffset.y()));
			operationalRiskTable = aggregateOperationalRiskTable(operationalRiskTable, report);
			XyOffset operationalRiskTableXYOffset = operationalRiskTable.buildTable();

			float componentTableTitleX = (lp.pageWidth() / 2) - ((CELL_SIZE_HIDDEN_COLUMN + CELL_SIZE_COMPONENT_VERSION + CELL_SIZE_HIDDEN_COLUMN + CELL_SIZE_COMPONENT_LICENSE + CELL_SIZE_COMPONENT_NAME + 4 * CELL_SIZE_NUMBER) / 2);	
			TableBuilder componentTitleTable = lp.tableBuilder(XyOffset.of(componentTableTitleX, operationalRiskTableXYOffset.y() - PADDING));
			componentTitleTable = componentTableTitle(componentTitleTable, report);
			XyOffset componentTitleTableXYOffset = componentTitleTable.buildTable();

			componentTable(pageMgr, lp, componentTitleTableXYOffset, componentTableTitleX, report);

			lp.commit();
			OutputStream os = new FileOutputStream(fileName);
			pageMgr.save(os);

			return new File(fileName);
		} catch (final IOException | URISyntaxException e) {
			throw new RiskReportException("Couldn't create the report: " + e.getMessage(), e);
		}
	}

	public TableBuilder riskTableHeader(TableBuilder tb, String riskType) {
		return tb.addCellWidths(vec(CELL_SIZE_RISK_TABLE_BODY + CELL_SIZE_NUMBER + CELL_SIZE_NUMBER))
				.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, FONT_SIZE_CELL_HEADER, BLACK))
				.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), Color.WHITE, BorderStyle.NO_BORDERS))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(riskType).buildCell()
				.buildRow()
				.buildPart();
	}

	public TableBuilder riskTableRow(TableBuilder tb, String riskType, String count, Cell riskColorCell) {
		return tb.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), CELL_COLOR_BACKGROUND_BODY, BorderStyle.NO_BORDERS))
				.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, CELL_COLOR_FONT_BODY))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_LEFT).addStrs(riskType).buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(count)).buildCell()
				.cellBuilder().align(MIDDLE_LEFT).add(riskColorCell).buildCell()
				.buildRow()
				.buildPart();
	}

	public Cell percentageColorCell(Color color, float percentFill) {
		return Cell.of(CellStyle.of(MIDDLE_LEFT, Padding.of(5, percentFill, 5, 0), color, BorderStyle.NO_BORDERS), CELL_SIZE_NUMBER);
	}

	public TableBuilder aggregateSecurityRiskTableHeader(TableBuilder tb, ReportData report ) {
		return riskTableHeader(tb, "Security Risk");
	}

	public TableBuilder aggregateSecurityRiskTable(TableBuilder tb, ReportData report) {
		float totalCellWidth = 37;
		int total = report.getVulnerabilityRiskHighCount() + report.getVulnerabilityRiskMediumCount() + report.getVulnerabilityRiskLowCount() + report.getVulnerabilityRiskNoneCount();
		float highRisk = ((float) report.getVulnerabilityRiskHighCount() / (float) total) * totalCellWidth;
		float medRisk = ((float) report.getVulnerabilityRiskMediumCount() / (float) total) * totalCellWidth;
		float lowRisk = ((float) report.getVulnerabilityRiskLowCount() / (float) total) * totalCellWidth;
		float noneRisk = ((float) report.getVulnerabilityRiskNoneCount() / (float) total) * totalCellWidth;
		
		tb.addCellWidths(vec(CELL_SIZE_RISK_TABLE_BODY, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER));
		tb = riskTableRow(tb, HIGH_RISK, report.getVulnerabilityRiskHighCount()+"", percentageColorCell(decode("#b52b24"), highRisk));
		tb = riskTableRow(tb, MED_RISK, report.getVulnerabilityRiskMediumCount()+"", percentageColorCell(decode("#eca4a0"), medRisk));
		tb = riskTableRow(tb, LOW_RISK, report.getVulnerabilityRiskLowCount()+"", percentageColorCell(new Color(153,153,153), lowRisk));		
		tb = riskTableRow(tb, NO_RISK, report.getVulnerabilityRiskNoneCount()+"", percentageColorCell(new Color(221,221,221), noneRisk));
		return tb;
	}

	public TableBuilder aggregateLicenseRiskTableHeader(TableBuilder tb, ReportData report ) {
		return riskTableHeader(tb, "License Risk");
	}

	public TableBuilder aggregateLicenseRiskTable(TableBuilder tb, ReportData report) {
		float totalCellWidth = 37;
		int total = report.getLicenseRiskHighCount() + report.getLicenseRiskMediumCount() + report.getLicenseRiskLowCount() + report.getLicenseRiskNoneCount();
		float highRisk = ((float) report.getLicenseRiskHighCount() / (float) total) * totalCellWidth;
		float medRisk = ((float) report.getLicenseRiskMediumCount() / (float) total) * totalCellWidth;
		float lowRisk = ((float) report.getLicenseRiskLowCount() / (float) total) * totalCellWidth;
		float noneRisk = ((float) report.getLicenseRiskNoneCount() / (float) total) * totalCellWidth;
		
		tb.addCellWidths(vec(CELL_SIZE_RISK_TABLE_BODY, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER));
		tb = riskTableRow(tb, HIGH_RISK, report.getLicenseRiskHighCount()+"", percentageColorCell(decode("#b52b24"), highRisk));
		tb = riskTableRow(tb, MED_RISK, report.getLicenseRiskMediumCount()+"", percentageColorCell(decode("#eca4a0"), medRisk));
		tb = riskTableRow(tb, LOW_RISK, report.getLicenseRiskLowCount()+"", percentageColorCell(new Color(153,153,153), lowRisk));
		tb = riskTableRow(tb, NO_RISK, report.getLicenseRiskNoneCount()+"", percentageColorCell(new Color(221,221,221), noneRisk));
		return tb;
	}

	public TableBuilder aggregateOperationalRiskTableHeader(TableBuilder tb, ReportData report ) {
		return riskTableHeader(tb, "Operational Risk");	
	}

	public TableBuilder aggregateOperationalRiskTable(TableBuilder tb, ReportData report) {
		float totalCellWidth = 37;
		int total = report.getOperationalRiskHighCount() + report.getOperationalRiskMediumCount() + report.getOperationalRiskLowCount() + report.getOperationalRiskNoneCount();
		float highRisk = ((float) report.getOperationalRiskHighCount() / (float) total) * totalCellWidth;
		float medRisk = ((float) report.getOperationalRiskMediumCount() / (float) total) * totalCellWidth;
		float lowRisk = ((float) report.getOperationalRiskLowCount() / (float) total) * totalCellWidth;
		float noneRisk = ((float) report.getOperationalRiskNoneCount() / (float) total) * totalCellWidth;
		
		tb.addCellWidths(vec(CELL_SIZE_RISK_TABLE_BODY, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER));
		tb = riskTableRow(tb, HIGH_RISK, report.getOperationalRiskHighCount()+"", percentageColorCell(decode("#b52b24"), highRisk));
		tb = riskTableRow(tb, MED_RISK, report.getOperationalRiskMediumCount()+"", percentageColorCell(decode("#eca4a0"), medRisk));
		tb = riskTableRow(tb, LOW_RISK, report.getOperationalRiskLowCount()+"", percentageColorCell(new Color(153,153,153), lowRisk));
		tb = riskTableRow(tb, NO_RISK, report.getOperationalRiskNoneCount()+"", percentageColorCell(new Color(221,221,221), noneRisk));
		return tb;
	}

	public TableBuilder projectInformationTable(TableBuilder tb, float pageWidth, ReportData report) {
		tb.addCellWidths(vec(pageWidth))
		.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_MAIN_TITLE, decode("#46759E")))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_LEFT, Padding.of(2), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().addStrs(report.getProjectName() + " - " + report.getProjectVersion()).buildCell()
		.buildRow()
		.buildPart()

		//phase and distrib
		.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, CELL_COLOR_FONT_BODY))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_LEFT, Padding.of(3), Color.WHITE, BorderStyle.of(WHITE)))
		.rowBuilder()
		.cellBuilder().addStrs("Phase:    " + report.getPhase() + "    |    Distribution:    " + report.getDistribution()).buildCell()
		.buildRow()
		.buildPart();

		return tb;
	}

	public TableBuilder titleTable(TableBuilder tb, ReportData report, float pageWidth) throws IOException, URISyntaxException {
		return tb.addCellWidths(vec(pageWidth / 2, pageWidth / 2))
				.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD, FONT_SIZE_MAIN_TITLE, WHITE))
				.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), Color.BLACK, BorderStyle.of(BLACK)))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_LEFT).addStrs("Black Duck Risk Report").buildCell()
				.cellBuilder().align(MIDDLE_RIGHT).add(ScaledPng.of(ImageIO.read(getClass().getResourceAsStream("/riskreport/web/images/Hub_BD_logo.png")), 203, 45)).buildCell()
				.buildRow()
				.buildPart();
	}

	public TableBuilder componentTableTitle(TableBuilder tb, ReportData report) throws IOException {

		tb.addCellWidths(vec(CELL_SIZE_HIDDEN_COLUMN, CELL_SIZE_COMPONENT_NAME, CELL_SIZE_COMPONENT_VERSION, CELL_SIZE_HIDDEN_COLUMN, CELL_SIZE_COMPONENT_LICENSE, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER));

		tb.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, Color.BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), Color.WHITE, BorderStyle.NO_BORDERS))
		.rowBuilder().addTextCells("", "BOM Entries " + report.getTotalComponents()).buildRow()
		.buildPart();

		//title rows
		tb.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD_OBLIQUE, FONT_SIZE_CELL_HEADER, BLACK))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#f5f5f5"), BorderStyle.NO_BORDERS))
		.rowBuilder().addTextCells("", "Component", "Version", "", "License", "H", "M", "L", "Opt R").buildRow()
		.buildPart();
		return tb;
	}

	public void componentTable(PdfLayoutMgr pageMgr, LogicalPage lp, XyOffset initialOffset, float startingX, ReportData report) throws IOException, URISyntaxException {
		boolean row = false;
		XyOffset lastRow = initialOffset;

		for (BomComponent b : report.getComponents()) {
			int h = b.getLicenseRiskHighCount() + b.getOperationalRiskHighCount() + b.getSecurityRiskHighCount();
			int m = b.getLicenseRiskMediumCount() + b.getOperationalRiskMediumCount() + b.getSecurityRiskMediumCount();
			int l = b.getLicenseRiskLowCount() + b.getOperationalRiskLowCount() + b.getSecurityRiskLowCount();
			String licR = licenseRisk(b);
			String optR = operationalRisk(b);
			Color bodyCellBackgroundColor = (row) ? new Color(221,221,221) : decode("#ffffff");
			Color licRColor = (licR.equals("H")) ? decode("#b52b24") : (licR.equals("M")) ? decode("#eca4a0") : (licR.equals("L")) ? new Color(153,153,153) : bodyCellBackgroundColor;
			Color optRColor = (optR.equals("H")) ? decode("#b52b24") : (optR.equals("M")) ? decode("#eca4a0") : (optR.equals("L")) ? new Color(153,153,153) : bodyCellBackgroundColor;
//			ScaledPng crossOutImg = ScaledPng.of(ImageIO.read(getClass().getResourceAsStream("/riskreport/web/images/cross_through_circle.png")), 8, 8); 
			Renderable r = (b.getPolicyStatus().equalsIgnoreCase("IN_VIOLATION")) ? ScaledPng.of(ImageIO.read(getClass().getResourceAsStream("/riskreport/web/images/cross_through_circle.png")), 8, 8) : null;
			Cell licRCell = Cell.of(CellStyle.of(MIDDLE_CENTER, Padding.of(2, 2, 0, 2), licRColor, BorderStyle.NO_BORDERS), CELL_SIZE_HIDDEN_COLUMN, Text.of(TextStyle.of(PDType1Font.HELVETICA, 8f, BLACK), licR));

			TableBuilder tb = 
					lp.tableBuilder(XyOffset.of(startingX, lastRow.y())).addCellWidths(vec(CELL_SIZE_HIDDEN_COLUMN, CELL_SIZE_COMPONENT_NAME, CELL_SIZE_COMPONENT_VERSION, CELL_SIZE_HIDDEN_COLUMN, CELL_SIZE_COMPONENT_LICENSE, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER, CELL_SIZE_NUMBER))
					.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.NO_BORDERS))
					.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, CELL_COLOR_FONT_BODY))
					.rowBuilder()
					.cellBuilder().align(MIDDLE_CENTER).add(r).buildCell()
					.cellBuilder().align(MIDDLE_LEFT).addStrs(StringWrapper.wrap(b.getComponentName())).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(b.getComponentVersion())).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).add(licRCell).buildCell()
					.cellBuilder().align(MIDDLE_LEFT).addStrs(StringWrapper.wrap(b.getLicense())).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(h+"")).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(m+"")).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(StringWrapper.wrap(l+"")).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(optR).cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.NO_PADDING, optRColor, BorderStyle.NO_BORDERS)).buildCell()
					.buildRow()
					.buildPart();

			XyOffset temp = tb.buildTable();
			if (b.getPolicyStatus().equals("IN_VIOLATION") && b.getPolicyRulesViolated() != null) {
				TableBuilder policyViolationRow = policyViolationRow(b, lp, temp, startingX, bodyCellBackgroundColor);
				lastRow = policyViolationRow.buildTable();
			} else {
				lastRow = tb.buildTable();
			}
			row = !row;
		}
	}

	public TableBuilder policyViolationRow(BomComponent b, LogicalPage lp, XyOffset prevRow, float startingX, Color bodyCellBackgroundColor) {
		return lp.tableBuilder(XyOffset.of(startingX, prevRow.y())).addCellWidths(vec(CELL_SIZE_HIDDEN_COLUMN, CELL_SIZE_NUMBER+CELL_SIZE_NUMBER, CELL_SIZE_COMPONENT_VERSION+CELL_SIZE_COMPONENT_NAME+CELL_SIZE_COMPONENT_LICENSE+CELL_SIZE_NUMBER+CELL_SIZE_NUMBER+CELL_SIZE_HIDDEN_COLUMN))
				.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), bodyCellBackgroundColor, BorderStyle.NO_BORDERS))
				.textStyle(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, CELL_COLOR_FONT_BODY))
				.rowBuilder()
				.cellBuilder().addStrs("").buildCell()
				.cellBuilder().cellStyle(CellStyle.of(MIDDLE_LEFT, Padding.DEFAULT_TEXT_PADDING, bodyCellBackgroundColor, BorderStyle.NO_BORDERS)).add(TextStyle.of(FONT_BODY, FONT_SIZE_CELL_BODY, Color.RED), vec("Policy Violation:")).buildCell()
				.cellBuilder().align(MIDDLE_LEFT).addStrs(joiner(b.getPolicyRulesViolated(), ",", 65)).buildCell()
				.buildRow()
				.buildPart();
	}

	public String licenseRisk(BomComponent b) {
		if (b.getLicenseRiskHighCount() > 0) {
			return "H";
		} else if (b.getLicenseRiskMediumCount() > 0) {
			return "M";
		} else if (b.getLicenseRiskLowCount() > 0) {
			return "L";
		} else {
			return "";
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
	
	public String joiner(List<PolicyRule> list, String sep, int charLimit) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				sb.append(StringWrapper.wrap(list.get(i).getName(), charLimit));
			} else {
				sb.append(sep + " ").append(StringWrapper.wrap(list.get(i).getName(), charLimit));
			}
		}
		return sb.toString();
	}

	private static <T> List<T> vec(T... ts) { return Arrays.asList(ts); }
	
//	public static void main(String[] args) throws RiskReportException {
//		ReportData report = new ReportData();
//		BomComponent b1 = new BomComponent();
//		b1.setComponentName("comp name");
//		b1.setComponentVersion("version");
//		b1.setLicense("license");
//	
//		
//		PolicyRule p1 = new PolicyRule();
//		p1.setName("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
//		
//		PolicyRule p2 = new PolicyRule();
//		p2.setName("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
////		
////		PolicyRule p3 = new PolicyRule();
////		p3.setName("3this is a longer name");
////		
////		PolicyRule p4 = new PolicyRule();
////		p4.setName("4this is a longer name");
//		
//		b1.setPolicyRulesViolated(vec(p1, p2));
//		b1.setPolicyStatus("IN_VIOLATION");
//		report.setComponents(vec(b1));
//		
//		new RiskReportPDFWriter().createPDFReportFile(new File("."), report);
//	}
}