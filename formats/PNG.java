package formats;

import exceptions.GigaException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PNG implements Format {
    private final BufferedImage bufferedImage;
    private final int width;
    private final int height;


    public PNG(String filename) throws GigaException {
        try {
            bufferedImage = ImageIO.read(new File(filename));
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (IOException e) {
            throw new GigaException("There was an error loading your image!", true);
        }
    }


    @Override
    public int getWidth() {
        return width;
    }


    @Override
    public int getHeight() {
        return height;
    }


    @Override
    public int getPixel(int i, int j) {
        return bufferedImage.getRGB(i, j);
    }
}

