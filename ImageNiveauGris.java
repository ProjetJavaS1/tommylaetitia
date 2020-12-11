package mam3.ipa.Projet;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import javax.media.jai.RasterFactory;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.BufferedWriter;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.lang.Math;
import java.awt.Point;

public class ImageNiveauGris{
	private byte[] tab;
	private int img_width;
	private int img_height;
	private Path p;
	
	public ImageNiveauGris(String fn){
		RenderedOp ropimage; //Objet contenant les META-DONNÉES
		ropimage = JAI.create("fileload", fn); // Ouverture du fichier
		BufferedImage bi = ropimage.getAsBufferedImage(); // BufferedImage contiendra les données
		img_width = ropimage.getWidth(); // largeur de l'image 
		img_height = ropimage.getHeight(); // hauteur de l'image
	 	// On commance par avoir le mode de color
	  	ColorModel cm = ropimage.getColorModel();
	  	// On regarde s'il s'agit d'une image en niveau de gris 
	  	 if (bi.getType() == BufferedImage.TYPE_BYTE_GRAY && cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY) {
	  	// On utilise l'objet Raster pour avoir les données de l'image 
	  	//On ne peut pas le faire à partir du buffer
	  	 Raster r = ropimage.getData();
	  	// On récupère un DataBufferByte
	  	 DataBufferByte db = (DataBufferByte)(r.getDataBuffer());
	  	 // Puis on construit le tableau d'octets
	  	 tab = db.getData();

	  	}

	  	p=Paths.get(fn);
		if(!Files.exists(p)){
			p=null;
			System.out.print("Ce fichier n'existe pas");
		}	
	}

	public void constructionFichier(){
		//On créé le fichier de sortie 
		String[] tokens=p.getFileName().toString().split("\\.");
		String s2=tokens[0]+"-h.csv";

		Path p2=Paths.get(s2); //On créé le chemin vers ce fichier
		int[][] r = new int[256][2]; //On construit un tableau d'entiers qui nous permettra de tracer l'histogramme

		if(Files.isDirectory(p2)){ //On vérifie si le fichier est un répertoire existant 
					System.out.println("Le fichier est un répertoire existant");
		}


		try(BufferedWriter w=Files.newBufferedWriter(p2,StandardOpenOption.CREATE, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING)){

			for(int i=0;i<=255;i++){ 
				r[i][0]=i; //On construit l'abscisse de l'histogramme
				int compt=0;
				byte res=(byte)i;
				for(int j=0;j<tab.length;j++){
					if(tab[j]==res){
						compt+=1; //Compte combien de fois on a zéro, un...
					}
				}
				r[i][1]=compt; //Ajoute au tableau le résultat du compteur
				w.write(String.valueOf(r[i][0])+";");
				w.write(String.valueOf(r[i][1]));
				w.newLine();
			}
		}
		catch(IOException e){System.out.println("Problème d'exception : "+e);}
	}

	public void assombrissement(){
		int n=tab.length;
		for(int i=0;i<n;i++){
			int entier=(int)(0xFF & tab[i]);
			tab[i]=(byte)(Math.floor((entier*entier)/255));
		}


		// Start by defining the type of image we want to save
		// let's create a model of type TYPE_BYTE, to store an image of
 		// IMG_WIDTH et IMG_HEIGHT dimensions. Last argument is the number of bands
 		// for grayscale = 1 band (no surprises! gray-scale needs only one channel)
 		SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,img_width,img_height,1);
  		// The same way we got the image data in a BufferedImage when opening a file
 		// we now need to create a BufferedImage where to put the image data
 		// The BufferedImage object will be an image of IMG_WIDTH and IMG_HEIGHT
 		// dimension. It's also a TYPE_BYTE_GRAY image
 		BufferedImage image = new BufferedImage(img_width, img_height, BufferedImage.TYPE_BYTE_GRAY);
 		// let's put the data from b (declared as "byte[] b;")
  		image.setData(Raster.createRaster(sm, new DataBufferByte(tab, tab.length), new Point()));
 		// and save it in a file!
 		// here, the image will be called "res-gs.png"
  		JAI.create("filestore",image,"image.png","PNG");
	}

	public void eclairage(){
		int n=tab.length;
		for(int i=0;i<n;i++){
			int entier=(int)(0xFF & tab[i]);
			tab[i]=(byte)(Math.sqrt(entier)*Math.sqrt(255));
		}

		// Start by defining the type of image we want to save
		// let's create a model of type TYPE_BYTE, to store an image of
 		// IMG_WIDTH et IMG_HEIGHT dimensions. Last argument is the number of bands
 		// for grayscale = 1 band (no surprises! gray-scale needs only one channel)
 		SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,img_width,img_height,1);
  		// The same way we got the image data in a BufferedImage when opening a file
 		// we now need to create a BufferedImage where to put the image data
 		// The BufferedImage object will be an image of IMG_WIDTH and IMG_HEIGHT
 		// dimension. It's also a TYPE_BYTE_GRAY image
 		BufferedImage image = new BufferedImage(img_width, img_height, BufferedImage.TYPE_BYTE_GRAY);
 		// let's put the data from b (declared as "byte[] b;")
  		image.setData(Raster.createRaster(sm, new DataBufferByte(tab, tab.length), new Point()));
 		// and save it in a file!
 		// here, the image will be called "res-gs.png"
  		JAI.create("filestore",image,"image.png","PNG");	
	}


}
