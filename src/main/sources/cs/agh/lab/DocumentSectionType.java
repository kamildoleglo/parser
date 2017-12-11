package cs.agh.lab;

public enum DocumentSectionType {
    MAIN, ARTICLE, SECTION, SUBSECTION, CHAPTER, POINT, SUBPOINT, CHARACTER;


    static DocumentSectionType toEnum(String name){
        switch(name.toLowerCase()){
            case "art":
                return ARTICLE;
            case "rozdział":
                return CHAPTER;
            case "dział":
                return SECTION;
            default:
                return null;
        }
    }

}
