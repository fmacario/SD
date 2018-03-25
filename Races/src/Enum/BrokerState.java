package Enum;

/**
 * Estados do Broker.
 * @author fm
 */
public enum BrokerState {

    /**
     * Broker anuncia a abertura do evento.
     */
    OPENING_THE_EVENT,

    /**
     * Broker anuncia a próxima corrida.
     */
    ANNOUNCING_NEXT_RACE,

    /**
     * Broker espera pelas apsotas dos espetadores.
     */
    WAITING_FOR_BETS,

    /**
     * Broker supervisiona a corrida.
     */
    SUPERVISING_THE_RACE,

    /**
     * Broker liquida as contas.
     */
    SETTLING_ACCOUNTS,

    /**
     * Broker está no estado final.
     */
    PLAYING_HOST_AT_THE_BAR;
}
