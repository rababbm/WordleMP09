package wordle.mp09.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleLogic {
    private ArrayList<String> wordsList = new ArrayList<String>();//lectura del txt
    private String WRONG = "\u001B[31m";
    private String CLOSE = "\u001B[33m";
    private String CORRECT = "\u001B[32m";
    private String RESET = "\u001B[00m";

    public WordleLogic() { //constructor para leer las palabras
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/rabab/Descargas/WordleMP09_rababbeihaqi/src/wordle/mp09/words/words.txt"));
            String line = reader.readLine();
            while (line != null) {
                wordsList.add(line.trim());
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomWord() {//palabra ganadora
        Random random = new Random();
        int index = random.nextInt(wordsList.size());
        return wordsList.get(index);
    }

    public String checkGuess(String guess, String word) { //logica de colores y palabra ganadora
        guess = guess.toUpperCase();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            boolean found = false;
            for (int j = 0; j < word.length(); j++) {
                if (c == word.charAt(j)) {
                    if (i == j) {
                        // Si la letra existe y está en la posición correcta, la muestra en verde
                        result.append(CORRECT).append(c).append(RESET);
                    } else {
                        // Si la letra existe pero no está en la posición correcta, la muestra en amarillo/naranja
                        result.append(CLOSE).append(c).append(RESET);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Si la letra no existe en absoluto, la muestra en rojo
                result.append(WRONG).append(c).append(RESET);
            }
        }
        return result.toString();
    }

}