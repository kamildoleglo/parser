package cs.agh.lab;

import java.util.List;

public class Main {

    public static void main(String args[]) {
        Functions.runAll(OptionsFilter.filter(args));
        String fileName = "src/main/resources/uokik.txt";
        DocumentParser documentParser = new DocumentParser(fileName);
        documentParser.parse();
        Tree root = documentParser.getRoot().getChildren().get(0);
        DocumentTree newRoot = (DocumentTree)root;
        //newRoot.printTree();
        //newRoot.printTOC();
        //newRoot.deepPrint();
    }
}
