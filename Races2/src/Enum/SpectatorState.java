package Enum;

/**
 * Estados do Espetador
 * @author fm
 */
public enum SpectatorState {

    /**
     * Espetador está à espera que uma corrida comece.
     */
    WAITING_FOR_A_RACE_TO_START, 

    /**
     * Espetador está a avaliar os cavalos.
     */
    APPRAISING_THE_HORSES, 

    /**
     * Espetador está a fazer uma aposta.
     */
    PLACING_A_BET, 

    /**
     * Espetador está a ver a corrida.
     */
    WATCHING_A_RACE,

    /**
     * Espetador está a receber os lucros da aposta.
     */
    COLLECTING_THE_GAINS,

    /**
     * Espetador está a celebrar.
     */
    CELEBRATING;
}
