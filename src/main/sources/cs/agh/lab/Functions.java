package cs.agh.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static cs.agh.lab.FunctionTypes.*;

public class Functions {
    private DocumentTree root;
    private String[] args;


    public void Functions(List<String> arguments){
        this.args = arguments.toArray(new String[arguments.size()]);
        if(!FunctionTypes.OPEN_FILENAME.is(this.args[0])){
            throw new RuntimeException("You did not pass filename as a first argument");
        }
        try {
            DocumentParser documentParser = new DocumentParser(this.args[1]);
            documentParser.parse();
            Tree root = documentParser.getRoot().getChildren().get(0);
            this.root = (DocumentTree) root;
            }catch(IndexOutOfBoundsException | NullPointerException e){
                if (e instanceof NullPointerException) {
                    throw new RuntimeException("File is corrupted or empty");
                }else{
                    throw new IllegalArgumentException("You did not pass the filename");
                }
            }
    }

    public void parse(){
        String arg = this.args[2];
        if(FunctionTypes.PRINT_TOC.is(this.args[2])){
            this.printTOC(this.getChapter(3));
            return;
        }else if(FunctionTypes.PRINT_ELEMENT.is(this.args[2])){
            if(FunctionTypes.DEEP_PRINT_CHAPTER.is(this.args[3])){
                this.getChapter(4).deepPrint();
                return;
            }
            if(FunctionTypes.ARTICLE.is(this.args[3])){
                Matcher rangeMatcher = FunctionTypes.ARTICLE_RANGE.getPattern().matcher(args[4]);
                if(rangeMatcher.matches()){
                    DocumentTree startNode = this.root.find(DocumentSectionType.ARTICLE, rangeMatcher.group(1));
                    DocumentTree endNode = this.root.find(DocumentSectionType.ARTICLE, rangeMatcher.group(2));
                }else{
                    this.root.find(DocumentSectionType.ARTICLE, this.args[4]);
                }

            }

        }



        for (FunctionTypes argumentType : FunctionTypes.values()) {
            if (argumentType.toArray()[0].equals(arg) || argumentType.toArray()[1].equals(arg)) {
                try {
                    if (argumentType == PRINT_TOC) {
                        this.printTOC(this.getChapter(3));
                        return;
                    }
                    if (argumentType == PRINT_ELEMENT){

                    }
                        break;

                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("No value supplied for ");

                }
            }
        }
    }
    private DocumentTree getChapter(int index){
        try {
            if (FunctionTypes.CHAPTER.is(args[index])) {
                return this.root.find(DocumentSectionType.CHAPTER, args[index+1]);
            }
        }catch(IndexOutOfBoundsException e){
            return this.root;
        }
    }

    public void printTOC(DocumentTree node){
        node.printTOC();
    }
}
