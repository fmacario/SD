/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package races2;

import java.io.IOException;
import java.net.ServerSocket;
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
public class Races2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException {
        JSONObject json = new JSONObject();

        json.put("metodo", "summonHorsesToPaddock");
        json.put("nRace", "2");
        
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(0, 3);
        list1.add(1, 50);
        
        map.put(1, list1);
        
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(0, 2);
        list2.add(1, 150);
        
        map.put(2, list2);
        
        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(0, 1);
        list3.add(1, 50);
        
        map.put(3, list3);
        System.out.println(map.toString());
        System.out.println(map);
        json.put("hash", map.toString());
        
        map.clear();
        
        String s = json.getString("hash");
        
        s = s.substring(1, s.length()-2);
        System.out.println(s);
        String parts[] = s.split("], ");
        
        
        for (String part : parts) {
            System.out.println(part);
            String[] split = part.split("=");
            
            split[1] = split[1].substring(1);
            System.out.println(split[0]);
            
            String[] splitLista = split[1].split(", ");
            System.out.println(splitLista[0]);
            System.out.println(splitLista[1]);
            
            List<Integer> lista = new ArrayList<Integer>();
            lista.add(0, Integer.parseInt(splitLista[0]));
            lista.add(1, Integer.parseInt(splitLista[1]));
            
            map.put(Integer.parseInt(split[0]), lista);
        }
        System.out.println( map );
    }
    
}
