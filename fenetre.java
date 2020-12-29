package mam3.ipa.Projet; 

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.awt.event.*;

public class fenetre{

	public static void main( String[] args){
		JFrame maFenetre =  new JFrame();

		// initialisation de la fenêtre graphique 

		maFenetre.setVisible(true); // je rend ma fenêtre visible 
		maFenetre.setSize(400,400); // je défini la taille de ma fenêtre 
		maFenetre.setTitle(" Projet JAVA ");

		JPanel panel= new JPanel();
		maFenetre.setContentPane(panel); // on associe le contenu de panel à la fenêtre

		JButton button1 = new JButton ("choix1");
		maFenetre.getContentPane().add(button1);

		//j'attribue une action aux bouttons

		button1.addActionListener(new addActionListener() ){
			public void actionPerformed(ActionEvent e){

			}
		}




		


	}
}
