/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSON;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fm
 */
public class JSON {
    public static JSONObject receiveJSON( Socket socket ) throws IOException, JSONException, ClassNotFoundException {
        String s = null;
        JSONObject jsonObject = null;
        try{
        InputStream in = socket.getInputStream();
        
        ObjectInputStream i = new ObjectInputStream(in);
        
        
        s = (String) i.readObject();
        }catch(EOFException e){
            
        }
        try{
            jsonObject = new JSONObject(s);
        }catch(NullPointerException e){
            
        }
        return jsonObject;
        }
    
    public static Map<Integer, Integer> stringToMap( String s ){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        
        s = s.substring(1, s.length()-1);
        String parts[] = s.split(", ");
        for (String part : parts) {
            String[] split = part.split("=");
            map.put( Integer.parseInt(split[0]), Integer.parseInt(split[1]) );
        }
        return map;
    }
    
    public static Map<Integer, List<Integer>> stringToMapList( String s ){
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        
        s = s.substring(1, s.length()-2);
        String parts[] = s.split("], ");
        
        for (String part : parts) {
            String[] split = part.split("=");
            
            split[1] = split[1].substring(1);
            
            String[] splitLista = split[1].split(", ");
            
            List<Integer> lista = new ArrayList<Integer>();
            lista.add(0, Integer.parseInt(splitLista[0]));
            lista.add(1, Integer.parseInt(splitLista[1]));
            
            map.put(Integer.parseInt(split[0]), lista);
        }
        
        return map;
    }
            
    
    
    public static ArrayList<Integer> stringToArrayList ( String s ){
        ArrayList<Integer> arrayList = new ArrayList<>();
        
        s = s.substring(1, s.length()-1);
        String parts[] = s.split(", ");
        for (String part : parts) {
            String[] split = part.split("=");
            arrayList.add( Integer.parseInt(split[0]));
        }
        return arrayList;
    }
    
    
    public static void sendMessage( Socket socket, JSONObject json ) throws IOException{
        System.out.println(json.toString());
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);

        o.writeObject( json.toString() );
        out.flush();
        
    }
    
    
}
