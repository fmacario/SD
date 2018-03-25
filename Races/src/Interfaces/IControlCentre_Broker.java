package Interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fm
 */
public interface IControlCentre_Broker {

    /**
     *
     * @param winnersList
     * @param mapSpec_Horse_Bet
     * @return
     */
    ArrayList<Integer> reportResults( ArrayList<Integer> winnersList , Map<Integer, List<Integer>> mapSpec_Horse_Bet );

    /**
     *
     * @param mapSpec_Horse_Bet
     * @return
     */
    boolean areThereAnyWinners( Map<Integer, List<Integer>> mapSpec_Horse_Bet );

    /**
     *
     */
    void entertainTheGuests();
}
