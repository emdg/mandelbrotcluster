package colors;

public interface ColorMap1D
{
    /**
     * Returns an int representing the RGB color, for the given value in [0,1]
     *
     * @param value The value in [0,1]
     * @return The RGB color
     */
    public int getColor(double value);
}