package mandelbrot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static math.DoubleDouble.ddValueOf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.math.RoundingMode;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import math.DoubleDouble;
import colors.ColorMaps1D;
import colors.ColorMap1D;
import gui.ImgPanel;
import java.lang.Math.*;

public class MandelbrotThread extends Thread {

	private final int[] pix;
	private final int threadID;
	private final int threads;
	private final BigDecimal xa;
	private final BigDecimal ya;
	private final BigDecimal xb;
	private final BigDecimal yb;
	private int w;
	private int h;
	private int maxIterations = 30000;
	private final ColorMap1D colorMap;
	private final double zoom;

	public MandelbrotThread(int threadID, int threads, int[] pix, int width, int height,double zoom,BigDecimal xa, BigDecimal ya,
			BigDecimal xb, BigDecimal yb){
		this.threadID = threadID;
		this.threads = threads;
		this.pix = pix;
		this.w = width;
		this.h = height;
		this.xa = xa;
		this.ya = ya;
		this.xb = xb;
		this.yb = yb;
		this.zoom = zoom;
		this.maxIterations = (int) (Math.sqrt(Math.abs(2*Math.sqrt(Math.abs(1-Math.sqrt((5*zoom))))))*300);
	    colorMap = ColorMaps1D.createDefault(maxIterations, 
		    new Color(0,0,0),
			new Color(0,0,255),
			new Color(0,255,255),
			new Color(64,64,64),
			new Color(128,128,128),
			new Color(0,255,0),
			new Color(192,192,192),
		    new Color(255,0,255),
		    new Color(255,200,0),
		    new Color(255,175,175),
		    new Color(255,0,0),
		    new Color(255,255,0),
		    new Color(255,255,255)
	    );
	}
	
	
	public void run(){
			calculateUsingDoubleDouble();

		//calculateUsingBigDecimal();
		
	}
	
	
	
	public void calculateUsingDouble() {
		int imax = w * h;

		double xb = this.xb.doubleValue();
		double xa = this.xa.doubleValue();
		double ya = this.ya.doubleValue();
		double yb = this.yb.doubleValue();

		// Each thread only calculates its own share of pixels!
		for (int i = threadID; i < imax; i += threads) {
			int kx = i % w;
			int ky = (i - kx) / w;
			double a = (double) kx / w * (xb - xa) + xa;
			double b = (double) ky / h * (yb - ya) + ya;
			double x = a;
			double y = b;
			int iter = 0;
			int v = 0;

			for (int kc = 0; kc < maxIterations; kc++) {
				double x2 = x * x;
				double y2 = y * y;
				double x0 = x2 - y2 + a;
				y = 2 * x * y + b;
				x = x0;
				double modulus = x2 + y2;

				if (modulus > 4) {
					break;
				}
				iter++;
			}
            double value = (double)iter / maxIterations;
			pix[w * ky + kx] = colorMap.getColor(value);
		}
	}
	public void calculateUsingDoubleDouble(){
		int imax = w * h;

		DoubleDouble xa = new DoubleDouble(this.xa.toPlainString());
		DoubleDouble xb = new DoubleDouble(this.xb.toPlainString());
		DoubleDouble ya = new DoubleDouble(this.ya.toPlainString());
		DoubleDouble yb = new DoubleDouble(this.yb.toPlainString());
		// Each thread only calculates its own share of pixels!
		for (int i = threadID; i < imax; i += threads) {
			int kx = i % w;
			int ky = (i - kx) / w;
			// double a = (double) kx / w * (xb - xa) + xa;
			DoubleDouble a = ddValueOf(kx).divide(ddValueOf(w)).multiply(xb.subtract(xa)).add(xa);
			// double b = (double) ky / h * (yb - ya) + ya;
			DoubleDouble b = ddValueOf(ky).divide(ddValueOf(h)).multiply(yb.subtract(ya)).add(ya);
			DoubleDouble x = a;
			DoubleDouble y = b;
			int iter = 0;
			for (int kc = 0; kc < maxIterations; kc++) {
				// double x2 = x * x;
				DoubleDouble x2 = x.multiply(x);
				// double y2 = y * y;
				DoubleDouble y2 = y.multiply(y);
				// double x0 = x2 - y2 + a;
				DoubleDouble x0 = x2.subtract(y2).add(a);
				// y = 2 * x * y + b;
				y = ddValueOf(2).multiply(x).multiply(y).add(b);
				// x = x0;
				x = x0;
				// double modulus = x2 + y2;
				double modulus = x2.doubleValue() + y2.doubleValue();
				if (modulus > 4) {
					break;
				}
				iter++;
			}
			
            double value = (double)iter / maxIterations;
			pix[w * ky + kx] = colorMap.getColor(value);
			
		}
		
	}
	
	public void calculateUsingBigDecimal(){
		int imax = w * h;



		BigDecimal diffX = xb.subtract(xa);
		BigDecimal diffY = yb.subtract(ya);
		BigDecimal four = BigDecimal.valueOf(4);
		// Each thread only calculates its own share of pixels!
		for (int i = threadID; i < imax; i += threads) {
			if (i % 1000 == 0)
				System.out.println(i + "/" + imax);
			int kx = i % w;
			int ky = (i - kx) / w;
			// double a = (double) kx / w * (xb - xa) + xa;
			BigDecimal a = valueOf(kx)
					.divide(valueOf(w), 100, RoundingMode.HALF_UP)
					.multiply(diffX).add(xa);
			// double b = (double) ky / h * (yb - ya) + ya;
			BigDecimal b = valueOf(ky)
					.divide(valueOf(h), 100, RoundingMode.HALF_UP)
					.multiply(diffY).add(ya);
			BigDecimal x = a;
			BigDecimal y = b;
			int iter = 0;
			for (int kc = 0; kc < maxIterations; kc++) {

				BigDecimal x2 = x.multiply(x);
				BigDecimal y2 = y.multiply(y);
				BigDecimal x0 = x2.subtract(y2).add(a);
				// BigDecimal x0 = x.pow(2).subtract(y.pow(2)).add(a);
				y = valueOf(2).multiply(x).multiply(y).add(b);
				// y = valueOf(2).multiply(x).multiply(y).add(b);
				x = x0;

				if (x2.add(y2).compareTo(four) > 0) {
					break;
				}
				iter++;
			}
            double value = (double)iter / maxIterations;
			pix[w * ky + kx] = colorMap.getColor(value);
		}
	}
	
	
	public static BufferedImage getImage(int w, int h, int[] pix){
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, w, h, pix, 0, w);
		return image;
	}
	

	private static BigDecimal d(String s) {
		return new BigDecimal(s);
	}
	
}
