package wordle.mp09.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientWordleTCP {
    private Scanner sc = new Scanner(System.in);

    public void connect(String address, int port) {//CONEXION CON EL SERVIDOR
        String serverData;
        String request;
        boolean continueConnected = true;
        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(address), port);//Pasamos info
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            while (continueConnected) {//CLIENTE TRABAJA EN EL PUERTO HASTA QUE ACABE
                // Solicita al usuario que inserte una palabra para adivinar
                System.out.print("\u001B[35mInserta una palabra: \u001B[00m");
                while (true) {
                    request = sc.nextLine();
                    if (request.length() == 5) {
                        break; // Salir del bucle si la longitud de la palabra es válida
                    } else {
                        System.out.println("\u001B[36mError: La palabra debe tener exactamente 5 letras.\u001B[00m");
                        System.out.print("\u001B[35mInserta una palabra: \u001B[00m");
                    }
                }
                // Envía esta palabra al servidor para que la compare con la palabra generada del archivo
                out.println(request);
                // Obtiene la respuesta del servidor y la imprime de nuevo en el cliente
                while ((serverData = in.readLine()) != null) {
                    System.out.println(serverData);
                    if (serverData.contains("Intentos restantes")) {
                        break;
                    }
                }
            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.printf("Error de connexió. No existeix el host, %s", ex);
        } catch (IOException ex) {
            System.out.printf("Error de connexió indefinit, %s", ex);
        }
    }


    private void close(Socket socket){//CERRAR LA CONEXCION CON SOCKET
        try {
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {

            Logger.getLogger(ClientWordleTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ClientWordleTCP client = new ClientWordleTCP();
        client.connect("localhost", 5555);
    }
}