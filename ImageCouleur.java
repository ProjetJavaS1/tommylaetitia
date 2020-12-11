package mam3.ipa.projet; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.ColorModel;
import java.awt.color.ColorSpace;

public class ImageCouleur{
 	
 	private byte[] tableau;
	private int IMG_WIDTH;
	private int IMG_HEIGHT;


	public ImageCouleur( String fn){

		RenderedOp ropimage; // contiendra les métadonnées et les données
		ropimage = JAI.create("fileload", fn); // ouvre fichier

		BufferedImage bi = ropimage.getAsBufferedImage(); 
		// on utilise un bufferedImage pour stocker nos donées 

		IMG_WIDTH = ropimage.getWidth(); // largeur de l'image 
		IMG_HEIGHT = ropimage.getHeight(); // longueur de l'image 
 	
 		//voyons si c'est une image en niveaux de gris ou en couleur
 		// on récupère le mode couleur

  		ColorModel cm = ropimage.getColorModel();
	
		if (cm.getColorSpace ().getType() == ColorSpace.TYPE_RGB) {
 	
 		//si on entre dans le if, on peut obtenir chaque pixel RVB
		// getRGB () formate tjrs chaque pixel dans un int (4 octets) 

 		int[] px2;

 		// on stocke ds px2 chaque point d'image comme un Int
		// on passe de x=0, y=0, à img_width et img_height octets
 		// null = stocker les données dans px2
 		// 0 = stocker les données à l'offset 0 du tableau de destination
 		// dernier "IMG_WIDTH" = cette image contient IMG_WIDTH pixels par ligne
   		px2 = bi.getRGB(0,0,IMG_WIDTH,IMG_HEIGHT,null,0,IMG_WIDTH);
  		}
	}

}
