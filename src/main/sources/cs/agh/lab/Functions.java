package cs.agh.lab;

import java.util.List;
import java.util.regex.Matcher;

public class Functions {

    private DocumentTree root;
    private String[] args;

    public Functions(List<String> arguments) {
        this.args = arguments.toArray(new String[arguments.size()]);
        if (FunctionTypes.HELP.is(this.args[0])) {
            this.printHelp();
            return;
        }
        if (!FunctionTypes.OPEN_FILENAME.is(this.args[0])) {
            System.out.println("You did not pass filename as a first argument");
            return;
        }
        try {
            DocumentParser documentParser = new DocumentParser(this.args[1]);
            documentParser.parse();
            Tree root = documentParser.getRoot().getChildren().get(0);
            this.root = (DocumentTree) root;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            if (e instanceof NullPointerException) {
                System.out.println("File is corrupted or empty");
            } else {
                System.out.println("You did not pass the filename");
            }
            return;
        }
    }

    public void parse() {
        if (args.length < 2) return;
        if (FunctionTypes.PRINT_TOC.is(this.args[2])) {
            this.printTOC(this.getChapter(3));
        } else if (FunctionTypes.PRINT_ELEMENT.is(this.args[2])) {
            if (3 >= this.args.length) {
                this.root.deepPrint();
                return;
            }
            if (FunctionTypes.DEEP_PRINT_CHAPTER.is(this.args[3])) {
                this.getChapter(3).deepPrint();
                return;
            }
            if (FunctionTypes.ARTICLE.is(this.args[3])) {
                Matcher rangeMatcher = FunctionTypes.ARTICLE_RANGE.getPattern().matcher(args[4]);
                if (rangeMatcher.matches()) {
                    DocumentTree startNode = this.root.find(DocumentSectionType.ARTICLE, rangeMatcher.group(1));
                    DocumentTree endNode = this.root.find(DocumentSectionType.ARTICLE, rangeMatcher.group(2));
                    if (startNode == null || endNode == null) {
                        System.out.print("Cannot find specified range");
                    }
                    this.root.deepPrint(startNode, endNode);
                    return;
                }
                try {
                    DocumentTree article = this.root.find(DocumentSectionType.ARTICLE, this.args[4]);
                    for (int i = 5; i < this.args.length; i += 2) {
                        if (FunctionTypes.PARAGRAPH.is(this.args[i])) {
                            article = article.find(DocumentSectionType.PARAGRAPH, this.args[i + 1]);
                        } else if (FunctionTypes.POINT.is(this.args[i])) {
                            article = article.find(DocumentSectionType.POINT, this.args[i + 1]);
                        } else if (FunctionTypes.LITERAL.is(this.args[i])) {
                            article = article.find(DocumentSectionType.LITERAL, this.args[i + 1]);
                        }

                    }
                    article.deepPrint();
                } catch (NullPointerException e) {
                    System.out.println("Cannot find specified element :(");
                }
            }
        } else {
            this.printHelp();
        }
    }


    private DocumentTree getChapter(int index) {

        if (FunctionTypes.CHAPTER.is(args[index]) || FunctionTypes.DEEP_PRINT_CHAPTER.is(args[index])) {
            DocumentTree object = this.root.find(DocumentSectionType.CHAPTER, args[index + 1]);
            if(object == null){
                System.out.println("Cannot find chapter");
            }
            return object;
        } else {
            System.out.println("Wrong argument for chapter");
        }

        return null;
    }

    private void printTOC(DocumentTree node) {
        if (node == null) {
            System.out.println("Cannot find specified element, sorry :(");
            return;
        }
        node.printTOC();
    }

    private void printHelp() {
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
