package features;

import java.util.List;

public class VerticalReflection implements Feature{

    @Override
    public String getLabel() {
        return "Vertical flip";
    }

    @Override
    public void function(List<List<Integer>> pixels, Integer value) {
        int tmp = 0;
        for (int i = 0; i < pixels.size(); i++) {
            for (int j = 0,  k = pixels.get(i).size()-1; j < k; j++, k--) {
                tmp = pixels.get(i).get(j);
                pixels.get(i).set(j, pixels.get(i).get(k));
                pixels.get(i).set(k, tmp);
            }
        }
    }
}
