package SV_BettingCentre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fm
 */
public interface IBettingCentre_Broker {

    /**
     *
     * @param hashHorsesAgile
     * @return
     */
    Map<Integer, List<Integer>> acceptTheBets( Map<Integer, Integer> hashHorsesAgile );

    /**
     *
     * @param winnersList
     * @param specsWinnersList
     */
    void honourTheBets( ArrayList<Integer> winnersList, ArrayList<Integer> specsWinnersList);
}
