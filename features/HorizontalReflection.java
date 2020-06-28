package features;

import java.util.List;

public class HorizontalReflection implements Feature {

    @Override
    public String getLabel() {
        return "Horizontal flip";
    }

    @Override
    public void function(List<List<Integer>> pixels, Integer value) {
        int tmp = 0;
        for (int i = 0, j = pixels.size()-1; i < j; i++, j--) {
            for (int k = 0; k < pixels.get(i).size(); k++) {
                tmp = pixels.get(i).get(k);
                pixels.get(i).set(k, pixels.get(j).get(k));
                pixels.get(j).set(k, tmp);
            }
        }
    }

}
