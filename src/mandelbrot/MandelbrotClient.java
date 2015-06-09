package mandelbrot;

import static java.math.BigDecimal.valueOf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.math.RoundingMode;


import javax.imageio.ImageIO;

import api.Client;
import api.Priority;
import api.Result;
import api.Space;
import api.Task;




public class MandelbrotClient extends UnicastRemoteObject implements Client {
	
	private MandelbrotGUI gui;
	
	private Space space;
	private int height = 500;
	private int width = 500;
	private BigDecimal xa, xb, ya, yb;
	private double zoomFactor = 1.10;
	private double currentZoom = 1.0;
	private int currentSeq = 0;

	
	public MandelbrotClient(String domain) throws MalformedURLException, RemoteException, NotBoundException {
		
		String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;

		space = (Space) Naming.lookup( url );

		space.registerClient(this);

		xa = d("-2.0");
		ya = d("-2.0");
		xb = d("2.0");
		yb = d("2.0");
		gui = new MandelbrotGUI(this, height, width);
		MandelbrotTask first = new MandelbrotTask(0, 0, width, height, 1, xa, ya, xb, yb);
		space.put(first);
		currentSeq++;
	}
	
	
	
	@Override
	public void didReceiveResult(Result res) {
		MandelbrotResult mres = (MandelbrotResult) res;
		if (res.getSeqNr() == currentSeq - 1 || res.getSeqNr() == 0){
			System.out.println(currentZoom);
			gui.setMandelbrotImage(mres.getValue().getBImage());
		}
		writeImage(mres);
		
	}
	
	

	public void mousePressed(int x, int y) {
		double propX = (double) x / width;
		double propY = (double) y / height;	
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < 30; i++){
				BigDecimal newDiffX = xb.subtract(xa).divide(
						valueOf(zoomFactor), RoundingMode.HALF_UP);
				xa = xb.subtract(xa).multiply(valueOf(propX)).add(xa)
						.subtract(newDiffX.divide(valueOf(2)));
	
				xb = xa.add(newDiffX);

				BigDecimal newDiffY = yb.subtract(ya).divide(
						valueOf(zoomFactor), RoundingMode.HALF_UP);
				ya = yb.subtract(ya).multiply(valueOf(propY)).add(ya)
						.subtract(newDiffY.divide(valueOf(2)));
				yb = ya.add(newDiffY);

			MandelbrotTask t;
			if (i == 29){
				t = new MandelbrotTask(currentSeq, Priority.VERY_HIGH, width, height, currentZoom, 
					new BigDecimal(xa.toString()), 
					new BigDecimal(ya.toString()), 
					new BigDecimal(xb.toString()), 
					new BigDecimal(yb.toString()));	
			
			}else{
				t = new MandelbrotTask(currentSeq, Priority.VERY_LOW, width, height, currentZoom, 
						new BigDecimal(xa.toString()), 
						new BigDecimal(ya.toString()), 
						new BigDecimal(xb.toString()), 
						new BigDecimal(yb.toString()));
			}
			System.out.println(xa);
			currentZoom *= zoomFactor;
			tasks.add(t);
			currentSeq += 1;
			propX = 0.5;
			propY = 0.5;
		}
		try {
			space.putAll(tasks);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	private static BigDecimal d(String s) {
		return new BigDecimal(s);
	}
	
	
	
	private static void writeImage(MandelbrotResult res) {
		int seqNr = res.getSeqNr();
		BufferedImage image = res.getValue().getBImage();
		
		DecimalFormat df = new DecimalFormat("000000");

		try {
			ImageIO.write(image, "png",
					new File("img/m" + df.format(seqNr) + ".png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new SecurityManager());
		String domain;
		if(args.length==0) {
			domain = "localhost";
		} else {
			domain = args[0];
		}
		
		
		final MandelbrotClient client = new MandelbrotClient(domain);
	}

}
