import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private final Condition condSpectators;
    private int nSpectators = 0;
    
    public BettingCentre(GRI gri){
        this.gri = gri;
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    @Override
    public void placeABet() {
        rl.lock();
        Spectator spec = ((Spectator)Thread.currentThread());
        System.out.println("placeABet - "+spec.getSpecId());
        
        try{
            try {
                nSpectators++;
                fifoSpectators.add(spec);
                spec.setSpecState(SpectatorState.PLACING_A_BET);
                System.out.println("Spectator " + spec.getSpecId() + " " + spec.getSpecState());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
        
    }

    @Override
    public void goCollectTheGains() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void acceptTheBets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void honourTheBets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
