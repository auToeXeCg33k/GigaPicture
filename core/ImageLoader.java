package core;

import formats.*;
import exceptions.GigaException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;


public class ImageLoader {
    HashSet<Class<Format>> formats;


    //add data of builtins
    public ImageLoader(HashSet<Class<Format>> formats) {
        this.formats = new HashSet<>();
        this.formats.addAll(formats);
        Class<?> temp = PNG.class;
        this.formats.add((Class<Format>) temp);
        temp = JPG.class;
        this.formats.add((Class<Format>) temp);
        temp = BMP.class;
        this.formats.add((Class<Format>) temp);
    }


    //do the job
    BufferedImage load(File file) throws GigaException {
        for (Class<Format> clazz : formats)
            if ((clazz.getName().contains(".")) ? file.getName().toLowerCase().endsWith(clazz.getName().toLowerCase().substring(clazz.getName().toLowerCase().lastIndexOf("."))) : file.getName().toLowerCase().endsWith(clazz.getName().toLowerCase()))
                try {
                    Format imageData = clazz.getDeclaredConstructor(String.class).newInstance(file.getAbsolutePath());
                    BufferedImage img = new BufferedImage(imageData.getWidth(), imageData.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    for (int i = 0; i < img.getWidth(); i++)
                        for (int j = 0; j < img.getHeight(); j++)
                            img.setRGB(i, j, imageData.getPixel(i, j));
                    return img;
                } catch (InvocationTargetException f) {
                    throw (GigaException)f.getTargetException();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException f) {
                    throw new GigaException("Error loading the image using plugin: " + clazz.getName() + ". The addon does not meet the necessary conditions!", true);
                }
        throw new GigaException("Unknown file format: " + file.getName().substring(file.getName().lastIndexOf(".") + 1), true);
    }
}
