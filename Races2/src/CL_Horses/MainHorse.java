package CL_Horses;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main dos Horses
 * @author fm
 */
public class MainHorse {
    
    /**
     * Número de corridas (K).
     */
    private static int NO_RACES;

    /**
     * Número de competidores (N).
     */
    private static int NO_COMPETITORS;

    /**
     * Distância da pista de corrida (D).
     */
    private static int TRACK_DISTANCE;
    
    /**
     * Array de cavalos/competidores.
     */
    private static Horse[] horses;

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

            NO_RACES = Integer.parseInt( prop.getProperty("NO_RACES") );
            NO_COMPETITORS = Integer.parseInt( prop.getProperty("NO_COMPETITORS") );
            TRACK_DISTANCE = Integer.parseInt( prop.getProperty("TRACK_DISTANCE") );

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
        
        System.out.println("HORSE");
        
        horses = new Horse[NO_COMPETITORS];
        
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
