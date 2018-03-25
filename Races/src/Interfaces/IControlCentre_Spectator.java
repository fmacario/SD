package Interfaces;

/**
 *
 * @author fm
 */
public interface IControlCentre_Spectator {
    //void waitForNextRace();

    /**
     *
     * @param specId
     */
    void goWatchTheRace(int specId);

    /**
     *
     * @param specId
     * @return
     */
    boolean haveIWon(int specId);

    /**
     *
     * @param specId
     */
    void relaxABit(int specId);

}
