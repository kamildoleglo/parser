package cs.agh.lab;


import java.util.*;

public class DocumentTree extends Tree<ArrayList<String>> {
    private final DocumentSectionType type;
    private String index;

    public DocumentTree(DocumentSectionType type, String index, String data) {
        this.data = new ArrayList<>();
        if (data != null) {
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

    public void setIndex(String index) {
        this.index = index;
    }

    public void appendData(String newData) {
        this.data.add(newData);
    }

    public void removeData() {
        this.data.clear();
    }

    public void printTree() {
        printTree("", true);
    }

    private void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + this.type.toString() + " " + this.index);
        for (int i = 0; i < children.size() - 1; i++) {
            DocumentTree child = (DocumentTree) this.children.get(i);
            child.printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            DocumentTree child = (DocumentTree) this.children.get(children.size() - 1);
            child.printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public void printTOC() {
        if (this.deeperThanOrEqual(DocumentSectionType.ARTICLE)) {
            return;
        }

        String articlesRange = (this.type == DocumentSectionType.MAIN) ? "" : "(art. " + this.firstArticle().getIndex() + " - " + this.lastArticle().getIndex() + ")";

        String index = (this.type == DocumentSectionType.MAIN || this.type == DocumentSectionType.SUBCHAPTER) ? this.index : this.index + ".";
        if (this.type == DocumentSectionType.MAIN) {
            System.out.println(this.type.toString() + " " + index + " " + this.concatenateData(1) + " " + articlesRange);
        } else {
            System.out.println(this.type.toString() + " " + index + " " + this.concatenateData() + " " + articlesRange);
        }
        for (int i = 0; i < children.size(); i++) {
            DocumentTree child = (DocumentTree) this.children.get(i);
            child.printTOC();
        }
    }

    public DocumentTree firstArticle() {
        if (this.type == DocumentSectionType.ARTICLE) {
            return this;
        }

        return ((DocumentTree) this.children.get(0)).firstArticle();
    }

    public DocumentTree lastArticle() {
        if (this.type == DocumentSectionType.ARTICLE) {
            return this;
        }
        return ((DocumentTree) this.children.get(children.size() - 1)).lastArticle();
    }

    private String concatenateData() {
        return concatenateData(this.data.size());
    }

    private String concatenateData(int elements) {
        StringBuilder concatenated = new StringBuilder(512);
        for (int i = 0; i < elements; i++) {
            String line = this.data.get(i);
            concatenated.append(line);
        }
        return concatenated.toString();
    }

    private boolean deeperThanOrEqual(DocumentSectionType type) {
        if (this.type == type) {
            return true;
        }
        if (this.parent == null) {
            return false;
        }
        return ((DocumentTree) this.parent).deeperThanOrEqual(type);
    }

    private boolean deeperThan(DocumentSectionType type) {
        if (this.parent == null) {
            return false;
        }
        if (((DocumentTree) this.parent).getType() == type) {
            return true;
        }
        return ((DocumentTree) this.parent).deeperThan(type);
    }

    public DocumentTree find(DocumentSectionType type, String index) {
        if (this.type == type && this.index.equals(index)) {
            return this;
        }
        DocumentTree node = null;
        for (Tree child : this.children) {
            if (((DocumentTree) child).find(type, index) != null) {
                node = ((DocumentTree) child).find(type, index);
                return node;
            }
        }
        return node;
    }

    public DocumentTree shallowFind(DocumentSectionType type, String index) {
        if (this.type == type && this.index.equals(index)) {
            return this;
        }
        for (Tree child : this.children) {
            if (((DocumentTree) child).getType() == type && ((DocumentTree) child).getIndex().equals(index)) {
                return (DocumentTree) child;
            }
        }
        return null;
    }

    public void print() {
        System.out.print(this.type.toString() + " " + this.index);
        System.out.print((this.type == DocumentSectionType.POINT || this.type == DocumentSectionType.LITERAL) ? ")" : ".");
        System.out.println(this.concatenateData());
    }

    private Integer findIndex(DocumentTree node) {
        DocumentTree startParent = (DocumentTree) this.parent;
        Tree[] children = startParent.getChildren().toArray(new Tree[startParent.getChildren().size()]);
        for (int i = 0; i < children.length; i++) {
            if ((children[i]).equals(node)) {
                return i;
            }
        }
        return null;
    }

    public void deepPrint(DocumentTree start, DocumentTree end) {
        deepPrint(start, end, false, false);
    }

    public boolean[] deepPrint(DocumentTree start, DocumentTree end, boolean started, boolean ended) {
        if (this.equals(start)) started = true;
        if (started && !ended) this.deepPrint();
        if (this.equals(end)) ended = true;


        for (Tree child : this.children) {
            boolean[] flags = ((DocumentTree) child).deepPrint(start, end, started, ended);
            if (flags[0]) started = true;
            if (flags[1]) ended = true;
        }
        return new boolean[]{started, ended};
    }


    public void deepPrint(){
        this.deepPrint("");
    }
    public void deepPrint(String prefix) {
        System.out.print(prefix);
        this.print();
        for (Tree child : this.children) {
            ((DocumentTree) child).deepPrint(prefix + "  ");
        }
    }

}
