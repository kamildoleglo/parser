package cs.agh.lab;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.function.DoubleConsumer;


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

    public void printTree() {
        printTree("", true);
    }

    private void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + this.type.toString() + " "+  this.index);
        for (int i = 0; i < children.size() - 1; i++) {
            DocumentTree child = (DocumentTree)this.children.get(i);
            child.printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            DocumentTree child = (DocumentTree)this.children.get(children.size() - 1);
                    child.printTree(prefix + (isTail ?"    " : "│   "), true);
        }
    }

    public void printTOC() {
        if(this.deeperThanOrEqual(DocumentSectionType.ARTICLE)){return;}

        String articlesRange = (this.type == DocumentSectionType.MAIN) ? "" : "(art. "+ this.firstArticle().getIndex() + " - " + this.lastArticle().getIndex() +")";
        String index = (this.type == DocumentSectionType.MAIN || this.type == DocumentSectionType.SUBCHAPTER) ? this.index : this.index+".";
        System.out.println(this.type.toString() + " " + index + " " + this.concatenateData() + " " + articlesRange);
        for (int i = 0; i < children.size(); i++) {
            DocumentTree child = (DocumentTree)this.children.get(i);
            child.printTOC();
        }
    }

    private DocumentTree firstArticle(){
        if(this.type == DocumentSectionType.ARTICLE){return this;}
        return ((DocumentTree)this.children.get(0)).firstArticle();
    }

    private DocumentTree lastArticle(){
        if(this.type == DocumentSectionType.ARTICLE){return this;}
        return ((DocumentTree)this.children.get(children.size() - 1)).lastArticle();
    }

    private String concatenateData(){
        StringBuilder concatenated = new StringBuilder(512);
        for(String line : this.data){
            concatenated.append(" ").append(line);
        }
        return concatenated.toString();
    }

    private boolean deeperThanOrEqual(DocumentSectionType type){
        if(this.type == type){ return true;}
        if(this.parent == null){ return false; }
        return ((DocumentTree)this.parent).deeperThanOrEqual(type);
    }

    public DocumentTree find(DocumentSectionType type, String index){
        if(this.type == type && this.index.equals(index)){return this;}
        for(Tree child : this.children){
            ((DocumentTree)child).find(type, index);
        }
        return null;
    }

    public DocumentTree shallowFind(DocumentSectionType type, String index){
        if(this.type == type && this.index == index){return this;}
        for(Tree child : this.children){
            if(((DocumentTree)child).getType() == type && ((DocumentTree)child).getIndex().equals(index)){
                return (DocumentTree)child;
            }
        }
        return null;
    }

    public void print(){
        System.out.print(this.type.toString() + " " + this.index);
        System.out.print((this.type == DocumentSectionType.POINT || this.type == DocumentSectionType.LITERAL) ? ")" : ".");
        System.out.println(this.concatenateData());
    }

    public void deepPrint(){
        this.print();
        for(Tree child : this.children){
            ((DocumentTree)child).deepPrint();
        }
    }
}
