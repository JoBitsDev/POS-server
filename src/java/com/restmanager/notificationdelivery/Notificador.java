/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.notificationdelivery;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.eclipse.persistence.oxm.json.JsonObjectBuilderResult;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class Notificador extends Thread {

    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private Socket client; // socket to communicate with server
    private final String host;
    private final Notificable notificacion;
    public  boolean NOTIFICACION_ENVIADA = false;

    // initialize chatServer and set up GUI
    public Notificador(String host, Notificable notificacion) {
        this.host = host;
        this.notificacion = notificacion;
    }

    @Override
    public void run() {
        notificar();
    }

    public void notificar() {
        try // connect to server, get streams, process connection
        {
            if (InetAddress.getByName(host).isReachable(3000)) {
                connectToServer(); // create a Socket to make connection
                getStreams(); // get the input and output streams
                sendNotification(notificacion);
                closeConnection();
            }
        }// end try
        catch (EOFException eofException) {
            closeConnection();
        } // end catch
        catch (IOException ioException) {
            closeConnection();
        } // end catch
        catch (Exception e) {
            closeConnection(); // close connection
        }
// end finally

    }

    private void sendNotification(Notificable notificacion) {

        try {
            output.writeObject(notificacion.getTituloNotificacion() + "_" + notificacion.getMensajeNotificacion() + "_" + notificacion.getDescripcionNotificacion());
            output.flush();
            NOTIFICACION_ENVIADA = true;

        } catch (IOException iOException) {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        client = new Socket(host, 8888);
    } // end method connectToServer

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information
        input = new ObjectInputStream(client.getInputStream());

    }

    private void processConnection() throws IOException {
        do {
            try {
                message = (String) input.readObject();

            } catch (ClassNotFoundException e) {
            }
        } while (!message.equals("SERVER>>> TERMINATE"));

    }

    private void closeConnection() {
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retry() {
        throw new UnsupportedOperationException(); //To change body of generated methods, choose Tools | Templates.
    }

}
