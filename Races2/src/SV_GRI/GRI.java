package SV_GRI;

import Enum.*;
import genclass.TextFile;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Métodos do GRI (General Repository of Information).
 * @author fm
 */
public class GRI {
    private final String logName = "Races.log";
    private final TextFile log;
    private final ReentrantLock rl;
    
    private final int NO_COMPETITORS;
    private final int NO_SPECTATORS;
    private final int TRACK_DISTANCE;
    
    private BrokerState brokerState;        // Stat
    
    private SpectatorState spectatorState[];   // St#
    
    private int money[];            // Am#
    private int rn;                 // RN
    
    private HorseState horseState[];       // St#
    private int horseMaxDistance[]; // Len#
    
    private int raceTrackDistance;  // Dist
    
    private int betSelection[];     // BS#
    private int betAmount[];        // BA#
    
    private double horseWinningProb[];  // Od#
    
    private int iterationNumber[];  // N#
    private int trackPosition[];    // Ps#
    private int standingAtTheEnd[]; // SD#    
       
    /**
     *
     */
    public GRI(int NO_COMPETITORS, int NO_SPECTATORS, int TRACK_DISTANCE){
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.NO_SPECTATORS = NO_SPECTATORS;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        
        log = new TextFile();
        rl = new ReentrantLock();
        
        spectatorState = new SpectatorState[NO_SPECTATORS];
        money = new int[NO_SPECTATORS];
        betSelection = new int[NO_SPECTATORS];
        betAmount = new int[NO_SPECTATORS];

        horseState = new HorseState[NO_COMPETITORS];
        horseMaxDistance = new int[NO_COMPETITORS];
        horseWinningProb = new double[NO_COMPETITORS];
        iterationNumber = new int[NO_COMPETITORS];
        trackPosition = new int[NO_COMPETITORS];
        standingAtTheEnd = new int[NO_COMPETITORS];
        
        brokerState = null;        // Stat
    
        for (int i = 0; i < NO_SPECTATORS; i++) {
            spectatorState[i] = null;   // ST#
            money[i] = -1;              // Am#
            betSelection[i] = -1;       // BS
            betAmount[i] = -1;          // BA#
        }
            
        for (int i = 0; i < NO_COMPETITORS; i++) {
            horseState[i] = null;       // St#
            horseMaxDistance[i] = -1;   // Len#
            horseWinningProb[i] = -1;   // Od#
            iterationNumber[i] = -1;    // N#
            trackPosition[i] = -1;      // Ps#
            standingAtTheEnd[i] = -1;   // SD# 
        }

        raceTrackDistance = TRACK_DISTANCE;  // Dist

        init();
        updateStatus();
    }

    /**
     * Imprime o cabeçalho inicial.
     */
    private void init() {
        log.openForWriting(".",logName);
        log.writelnString("         AFTERNOON AT THE RACE TRACK - Description of the internal state of the problem\n\n"
                + "MAN/BRK          SPECTATOR/BETTER             HORSE/JOCKEY PAIR at Race RN\n"
                + "  Stat  St0 Am0  St1 Am1  St2 Am2  St3 Am3   RN St0 Len0 St1 Len1 St2 Len2 St3 Len3"

        );
        
        log.writelnString(
                "                                    Race RN Status\n"
                        + " RN Dist BS0  BA0 BS1  BA1 BS2  BA2 BS3  BA3  Od0 N0 Ps0 SD0 Od1 N1 Ps1 Sd1 Od2 N2 Ps2 Sd2 Od3 N3 Ps3 St3"
        );
        
        log.close();
        
    }
    
