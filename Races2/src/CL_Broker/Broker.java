/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Broker;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fm
 */
public class Broker {
    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
        Socket cliente = new Socket("localhost", 12345);
        //ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        
        JSONObject json = new JSONObject();
        
        json.append("metodo", "queroIrPara");
        json.append("teste", "2");
        
        PrintStream msg = new PrintStream(cliente.getOutputStream());
        //msg.print( "OLA; ESTOU A ENVIAR MSG" );
        msg.append( json.toString() );
        
    }
}
