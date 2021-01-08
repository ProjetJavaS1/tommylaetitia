import mam3.ipa.Projet.*;
import java.util.Scanner;

public class Test2{
	public static void main(String[] args){
		Scanner scanner=new Scanner(System.in);
		TraitementImage i;
		DonneesFichier donnee;
		
		// si l'utilisateur ne rentre aucun argument, on lui demande qu'elle image il veut modifier
		
		if(args.length==0){
			System.out.println("Quelle image voulez-vous modifier ?");
			String choix1=scanner.nextLine();
			donnee=new DonneesFichier(choix1);
		}
		// sinon, on considère que son premier argument est le nom de l'image
		else{
			donnee=new DonneesFichier(args[0]);
		}
		
		// on analyse son image passée en argument pour savoir si c'est une image en couleur ou en niveau de gris
		// afin de savoir quels traitements on pourra proposer et quelle classe on doit utilisée
		if(donnee.getType()=="gris"){ 
			i=new ImageNiveauGris(donnee.getPath(), donnee.getImgHeight(),donnee.getImgWidth(),donnee.getTableauByte());
		}
		else {
			i=new ImageCouleur(donnee.getPath(), donnee.getImgHeight(),donnee.getImgWidth(),donnee.getTableauInt());
		}
		// selon si c'est une image en couleur ou en niveaux de gris, on affiche un menu avec les différents traitements disponibles
		if(args.length==0){

			boolean envie=true;
			while(envie==true){
				// affichage du menu 
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
				// si l'image est en couleur on a un traitement de plus, celui de passer l'image en niveau de gris
				if(donnee.getType()=="couleur"){
					System.out.println("7.GrayScale");
				}

				System.out.println(" ");
				System.out.println("Voici les différents traitement disponible, lequel voulez-vous choisir ?(insérer le chiffre)");
				String choix=scanner.nextLine(); // on prend en compte le chiffre entrer par l'utilisateur 
				
				// on associe son chiffre au traitement correspondant et on l'applique
				// de plus, une phrase confirme le traitement en cours
				
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
					// si  l'utilisateur ne rentre pas une matrice de la bonne taille, le terminale lui affiche l'erreur
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(choix.equals("5")){
					try{
						i.matriceConv("Flou5x5.csv");
						System.out.println("Vous avez appliquée la matrice Flou5x5.csv sur votre image");
					}
					// si la matrice n'a pas la bonne taille, le terminale affiche l'erreur
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(choix.equals("6")){
					try{
						i.matriceConv("Edge.csv");
						System.out.println("Vous avez appliquée la matrice Edge.csv sur votre image");
					}
					// si la matrice n'a pas la bonne taille, le terminale affiche l'erreur
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}

				else if(choix.equals("7")){
					i.imageGrayScale();
					System.out.println("Votre image est maintenant en noir et blanc");
				}
				// s'il écrit un chiffre qui ne correpond à aucun traitement disponible, le terminale le lui indique
				else{
					System.out.println("Le traitement numéro "+choix+" n'est pas disponible");
				}
				
				System.out.println(" ");
				System.out.println("Voulez-vous choisir un autre traitement ?(oui/non)");
				// une fois le traitement fait, il peut en appliquer un autre dans ce cas le menu se re affiche
				// si l'utilisateur répond non le programme se ferme
				String choix2="entree";
				while(!choix2.equals("Oui")&& !choix2.equals("oui") && !choix2.equals("Non")&& !choix2.equals("non")){

					choix2 =scanner.nextLine();

					if(choix2.equals("oui") || choix2.equals("Oui")){
						// on prend en compte le fait que l'utilisateur peut mettre des majuscule
						envie=true;
					}
					else if(choix2.equals("non") || choix2.equals("Non")){
						// on prend en compte le fait que l'utilisateur peut mettre des majuscule
						envie=false;
					}
					else{
						System.out.println("Vous devez répondre oui ou non");
					}
				}
			}
			// une fois que l'utilisateur a fait tous les traitements qui voulait, on lui propose d'enregistrer l'image
			
			System.out.println(" ");
			System.out.println("Voulez-vous enregistrer cette image ?(Oui/Non)");
			System.out.println("Si non vous quitterez la page");
			String choix3 = "entree";
			while(!choix3.equals("Oui")&& !choix3.equals("oui") && !choix3.equals("Non")&& !choix3.equals("non")){

				choix3 =scanner.nextLine();

				if(choix3.equals("Oui")||choix3.equals("oui")){
					// on prend en compte le fait que l'utilisateur peut mettre des majuscules
					i.miseEnFichier();
					System.out.println(" ");
					System.out.println("Votre image a été enregistrée");
				}
				// s'il dit non, le programme s'arrête 
				else if(choix3.equals("Non")||choix3.equals("non")){
					// on prend en compte le fait que l'utilisateur peut mettre des majuscule
					System.exit(0);
				}
				// si l'utilisateur répond autre chose que oui ou non, le terminal lui indique qu'il faut répondre par oui ou par non
				else{
					System.out.println("Vous devez répondre oui ou non");
				}
			}
		}
		// si l'utilisateur avait passé des arguments, on applique à l'image passée en premier argument les traitements demandés
		// de plus, une phrase confirme le traitement en cours
		else{

			System.out.println("Vous avez choisi de modifier l'image "+String.valueOf(args[0]));
			for(int j=1;j<args.length;j++){
				String[] tokens=args[j].split("\\.");
						
				if(args[j].equals("Analyse") || args[j].equals("analyse")){ 
					// on prend en compte le fait que l'utilisateur peut mettre des majuscules
					i.constructionFichier();
					System.out.println("Votre analyse de fichier à été faite");
				}
				else if(args[j].equals("Assombrissement")|| args[j].equals("assombrissement")){ 
					// on prend en compte le fait que l'utilisateur peut mettre des majuscules
					i.assombrissement();
					System.out.println("Un assombrissement à été fait");
				}
				else if(args[j].equals("Eclairage") || args[j].equals("éclairage")){
					// on prend en compte le fait que l'utilisateur peut mettre des majuscules
					i.eclairage();
					System.out.println("Un eclairage à été fait");
				}
				// on peut également passer une nouvelle matrice en argument
				else if(args[j].equals(tokens[0]+".csv")){
					try{
						i.matriceConv(args[j]);
						System.out.println("Vous avez appliquée la matrice "+String.valueOf(args[j])+" sur votre image");
					}
					// si  l'utilisateur ne rentre pas une matrice de la bonne taille, le terminale lui affiche l'erreur
					// il doit donner une matrice 3x3 ou 5x5 
					catch(BadMatriceException e){System.out.println("La matrice n'est pas conforme");}
				}
				else if(args[j].equals("Grayscale") || args[j].equals("grayscale")){
					i.imageGrayScale();
					if(donnee.getType()=="couleur"){
						System.out.println("Votre image est maintenant en noir et blanc");
					}
				}
				// si le traitement demandé en argument n'existe pas, on l'affiche dans le terminal
				else{
					System.out.println("Le traitement numéro "+j+" n'est pas disponible");
				}
			}
			// on finit toujours en proposant d'enregistrer l'image 
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
				// si l'utilisateur dit non, le programme se ferme
				else if(choix.equals("Non")||choix.equals("non")){
					System.exit(0);
				}
				// si il répond autre chose que oui ou non, on affiche un message d'erreur
				else{
					System.out.println("Vous devez répondre oui ou non"); 
				}
			}			
		}
	}
}
