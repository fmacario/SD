package Interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IBettingCentre_Broker {
    Map<Integer, List<Integer>> acceptTheBets( Map<Integer, Integer> hashHorsesAgile );
    void honourTheBets( ArrayList<Integer> winnersList, ArrayList<Integer> specsWinnersList);
}
