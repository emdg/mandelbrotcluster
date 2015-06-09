package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializedImage implements Serializable {
	  /**
	   * A BufferedImage to serialized.
	   * @serial bimage A BufferedImage to serialized.
	   */
	  private BufferedImage bimage;

	  /**
	   * Create a SerializedImage with a BufferedImage (<I>Can be use with RMI</I>).
	   * @param bufferedImage Initial BufferedImage.
	   */
	  public SerializedImage(BufferedImage bufferedImage) {
	    bimage=bufferedImage;
	  }

	  /**
	   * Create a SerializedImage (<I>Can be use with RMI</I>).
	   * @param image Initial Image.
	   * @param width Width of this image.
	   * @param Height height of this image.
	   */
	  public SerializedImage(Image image,int width,int height) {
	    boolean errorFlag=false;

	    PixelGrabber pg = new PixelGrabber(image,0,0,width,height,true);
	    try {
	      pg.grabPixels();
	    } catch (InterruptedException e) {
	      System.out.println("* Error while creating SerializedImage from Image");
	      errorFlag=true;
	    }
	    bimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	    if (!errorFlag) {
	      bimage.setRGB(0,0,width,height,(int[])pg.getPixels(),0,width);
	    }
	  }

	  /**
	   * Get an BufferedImage of this SerializedImage.
	   */
	  public BufferedImage getBImage() {
	    return bimage;
	  }

	  /**
	   * Get the width of this SerializedImage.
	   * @return Width in pixels.
	   */
	  public int getWidth() {
	    return bimage.getWidth();
	  }

	  /**
	   * Get the height of this SerializedImage.
	   * @return Height in pixels.
	   */
	  public int getHeight() {
	    return bimage.getHeight();
	  }

	  /**
	   * Returns a string representation of the object.
	   * Like this : SerializedImage[w=bimageWidth,h=bimageHeight].
	   */
	  public String toString() {
	    return new String("SerializedImage[w=" + bimage.getWidth() + ",h=" + bimage.getHeight() + "]");
	  }

	  // Serialization of the BufferedImage 
	  private void writeObject(ObjectOutputStream s) throws IOException {
	    s.writeInt(bimage.getWidth());
	    s.writeInt(bimage.getHeight());
	    s.writeInt(bimage.getType());

	    for(int i=0;i<bimage.getWidth();i++) {
	      for(int j=0;j<bimage.getHeight();j++) {
		s.writeInt(bimage.getRGB(i,j));
	      }
	    }
	    s.flush();
	  }

	  private void readObject(ObjectInputStream s) throws IOException,ClassNotFoundException  {
	    bimage = new BufferedImage(s.readInt(),s.readInt(),s.readInt());
	    for(int i=0;i<bimage.getWidth();i++) {
	      for(int j=0;j<bimage.getHeight();j++) {
		bimage.setRGB(i,j,s.readInt());
	      }
	    }

	  }

	}