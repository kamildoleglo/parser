package cs.agh.lab;

import java.util.TreeMap;

public class DocumentTree {
    private final DocumentSectionType type;
    private final String number;
    private TreeMap<String, TreeMap> children = new TreeMap<>();

    public DocumentTree(DocumentSectionType type, String number){
        this.type = type;
        this.number = number;
    }



}
