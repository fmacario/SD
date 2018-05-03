package SV_Paddock;

import Enum.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONObject;

/**
 * Métodos do Paddock.
 * @author fm
 */
public class Paddock implements IPaddock_Horse, IPaddock_Spectator, IPaddock_Broker{    
    //private GRI gri;
    private final Condition condSpectators;
    private final Condition condHorses;
    private final Condition condBroker;
    private final ReentrantLock rl;
    
    private final int NO_SPECTATORS;
    private final int NO_COMPETITORS;
        
    private int nSpectators = 0;
    private int nHorses = 0;
    private static boolean GO = false;
    private boolean allHorses = false;
    private boolean lastHorse = false;
    
    private String IP_GRI;
    private int PORT_GRI;
    
    /**
     * 
     * @param gri General Repository of Information (GRI).
     */
    public Paddock(int NO_COMPETITORS, int NO_SPECTATORS, String IP_GRI, int PORT_GRI){
        //this.gri = gri;
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.NO_SPECTATORS = NO_SPECTATORS;
        this.IP_GRI = IP_GRI;
        this.PORT_GRI = PORT_GRI;
               
        rl = new ReentrantLock(true);
        condSpectators = rl.newCondition();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    /**
     * <b>Cavalo</b> vai para o Paddock.
     * @param horseID Id do cavalo.
     */
    @Override
    public void proceedToPaddock(int horseID) {
        rl.lock();
        try{
            try{
                nHorses++;
                //gri.setHorseState(horseID, HorseState.AT_THE_PADDOCK);
                //gri.updateStatus();
                System.out.println("Horse " + horseID + " at the paddock.");
                
                if(nHorses == NO_COMPETITORS){
                    allHorses = true;
                    condSpectators.signalAll();
                }
                
                while( GO == false ){
                    condHorses.await();
                }                
                nHorses--;
                if(nHorses == 0){
                    lastHorse = true;
                    allHorses= false;
                    condSpectators.signalAll();
                }
                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    /**
     * <b>Espetador</b> vai analisar os cavalos.
     * @param spectatorID Id do espetador.
     */
    @Override
    public void goCheckHorses(int spectatorID) {
        rl.lock();
        try{
            try{
                nSpectators++;
                
                //gri.setSpectatorState(spectatorID, SpectatorState.APPRAISING_THE_HORSES);
                //gri.updateStatus();
                System.out.println("Spectator " + spectatorID + " goCheckHorses.");
                
                if(nSpectators == NO_SPECTATORS){
                    GO = true;
                    condHorses.signalAll();
                    condBroker.signal();
                }
                while( !lastHorse ){
                    condSpectators.await();
                }
                
                nSpectators--;
                if(nSpectators==0){
                    GO = false;
                    lastHorse=false;
                }
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * <b>Espetador</b> espera pela próxima corrida.
     * @param spectatorID Id do espetador.
     */
    @Override
    public void waitForNextRace(int spectatorID){
        rl.lock();
        
        try {
            try{
                //gri.setSpectatorState(spectatorID, SpectatorState.WAITING_FOR_A_RACE_TO_START);
                //gri.updateStatus();
                System.out.println("Spectator " + spectatorID + " waitForNextRace.");
                
                while ( !allHorses ) {
                    condSpectators.await();
                }
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * <b>Broker</b> espera que os espetadores cheguem ao Paddock.
     * @return true - quando todos os espetadores chegam ao Paddock.
     */
    @Override
    public boolean waitForSpectators(){
        rl.lock();
        
        try {
            try {
                while( GO == false ){
                    condBroker.await();
                }
                return true;
                
            } catch (Exception e) {
                e.printStackTrace();
                return true;
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
