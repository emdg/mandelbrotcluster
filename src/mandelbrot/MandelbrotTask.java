package mandelbrot;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.SerializedImage;
import api.Result;
import api.Task;
import api.Priority;

public class MandelbrotTask implements Task, Comparable<Task> {
	
	private int[] pix;
	private ArrayList<MandelbrotThread> threads;
	public int height;
	public int width;
	private double zoom;
	private BigDecimal xa;
	private BigDecimal ya;
	private BigDecimal xb;
	private BigDecimal yb;
	private int priority;
	private int seqNr;
	
	public MandelbrotTask(int seqNr, int priority, int width, int height,double zoom,BigDecimal xa, BigDecimal ya,
			BigDecimal xb, BigDecimal yb){
		pix = new int[height * width];	
		threads = new ArrayList<MandelbrotThread>();
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.xa = xa;
		this.ya = ya;
		this.xb = xb;
		this.yb = yb;
		this.priority = priority;
		this.seqNr = seqNr;
	}
	
	public Result execute() {
		int cores = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < cores; i ++){
			MandelbrotThread m = new MandelbrotThread(i, cores, pix,width,height, zoom, xa, ya, xb, yb);
			m.start();
			threads.add(m);
		}
		
		for (Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		SerializedImage si = new SerializedImage(getImage(width, height, pix));
		
		return new MandelbrotResult(seqNr, si);
		
	}
	
	
	
	
	
	public static BufferedImage getImage(int w, int h, int[] pix){
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, w, h, pix, 0, w);
		return image;
	}
	
	
	
	public static void main(String[] args){

	}

	@Override
	public int compareTo(Task o) {
		return -Integer.compare(this.priority(), o.priority());
	}

	@Override
	public int priority() {
		return priority;
	}
	
	
	
	
	
	
	
	
}
