/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.server;
import java.util.LinkedList;
import p2p.model.MessageProcessor;
import p2p.model.SipLayer;
/**
 *
 * @author Jorge
 */
public class Server implements MessageProcessor{

    private SipLayer sipLayer;
    
    private LinkedList<SipLayer> clientes;

    public Server(SipLayer sipLayer) {
        this.sipLayer = sipLayer;
        clientes = new LinkedList<>();
    }
    
    @Override
    public void processMessage(String sender, String message) {
        String username = sender.substring(sender.indexOf(":") + 1, sender.indexOf("@"));
	String address = sender.substring(sender.indexOf("@") + 1);
        //TODO find ip y port
        String ipCliente =  address.substring(0,address.indexOf(":") );
        String port =  address.substring(address.indexOf(":")+1, address.length()-1);
        
        if(message.equals("connected")){
            System.out.println(" Está conectado "+ipCliente +" Por puerto: "+ port);
            
        }else if(message.equals("disconnected")){
            System.out.println(" Se ha desconectado "+ipCliente +" Por puerto: "+ port);
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
