// EchoServer.java
// Ce fichier contient du matériel soutenant la section 3.7 du manuel :
// "Ingénierie logicielle orientée objet" et est publié sous la licence open-source
// trouvée sur www.lloseng.com

package edu.seg2105.edu.server.backend;

import com.lloseng.ocsf.server.*;
import java.io.*;
import edu.seg2105.client.common.ChatIF;

/**
 * Cette classe remplace certaines des méthodes dans la superclasse abstraite
 * pour donner plus de fonctionnalités au serveur.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani
 * @author François Bélanger
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Variables de classe *************************************************

  /**
   * Le port par défaut pour écouter.
   */
  final public static int DEFAULT_PORT = 5555;

  // Variables d'instance **********************************************

  /**
   * La variable de type interface. Elle permet l'implémentation de
   * la méthode display() dans le serveur.
   */
  ChatIF serverUI;

  // Constructeurs ****************************************************

  /**
   * Construit une instance du serveur d'écho.
   *
   * @param port Le numéro de port pour se connecter.
   */
  public EchoServer(int port) {
    super(port);
  }

  /**
   * Construit une instance du serveur d'écho avec une interface utilisateur.
   *
   * @param port     Le numéro de port pour se connecter.
   * @param serverUI L'interface pour les messages du serveur.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  // Méthodes d'instance ************************************************

  public void handleMessageFromServerUI(String message) {
    if (message.startsWith("#")) {
      // Gérer les commandes
      String[] tokens = message.split(" ");
      String command = tokens[0];

      switch (command) {
        case "#quit":
          try {
            close();
          } catch (IOException e) {
            serverUI.display("Erreur lors de la fermeture du serveur.");
          }
          System.exit(0);
          break;
        case "#stop":
          stopListening();
          serverUI.display("Le serveur a cessé d'écouter les connexions.");
          break;
        case "#close":
          try {
            sendToAllClients("Le serveur est en train de s'arrêter.");
            close();
          } catch (IOException e) {
            serverUI.display("Erreur lors de la fermeture du serveur.");
          }
          break;
        case "#setport":
          if (!isListening() && getNumberOfClients() == 0) {
            if (tokens.length >= 2) {
              try {
                int port = Integer.parseInt(tokens[1]);
                setPort(port);
                serverUI.display("Port défini sur " + getPort());
              } catch (NumberFormatException e) {
                serverUI.display("Numéro de port invalide.");
              }
            } else {
              serverUI.display("Usage : #setport <port>");
            }
          } else {
            serverUI.display("Impossible de définir le port pendant que le serveur est ouvert.");
          }
          break;
        case "#start":
          if (!isListening()) {
            try {
              listen();
              serverUI.display("Le serveur écoute les connexions sur le port " + getPort());
            } catch (IOException e) {
              serverUI.display("Erreur lors du démarrage du serveur.");
            }
          } else {
            serverUI.display("Le serveur écoute déjà.");
          }
          break;
        case "#getport":
          serverUI.display("Port actuel : " + getPort());
          break;
        default:
          serverUI.display("Commande inconnue.");
          break;
      }
    } else {
      // Diffuser le message à tous les clients
      String serverMessage = "SERVER MSG> " + message;
      serverUI.display(serverMessage);
      sendToAllClients(serverMessage);
    }
  }

  @Override
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String message = msg.toString();

    if (message.startsWith("#login ")) {
      String loginID = message.substring(7).trim();
      if (client.getInfo("loginID") == null) {
        client.setInfo("loginID", loginID);
        System.out.println(loginID + " s'est connecté.");
        try {
          client.sendToClient("Vous vous êtes connecté en tant que " + loginID);
          sendToAllClients(loginID + " s'est connecté.");
        } catch (IOException e) {
          System.out.println("Erreur lors de l'envoi de la confirmation de connexion au client.");
        }
      } else {
        try {
          client.sendToClient("Erreur : Déjà connecté.");
          client.close();
        } catch (IOException e) {
          System.out.println("Erreur lors de la fermeture de la connexion du client.");
        }
      }
    } else {
      if (client.getInfo("loginID") == null) {
        try {
          client.sendToClient("Erreur : Vous devez d'abord vous connecter.");
          client.close();
        } catch (IOException e) {
          System.out.println("Erreur lors de la fermeture de la connexion du client.");
        }
      } else {
        String loginID = (String) client.getInfo("loginID");
        System.out.println("Message reçu : " + message + " de " + loginID);
        this.sendToAllClients(loginID + "> " + message);
      }
    }
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Un nouveau client s'est connecté au serveur.");
  }

  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    String loginID = (String) client.getInfo("loginID");
    System.out.println(loginID + " s'est déconnecté.");
  }

  @Override
  protected void serverStarted() {
    System.out.println("Le serveur écoute les connexions sur le port " + getPort());
  }

  @Override
  protected void serverStopped() {
    System.out.println("Le serveur a cessé d'écouter les connexions.");
  }
}
// Fin de la classe EchoServer
