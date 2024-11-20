

package edu.seg2105.client.backend;




import com.lloseng.ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.ChatIF;

/**
 * Cette classe remplace certaines des méthodes définies dans la classe abstraite
 * pour donner plus de fonctionnalités au client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani
 * @author François Bélanger
 */
public class ChatClient extends AbstractClient {
  // Variables d'instance **********************************************

  /**
   * La variable de type interface. Elle permet l'implémentation de
   * la méthode display() dans le client.
   */
  ChatIF clientUI;

  /**
   * L'ID de connexion du client.
   */
  private String loginID;

  // Constructeurs ****************************************************

  /**
   * Construit une instance du client de chat.
   *
   * @param loginID  L'ID de connexion du client.
   * @param host     Le serveur pour se connecter.
   * @param port     Le numéro de port pour se connecter.
   * @param clientUI La variable de type interface.
   */
  public ChatClient(String loginID, String host, int port, ChatIF clientUI)
      throws IOException {
    super(host, port); // Appeler le constructeur de la superclasse
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  // Méthodes d'instance ************************************************

  /**
   * Cette méthode gère toutes les données qui arrivent du serveur.
   *
   * @param msg Le message du serveur.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());
  }

  /**
   * Cette méthode gère toutes les données provenant de l'interface utilisateur.
   *
   * @param message Le message de l'interface utilisateur.
   */
  public void handleMessageFromClientUI(String message) {
    if (message.startsWith("#")) {
      // Gérer les commandes
      String[] tokens = message.split(" ");
      String command = tokens[0];

      switch (command) {
        case "#quit":
          quit();
          break;
        case "#logoff":
          try {
            closeConnection();
            clientUI.display("Connexion fermée.");
          } catch (IOException e) {
            clientUI.display("Erreur lors de la fermeture de la connexion.");
          }
          break;
        case "#sethost":
          if (!isConnected()) {
            if (tokens.length >= 2) {
              setHost(tokens[1]);
              clientUI.display("Hôte défini sur " + getHost());
            } else {
              clientUI.display("Usage : #sethost <host>");
            }
          } else {
            clientUI.display("Impossible de définir l'hôte pendant la connexion.");
          }
          break;
        case "#setport":
          if (!isConnected()) {
            if (tokens.length >= 2) {
              try {
                int port = Integer.parseInt(tokens[1]);
                setPort(port);
                clientUI.display("Port défini sur " + getPort());
              } catch (NumberFormatException e) {
                clientUI.display("Numéro de port invalide.");
              }
            } else {
              clientUI.display("Usage : #setport <port>");
            }
          } else {
            clientUI.display("Impossible de définir le port pendant la connexion.");
          }
          break;
        case "#login":
          if (!isConnected()) {
            try {
              openConnection();
            } catch (IOException e) {
              clientUI.display("Erreur de connexion au serveur.");
            }
          } else {
            clientUI.display("Déjà connecté.");
          }
          break;
        case "#gethost":
          clientUI.display("Hôte actuel : " + getHost());
          break;
        case "#getport":
          clientUI.display("Port actuel : " + getPort());
          break;
        default:
          clientUI.display("Commande inconnue.");
          break;
      }
    } else {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display("Impossible d'envoyer le message au serveur. Fin du client.");
        quit();
      }
    }
  }

  @Override
  protected void connectionClosed() {
    clientUI.display("Connexion au serveur fermée.");
  }

  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("Le serveur a été arrêté. Connexion fermée.");
    System.exit(0);
  }

  @Override
  protected void connectionEstablished() {
    try {
      sendToServer("#login " + loginID);
    } catch (IOException e) {
      clientUI.display("Erreur lors de l'envoi de l'ID de connexion au serveur.");
    }
  }

  /**
   * Cette méthode termine le client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }
}
// Fin de la classe ChatClient
