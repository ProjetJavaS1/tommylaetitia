import mam3.ipa.Projet.*;
import java.util.Scanner;

public class Test2{
	public static void main(String[] args){
		Scanner scanner=new Scanner(System.in);
		TraitementImage i;
		DonneesFichier donnee;
		if(args.length==0){
			System.out.println("Quelle image voulez-vous modifier ?");
			String choix1=scanner.nextLine();
			donnee=new DonneesFichier(choix1);
		}
		else{
			donnee=new DonneesFichier(args[0]);
		}

		if(donnee.getType()=="gris"){
			i=new ImageNiveauGris(donnee.getPath(), donnee.getImgHeight(),donnee.getImgWidth(),donnee.getTableauByte());
		}
		else {
			i=new ImageCouleur(donnee.getPath(), donnee.getImgHeight(),donnee.getImgWidth(),donnee.getTableauInt());
		}

		if(args.length==0){

			boolean envie=true;
			while(envie==true){
				System.out.println(" ");
				System.out.println("-----------------------");
				System.out.println("         Menu          ");
				System.out.println("-----------------------");

				System.out.println("1.Analyse");
				System.out.println("2.assombrissement");
				System.out.println("3.eclairage");
				System.out.println("4.Matrice_Flou3x3");
				System.out.println("5.Matrice_Flou5x5");
				System.out.println("6.Matrice_Edge");
				if(donnee.getType()=="couleur"){
					System.out.println("7.GrayScale");
				}

				System.out.println(" ");
				System.out.println("Voici les différents traitement disponible, lequel voulez-vous choisir ?(insérer le chiffre)");
				String choix=scanner.nextLine();

				if(choix.equals("1")){
					i.constructionFichier();
					System.out.println("Votre analyse de fichier à été faite");
				}
				else if(choix.equals("2")){
					i.assombrissement();
					System.out.println("Un assombrissement à été fait");
				}
				else if(choix.equals("3")){
					i.eclairage();
					System.out.println("Un eclairage à été fait");
				}
				else if(choix.equals("4")){
					try{
						i.matriceConv("Flou3x3.csv");
						System.out.println("Vous avez appliquée la matrice Flou3x3.csv sur votre image");
					}
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(choix.equals("5")){
					try{
						i.matriceConv("Flou5x5.csv");
						System.out.println("Vous avez appliquée la matrice Flou5x5.csv sur votre image");
					}
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(choix.equals("6")){
					try{
						i.matriceConv("Edge.csv");
						System.out.println("Vous avez appliquée la matrice Edge.csv sur votre image");
					}
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}

				else if(choix.equals("7")){
					i.imageGrayScale();
					System.out.println("Votre image est maintenant en noir et blanc");
				}

				else{
					System.out.println("Le traitement numéro "+choix+" n'est pas disponible");
				}

				System.out.println(" ");
				System.out.println("Voulez-vous choisir un autre traitement ?(oui/non)");
				String choix2="entree";
				while(!choix2.equals("Oui")&& !choix2.equals("oui") && !choix2.equals("Non")&& !choix2.equals("non")){

					choix2 =scanner.nextLine();

					if(choix2.equals("oui") || choix2.equals("Oui")){
						envie=true;
					}
					else if(choix2.equals("non") || choix2.equals("Non")){
						envie=false;
					}
					else{
						System.out.println("Vous devez répondre oui ou non");
					}
				}
			}

			System.out.println(" ");
			System.out.println("Voulez-vous enregistrer cette image ?(Oui/Non)");
			System.out.println("Si non vous quitterez la page");
			String choix3 = "entree";
			while(!choix3.equals("Oui")&& !choix3.equals("oui") && !choix3.equals("Non")&& !choix3.equals("non")){

				choix3 =scanner.nextLine();

				if(choix3.equals("Oui")||choix3.equals("oui")){
					i.miseEnFichier();
					System.out.println(" ");
					System.out.println("Votre image a été enregistrée");
				}
				else if(choix3.equals("Non")||choix3.equals("non")){
					System.exit(0);
				}
				else{
					System.out.println("Vous devez répondre oui ou non");
				}
			}
		}
		else{

			System.out.println("Vous avez choisi de modifier l'image "+String.valueOf(args[0]));
			for(int j=1;j<args.length;j++){
				String[] tokens=args[j].split("\\.");
						
				if(args[j].equals("Analyse") || args[j].equals("analyse")){
					i.constructionFichier();
					System.out.println("Votre analyse de fichier à été faite");
				}
				else if(args[j].equals("Assombrissement")|| args[j].equals("assombrissement")){
					i.assombrissement();
					System.out.println("Un assombrissement à été fait");
				}
				else if(args[j].equals("Eclairage") || args[j].equals("éclairage")){
					i.eclairage();
					System.out.println("Un eclairage à été fait");
				}
				else if(args[j].equals(tokens[0]+".csv")){
					try{
						i.matriceConv(args[j]);
						System.out.println("Vous avez appliquée la matrice "+String.valueOf(args[j])+" sur votre image");
					}
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(args[j].equals("Grayscale") || args[j].equals("grayscale")){
					i.imageGrayScale();
					if(donnee.getType()=="couleur"){
						System.out.println("Votre image est maintenant en noir et blanc");
					}
				}
				else{
					System.out.println("Le traitement numéro "+j+" n'est pas disponible");
				}
			}
			System.out.println(" ");
			System.out.println("Voulez-vous enregistrer cette image ?(Oui/Non)");
			System.out.println("Si non vous quitterez la page");
			String choix = "entrée";
			while(!choix.equals("Oui")&& !choix.equals("oui") && !choix.equals("Non")&& !choix.equals("non")){
				choix = scanner.nextLine();

				if(choix.equals("Oui")||choix.equals("oui")){
					i.miseEnFichier();
					System.out.println(" ");
					System.out.println("Votre image a été enregistrée");
				}
				else if(choix.equals("Non")||choix.equals("non")){
					System.exit(0);
				}
				else{
					System.out.println("Vous devez répondre oui ou non");
				}
			}			
		}
	}
}
