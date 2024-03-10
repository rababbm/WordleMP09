package wordle.mp09.communication;

import wordle.mp09.model.WordleLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorWordleTCP {
    static final int PORT = 5555;//iniciar servidor
    private boolean end = false;
    private WordleLogic wordleLogic;
    private String currentWordPlay;
    private List<Character> guessedLetters;

    public ServidorWordleTCP() {
        this.wordleLogic = new WordleLogic();//genera palabra aleatoria
        this.currentWordPlay = "";//palabra del user
        this.guessedLetters = new ArrayList<>();
    }

    public void listen() { //para esucuchar las conexiones de clientes
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);//esuchca las llamadas d elos clientes
            while (!end) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection with " + clientSocket.getInetAddress() + " dealt correctly!");
                currentWordPlay = wordleLogic.getRandomWord().toUpperCase();
                //currentWordPlay = wordleLogic.getRandomWord(); // Mantener la palabra adivinada en su forma original
                guessedLetters.clear();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintStream out = new PrintStream(clientSocket.getOutputStream());

                String clientWord;
                boolean gameOver = false;
                int attemptsGuessing = 0;
                while (!gameOver && (clientWord = in.readLine()) != null) { //LOGICA DEL JUEGO
                    attemptsGuessing++;
                    clientWord = clientWord.toUpperCase(); // Convertir la entrada del cliente a mayúsculas
                    System.out.println("Client send the word: " + clientWord);

                    if (attemptsGuessing == 10) {
                        out.println("\033[31mHas superado los intentos máximos permitidos. Otra vez será!\033[0m");
                        out.println("\033[32mLa palabra correcta es: " + currentWordPlay + "\033[0m");
                        gameOver = true;
                    } else if (clientWord.length() != 5) {
                        out.println("\033[36mError: word must have exactly 5 letters!\033[0m");
                    } else if (clientWord.equals(currentWordPlay)) {
                        out.println("Felicidades! Has acertado la palabra!!");
                        gameOver = true;
                    } else {
                        out.println(wordleLogic.checkGuess(clientWord, currentWordPlay));
                        out.println("Intentos restantes: " + (10 - attemptsGuessing));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorWordleTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {
        ServidorWordleTCP tcpSocketServer = new ServidorWordleTCP();
        tcpSocketServer.listen();
    }
}