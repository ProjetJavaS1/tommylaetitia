import java.util.Scanner;

public class InterfaceUtilisateur{

	public static void main(String[] args) {

    	Scanner utilisateur = new Scanner(System.in);  
    	//crée un scanner par-dessus un flux
    	

    	// on demande à l'utilisateur ce qu'il veut passer en argument
    	try{
    	System.out.println("Savez vous quel fichier vous voulez traiter  (TRUE ou FALSE) ?");
    	Boolean fichier = utilisateur.nextBoolean();
    	}catch (IOE Exception e){
    		System.out.println("repondez un boolean");
    	}

    	if (fichier == TRUE){
    		// demander le fichier à traiter 
    		try{

    		System.out.println("Quel est le fichier à traiter");

    		String nomFichier = utilisateur.nextLine();  //lire le string 
    		System.out.println("Nous traitons le fichier demandé: " + nomFichier); 

    		}catch  (IOE Exception e0){
    			System.out.println ("rentrez un nom de fichier valide");
    		}
    	

    		// on veut savoir quel constructeur appeler
    		try{

    		System.out.println("Savez vous quel traitement vous voulez (TRUE ou FALSE) ?");
    		boolean traitement = utilisateur.nextBoolean();

    		}catch  (IOE Exception e1){
    			System.out.println ("traitement non reconnu");
    		}

    		try{
    		System.out.println("Savez vous quelle analyse vous voulez (TRUE ou FALSE)?");
    		boolean analyse = utilisateur.nextBoolean();

    		}catch  (IOE Exception e3){
    			System.out.println ("analyse non reconnue");
    		}

    		if ( traitement== TRUE && analyse == TRUE ){
    			// on appelle le constructeur avec 3 arguments
    			//APPEL  
    			System.out.println("Nous traitons le fichier: " + nomFichier+ "avec l'analyse" + analyse+" et le traitement"+ traitement);
    		}

    		if ( traitement== FALSE && analyse == TRUE ){
    			// on appelle le constructeur avec 2 arguments 
    			// le nom du fichier et l'analyse 
    			//APPEL 
    			System.out.println("Nous traitons le fichier: " + nomFichier+ "avec l'analyse" + analyse+);
    		}

    		if ( traitement== TRUE && analyse == FALSE ){
    			// on appelle le constructeur avec 2 arguments 
    			// le nom du fichier et le traitement 
    			//APPEL 
    			System.out.println("Nous traitons le fichier: " + nomFichier+ " avec le traitement"+ traitement);
    		}

    	}
    	else{
    	// on considère que si l'utilisateur ne donne pas de nom de fichiers en argument, on lance le menu utilisateur. 

    		// ouvrir menu utilisateur
    		// on doit entrer un nom de fichier 
    		// et choisir un traitement ou une analyse 
    		// et pouvoir sortir


    	}

    	utilisateur.close();
	}
}
