package SV_Stable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.*;
import java.util.concurrent.locks.*;
import org.json.JSONException;
import org.json.JSONObject;
        
/**
 * Métodos do Estábulo.
 * @author fm
 */
public class Stable implements IStable_Broker, IStable_Horse{ 
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    
    private final int NO_COMPETITORS;
    
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private int nHorses = 0;
    private boolean GO = false;
    private boolean allHorses = false;
    private boolean end = false;
    
    private final String IP_GRI;
    private final int PORT_GRI;
    
    /**
     *
     * @param NO_COMPETITORS Número de competidores.
     * @param IP_GRI IP do GRI.
     * @param PORT_GRI Porta do Gri.
     * @throws JSONException
     * @throws IOException
     */
    public Stable (int NO_COMPETITORS, String IP_GRI, int PORT_GRI) throws JSONException, IOException{
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.IP_GRI = IP_GRI;
        this.PORT_GRI = PORT_GRI;
        
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
        
        JSONObject json = new JSONObject();
        json.put("metodo", "setBrokerState");
        json.put("BrokerState", "OPENING_THE_EVENT");
        sendMessage(json);
    }
    
    /**
     * <b>Cavalo</b> vai para o estábulo.
     * @param horseID Id do cavalo.
     * @param agile Agilidade do cavalo.
     */
    @Override
    public void proceedToStable(int horseID, int agile) {
        rl.lock();
        try{
            try{
                JSONObject json;
                nHorses++;
                
                hashHorsesAgile.put( horseID, agile );
                
                //gri.setHorseState(horseID, HorseState.AT_THE_STABLE);
                json = new JSONObject();
                json.put("metodo", "setHorseState");
                json.put("id", horseID);
                json.put("HorseState", "AT_THE_STABLE");
                sendMessage(json);
                
                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                //System.out.println("Horse " + horseID + " at the stable.");
                
                if(nHorses == NO_COMPETITORS){
                    allHorses=true;
                    condBroker.signal();
                }
                
                while(GO == false){
                    condHorses.await();
                }
                
                nHorses--;

                if(nHorses == 0){
                    GO = false;
                    allHorses=false;
                }
            } catch (Exception e) { 
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }
    
    /**
     * <b>Broker</b> anuncia a corrida e acorda os cavalos para irem para o Paddock.
     * @param nRace Número da corrida.
     * @return Mapa com id dos cavalos e agilidade de cada um.
     */
    @Override
    public Map<Integer, Integer> summonHorsesToPaddock( int nRace ) {
        rl.lock();
        try {
            try{
                JSONObject json;
                
                //gri.setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
                json = new JSONObject();
                json.put("metodo", "setBrokerState");
                json.put("BrokerState", "ANNOUNCING_NEXT_RACE");
                sendMessage(json);
                
                //gri.setRn( nRace );
                json = new JSONObject();
                json.put("metodo", "setRn");
                json.put("Rn", nRace);
                sendMessage(json);
                
                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                //System.out.println("BrokerState.ANNOUNCING_NEXT_RACE");
                
                while(!allHorses){
                    condBroker.await();
                }
                
                GO = true;
                
                int totalAgile = 0;
                
                for (Map.Entry<Integer, Integer> entry : hashHorsesAgile.entrySet())
                {
                    totalAgile += entry.getValue();
                }
                
                for (int i = 0; i < NO_COMPETITORS; i++) {
                    int odd = hashHorsesAgile.get(i)*100 / totalAgile;
                    double o = (double)odd/100;
                    
                    //gri.setHorseMaxDistance(i, hashHorsesAgile.get(i));
                    json = new JSONObject();
                    json.put("metodo", "setHorseMaxDistance");
                    json.put("id", i);
                    json.put("dist", hashHorsesAgile.get(i));
                    sendMessage(json);
                
                    //gri.setHorseWinningProb(i, o);
                    json = new JSONObject();
                    json.put("metodo", "setHorseWinningProb");
                    json.put("id", i);
                    json.put("odd", o);
                    sendMessage(json);
                    hashHorsesAgile.put(i, odd);
                }
                                
                condHorses.signalAll();
                return hashHorsesAgile;
                
            } catch (Exception e) { 
                e.printStackTrace();
                return null;
            }
        }finally{
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> termina o evento.
     */
    @Override
    public void end() {
        rl.lock();
        try {
            try {
                condHorses.signalAll();
                GO = true;
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
