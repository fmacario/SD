package Interfaces;

public interface IRacingTrack_Horse {
    void proceedToStartLine(int horseID);
    boolean hasFinishLineBeenCrossed( int horseID );
    void makeAMove( int horseID, int Pnk );

}
