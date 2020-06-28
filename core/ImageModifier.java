package core;

import exceptions.GigaException;
import features.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ImageModifier {
    private final HashMap<String, Feature> modifiers;


    //store data for builtins
    public ImageModifier() {
        modifiers = new HashMap<>();
        Feature temp = new HorizontalReflection();
        modifiers.put(temp.getLabel(), temp);
        temp = new VerticalReflection();
        modifiers.put(temp.getLabel(), temp);
        temp = new RotateLeft();
        modifiers.put(temp.getLabel(), temp);
        temp = new RotateRight();
        modifiers.put(temp.getLabel(), temp);
        temp = new Resize();
        modifiers.put(temp.getLabel(), temp);
    }


    //load plugins
    public void loadAddons(ArrayList<Class<Feature>> features) {
        for (Class<Feature> clazz : features)
            try {
                Feature temp = clazz.getDeclaredConstructor().newInstance();
                modifiers.put(temp.getLabel(), temp);
                //we ignore the exception here because the window will also run into it and handle it using a popup error message
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
    }


    //do the job
    public BufferedImage modify(String featureName, BufferedImage display, Integer value) throws GigaException {
        if (display == null)
            throw new GigaException("Nothingness cannot be edited!", false);

        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < display.getWidth(); i++) {
            list.add(new ArrayList<>());
            for (int j = 0; j < display.getHeight(); j++)
                list.get(i).add(display.getRGB(i, j));
        }

        try {
            modifiers.get(featureName).function(list, value);
        } catch (AbstractMethodError abstractMethodError) {
            throw new GigaException("There was a problem with the plugin: " + modifiers.get(featureName).getLabel(), true);
        }


        display = new BufferedImage(list.size(), list.get(0).size(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < list.size(); i++)
            for (int j = 0; j < list.get(0).size(); j++)
                display.setRGB(i, j, list.get(i).get(j));

        return display;
    }
}
