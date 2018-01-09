package cs.agh.lab;

import java.util.regex.Pattern;

public enum FunctionTypes {
    HELP, OPEN_FILENAME, PRINT_TOC, ARTICLE_RANGE, PRINT_ELEMENT, DEEP_PRINT_CHAPTER, ARTICLE, CHAPTER, SECTION, LITERAL, PARAGRAPH, POINT ;

    public String toString(){
        switch(this){
            case HELP:
                return "-h";
            case OPEN_FILENAME:
                return "-f";
            case PRINT_TOC:
                return "-t";
            case PRINT_ELEMENT:
                return "-s";
            case DEEP_PRINT_CHAPTER:
                return "-cl";
            case ARTICLE:
                return "-a";
            case CHAPTER:
                return "-c";
            case SECTION:
                return "-d";
            case LITERAL:
                return "-l";
            case PARAGRAPH:
                return "-p";
            case POINT:
                return "-i";
            default:
                return "";
        }
    }

    public String[] toArray(){
        String[] array = new String[2];
        array[0] = this.toString();
        array[1] = this.toLongString();
        return array;
    }

    public String toLongString(){
        switch(this){
            case HELP:
                return "--help";
            case OPEN_FILENAME:
                return "--filename";
            case PRINT_TOC:
                return "--table-of-contents";
            case PRINT_ELEMENT:
                return "--show";
            case DEEP_PRINT_CHAPTER:
                return "-chapter-long";
            case ARTICLE:
                return "--article";
            case CHAPTER:
                return "--chapter";
            case SECTION:
                return "--section";
            case LITERAL:
                return "--literal";
            case PARAGRAPH:
                return "--paragraph";
            case POINT:
                return "--point";
            default:
                return "";
        }
    }

    public boolean is(String arg){
        if(this.toLongString().equals(arg) || this.toString().equals(arg)){ return true; }
        return false;
    }

    public Pattern getPattern(){
        if(this == ARTICLE_RANGE) return Pattern.compile("^(\\w|\\d)+-(\\w|\\d)+\\s?$");
        return null;
    }
}
