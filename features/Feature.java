package features;

import java.util.List;

public interface Feature {
    String getLabel();
    void function(List<List<Integer>> pixels, Integer value);
}
