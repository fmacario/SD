/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Horses;

import java.io.IOException;

/**
 *
 * @author fm
 */
public class MainHorse {
    
    /**
     * Número de corridas (K).
     */
    public static final int NO_RACES = 4;

    /**
     * Número de competidores (N).
     */
    public static final int NO_COMPETITORS = 4;

    /**
     * Número de espetadores (M).
     */
    public static final int NO_SPECTATORS = 4;

    /**
     * Distância da pista de corrida (D).
     */
    public static final int TRACK_DISTANCE = 50;

    /**
     * Aposta máxima.
     */
    public static final double MAX_BET = 1000;
    
    /**
     * Array de cavalos/competidores.
     */
    public static Horse[] horses = new Horse[NO_COMPETITORS];

    
    public static void main(String[] args) throws IOException {
        System.out.println("HORSE");
        for (int i = 0; i < NO_COMPETITORS; i++) {
            horses[i] = new Horse(NO_RACES, TRACK_DISTANCE, i);
            horses[i].start();
        }
        
        while (true) {            
            try {

                for (int i = 0; i < NO_COMPETITORS; i++) {
                    horses[i].join();
                }

                System.exit(0);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
    }
}
