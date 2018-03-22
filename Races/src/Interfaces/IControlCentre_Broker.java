package Interfaces;

import java.util.ArrayList;

public interface IControlCentre_Broker {
    void reportResults( ArrayList<Integer> winnersList );
    boolean areThereAnyWinners( ArrayList<Integer> winnersList );
    void entertainTheGuests();
}
