package cs.agh.lab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Parser {
    // \p{L} \p{Ll} \p{Lu}  <-- wszystkie litery we wszystkich alfabetach w UTF8
    private Pattern datePattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    private Pattern officePattern = Pattern.compile("^.Kancelaria Sejmu.*");
    private Pattern randomCharactersPattern = Pattern.compile("^[0-9|A-z]$");
    //Move to filter ^^^^^

    private String[] document;
    private DocumentTree root;

    public Parser(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            this.document = stream
                    .filter(line -> !datePattern.matcher(line).matches())
                    .filter(line -> !officePattern.matcher(line).matches())
                    .filter(line -> !randomCharactersPattern.matcher(line).matches())
                    .toArray(String[]::new);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening file " + fileName);
        }
        this.root = new DocumentTree(DocumentSectionType.MAIN, null, null);
        Integer childStart = this.findNewElement(0, this.document.length - 1);
        if(this.document[childStart].contentEquals("KONSTYTUCJA") && childStart+1 < this.document.length){
            this.document[childStart] += " " + this.document[childStart+1];
            this.document[childStart+1] = "";
        }
    }

    public Parser(DocumentTree root){
        this.root = root;
        this.document = root.getData().toArray(new String[root.getData().size()]);
    }


    //Take document, detect first level, detect end of element, create element, parse element
    public void parse(){

      //  Matcher matcher = this.root.getType().getPattern().matcher(this.document[0]);
/*
        if(this.root.getType() == DocumentSectionType.ARTICLE && matcher.group(2) != null){

        }
*/
       // this.root.setIndex(matcher.group(1));



        Integer childStart = this.findNewElement(0, this.document.length - 1);
        if(childStart == null){
            return;
        }
        //System.out.println("Data ends = "+this.document[childStart]);

        this.root.removeData();
        this.copyTextToDocumentTree(this.root, 0, childStart - 1);

        DocumentSectionType childType = this.getType(childStart);

        do {
            Integer end = this.findNextSameLevelElement(childStart + 1, childType);
            //now we have boundaries of our new element, so let's grab it's index
            //we need appropriate matcher

            Matcher childMatcher = childType.getPattern().matcher(this.document[childStart]);
            childMatcher.find();
            DocumentTree child = new DocumentTree(childType, childMatcher.group(1), null);

            //now clear first line
            try {
                this.document[childStart] = childMatcher.group(2) + " " +childMatcher.group(3);
            }catch(IndexOutOfBoundsException e){
                this.document[childStart] = "";
            }
            //and copy it's text
            this.copyTextToDocumentTree(child, childStart, end - 1);
            root.addChild(child);
            //finally, parse it
            Parser childParser = new Parser(child);
            childParser.parse();

            childStart = end;
        }while(childStart != this.document.length);

    }

    public DocumentTree getRoot() {
        return root;
    }

    private void copyTextToDocumentTree(DocumentTree node, int start, int end){
        for(int i = start; i < this.document.length && i <= end; i++){
            node.appendData(this.document[i]);
        }
    }

    private Integer findNewElement(int from, int to){
        List<Matcher> matchers = new LinkedList<>();

        for(int i = from; i <= to; i++){
            String line = this.document[i];
            if(line == null || line.equals("")){ continue;}
            for (DocumentSectionType element : DocumentSectionType.values()){
                //System.out.println(element.getPattern().toString());
                matchers.add(element.getPattern().matcher(line));
            }

            Iterator<Matcher> matcher = matchers.iterator();

            while(matcher.hasNext()){
                try {
                    if (matcher.next().matches()) {
                        return i;
                    }
                }catch(NullPointerException e){
                    e.getSuppressed();
                }
            }
            matchers.clear();
        }
        return null;
    }

    private Integer findNextSameLevelElement(int start, DocumentSectionType type){
        Pattern typePattern = type.getPattern();
        for(int i = start; i < this.document.length; i++) {
            String line = this.document[i];
            Matcher typeMatcher = typePattern.matcher(line);
            if(typeMatcher.matches()){
                return i;
            }
        }
        return this.document.length;
    }

    private DocumentSectionType getType(int lineNumber){
        String line = this.document[lineNumber];

        for (DocumentSectionType type : DocumentSectionType.values()){
            if(type.getPattern().matcher(line).matches()){
                return type;
            }
        }
        return null;
    }

    public static boolean isNumeric(String str){
        return str.matches("^(\\d+)\\)?\\.?\\s*");  //match a number with optional bracket or dot and trailing spaces
    }

}
