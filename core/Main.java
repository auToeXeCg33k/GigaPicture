package core;

import features.*;
import formats.Format;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


public class Main {
    public static void main(String[] args) {
        File dir = new File("plugins");
        if (!dir.isDirectory())
            dir.mkdir();


        //containers for the plugins
        ArrayList<Class<Feature>> features = new ArrayList<>();
        HashSet<Class<Format>> formats = new HashSet<>();


        //add builtin features to featurelist
        Class<?> tmp = HorizontalReflection.class;
        features.add((Class<Feature>)tmp);
        tmp = VerticalReflection.class;
        features.add((Class<Feature>)tmp);
        tmp = RotateRight.class;
        features.add((Class<Feature>)tmp);
        tmp = RotateLeft.class;
        features.add((Class<Feature>)tmp);


        //load the plugins
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()})) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory() || !file.getName().endsWith(".class"))
                    continue;
                Class<?> temp = classLoader.loadClass(file.getName().substring(0, file.getName().length() - 6));
                if (Feature.class.isAssignableFrom(temp))
                    features.add((Class<Feature>) temp);
                if (Format.class.isAssignableFrom(temp))
                    formats.add((Class<Format>)temp);
            }
            //exceptions here (in theory) can only be caused by poorly implemented plugins. we just ignore those
        } catch (Exception ignored) {}


        //create window
        Window window = new Window(features, formats);
    }
}
