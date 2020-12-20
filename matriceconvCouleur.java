public void matriceConv(String s){

		Path chemin=Paths.get(s); //On créé le chemin vers ce fichier

		if(Files.isDirectory(chemin)){ //On vérifie si le fichier est un répertoire existant 
				System.out.println("Le fichier est un répertoire existant");
		}

		else{
			try(BufferedReader reader= Files.newBufferedReader(chemin)){

				int l=0;
				int [][] filtre=new int[5][5];
				String line=null;

				while((line = reader.readLine()) != null){
					String[] tokens = line.split(";");
					for(int j=0;j<tokens.length;j++){
						filtre[l][j]=Integer.parseInt(tokens[j]);
					}
					l=l+1;
				}

				int n=l; //Taille tableau à garder
				this.matfiltre = new int[l][l];

				for(int kl=0;kl<n;kl++){
					for(int kc=0;kc<n;kc++){
						this.matfiltre[kl][kc]=filtre[kl][kc];
					}
				}

				int norm=0;
				for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){
						norm=norm+matfiltre[i][j];
					}
				}

				if(norm==0){
					norm=1;
				}
			
				if (n==3){

					int[] nouvtab = new int[tableau.length];

					int nmat=(int)Math.sqrt(tableau.length);

					for(int i=0; i<tableau.length;i++){
						int x =(int)Math.floor(i/nmat);
						int y =i-nmat*x;
						int sommeRed=0;
						int sommeGreen=0;
						int sommeBlue=0;
						

						for(int u=-1;u<=1;u++){
							for(int v=-1;v<=1;v++){
							    if((x-u)<nmat && (x-u)>=0 && (y-v)<nmat && (y-v)>=0){
								    int redInt=(tableau[(x-u)*nmat+y-v] >> 16) & 0xFF;
									int greenInt=(tableau[(x-u)*nmat+y-v] >> 8) & 0xFF;
									int blueInt=tableau[(x-u)*nmat+y-v] & 0xFF;
									 
									sommeRed=sommeRed+(matfiltre[u+1][v+1]*redInt);
									sommeGreen=sommeGreen+(matfiltre[u+1][v+1]*greenInt);
									sommeBlue=sommeBlue+(matfiltre[u+1][v+1]*blueInt);
								}
							}
						}

						sommeRed=(int)(Math.floor(sommeRed/norm));
						int resRed=(int)(0xFF & sommeRed);

						sommeGreen=(int)(Math.floor(sommeGreen/norm));
						int resGreen=(int)(0xFF & sommeGreen);

						sommeBlue=(int)(Math.floor(sommeBlue/norm));
						int resBlue=(int)(0xFF & sommeBlue);

						nouvtab[i]= ( resRed << 16| resGreen << 8 | resBlue ) ;
					}
				}

				else if(n==5){
					int nmat=(int)Math.sqrt(tableau.length);
					for(int i=0; i<tableau.length;i++){
						int x =(int)Math.floor(i/nmat);
						int y =i-nmat*x;
						int sommeRed=0;
						int sommeGreen=0;
						int sommeBlue=0;

						for(int u=-2;u<=2;u++){
							for(int v=-2;v<=2;v++){
								if((x-u)<=n && (x-u)>=0 && (y-v)<=n &&(y-v)>=0){
									int redInt= (int)( ( tableau[(x-u)*nmat+y-v] >> 16) &0xFF) ;
									int greenInt= (int)( (tableau[(x-u)*nmat+y-v] >> 8) &0xFF ) ;
									int blueInt= (int) ( tableau[(x-u)*nmat+y-v] & 0xFF );
									sommeRed=sommeRed+(matfiltre[u+1][v+1]*redInt);
									sommeGreen=sommeGreen+(matfiltre[u+1][v+1]*greenInt);
									sommeBlue=sommeBlue+(matfiltre[u+1][v+1]*blueInt);
								}
							}
						}

						sommeRed=(int)(Math.floor(sommeRed/norm));
						int resRed=(int)(0xFF & sommeRed);

						sommeGreen=(int)(Math.floor(sommeGreen/norm));
						int resGreen=(int)(0xFF & sommeGreen);

						sommeBlue=(int)(Math.floor(sommeBlue/norm));
						int resBlue=(int)(0xFF & sommeBlue);

						nouvtab[i]= ( resRed << 16| resGreen << 8 | resBlue );
					}

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
		catch(IOException e){System.out.println("Problème d'exception : "+e);}
	}

}
