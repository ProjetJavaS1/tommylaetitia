package mam3.ipa.projet; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import javax.media.jai.RasterFactory;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.ColorModel;
import java.awt.color.ColorSpace;
import java.awt.image.SampleModel;
import java.awt.image.DataBufferInt;
import java.io.BufferedWriter;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.lang.Math;
import java.awt.Point;

public class ImageCouleur{
 	
 	private int[] tableau;
	private int img_width;
	private int img_height;
	private Path p;


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

 		// on stocke ds tableau chaque point d'image comme un Int
		// on passe de x=0, y=0, à img_width et img_height octets
 		// null = stocker les données dans tableau
 		// 0 = stocker les données à l'offset 0 du tableau de destination
 		// dernier "IMG_WIDTH" = cette image contient IMG_WIDTH pixels par ligne
   		tableau= bi.getRGB(0,0,img_width,img_height,null,0,img_width);
   		System.out.println(tableau);

   		p=Paths.get(fn);
		if(!Files.exists(p)){
			p=null;
			System.out.print("Ce fichier n'existe pas");
		}	

		}
	}

	// convertir l'image en échelle de gris 

	public void imageGrayScale (){
		int r=0; 
		int g=0; 
		int b=0; 
		int alpha=255; //taux de transparence des pixels
		int grayScale=0;
		int[] tableauGris = new int[tableau.length];

		//on va changer chaque valeur d'octet

		for(int i=0; i < tableau.length; i++) {
			//on note la valeur r,b,g
			// >> permet le décalage binaire vers la droite 
			// cad vers le bit le plus faible
			// oxff pour que ce soit un hexadécimal
			alpha = (tableau[i] >> 24) & 0xff;
			r = (tableau[i] >> 16) & 0xff;
			g = (tableau[i] >> 8) & 0xff;
			b = (tableau[i]) & 0xff;

			grayScale = (r+b+g)/3 ; 
			// on prend la moyenne des 3 composantes

 			tableauGris[i]= (alpha<< 24 | grayScale<<16| grayScale<<8| grayScale );
 			// | correspond à un ou inclusif 
		}
		System.out.println(tableauGris);

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

	public void genererFichier(){

		//On créé le fichier de sortie 
		String[] tokens=p.getFileName().toString().split("\\.");
		String sortie=tokens[0]+"-h1.csv";

		Path p2 =Paths.get(sortie); //On créé le chemin vers ce fichier
		int[][] t = new int[256][4]; 
		//On construit un tableau d'entiers à 4 dimensions qui nous permettra de tracer l'histogramme
		if(Files.isDirectory(p2)){ 
		//On vérifie si le fichier est un répertoire existant 
				System.out.println("Le fichier est un répertoire existant");
		}

		try(BufferedWriter w=Files.newBufferedWriter(p2,StandardOpenOption.CREATE, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING)){

			for(int i=0;i<=255;i++){ 
				t[i][0]=i; 
				//On construit l'abscisse de l'histogramme sur la 1ère colonne
				int comptR=0;
				int comptG=0;
				int comptB=0;
				// on initialise les compteurs

				for(int j=0;j<tableau.length;j++){
					int r = (tableau[j] >> 16) & 0xff;
					int g = (tableau[j] >> 8) & 0xff;
					int b = (tableau[j]) & 0xff;

					if(r==i){
						comptR+=1; 
					//Compte combien de fois on a zéro, un...pour le rouge
					}
					if(g==i){
						comptG+=1; 
					}
					if(b==i){
						comptB+=1; 
					}
				}
				t[i][1]=comptR; //Ajoute au tableau le résultat du compteur
				t[i][2]=comptG;
				t[i][3]=comptB;
				w.write(String.valueOf(t[i][0])+";");
				w.write(String.valueOf(t[i][1])+";");
				w.write(String.valueOf(t[i][2])+";");
				w.write(String.valueOf(t[i][3]));
				w.newLine();
			}
		}catch(IOException e){System.out.println("Problème d'exception : "+e);}
	}

	public void assombrissement(){
		int n=tableau.length;
		for(int i=0;i<n;i++){
			int r= (tableau[i] >> 16) & 0xFF;
			int g= (tableau[i] >> 8) & 0xFF;
			int b= tableau[i] & 0xFF;
			int alpha= 255;
			
			alpha = (tableau[i] >> 24) & 0xff;

			r = (int)Math.floor((r*r)/255);

			g = (int)Math.floor((g*g)/255);

			b = (int)Math.floor((b*b)/255);

			tableau[i]= ( alpha<<24 | r <<16| g <<8| b);
		}
		DataBufferInt dataBuffer = new DataBufferInt(tableau,tableau.length);
 		
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

	public void eclairage(){
		int n=tableau.length;

		for(int i=0;i<n;i++){
			int r = (tableau[i] >> 16) & 0xFF;
			int g = (tableau[i] >> 8) & 0xFF;
			int b = tableau[i] & 0xFF;;
			int alpha= 255;
			
			alpha = (tableau[i] >> 24) & 0xFF;

			r=(int)(Math.sqrt(r)*Math.sqrt(255));

			g =(int)(Math.sqrt(g)*Math.sqrt(255));

			b =(int)(Math.sqrt(b)*Math.sqrt(255));

			tableau[i]= ( alpha<<24 | r <<16| g <<8| b);
		}	
	
	DataBufferInt dataBuffer = new DataBufferInt(tableau,tableau.length);
 		
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

