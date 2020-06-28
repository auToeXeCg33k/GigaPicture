package formats;

public interface Format {
    int getWidth();
    int getHeight();
    int getPixel(int i, int j);
}
