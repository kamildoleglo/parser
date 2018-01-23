package cs.agh.lab;

import java.util.List;

public class Main {

    public static void main(String args[]) {

        List<String> argumentsList = OptionsFilter.filter(args);
        try {
            Functions arguments = new Functions(argumentsList);
            arguments.parse();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            if (e instanceof IndexOutOfBoundsException) System.out.println("Wrong arguments. For help see -h or --help");

        }

/*
        String fileName = "src/main/resources/konstytucja.txt";
        DocumentParser documentParser = new DocumentParser(fileName);
        documentParser.parse();
        Tree root = documentParser.getRoot().getChildren().get(0);
        DocumentTree newRoot = (DocumentTree)root;
        //newRoot.printTree();
        newRoot.printTOC();
        //newRoot.deepPrint(); */
    }
}
