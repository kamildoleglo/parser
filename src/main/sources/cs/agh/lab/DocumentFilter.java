package cs.agh.lab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DocumentFilter {
    private static Pattern datePattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    private static Pattern officePattern = Pattern.compile("^.Kancelaria Sejmu.*");
    private static Pattern randomCharactersPattern = Pattern.compile("^[0-9|A-z]$");
    private static Pattern skippedArticlesPattern = Pattern.compile(".*\\(pominięte\\).*");

    public static String[] filter(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            return stream
                    .filter(line -> !datePattern.matcher(line).matches())
                    .filter(line -> !officePattern.matcher(line).matches())
                    .filter(line -> !randomCharactersPattern.matcher(line).matches())
                    .filter(line -> !skippedArticlesPattern.matcher(line).matches())
                    .toArray(String[]::new);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening file " + fileName);
            return null;
        }
    }
}
