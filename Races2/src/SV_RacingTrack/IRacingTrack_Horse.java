package SV_RacingTrack;

/**
 *
 * @author fm
 */
public interface IRacingTrack_Horse {

    /**
     *
     * @param horseID
     */
    void proceedToStartLine(int horseID);

    /**
     *
     * @param horseID
     * @return
     */
    boolean hasFinishLineBeenCrossed( int horseID );

    /**
     *
     * @param horseID
     * @param Pnk
     */
    void makeAMove( int horseID, int Pnk );

}
