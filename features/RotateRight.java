package features;

import java.util.ArrayList;
import java.util.List;

public class RotateRight implements Feature {

    @Override
    public String getLabel() {
        return "Rotate right 90";
    }

    @Override
    public void function(List<List<Integer>> pixels, Integer value) {
        int pWidth = pixels.size();
        int pHeight = pixels.get(0).size();

         List<List<Integer>> tmp = new ArrayList<>();

        for (int i = 0; i < pHeight; i++) {
            tmp.add(new ArrayList<>());
            for (int j = 0; j < pWidth; j++) {
                tmp.get(i).add(null);
            }
        }

        for ( int i = 0; i < pWidth; i++ )
            for ( int j = 0; j < pHeight; j++ )
                tmp.get(pHeight -1 -j).set(i, pixels.get(i).get(j));



        pixels.clear();
        pixels.addAll(tmp);

    }
}
