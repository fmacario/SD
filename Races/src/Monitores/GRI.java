package Monitores;

import Main.Main;
import Enum.*;

public class GRI {
    private final String logName = "Races.log";
    //private TextFile log;
    
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private final int NO_SPECTATORS = Main.NO_SPECTATORS;
    private final int NO_RACES = Main.NO_RACES;
    private final int TRACK_DISTANCE = Main.TRACK_DISTANCE;
    
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
    
    
    
    public GRI(){
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

        rn = 0;                             // RN
        raceTrackDistance = TRACK_DISTANCE;  // Dist

        init();
        updateStatus();
    }

    private void init() {
        System.out.println("         AFTERNOON AT THE RACE TRACK - Description of the internal state of the problem\n\n"
                + "MAN/BRK          SPECTATOR/BETTER             HORSE/JOCKEY PAIR at Race RN\n"
                + "  Stat  St0 Am0  St1 Am1  St2 Am2  St3 Am3   RN St0 Len0 St1 Len1 St2 Len2 St3 Len3"

        );
        
        System.out.println(
                "                                    Race RN Status\n"
                        + " RN Dist BS0 BA0 BS1 BA1 BS2 BA2 BS3 BA3 Od0 N0 Ps0 SD0 Od1 N1 Ps1 Sd1 Od2 N2 Ps2 Sd2 Od3 N3 Ps3 St3"
        );
        
    }
    
    public void updateStatus(){
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
        
        s += " - ";
        
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
                    s += horseMaxDistance + "  ";
                else
                    s += horseMaxDistance + "   ";
            }
            else
                s += "--  ";
        }
        
        s += "\n";
        
        if ( raceTrackDistance >= 10)
            s += raceTrackDistance + "  ";
        else 
            s += raceTrackDistance + "   ";
        
        for (int i = 0; i < NO_SPECTATORS; i++) {
            if ( betSelection[i] != -1)
                s += betSelection[i] + " ";
            else
                s += "- ";
            
            
            if ( betAmount[i] != -1) {
                if ( betAmount[i] >= 1000 )
                    s += betAmount[i] + "  ";
                else if ( betAmount[i] >= 100 && betAmount[i] < 1000 )
                    s += betAmount[i] + "   ";
                else if ( betAmount[i] >= 10 && betAmount[i] < 100 )
                    s += betAmount[i] + "    ";
                else 
                    s += betAmount[i] + "     ";
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
            
            if ( iterationNumber[i] != -1 ){
                if ( iterationNumber[i] >= 10) {
                    s += iterationNumber[i] + "  ";
                }
                else
                    s += iterationNumber[i] + "   ";
            }
            else
                s += "--   ";
            
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
        
        
    }
    
    public void setBrokerState(BrokerState brokerState) {
        this.brokerState = brokerState;
    }

    public void setSpectatorState(int id, SpectatorState spectatorState) {
        this.spectatorState[id] = spectatorState;
    }

    public void setMoney(int[] money) {
        this.money = money;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }
    
    public void addRn() {
        this.rn++;
    }

    public void setHorseState(int id, HorseState horseState) {
        this.horseState[id] = horseState;
    }

    public void setHorseMaxDistance(int[] horseMaxDistance) {
        this.horseMaxDistance = horseMaxDistance;
    }

    public void setRaceTrackDistance(int raceTrackDistance) {
        this.raceTrackDistance = raceTrackDistance;
    }

    public void setBetSelection(int[] betSelection) {
        this.betSelection = betSelection;
    }

    public void setBetAmount(int[] betAmount) {
        this.betAmount = betAmount;
    }

    public void setHorseWinningProb(double[] horseWinningProb) {
        this.horseWinningProb = horseWinningProb;
    }

    public void setIterationNumber(int[] iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public void setTrackPosition(int[] trackPosition) {
        this.trackPosition = trackPosition;
    }

    public void setStandingAtTheEnd(int[] standingAtTheEnd) {
        this.standingAtTheEnd = standingAtTheEnd;
    }
}
