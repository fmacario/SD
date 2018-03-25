package Interfaces;

import java.util.Map;

/**
 *
 * @author fm
 */
public interface IStable_Broker {

    /**
     *
     * @param nRace
     * @return
     */
    Map<Integer, Integer> summonHorsesToPaddock( int nRace );

    /**
     *
     */
    public void end();
}
