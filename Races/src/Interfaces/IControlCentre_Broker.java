package Interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IControlCentre_Broker {
    ArrayList<Integer> reportResults( ArrayList<Integer> winnersList );
    boolean areThereAnyWinners( Map<Integer, List<Integer>> mapSpec_Horse_Bet );
    void entertainTheGuests();
}
