package com.blackducksoftware.integration.hub.report.pdf.style;

import java.awt.Color;

public class Border {

	public Color color = Color.BLACK;
	public int width = 1;
	
	public Border() {
	}
	
	public Border(Color color) {
		this.color = color;
	}
	
	public Border(int width) {
		this.width = width;
	}
	
	public Border(Color color, int width) {
		this.color = color;
		this.width = width;
	}

	@Override
	public String toString() {
		return "Border [color=" + color + ", width=" + width + "]";
	}
	
	

}
