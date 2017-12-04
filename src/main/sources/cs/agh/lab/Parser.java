package cs.agh.lab;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    private JTree tree;
    // \p{L} \p{Ll} \p{Lu}  <-- wszystkie litery we wszystkich alfabetach w UTF8

    public static void main(String args[]) throws IOException{
        File file = new File("src/main/resources/konstytucja.txt");
        try(Scanner scanner = new Scanner(file)){
            System.out.print(scanner.nextLine());
            System.out.print(scanner.nextLine());

        }

    }
}
