package cs.agh.lab;

import java.util.ArrayList;


public class DocumentTree extends Tree<ArrayList<String>> {
    private final DocumentSectionType type;
    private String index;

    public DocumentTree(DocumentSectionType type, String index, String data) {
        this.data = new ArrayList<>();
        if(data != null){
            this.data.add(data);
        }
        this.type = type;
        this.index = index;
    }

    public void addChild(DocumentSectionType type, String index, String data) {
        DocumentTree newChild = new DocumentTree(type, index, data);
        newChild.setParent(this);
        this.children.add(newChild);
    }


    public DocumentSectionType getType() {
        return this.type;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index){
        this.index = index;
    }

    public void appendData(String newData){
        this.data.add(newData);
    }

    public void removeData(){
        this.data.clear();
    }
    // Generate table of contents
    // DFS in Tree, overload here
    // BFS in Tree, overload here

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + this.type.toString() + " "+  this.index);
        for (int i = 0; i < children.size() - 1; i++) {
            DocumentTree child = (DocumentTree)this.children.get(i);
            child.print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            DocumentTree child = (DocumentTree)this.children.get(children.size() - 1);
                    child.print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
}
