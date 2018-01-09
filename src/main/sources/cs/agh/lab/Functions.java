package cs.agh.lab;

import java.util.List;
import java.util.regex.Matcher;

public class Functions {

    private DocumentTree root;
    private String[] args;

    public  Functions(List<String> arguments){
        this.args = arguments.toArray(new String[arguments.size()]);
        if(FunctionTypes.HELP.is(this.args[0])){ this.printHelp(); return;}
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
                    startNode.deepPrint(endNode);
                    return;
                }
                DocumentTree article = this.root.find(DocumentSectionType.ARTICLE, this.args[4]);
                for(int i = 5; i < this.args.length; i += 2){
                    if(FunctionTypes.PARAGRAPH.is(this.args[i])){
                        article = article.find(DocumentSectionType.PARAGRAPH, this.args[i+1]);
                    }else if(FunctionTypes.POINT.is(this.args[i])){
                        article = article.find(DocumentSectionType.POINT, this.args[i+1]);
                    }else if(FunctionTypes.LITERAL.is(this.args[i])){
                        article = article.find(DocumentSectionType.LITERAL, this.args[i+1]);
                    }

                }
                article.deepPrint();
                return;
            }
        }else{
            this.printHelp();
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
        return null;
    }

    private void printTOC(DocumentTree node){
        node.printTOC();
    }

    private void printHelp(){
        String help = "\n" +
                "usage: parser -f|--filename file <mode> [<options> identifier] \n" +
                "\n" +
                "modes:\n" +
                "\t-t | --table-of-contents - prints table of contents for all document or selected chapter\n" +
                "\t-s | --show \t\t- prints selected element\n" +
                "\n" +
                "options:\n" +
                "\t-c | --chapter\t-prints contents of chapter\n" +
                "\t-cl | --chapter-long - prints chapter with subelements\n" +
                "\t-d | --section  -prints contents of section\n" +
                "\t-a | --article [<RANGE>] -prints article or range of articles. Range must be specified as <identifier>-<identifier>\n" +
                "\t\t-p | --paragraph -prints paragraph from specified article. Article must be given beforehand\n" +
                "\t\t-l | --literal\t -prints literal from specified article. Article must be given beforehand\n" +
                "\t\t-i | --point -prints point from specified article. Article must be given beforehand\n";
        System.out.println(help);
    }
}
