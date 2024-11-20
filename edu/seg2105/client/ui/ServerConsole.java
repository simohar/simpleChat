// ServerConsole.java
package edu.seg2105.client.ui;

// Ce fichier contient du matériel soutenant la section 3.7 du manuel :
// "Ingénierie logicielle orientée objet" et est publié sous la licence open-source
// trouvée sur www.lloseng.com

import java.io.*;
import java.util.Scanner;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;

/**
 * Cette classe construit l'interface utilisateur pour un serveur de chat.
 * Elle implémente l'interface ChatIF pour activer la méthode display().
 * Avertissement : Une partie du code ici est clonée dans ClientConsole.
 *
 * @author ...
 */
public class ServerConsole implements ChatIF {
  // Variables de classe *************************************************

  /**
   * Le port par défaut pour écouter.
   */
  final public static int DEFAULT_PORT = 5555;

  // Variables d'instance **********************************************

  /**
   * L'instance du serveur qui a créé ce ServerConsole.
   */
  EchoServer server;

  /**
   * Scanner pour lire depuis la console.
   */
  Scanner fromConsole;

  // Constructeurs ****************************************************

  /**
   * Construit une instance de l'interface utilisateur ServerConsole.
   *
   * @param port Le port pour se connecter.
   */
  public ServerConsole(int port) {
    server = new EchoServer(port, this);

    // Créer un objet scanner pour lire depuis la console
    fromConsole = new Scanner(System.in);
  }

  // Méthodes d'instance ************************************************

  /**
   * Cette méthode attend une entrée de la console. Une fois reçue,
   * elle l'envoie au gestionnaire de messages du serveur.
   */
  public void accept() {
    try {
      String message;

      while (true) {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);
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
    System.out.println(message);
  }

  // Méthodes de classe ***************************************************

  /**
   * Cette méthode est responsable de la création de l'interface serveur.
   *
   * @param args Arguments de la ligne de commande : [port]
   */
  public static void main(String[] args) {
    int port = DEFAULT_PORT;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Throwable t) {
      // Utiliser le port par défaut
    }

    ServerConsole serverConsole = new ServerConsole(port);

    try {
      serverConsole.server.listen();
    } catch (IOException e) {
      System.out.println("Erreur : Impossible d'écouter les clients !");
    }

    serverConsole.accept(); // Attendre les données de la console
  }
}
// Fin de la classe ServerConsole
