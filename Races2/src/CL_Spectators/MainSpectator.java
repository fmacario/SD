/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Spectators;

import java.io.IOException;

/**
 *
 * @author fm
 */
public class MainSpectator {
    
    /**
     * Número de corridas (K).
     */
    public static final int NO_RACES = 4;
    
    /**
     * Número de espetadores (M).
     */
    public static final int NO_SPECTATORS = 4;
    
    /**
     * Array de espetadores.
     */
    public static Spectator[] spectators = new Spectator[NO_SPECTATORS];
    
    public static void main(String[] args) throws IOException {
               
        for (int i = 0; i < NO_SPECTATORS; i++) {
            spectators[i] = new Spectator( i , NO_RACES);
            spectators[i].start();
        }
        
        while (true) {            
            try {

                for (int i = 0; i < NO_SPECTATORS; i++) {
                    spectators[i].join();
                }

                System.exit(0);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
    }
}
