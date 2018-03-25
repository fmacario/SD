package Interfaces;

/**
 *
 * @author fm
 */
public interface IBettingCentre_Spectator {

    /**
     *
     * @param spectatorID
     * @param money
     * @return
     */
    int placeABet( int spectatorID, int money );

    /**
     *
     * @param spectatorID
     * @return
     */
    int goCollectTheGains( int spectatorID );
}
