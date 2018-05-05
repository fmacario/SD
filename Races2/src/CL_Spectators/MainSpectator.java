package CL_Spectators;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main dos Spectators
 * @author fm
 */
public class MainSpectator {
    
    /**
     * Número de corridas (K).
     */
    private static int NO_RACES;
    
    /**
     * Número de espetadores (M).
     */
    private static int NO_SPECTATORS;
    
    /**
     * Array de espetadores.
     */
    private static Spectator[] spectators;// = new Spectator[NO_SPECTATORS];
    
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            NO_SPECTATORS = Integer.parseInt( prop.getProperty("NO_SPECTATORS") );
            NO_RACES = Integer.parseInt( prop.getProperty("NO_RACES") );

	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}
        
        System.out.println("SPECTATOR");
        
        spectators = new Spectator[NO_SPECTATORS];
        
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
