package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Select {

    Map<String, Map<String, Integer>> annotationsMap;

    public Select() {
        annotationsMap = new HashMap<>();
    }

    public void init(List<String[]> annotations) {
        for (String[] annotation : annotations) {

            Map<String, Integer> subannotationsMap = annotationsMap.get(annotation[0]);
            if (subannotationsMap == null) annotationsMap.put(annotation[0], subannotationsMap = new HashMap<>());
            subannotationsMap.put(annotation[1], Integer.parseInt(annotation[2]));
        }
    }

    public Map<String, Map<String, Integer>> getAnnotationsMap() {
        return annotationsMap;
    }

}
