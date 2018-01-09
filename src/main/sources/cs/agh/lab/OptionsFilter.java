package cs.agh.lab;

import java.util.ArrayList;
import java.util.List;

public class OptionsFilter {

    public static List<String> filter(String[] arguments){
        List<String> args = new ArrayList<>(arguments.length);
        for(int i = 0; i < arguments.length; i++){
            String arg = arguments[i];
            if(arg.charAt(0) != '-'){
                args.add(arg);
                continue;
            }
            boolean flag = false;
            for(FunctionTypes argumentType : FunctionTypes.values()){
                if(argumentType.toArray()[0].equals(arg) || argumentType.toArray()[1].equals(arg)){
                    flag = true;
                    break;
                }
            }
            if(!flag){ System.out.println(arg + " is not a valid argument"); return null; }
            args.add(arg);
        }
        return args;
    }
/*
    private void valueCheck(int from){
        valueCheck(from, from);
    }
    private void valueCheck(int from, int to){
        boolean flag = true;
        for(int i = from; i <= to; i++){
            if(i >= this.args.length || this.args[i].charAt(0) == '-'){
                flag = false;
            }
        }
        if(!flag) {
            throw new IllegalArgumentException(args[from - 1] + " lacks appropriate value");
        }
    }*/
}
