package cs.agh.lab;

import java.util.List;
import java.util.function.DoubleConsumer;

public class Main {

    public static void main(String args[]) {
        String fileName = "src/main/resources/uokik.txt";
        Parser parser = new Parser(fileName);
        parser.parse();
        Tree root = parser.getRoot().getChildren().get(0);
        DocumentTree newRoot = (DocumentTree)root;
        newRoot.print();
      /*  for(Object node : root.getChildren()){
            DocumentTree newNode = (DocumentTree)node;
            System.out.println(newNode.getType().toString()+" "+newNode.getData());
        }*/
        //System.out.println(root.getData());
    }
}
