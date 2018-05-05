package SV_GRI;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import Enum.*;
import java.io.IOException;
import org.json.JSONException;

/**
 * class MyRunnable
 * @author fm
 */
public class MyRunnable implements Runnable {
    GRI gri;
    Socket socket;
    
    MyRunnable( GRI gri, Socket socket) {
        this.gri = gri;
        this.socket = socket;
    }

    /**
     * Recebe, descodifica a mensagem, e chama o método pretendido.
     */
    @Override
    public void run() {
        JSONObject json;
        JSONObject jsonRes;
        
        try {
            json = JSON.receiveJSON( socket );
            boolean end = false;
            switch ( json.getString("metodo") ){
                case "updateStatus":
                    //System.out.println("case updateStatus");
                    
                    gri.updateStatus();
                    break;
                    
                case "setBrokerState":
                    //System.out.println("case setBrokerState");
                    BrokerState brokerState = null;
                    switch (json.getString("BrokerState")){
                        case "ANNOUNCING_NEXT_RACE":
                            brokerState = BrokerState.ANNOUNCING_NEXT_RACE;
                        break;
                        case "OPENING_THE_EVENT":
                            brokerState = BrokerState.OPENING_THE_EVENT;
                        break;
                        case "PLAYING_HOST_AT_THE_BAR":
                            brokerState = BrokerState.PLAYING_HOST_AT_THE_BAR;
                            end = true;
                        break;
                        case "SETTLING_ACCOUNTS":
                            brokerState = BrokerState.SETTLING_ACCOUNTS;
                        break;
                        case "SUPERVISING_THE_RACE":
                            brokerState = BrokerState.SUPERVISING_THE_RACE;
                        break;
                        case "WAITING_FOR_BETS":
                            brokerState = BrokerState.WAITING_FOR_BETS;
                        break;
                    }
                    
                    gri.setBrokerState(brokerState);
                    if(end)
                        System.exit(1);
                    break;
                    
                case "setSpectatorState":
                    //System.out.println("case setSpectatorState");
                    SpectatorState spectatorState = null;
                    switch (json.getString("SpectatorState")){
                        case "APPRAISING_THE_HORSES":
                            spectatorState = SpectatorState.APPRAISING_THE_HORSES;
                        break;
                        case "CELEBRATING":
                            spectatorState = SpectatorState.CELEBRATING;
                        break;
                        case "COLLECTING_THE_GAINS":
                            spectatorState = SpectatorState.COLLECTING_THE_GAINS;
                        break;
                        case "PLACING_A_BET":
                            spectatorState = SpectatorState.PLACING_A_BET;
                        break;
                        case "WAITING_FOR_A_RACE_TO_START":
                            spectatorState = SpectatorState.WAITING_FOR_A_RACE_TO_START;
                        break;
                        case "WATCHING_A_RACE":
                            spectatorState = SpectatorState.WATCHING_A_RACE;
                        break;
                    }
                    
                    gri.setSpectatorState(json.getInt("id"), spectatorState);
                    break;
                    
                case "setMoney":
                    //System.out.println("case setMoney");
                    
                    gri.setMoney(json.getInt("id"), json.getInt("money"));
                    break;
                    
                case "addMoney":
                    //System.out.println("case addMoney");
                    
                    gri.addMoney(json.getInt("id"), json.getInt("money"));
                    break;
                    
                case "setRn":
                    //System.out.println("case setRn");
                    
                    gri.setRn(json.getInt("Rn"));
                    break;
                    
                case "setHorseState":
                    //System.out.println("case setHorseState");
                    HorseState horseState = null;
                    switch (json.getString("HorseState")){
                        case "AT_THE_FINNISH_LINE":
                            horseState = HorseState.AT_THE_FINNISH_LINE;
                        break;
                        case "AT_THE_PADDOCK":
                            horseState = HorseState.AT_THE_PADDOCK;
                        break;
                        case "AT_THE_STABLE":
                            horseState = HorseState.AT_THE_STABLE;
                        break;
                        case "AT_THE_START_LINE":
                            horseState = HorseState.AT_THE_START_LINE;
                        break;
                        case "RUNNING":
                            horseState = HorseState.RUNNING;
                        break;
                    }
                    
                    gri.setHorseState(json.getInt("id"), horseState);
                    break;
                    
                case "setHorseMaxDistance":
                    //System.out.println("case setHorseMaxDistance");
                    
                    gri.setHorseMaxDistance(json.getInt("id"), json.getInt("dist"));
                    break;
                    
                case "setRaceTrackDistance":
                    //System.out.println("case setRaceTrackDistance");
                    
                    gri.setRaceTrackDistance( json.getInt("raceTrackDistance") );
                    break;
                    
                case "setBetSelection":
                    //System.out.println("case setBetSelection");
                    
                    gri.setBetSelection(json.getInt("id"), json.getInt("betSelection"));
                    break;
                    
                case "setBetAmount":
                    //System.out.println("case setBetAmount");
                    
                    gri.setBetAmount(json.getInt("id"), json.getInt("betAmount"));
                    break;
                    
                case "setHorseWinningProb":
                    //System.out.println("case setHorseWinningProb");
                    
                    gri.setHorseWinningProb(json.getInt("id"), json.getDouble("odd"));
                    break;
                    
                case "setIterationNumber":
                    //System.out.println("case setIterationNumber");
                    
                    int iterationNumber[] = JSON.stringToArrayInt( json.getString("iterationNumber") );
                    gri.setIterationNumber(iterationNumber);
                    break;
                    
                case "setTrackPosition":
                    //System.out.println("case setTrackPosition");
                    
                    int trackPosition[] = JSON.stringToArrayInt( json.getString("trackPosition") );
                    gri.setTrackPosition(trackPosition);
                    break;
                    
                case "setStandingAtTheEnd":
                    //System.out.println("case setStandingAtTheEnd");
                    
                    gri.setStandingAtTheEnd(json.getInt("id"), json.getInt("standingAtTheEnd"));
                    break;
            }
                

            
        } catch ( IOException | ClassNotFoundException | JSONException e ){
            e.printStackTrace();
        }
    }
}
