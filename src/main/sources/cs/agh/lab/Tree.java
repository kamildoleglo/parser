package cs.agh.lab;

import java.util.ArrayList;
import java.util.List;

public class Tree<T>{
    protected T data;
    protected List<Tree> children = new ArrayList<>();
    protected Tree parent = null;

    public Tree() {}
    public Tree(T data) {
        this.data = data;
    }

    public void addChild(Tree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChildren(int i, List<Tree> children) {
        for(Tree t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void replaceChild(Tree element, List<Tree> replacement){
        for(int i = 0; i < this.children.size(); i++){
            Tree child = this.children.get(i);
            if(child == element){
                this.children.remove(i);
                this.addChildren(i, replacement);
            }
        }
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Tree getParent() {
        return parent;
    }

    protected void setParent(Tree parent) {
        this.parent = parent;
    }


}