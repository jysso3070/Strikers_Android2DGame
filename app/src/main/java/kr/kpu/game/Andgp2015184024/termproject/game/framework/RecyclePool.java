package kr.kpu.game.Andgp2015184024.termproject.game.framework;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclePool {
    private HashMap<Class, ArrayList<Object>> map = new HashMap<>();

    public RecyclePool() {
    }

    public void add(Object obj){
        Class objClass = obj.getClass();
    ArrayList<Object> list = map.get(objClass);
        if(list == null){
        list = new ArrayList<>();
        map.put(objClass, list);
    }
        list.add(obj);
}

    public  Object get(Class objClass){
        ArrayList<Object> list = map.get(objClass);
        if(list == null){
            return null;
        }
        if(list.size() == 0){
            return null;
        }
        return list.remove(0);
    }
}
