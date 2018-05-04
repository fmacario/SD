package SV_ControlCentre;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONObject;

/**
 * Métodos do Control Centre.
 * @author fm
 */
public class ControlCentre implements IControlCentre_Spectator, IControlCentre_Broker{
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condSpectators;
    
    private ArrayList<Integer> horsesWinnersList = new ArrayList<>();
    private ArrayList<Integer> specsWinnersList = new ArrayList<>();
    
    private boolean wakeSpecs = false;
    private int nSpec = 0;
    
    private final String IP_GRI;
    private final int PORT_GRI;

    public ControlCentre(String IP_GRI, int PORT_GRI){
        this.IP_GRI = IP_GRI;
        this.PORT_GRI = PORT_GRI;
        
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    /**
     * <b>Espetador</b> vai ver a corrida.
     * @param specId Id do espetador.
     */
    @Override
    public void goWatchTheRace(int specId) {
        rl.lock();
        
        try {
            try {
                nSpec++;
                
                JSONObject json;
                
                //gri.setSpectatorState(specId, SpectatorState.WATCHING_A_RACE);
                json = new JSONObject();
                json.put("metodo", "setSpectatorState");
                json.put("id", specId);
                json.put("SpectatorState", "WATCHING_A_RACE");
                sendMessage(json);

                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("Spectator "+ specId + " WATCHING_A_RACE");
                
                while ( !wakeSpecs ) {
                    condSpectators.await();
                }
               
                nSpec--;
                
                if ( nSpec == 0 )
                    wakeSpecs = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Espetador</b> verifica se ganhou a aposta.
     * @param specId Id do espetador.
     * @return true - se o espetador ganhou a aposta <p>false - se o espetador não ganhou a aposta.
     */
    @Override
    public boolean haveIWon( int specId ) {
        rl.lock();
        try {
            try {
                for ( Integer specsWinner : specsWinnersList ) {
                    if( specId == specsWinner )
                        return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Espetador</b> relaxa.
     * @param specId Id do espetador.
     */
    @Override
    public void relaxABit(int specId) {
        rl.lock();
        try {
            try {
                JSONObject json;
                
                //gri.setSpectatorState(specId, SpectatorState.CELEBRATING);
                json = new JSONObject();
                json.put("metodo", "setSpectatorState");
                json.put("id", specId);
                json.put("SpectatorState", "CELEBRATING");
                sendMessage(json);

                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("Spectator "+ specId + " CELEBRATING");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> reporta os resultados das apostas.
     * @param winnersList ArrayList com os id dos cavalos que venceram a corrida.
     * @param mapSpec_Horse_Bet Mapa com o id do espetador e uma lista com o id do cavalo apostado e a respetiva aposta.
     * @return Lista de espetadores/apostadores vencedores. 
     */
    @Override
    public ArrayList<Integer> reportResults( ArrayList<Integer> winnersList , Map<Integer, List<Integer>> mapSpec_Horse_Bet) {
        rl.lock();
        try {
            try {
                specsWinnersList.clear();
                this.horsesWinnersList = winnersList;
                           System.out.println("BROKER: REPORT RESULTS");    
                for ( Integer horseWinner : horsesWinnersList ) {                    
                    for (Map.Entry<Integer, List<Integer>> entry : mapSpec_Horse_Bet.entrySet())
                    {
                        if( horseWinner == entry.getValue().get(0) )
                            specsWinnersList.add(entry.getKey());
                    }
                }
                
                wakeSpecs = true;
                condSpectators.signalAll();
                
                return specsWinnersList;
                
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> verifica se houve apostadores vencedores.
     * @param mapSpec_Horse_Bet Mapa com o id do espetador e uma lista com o id do cavalo apostado e a respetiva aposta. 
     * @return true - se houve apostadores vencedores.<p>false - se não houve apostadores vencedores.
     */
    @Override
    public boolean areThereAnyWinners( Map<Integer, List<Integer>> mapSpec_Horse_Bet ) {
        rl.lock();
        try {
            System.out.println("BROKER ARE THERE ANY WINNERS");
            try {
                if ( specsWinnersList.isEmpty() )
                    return false;
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> entretem os espetadores.
     */
    @Override
    public void entertainTheGuests() {
        rl.lock();
        try {
            try {
                JSONObject json = new JSONObject();
                
                //gri.setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
                json.put("metodo", "setBrokerState");
                json.put("BrokerState", "PLAYING_HOST_AT_THE_BAR");
                sendMessage(json);
                
                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("BrokerState.PLAYING_HOST_AT_THE_BAR");
                
                condHorses.signalAll();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    private void sendMessage(JSONObject json) throws IOException {
        Socket socket = new Socket( IP_GRI, PORT_GRI );
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        
        o.writeObject( json.toString() );
        out.flush();
    }
}
