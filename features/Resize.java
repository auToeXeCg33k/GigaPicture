package features;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Resize implements Feature{
    @Override
    public String getLabel() {
        return "Resize";
    }


    public void function(List<List<Integer>> pixels, Integer value) {
        int newWidth = (int)(pixels.size()*Math.sqrt(1+(double)value/100));
        int newHeight = (int)(pixels.get(0).size()*Math.sqrt(1+(double)value/100));

        BufferedImage bufferdimage = new BufferedImage(pixels.size(), pixels.get(0).size(), 2);

        for (int i = 0; i < pixels.size(); i++)
            for (int j = 0; j < pixels.get(0).size(); j++)
                bufferdimage.setRGB(i, j, pixels.get(i).get(j));

        BufferedImage tmp = new BufferedImage(newWidth, newHeight, 2);
        tmp.getGraphics().drawImage(bufferdimage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST), 0, 0, null);

        pixels.clear();

        for (int i = 0; i < newWidth; i++) {
            pixels.add(new ArrayList<>());
            for (int j = 0; j < newHeight; j++) {
                pixels.get(i).add(tmp.getRGB(i, j));
            }
        }
    }
}
