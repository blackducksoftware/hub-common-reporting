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
import com.planbase.pdf.layoutmanager.TextStyle;
import com.planbase.pdf.layoutmanager.XyOffset;
import static com.planbase.pdf.layoutmanager.CellStyle.Align.*;


import static java.awt.Color.*;


public class PDFBuilder {
	
	public void makePDF(ReportData report) throws IOException {	
		PdfLayoutMgr pageMgr = PdfLayoutMgr.newRgbPageMgr();
		LogicalPage lp = pageMgr.logicalPageStart();
		
		XyOffset xya =
			lp.tableBuilder(XyOffset.of(0, lp.yPageTop()))
				.addCellWidths(vec(120f, 120f, 120f, 120f))
				
				//title rows
				.textStyle(TextStyle.of(PDType1Font.COURIER_BOLD_OBLIQUE, 12f, YELLOW.brighter()))
				.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#3366cc"), BorderStyle.of(BLACK)))
				.rowBuilder().addTextCells("", "Security Risk", "License Risk", "Operational Risk").buildRow()
				.buildPart()
				
				//high risk row
				.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
				.textStyle(TextStyle.of(PDType1Font.COURIER, 12f, BLACK))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_CENTER).addStrs("High Risk").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskHighCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskHighCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskHighCount()+"").buildCell()
				.buildRow()
				.buildPart()
				
				//medium risk row
				.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
				.textStyle(TextStyle.of(PDType1Font.COURIER, 12f, BLACK))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_CENTER).addStrs("Medium Risk").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskMediumCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskMediumCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskMediumCount()+"").buildCell()
				.buildRow()
				.buildPart()
				
				//low risk row
				.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
				.textStyle(TextStyle.of(PDType1Font.COURIER, 12f, BLACK))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_CENTER).addStrs("Low Risk").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskLowCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskLowCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskLowCount()+"").buildCell()
				.buildRow()
				.buildPart()
				
				//no risk row
				.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
				.textStyle(TextStyle.of(PDType1Font.COURIER, 12f, BLACK))
				.rowBuilder()
				.cellBuilder().align(MIDDLE_CENTER).addStrs("No Risk").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getVulnerabilityRiskNoneCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getLicenseRiskNoneCount()+"").buildCell()
				.cellBuilder().align(MIDDLE_CENTER).addStrs(report.getOperationalRiskNoneCount()+"").buildCell()
				.buildRow()
				.buildPart()
				
				.buildTable();
		
			TableBuilder tb = lp.tableBuilder(XyOffset.of(0, xya.y()));
			
			int versionLength = versionScale(report);
			int componentLength = componentScale(report);
			int licenseLength = licenseScale(report);
			
			int total = versionLength + componentLength + licenseLength;
			int numberCells = 5;
			float numberCellSize = 50f;
			float pageSize = 800f - 55f * numberCells;
			float vs = pageSize * ( (float) versionLength / (float) total);
			System.out.println(vs);
			float cs = pageSize * ( (float) componentLength / (float) total);
			System.out.println(cs);
			float ls = pageSize * ( (float) licenseLength / (float) total);
			System.out.println(ls);
			
				tb.addCellWidths(vec(cs, vs, ls, numberCellSize, numberCellSize, numberCellSize, numberCellSize, numberCellSize));
				
				//title rows
				tb.textStyle(TextStyle.of(PDType1Font.COURIER_BOLD_OBLIQUE, 12f, YELLOW.brighter()))
				.partBuilder().cellStyle(CellStyle.of(BOTTOM_CENTER, Padding.of(2), decode("#3366cc"), BorderStyle.of(BLACK)))
				.rowBuilder().addTextCells("Component", "Version", "License", "H", "M", "L", "Lic R", "Opt R").buildRow()
				.buildPart();
				
				//component details rows
				for (BomComponent b : report.getComponents()) {
					int h = b.getLicenseRiskHighCount() + b.getOperationalRiskHighCount() + b.getSecurityRiskHighCount();
					int m = b.getLicenseRiskMediumCount() + b.getOperationalRiskMediumCount() + b.getSecurityRiskMediumCount();
					int l = b.getLicenseRiskLowCount() + b.getOperationalRiskLowCount() + b.getSecurityRiskLowCount();

					tb.partBuilder().cellStyle(CellStyle.of(MIDDLE_CENTER, Padding.of(2), decode("#ccffcc"), BorderStyle.of(DARK_GRAY)))
					.textStyle(TextStyle.of(PDType1Font.COURIER, 12f, BLACK))
					.rowBuilder()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(b.getComponentName()).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(b.getComponentVersion()).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(b.getLicense()).buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(h+"").buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(m+"").buildCell()
					.cellBuilder().align(MIDDLE_CENTER).addStrs(l+"").buildCell()
					.buildRow()
					.buildPart();
				}
				
				tb.buildTable();
		
		lp.commit();
		OutputStream os = new FileOutputStream("test.pdf");
		pageMgr.save(os);
	}
	
	public int versionScale(ReportData report) {
		int maxStrLen = 0;
		for (BomComponent b : report.getComponents()) {
			String[] substrings = b.getComponentVersion().split(" ");
			for (String s : substrings) {
				if (s.length() > maxStrLen) {
					maxStrLen = s.length();
				}
			}
		}
		return maxStrLen;
	}
	
	public int componentScale(ReportData report) {
		int maxStrLen = 0;
		for (BomComponent b : report.getComponents()) {
			String[] substrings = b.getComponentName().split(" ");
			for (String s : substrings) {
				if (s.length() > maxStrLen) {
					maxStrLen = s.length();
				}
			}
		}
		return maxStrLen;
	}
	
	public int licenseScale(ReportData report) {
		int maxStrLen = 0;
		for (BomComponent b : report.getComponents()) {
			String[] substrings = b.getLicense().split(" ");
			for (String s : substrings) {
				if (s.length() > maxStrLen) {
					maxStrLen = s.length();
				}
			}
		}
		return maxStrLen;
	}
	
	
	private static <T> List<T> vec(T... ts) { return Arrays.asList(ts); }
	
	public static void main(String[] args) throws IOException {
		ReportData report = new ReportData();
		
		BomComponent bc1 = new BomComponent();
		bc1.setComponentName("Manager Manager Manager");
		bc1.setComponentVersion("mvnref-book-parent-0.2.1");
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
