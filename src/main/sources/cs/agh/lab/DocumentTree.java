package cs.agh.lab;

import java.util.TreeMap;

public class DocumentTree {
    private String name;
    private TreeMap<String, TreeMap> children = new TreeMap<>();

    public DocumentTree(String name){
        this.name = name;
    }

}
