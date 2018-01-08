package cs.agh.lab;

import java.util.regex.Pattern;

/*private Pattern articlePattern = Pattern.compile("^(Art)\\.\\s+([0-9]+[A-z]*)\\.\\s+([0-9]+)?\\.?(.*)");
private Pattern chapterPattern = Pattern.compile("^(Rozdział)\\s+([0-9|A-z]+)");
private Pattern sectionPattern = Pattern.compile("^(DZIAŁ)\\s+([0-9|A-z]+)");

private Pattern uppercaseTextPattern = Pattern.compile("^[\\p{Lu}]+ .*$");
private Pattern paragraphPattern = Pattern.compile("^(\\d+)\\..*");
private Pattern pointPattern = Pattern.compile("^(\\d+)\\).*");
private Pattern literalPattern = Pattern.compile("^([a-z])\\).*");
*/
public enum DocumentSectionType {
    MAIN, ARTICLE, SECTION, CHAPTER, SUBCHAPTER, PARAGRAPH, POINT, LITERAL;


    public String toString(){
        switch(this){
            case ARTICLE:
                return "Art.";
            case CHAPTER:
                return "Rozdział";
            case SECTION:
                return "Dział";
            default:
                return "";
        }
    }

    public Pattern getPattern(){
        switch(this){
            case ARTICLE:
                return Pattern.compile("^Art\\.\\s+([0-9]+[A-z]*)\\.\\s*([0-9]+\\.)?(.*)");
            case CHAPTER:
                return Pattern.compile("^Rozdział\\s+([0-9|A-z]+)");
            case SECTION:
                return Pattern.compile("^DZIAŁ\\s+([0-9|A-z]+)");
            case PARAGRAPH:
                return Pattern.compile("^(\\d+)\\.()(.*)");
            case POINT:
                return Pattern.compile("^(\\d+)\\)()(.*)");
            case LITERAL:
                return Pattern.compile("^([a-z])\\)()(.*)");
            case MAIN:
                return Pattern.compile("((?:KONSTYTUCJA|USTAWA).*)");
            case SUBCHAPTER:
                return Pattern.compile("^([\\p{Lu}\\s,]+)\\s*$");
            default:
                return null;
        }
    }

}
