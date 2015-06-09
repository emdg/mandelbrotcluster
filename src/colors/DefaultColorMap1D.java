package colors;

class DefaultColorMap1D implements ColorMap1D
{
    /**
     * The backing array containing the RGB colors
     */
    private final int colorMapArray[];

    /**
     * Creates a color map that is backed by the given array
     *
     * @param colorMapArray The array containing RGB colors
     */
    DefaultColorMap1D(int colorMapArray[])
    {
        this.colorMapArray = colorMapArray;
    }

    @Override
    public int getColor(double value)
    {
        double d = Math.max(0.0, Math.min(1.0, value));
        int i = (int)(d * (colorMapArray.length - 1));
        return colorMapArray[i];
    }
}