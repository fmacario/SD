/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fm
 */
public class MainBroker {
    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
        Socket socket = new Socket("localhost", 12345);
        //ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "summonHorsesToPaddock");
        json.put("nRace", "2");
        Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
        hashHorsesAgile.put(1, 10);
        hashHorsesAgile.put(2, 20);
        
        json.put("hash", hashHorsesAgile.toString());
        
        System.out.println(json.get("hash"));
        
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject( json.toString() );
        out.flush();
        
        
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        String fromServer = in.readLine();
        System.out.println(fromServer);
        
    }
}
