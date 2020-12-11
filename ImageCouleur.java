package mam3.ipa.projet; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.ColorModel;
import java.awt.color.ColorSpace;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

public class ImageCouleur{
 	
 	private int[] tableau;
	private int img_width;
	private int img_height;


	public ImageCouleur( String fn){  //ouvrir image 

		RenderedOp ropimage; // contiendra les métadonnées et les données
		ropimage = JAI.create("fileload", fn); // ouvre fichier

		BufferedImage bi = ropimage.getAsBufferedImage(); 
		// on utilise un bufferedImage pour stocker nos donées 

		img_width = ropimage.getWidth(); // largeur de l'image 
		img_height = ropimage.getHeight(); // longueur de l'image 
 	
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
   		px2 = bi.getRGB(0,0,img_width,img_height,null,0,img_width);
  		}
	}

	// convertir l'image en échelle de gris 

	public void ImageGrayScale (){
		int r=0; 
		int g=0; 
		int b=0; 
		int grayScale;
		int[] tableauGris = new int[tableau.length];

		//on va changer chaque valeur d'octet

		for(int i=0; i < tableau.length; i++) {
			//on note la valeur r,b,g
			// >> permet le décalage binaire vers la droite 
			// oxff pour que ce soit un hexadécimal
			// on convertit sur 24bits 
			r = (tableau[i] >> 16) & 0xff;
			g = (tableau[i] >> 8) & 0xff;
			b = (tableau[i]) & 0xff;

			grayScale = (r+b+g)/3 ; 
			// on prend la moyenne des 3 composantes

 			tableauGris[i]= grayScale;
 			// | correspond à un ou inclusif 
		}

		DataBufferInt dataBuffer = new DataBufferInt(tableauGris,tableauGris.length);
 		
  		int samplesPerPixel = 4; //4 channels: aRGB

 		// on crée un ColorModel pour l'image aRGB 
 		// tous les pixel sont sur 32 bits 
 		// R is located doing an AND operation between the int and
 		// the 0xFF0000 mask. Following masks correspond to G, B
 		// and the alpha channel
  		ColorModel colorModel = new DirectColorModel(32,0xFF0000,0xFF00,0xFF,0xFF000000);
 		// we need now a Raster with the right image dimensions and masks
 		// for every channel. null = start writing the data at the beginning
 		// of the Raster data
  		WritableRaster raster = Raster.createPackedRaster(dataBuffer, img_width, img_height, img_width,
  			((DirectColorModel) colorModel).getMasks(), null);
  		// let's put all the stuff together with a BufferedImage object
 		// we provide the appropriate color model, raster, we says if
 		// the alpha channel appears as a factor in the other channels
 		// (isAlphaPremultiplied()). null = no need for additional parameters
 		BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
 		// Everything is in place. Just write it down to the disk
  		RenderedOp op1 = JAI.create("filestore", image, "res.png", "png");

	}

}
