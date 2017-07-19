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
package com.blackducksoftware.integration.hub.report.PDFBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
import com.planbase.pdf.layoutmanager.TableBuilder;
import com.planbase.pdf.layoutmanager.TablePart;
import com.planbase.pdf.layoutmanager.TextStyle;
import com.planbase.pdf.layoutmanager.XyOffset;
import static com.planbase.pdf.layoutmanager.CellStyle.Align.*;
import static java.awt.Color.*;


public class PDFBuilder {

	public void makePDF(ReportData report) throws IOException {	
		
		PdfLayoutMgr pageMgr = PdfLayoutMgr.newRgbPageMgr();
		LogicalPage lp = pageMgr.logicalPageStart();

		

		TableBuilder tb1 = lp.tableBuilder(XyOffset.of(0, lp.yPageTop()));
		tb1 = firstTable(tb1, report);
		XyOffset firstTableXYOffset = tb1.buildTable();

		TableBuilder tb2 = lp.tableBuilder(XyOffset.of(0, firstTableXYOffset.y()));
		tb2 = componentTable(tb2, report);
		XyOffset secondTableXYOffset = tb2.buildTable();

		lp.commit();
		OutputStream os = new FileOutputStream("test.pdf");
		pageMgr.save(os);
	}
	
	public TableBuilder firstTable(TableBuilder tb, ReportData report) {
		float titleFontSize = 12f;
		float bodyFontSize = 10f;
		float numberCellSize = 75f;
		
		return tb.addCellWidths(vec(numberCellSize, numberCellSize, numberCellSize, numberCellSize))
				
		//title rows
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD_OBLIQUE, titleFontSize, YELLOW.brighter()))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#3366cc"), BorderStyle.of(BLACK)))
		.rowBuilder().addTextCells("", "Security Risk", "License Risk", "Operational Risk").buildRow()
		.buildPart()

		//high risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_OBLIQUE, bodyFontSize, BLACK))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("High Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskHighCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskHighCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskHighCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//medium risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_OBLIQUE, bodyFontSize, BLACK))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Medium Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskMediumCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskMediumCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskMediumCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//low risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_OBLIQUE, bodyFontSize, BLACK))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("Low Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskLowCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskLowCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskLowCount()+"").buildCell()
		.buildRow()
		.buildPart()

		//no risk row
		.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
		.textStyle(TextStyle.of(PDType1Font.HELVETICA_OBLIQUE, bodyFontSize, BLACK))
		.rowBuilder()
		.cellBuilder().align(MIDDLE_CENTER).addStrs("No Risk").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskNoneCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskNoneCount()+"").buildCell()
		.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskNoneCount()+"").buildCell()
		.buildRow()
		.buildPart();	
	}
	
	public TableBuilder componentTable(TableBuilder tb, ReportData report) {
		float titleFontSize = 12f;
		float bodyFontSize = 10f;
		float numberCellSize = 50f;
		float componentVersionCellSize = 150f;
		float componentLicenseCellSize = 200f;
		float componentNameCellSize = 150f;
		
		tb.addCellWidths(vec(componentNameCellSize, componentVersionCellSize, componentLicenseCellSize, numberCellSize, numberCellSize, numberCellSize, numberCellSize, numberCellSize));

		//title rows
		tb.textStyle(TextStyle.of(PDType1Font.HELVETICA_BOLD_OBLIQUE, titleFontSize, YELLOW.brighter()))
		.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#3366cc"), BorderStyle.of(BLACK)))
		.rowBuilder().addTextCells("Component", "Version", "License", "H", "M", "L", "Lic R", "Opt R").buildRow()
		.buildPart();

		//component details rows
		for (BomComponent b : report.getComponents()) {
			int h = b.getLicenseRiskHighCount() + b.getOperationalRiskHighCount() + b.getSecurityRiskHighCount();
			int m = b.getLicenseRiskMediumCount() + b.getOperationalRiskMediumCount() + b.getSecurityRiskMediumCount();
			int l = b.getLicenseRiskLowCount() + b.getOperationalRiskLowCount() + b.getSecurityRiskLowCount();
			
			
			tb.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
			.textStyle(TextStyle.of(PDType1Font.HELVETICA_OBLIQUE, bodyFontSize, BLACK))
			.rowBuilder()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(wrapString(b.getComponentName())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(wrapString(b.getComponentVersion())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(wrapString(b.getLicense())).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(h+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(m+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(l+"").buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(licenseRisk(b)).buildCell()
			.cellBuilder().align(MIDDLE_CENTER).addStrs(operationalRisk(b)).buildCell()
			.buildRow()
			.buildPart();
		}
		
		return tb;
	}
	
	public String wrapString(String str) {
		ArrayList<String> subWords = new ArrayList<String>(Arrays.asList(str.split(" ")));		
		for (int i = 0; i < subWords.size(); i++) {
			String temp = subWords.get(i);
			if (temp.length() >= 20) {
				subWords.remove(temp);
				int maxCounter = 0;
				int lastBreak = 0;
				ArrayList<String> tempList = new ArrayList<String>();
//				System.out.println("\tworking with: " + temp);
				for (int j = 0; j < temp.length(); j++) {
//					System.out.println(j);
					if (!StringUtils.isAlphanumeric(temp.charAt(j)+"") || maxCounter == 20) {
						tempList.add(temp.substring(lastBreak, j) );
//						System.out.println("1 Adding " + temp.substring(lastBreak, j) + " to list");
						lastBreak = j;
						maxCounter = 0;
					} else {
						maxCounter++;
					}
	
					if (tempList.size() > 0 && temp.length() - j < 20) {
						tempList.add(temp.substring(j, temp.length()));
//						System.out.println("3 Adding rest of string " + temp.substring(j, temp.length()) + " to list");
						break;
					}
				}
				subWords.addAll(i, tempList);
//				System.out.println(tempList);
			}
		}
		return String.join(" ", subWords);
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
		bc1.setSecurityRiskHighCount(1);
		bc1.setSecurityRiskMediumCount(5);
		bc1.setSecurityRiskLowCount(3);

		bc1.setLicenseRiskHighCount(4);
		bc1.setLicenseRiskMediumCount(39);
		bc1.setLicenseRiskLowCount(2);

		bc1.setOperationalRiskHighCount(9);
		bc1.setOperationalRiskMediumCount(4);
		bc1.setOperationalRiskLowCount(9);


		BomComponent bc2 = new BomComponent();
		bc2.setComponentName("ComponentName");
		bc2.setComponentVersion("This is a super long version name");
		bc2.setLicense("mit license mit license mit license mit license mit license mit license mit license mit license mit license mit license mit license");
		bc2.setSecurityRiskHighCount(1);
		bc2.setSecurityRiskMediumCount(5);
		bc2.setSecurityRiskLowCount(3);

		bc2.setLicenseRiskHighCount(4);
		bc2.setLicenseRiskMediumCount(3);
		bc2.setLicenseRiskLowCount(2);

		bc2.setOperationalRiskHighCount(9);
		bc2.setOperationalRiskMediumCount(4);
		bc2.setOperationalRiskLowCount(9);

		ArrayList<BomComponent> list = new ArrayList<BomComponent>();
		list.add(bc1);
		list.add(bc2);

		report.setComponents(list);

		PDFBuilder builder = new PDFBuilder();
		builder.makePDF(report);




	}

}
