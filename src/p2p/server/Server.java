/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.server;

import java.util.LinkedList;
import javax.sip.InvalidArgumentException;
import p2p.model.MessageProcessor;
import p2p.model.SipLayer;

/**
 *
 * @author Jorge
 */
public class Server implements MessageProcessor {

    private SipLayer sipLayer;
    private LinkedList<String> clients;

    public Server(SipLayer sipLayer) {
        this.sipLayer = sipLayer;
        clients = new LinkedList<>();
    }

    @Override
    public void processMessage(String sender, String message) {
        String username = sender.substring(sender.indexOf(":") + 1, sender.indexOf("@"));
        String address = sender.substring(sender.indexOf("@") + 1);
        //TODO find ip y port
        String ipCliente = address.substring(0, address.indexOf(":"));
        String port = address.substring(address.indexOf(":") + 1, address.length() - 1);
        String strList = "*";
        try {
            if (message.equals("connected")) {
                System.out.println(" Está conectado " + ipCliente + " Por puerto: " + port);
                this.clients.add("sip:" + username +"@" +ipCliente + ":" + port);
                for (String str : this.clients) {
                    strList += str + " ";
                }
                for (String str : this.clients) {
                    sipLayer.sendMessage(str, strList);
                }
            } else if (message.equals("disconnected")) {
                System.out.println(" Se ha desconectado " + ipCliente + " Por puerto: " + port);
                this.clients.remove("sip:" + username +"@" +ipCliente + ":" + port);
                for (String str : this.clients) {
                    strList += str + " ";
                }
                for (String str : this.clients) {
                    sipLayer.sendMessage(str, strList);
                }
            }
        } catch (Exception ex) {
            System.out.println("No se pudieron actualizar las listas");
        }

    }

    @Override
    public void processError(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    public void processInfo(String infoMessage) {
        System.out.println(infoMessage);
    }
}
