package gui;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImgPanel extends JPanel{
		private BufferedImage image;

        public ImgPanel(){
        	setBounds(0,0,600,600);
        	
        }
        public ImgPanel(BufferedImage image){
        	this.image = image;
        	setBounds(0,0,600,600);
        }
        
        public void setImage(BufferedImage image){
        	this.image = image;
        }
        
		@Override
		public void paint (Graphics g){
			super.paint(g);
			g.drawImage(image, 0, 0, this);
		}
}