package Main;

import java.util.Comparator;
import java.util.Map;

class MapComparator implements Comparator<Map> {

    @Override
    public int compare(Map o1, Map o2) {
        if ((int)o1.get("angle") < (int) o2.get("angle")){
            return 1;
        }else {
            return -1;
        }
    }
}