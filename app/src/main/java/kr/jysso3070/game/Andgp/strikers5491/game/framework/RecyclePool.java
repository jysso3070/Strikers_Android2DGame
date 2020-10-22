package kr.jysso3070.game.Andgp.strikers5491.game.framework;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclePool {
    private HashMap<Class, ArrayList<Object>> objectsPool = new HashMap<>();

    public RecyclePool() { }

    public void add(Object obj){
        Class classObj = obj.getClass();
        ArrayList<Object> objectsList = objectsPool.get(classObj);
        if(objectsList == null){
            objectsList = new ArrayList<>();
            objectsPool.put(classObj, objectsList);
        }
        objectsList.add(obj);
}

    public Object get(Class objClass){
        ArrayList<Object> objectsList = objectsPool.get(objClass);
        if(objectsList == null){ return null; }
        if(objectsList.size() == 0){ return null; }
        return objectsList.remove(0);
    }
}
