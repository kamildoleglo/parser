package cs.agh.lab;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentParser {
    private String[] document;
    private DocumentTree root;
    private boolean joinChapterWithChapterNames = false;

    public DocumentParser(String fileName){
        this.document = DocumentFilter.filter(fileName);
        this.root = new DocumentTree(DocumentSectionType.MAIN, null, null);
        Integer childStart = this.findNewElement(0, this.document.length - 1);
        if(this.document[childStart].contentEquals("KONSTYTUCJA") && childStart+1 < this.document.length){
            this.document[childStart] += " " + this.document[childStart+1];
            this.document[childStart+1] = "";
            this.joinChapterWithChapterNames = true;
        }
        this.removeTrailingDashes();
    }

    public DocumentParser(DocumentTree root, boolean join){
        this.root = root;
        this.joinChapterWithChapterNames = join;
        this.document = root.getData().toArray(new String[root.getData().size()]);
    }


    //Take document, detect first level, detect end of element, create element, parse element
    public void parse(){

        Integer childStart = this.findNewElement(0, this.document.length - 1);
        if(childStart == null){
            return;
        }

        this.root.removeData();
        this.copyTextToDocumentTree(this.root, 0, childStart - 1);

        DocumentSectionType childType = this.getType(childStart);

        do {
            Integer end = this.findNextSameLevelElement(childStart + 1, childType);
            //now we have boundaries of our new element, so let's grab it's index
            //we need appropriate matcher

            Matcher childMatcher = childType.getPattern().matcher(this.document[childStart]);
            childMatcher.find();
            DocumentTree child = new DocumentTree(childType, childMatcher.group(1), null);

            //now clear first line
            try{
                String group2 = childMatcher.group(2);
                this.document[childStart] = group2 != null ? group2 + " " : "";
            }catch(IndexOutOfBoundsException e){
                this.document[childStart] = "";
            }
            try {
                this.document[childStart] += childMatcher.group(3);
            }catch(IndexOutOfBoundsException e){
                this.document[childStart] += "";
            }
            //and copy it's text
            this.copyTextToDocumentTree(child, childStart, end - 1);
            root.addChild(child);
            //finally, parse it
            DocumentParser childParser = new DocumentParser(child, joinChapterWithChapterNames);
            childParser.parse();

            childStart = end;
        }while(childStart != this.document.length);

        if(this.root.getType() == DocumentSectionType.CHAPTER && this.joinChapterWithChapterNames){
            DocumentTree nameNode = (DocumentTree)this.root.getChildren().get(0);
            this.root.removeData();
            this.root.appendData(nameNode.getIndex());
            this.root.replaceChild(nameNode, nameNode.getChildren());
        }

    }

    public DocumentTree getRoot() {
        return root;
    }

    private void copyTextToDocumentTree(DocumentTree node, int start, int end){
        for(int i = start; i < this.document.length && i <= end; i++){
            if(this.document[i] == null || this.document[i].trim().length() < 1){continue;}
            node.appendData(this.document[i]);
        }
    }

    private Integer findNewElement(int from, int to){
        List<Matcher> matchers = new LinkedList<>();

        for(int i = from; i <= to; i++){
            String line = this.document[i];
            if(line == null || line.equals("")){ continue;}
            for (DocumentSectionType element : DocumentSectionType.values()){
                matchers.add(element.getPattern().matcher(line));
            }

            Iterator<Matcher> matcher = matchers.iterator();

            while(matcher.hasNext()){
                try {
                    if (matcher.next().matches()) {
                        return i;
                    }
                }catch(NullPointerException e){
                    e.getSuppressed();
                }
            }
            matchers.clear();
        }
        return null;
    }

    private Integer findNextSameLevelElement(int start, DocumentSectionType type){
        Pattern typePattern = type.getPattern();
        for(int i = start; i < this.document.length; i++) {
            String line = this.document[i];
            Matcher typeMatcher = typePattern.matcher(line);
            if(typeMatcher.matches()){
                return i;
            }
        }
        return this.document.length;
    }

    private DocumentSectionType getType(int lineNumber){
        String line = this.document[lineNumber];

        for (DocumentSectionType type : DocumentSectionType.values()){
            if(type.getPattern().matcher(line).matches()){
                return type;
            }
        }
        return null;
    }
    private void removeTrailingDashes(){
        for(int i = 0; i < this.document.length; i++){
            this.document[i] = this.document[i].replaceAll("-+$", "");
        }
    }

    public static boolean isNumeric(String str){
        return str.matches("^(\\d+)\\)?\\.?\\s*");  //match a number with optional bracket or dot and trailing spaces
    }

}
