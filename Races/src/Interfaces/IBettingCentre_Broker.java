package Interfaces;

import java.util.ArrayList;

public interface IBettingCentre_Broker {
    void acceptTheBets();
    void honourTheBets( ArrayList<Integer> winnersList );
}
