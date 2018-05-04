/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_ControlCentre;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    ControlCentre controlCentre;
    Socket socket;
    
    MyRunnable( ControlCentre controlCentre, Socket socket) {
        this.controlCentre = controlCentre;
        this.socket = socket;
    }

    @Override
    public void run() {
        JSONObject json = null;
        JSONObject jsonRes;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("entidade") ){
                case "broker":
                    ArrayList<Integer> horsesWinnersList;
                    Map<Integer, List<Integer>> mapSpec_Horse_Bet;
                    switch ( json.getString("metodo") ){     
                        case "reportResults":                            
                            horsesWinnersList = JSON.stringToArrayList(json.getString("horsesWinnersList") );
                            mapSpec_Horse_Bet = JSON.stringToMapList(json.getString("mapSpec_Horse_Bet") );
                            
                            ArrayList<Integer> reportResults = controlCentre.reportResults(horsesWinnersList, mapSpec_Horse_Bet);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", reportResults.toString());
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "areThereAnyWinners":
                            mapSpec_Horse_Bet = JSON.stringToMapList(json.getString("mapSpec_Horse_Bet") );
                            boolean b = controlCentre.areThereAnyWinners(mapSpec_Horse_Bet);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", b);
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "entertainTheGuests":
                            System.out.println("entertainTheGuests - msg recebida");
                            controlCentre.entertainTheGuests();
                            System.out.println("entertainTheGuests - msg recebida e sai da funcao");
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "end":
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            System.exit(1);
                            break;
                    }
                    break;
                    
                case "spectator":
                    int spectatorID;
                    switch ( json.getString("metodo") ){     
                        case "goWatchTheRace":
                            spectatorID = json.getInt("spectatorID");
                            
                            controlCentre.goWatchTheRace(spectatorID);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "haveIWon":
                            spectatorID = json.getInt("spectatorID");
                            
                            boolean b = controlCentre.haveIWon(spectatorID);
                            System.out.println("B: _       ___ " + b);
                            jsonRes = new JSONObject();
                            jsonRes.put("return", b);
                            System.out.println(jsonRes.toString());
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "relaxABit":
                            
                            
                            spectatorID = json.getInt("spectatorID");
                            System.out.println("relaxABit " +spectatorID);
                            controlCentre.relaxABit(spectatorID);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                    }
                    break;
            }

            
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
