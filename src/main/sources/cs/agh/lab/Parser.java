package cs.agh.lab;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Parser {
    //private JTree tree;
    // \p{L} \p{Ll} \p{Lu}  <-- wszystkie litery we wszystkich alfabetach w UTF8

    public static void main(String args[]){

        String fileName = "src/main/resources/konstytucja.txt";
        Pattern articlePattern = Pattern.compile("Art\\.? +[0-9]+.?\\.");
        Pattern dataPattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        Pattern officePattern = Pattern.compile("^.Kancelaria Sejmu.*");
        Pattern randomCharactersPattern = Pattern.compile("^[0-9|A-z]$");
        //String name = scanner.findInLine("Art\\.? +[0-9]+.?\\.");
        //String name = scanner.findInLine("^[0-9]+\\.");
        //String name = scanner.findInLine("[-]$");
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.filter(line -> !dataPattern.matcher(line).matches())
                    .filter(line -> !officePattern.matcher(line).matches())
                    .filter(line -> !randomCharactersPattern.matcher(line).matches())
                    .forEach(line -> System.out.println(line));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
