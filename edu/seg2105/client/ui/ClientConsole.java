// ClientConsole.java
package edu.seg2105.client.ui;

// Ce fichier contient du matériel soutenant la section 3.7 du manuel :
// "Ingénierie logicielle orientée objet" et est publié sous la licence open-source
// trouvée sur www.lloseng.com

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;

/**
 * Cette classe construit l'interface utilisateur pour un client de chat.
 * Elle implémente l'interface ChatIF pour activer la méthode display().
 * Avertissement : Une partie du code ici est clonée dans ServerConsole.
 *
 * @author François Bélanger
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani
 */
public class ClientConsole implements ChatIF {
  // Variables de classe *************************************************

  /**
   * Le port par défaut pour se connecter.
   */
  final public static int DEFAULT_PORT = 5555;

  // Variables d'instance **********************************************

  /**
   * L'instance du client qui a créé ce ClientConsole.
   */
  ChatClient client;

  /**
   * Scanner pour lire depuis la console.
   */
  Scanner fromConsole;

  // Constructeurs ****************************************************

  /**
   * Construit une instance de l'interface utilisateur ClientConsole.
   *
   * @param loginID L'ID de connexion pour le client.
   * @param host    L'hôte pour se connecter.
   * @param port    Le port pour se connecter.
   */
  public ClientConsole(String loginID, String host, int port) {
    try {
      client = new ChatClient(loginID, host, port, this);
    } catch (IOException exception) {
      System.out.println("Erreur : Impossible de configurer la connexion ! Fin du client.");
      System.exit(1);
    }

    // Créer un objet scanner pour lire depuis la console
    fromConsole = new Scanner(System.in);
  }

  // Méthodes d'instance ************************************************

  /**
   * Cette méthode attend une entrée de la console. Une fois reçue,
   * elle l'envoie au gestionnaire de messages du client.
   */
  public void accept() {
    try {
      String message;

      while (true) {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    } catch (Exception ex) {
      System.out.println("Erreur inattendue lors de la lecture depuis la console !");
    }
  }

  /**
   * Cette méthode remplace la méthode dans l'interface ChatIF.
   * Elle affiche un message à l'écran.
   *
   * @param message La chaîne à afficher.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  // Méthodes de classe ***************************************************

  /**
   * Cette méthode est responsable de la création de l'interface client.
   *
   * @param args Arguments de la ligne de commande : loginID [host [port]]
   */
  public static void main(String[] args) {
    String loginID = "";
    String host = "localhost";
    int port = DEFAULT_PORT;

    try {
      loginID = args[0];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("ERREUR - Aucun ID de connexion spécifié. Connexion annulée.");
      System.exit(1);
    }

    try {
      host = args[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      // Utiliser l'hôte par défaut
    }

    try {
      port = Integer.parseInt(args[2]);
    } catch (ArrayIndexOutOfBoundsException e) {
      // Utiliser le port par défaut
    } catch (NumberFormatException e) {
      System.out.println("Numéro de port invalide. Utilisation du port par défaut " + DEFAULT_PORT);
    }

    ClientConsole chat = new ClientConsole(loginID, host, port);
    chat.accept(); // Attendre les données de la console
  }
}
// Fin de la classe ClientConsole
