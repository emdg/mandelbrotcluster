package mandelbrot;

import gui.ImgPanel;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JFrame;

import api.Client;
import api.Result;


public class MandelbrotGUI extends JFrame {
	/**
	 * 
	 */

	private ImgPanel imgPanel;
	
	private int width = 500;
	private int height = 500;
	private long currentZoom = 1;
	private BigDecimal xa;
	private BigDecimal ya;
	private BigDecimal xb;
	private BigDecimal yb;
	
	private boolean isReady = false;
	private MandelbrotClient client;
	
	public MandelbrotGUI(MandelbrotClient c, final int w, final int h){
		
		super("Mandelbrot");
		
		width = w;
		height = h;

		client = c;
		addMouseListener(new MouseAdapter(){
		    public void mousePressed(MouseEvent e) {
		    	if (isReady){
		    		client.mousePressed(e.getX(), e.getY());
		    		isReady = false;
		    	}
	        } 
		});
		
		
		imgPanel = new ImgPanel();
		setBounds(100, 100, width, height);
        Container contentPane = getContentPane();

        contentPane.setLayout(null);
        contentPane.add(imgPanel);
        setVisible(true);
        setResizable(false);
        validate();
    
	}
	
	

	
	
	public static BufferedImage getImage(int w, int h, int[] pix){
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, w, h, pix, 0, w);
		return image;
	}
	
	
	public void setMandelbrotImage(BufferedImage img){
		this.imgPanel.setImage(img);
		repaint();
		isReady = true;
	}

	
}