    /**
     * Imprime o estado atual.
     */
    public void updateStatus(){
        rl.lock();
        try{
            log.openForAppending(".", logName);

            String s = "  ";

            if ( brokerState != null)
                switch( brokerState.ordinal() ){
                    case 0:     // OPENING_THE_EVENT
                        s += "OPEN  ";
                        break;
                    case 1:     // ANNOUNCING_NEXT_RACE
                        s += "NEXT  ";
                        break;  
                    case 2:     // WAITING_FOR_BETS
                        s += "WAIT  ";
                        break;
                    case 3:     // SUPERVISING_THE_RACE
                        s += "SUPE  ";
                        break;
                    case 4:     // SETTLING_ACCOUNTS
                        s += "SETT  ";
                        break;
                    case 5:     // PLAYING_HOST_AT_THE_BAR;
                        s += "PLAY  ";
                        break;
                }
            else
                s += "----  ";

            for (int i = 0; i < NO_SPECTATORS; i++) {
                if ( spectatorState[i] != null )
                    switch( spectatorState[i].ordinal() ){
                        case 0:     // WAITING_FOR_A_RACE_TO_START
                            s += "WAI ";
                            break;
                        case 1:     // APPRAISING_THE_HORSES
                            s += "APP ";
                            break;  
                        case 2:     // PLACING_A_BET
                            s += "BET ";
                            break;
                        case 3:     // WATCHING_A_RACE
                            s += "WAR ";
                            break;
                        case 4:     // COLLECTING_THE_GAINS
                            s += "CTG ";
                            break;
                        case 5:     // CELEBRATING;
                            s += "CEL ";
                            break;
                    }
                else
                    s += "--- ";

                if ( money[i] != -1) {
                    if ( money[i] >= 1000)
                        s += money[i] + " ";
                    else if ( money[i] >= 100 && money[i] < 1000)
                        s += money[i] + "  ";
                    else if ( money[i] >= 10 && money[i] < 100)
                        s += money[i] + "   ";
                    else
                        s += money[i] + "    ";
                }else
                    s += "---- ";
            }

            s +=  rn + " ";

            for (int i = 0; i < NO_COMPETITORS; i++) {
                if ( horseState[i] != null )
                    switch( horseState[i].ordinal() ){
                        case 0:     // AT_THE_STABLE
                            s += "STA ";
                            break;
                        case 1:     // AT_THE_PADDOCK
                            s += "PAD ";
                            break;  
                        case 2:     // AT_THE_START_LINE
                            s += "STL ";
                            break;
                        case 3:     // RUNNING
                            s += "RUN ";
                            break;
                        case 4:     // AT_THE_FINNISH_LINE
                            s += "FIN ";
                            break;
                    }
                else
                    s += "--- ";

                if ( horseMaxDistance[i] != -1){
                    if ( horseMaxDistance[i] > 10 ) 
                        s += horseMaxDistance[i] + "  ";
                    else
                        s += horseMaxDistance[i] + "   ";
                }
                else
                    s += "--  ";
            }

            //s += "\n";
            log.writelnString(s);
            s= "  ";

            s += rn + "  ";

            if( raceTrackDistance != 0){
                if ( raceTrackDistance >= 10)
                    s += raceTrackDistance + "   ";
                else 
                    s += raceTrackDistance + "    ";
            }
            else
                s += "--   ";

            for (int i = 0; i < NO_SPECTATORS; i++) {
                if ( betSelection[i] != -1)
                    s += betSelection[i] + "  ";
                else
                    s += "-  ";


                if ( betAmount[i] != -1) {
                    if ( betAmount[i] >= 1000 )
                        s += betAmount[i] + "  ";
                    else if ( betAmount[i] >= 100 && betAmount[i] < 1000 )
                        s += betAmount[i] + "   ";
                    else if ( betAmount[i] >= 10 && betAmount[i] < 100 )
                        s += betAmount[i] + "    ";
                    else 
                        s += betAmount[i] + "      ";
                }else
                    s += "----  ";

            }

            for (int i = 0; i < NO_COMPETITORS; i++) {
                if ( horseWinningProb[i] != -1) {
                    if ( horseWinningProb[i] == 1 || horseWinningProb[i] == 0) 
                        s += horseWinningProb[i] + "   ";
                    else
                        s += horseWinningProb[i] + " ";
                }
                else
                    s += "---- ";

                if ( iterationNumber[i] != -1 ){
                    if ( iterationNumber[i] >= 10) {
                        s += iterationNumber[i] + "  ";
                    }
                    else
                        s += iterationNumber[i] + "   ";
                }
                else
                    s += "--  ";

                if ( trackPosition[i] != -1 ){
                    if ( trackPosition[i] >= 10) {
                        s += trackPosition[i] + "  ";
                    }
                    else
                        s += trackPosition[i] + "   ";
                }
                else
                    s += "--  ";

                if ( standingAtTheEnd[i] != -1 ){
                    s += standingAtTheEnd[i] + " ";
                }
                else
                    s += "- ";
            }
            //System.out.println(s);        
            log.writelnString(s);
            log.close();
        }catch( Exception e ){
            e.printStackTrace();
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Define o <b>estado</b> do <b>Broker</b>.
     * @param brokerState Estado do Broker.
     */
    public void setBrokerState(BrokerState brokerState) {
        this.brokerState = brokerState;
    }

    /**
     * Define o <b>estado</b> do <b>Espetador</b>.
     * @param id Id do espetador.
     * @param spectatorState Estado do espetador.
     */
    public void setSpectatorState(int id, SpectatorState spectatorState) {
        this.spectatorState[id] = spectatorState;
    }

    /**
     * Define o <b>dinheiro</b> do <b>Espetador</b>.
     * @param id Id do espetador.
     * @param money Dinheiro.
     */
    public void setMoney(int id, int money) {
        this.money[id] = money;
    }
    
    /**
     * Incrementa o <b>dinheiro</b> do <b>Espetador</b>.
     * @param id Id do espetador.
     * @param money Dinheiro.
     */
    public void addMoney(int id, int money) {
        this.money[id] += money;
    }

    /**
     * Define o <b>número da corrida</b>.
     * @param rn Número da corrida.
     */
    public void setRn(int rn) {
        this.rn = rn;
    }

    /**
     * Define o <b>estado</b> do <b>Cavalo</b>.
     * @param id Id do cavalo.
     * @param horseState Estado do cavalo.
     */
    public void setHorseState(int id, HorseState horseState) {
        this.horseState[id] = horseState;
    }

    /**
     * Define a <b>distância máxima</b> de uma iteração do <b>Cavalo</b>.
     * @param id Id do cavalo.
     * @param dist Distância máxima numa iteração.
     */
    public void setHorseMaxDistance(int id, int dist) {
        this.horseMaxDistance[id] = dist;
    }

    /**
     * Define a <b>distância</b> da <b>pista de corrida</b>.
     * @param raceTrackDistance
     */
    public void setRaceTrackDistance(int raceTrackDistance) {
        this.raceTrackDistance = raceTrackDistance;
    }

    /**
     * Define a <b>aposta</b> do <b>Espetador</b>.
     * @param id Id do espetador.
     * @param betSelection Id do cavalo.
     */
    public void setBetSelection(int id, int betSelection) {
        this.betSelection[id] = betSelection;
    }

    /**
     * Define o <b>dinheiro apostado</b> por parte do <b>Espetador</b>.
     * @param id Id do espetador.
     * @param betAmount Quantidade de dinheiro apostado.
     */
    public void setBetAmount(int id, int betAmount) {
        this.betAmount[id] = betAmount;
    }

    /**
     * Define a <b>probabilidade</b> de vencer do <b>Cavalo</b>.
     * @param id Id do cavalo.
     * @param odd Probabilidade.
     */
    public void setHorseWinningProb(int id, double odd) {
        this.horseWinningProb[id] = odd;
    }

    /**
     * Define a <b>iteração</b> atual do <b>Cavalo</b>.
     * @param iterationNumber Array de iterações.
     */
    public void setIterationNumber(int[] iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    /**
     * Define a <b>posição</b> atual do <b>Cavalo</b>.
     * @param trackPosition Array de posições.
     */
    public void setTrackPosition(int[] trackPosition) {
        this.trackPosition = trackPosition;
    }

    /**
     * Define a <b>posição</b> de chegada do <b>Cavalo</b>.
     * @param id Id do cavalo.
     * @param standingAtTheEnd Posição de chegada.
     */
    public void setStandingAtTheEnd(int id, int standingAtTheEnd) {
        this.standingAtTheEnd[id] = standingAtTheEnd;
    }
}
