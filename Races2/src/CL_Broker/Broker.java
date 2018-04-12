/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author fm
 */
public class Broker {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        System.out.println("OLAAA");
        Socket cliente = new Socket("localhost", 12345);
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

    }
}
