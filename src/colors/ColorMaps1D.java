package colors;

import java.awt.Color;
import java.util.Arrays;

public class ColorMaps1D
{
    /**
     * Creates a {@link ColorMap1D} that walks through the given delegate
     * color map using a sine function with the given frequency
     *
     * @param delegate The delegate
     * @param frequency The frequency
     * @return The new {@link ColorMap1D}
     */
    static ColorMap1D createSine(ColorMap1D delegate, final double frequency)
    {
        return create(delegate, new DoubleFunction<Double>()
        {
            @Override
            public Double apply(double value)
            {
                return 0.5 + 0.5 * Math.sin(value * frequency);
            }
        });
    }

    /**
     * Creates a {@link ColorMap1D} that will convert the argument
     * with the given function before it is looking up the color
     * in the given delegate
     *
     * @param delegate The delegate {@link ColorMap1D}
     * @param function The function for converting the argument
     * @return The new {@link ColorMap1D}
     */
    static ColorMap1D create(
        final ColorMap1D delegate, final DoubleFunction<Double> function)
    {
        return new ColorMap1D()
        {
            @Override
            public int getColor(double value)
            {
                return delegate.getColor(function.apply(value));
            }
        };
    }


    /**
     * Creates a new ColorMap1D that maps a value between 0.0 and 1.0
     * (inclusive) to the specified color range, internally using the
     * given number of steps for interpolating between the colors
     *
     * @param steps The number of interpolation steps
     * @param colors The colors
     * @return The color map
     */
   public static ColorMap1D createDefault(int steps, Color ... colors)
    {
        return new DefaultColorMap1D(initColorMap(steps, colors));
    }

    /**
     * Creates the color array which contains RGB colors as integers,
     * interpolated through the given colors.
     *
     * @param steps The number of interpolation steps, and the size
     * of the resulting array
     * @param colors The colors for the array
     * @return The color array
     */
    public static int[] initColorMap(int steps, Color ... colors)
    {
        int colorMap[] = new int[steps];
        if (colors.length == 1)
        {
            Arrays.fill(colorMap, colors[0].getRGB());
            return colorMap;
        }
        double colorDelta = 1.0 / (colors.length - 1);
        for (int i=0; i<steps; i++)
        {
            double globalRel = (double)i / (steps - 1);
            int index0 = (int)(globalRel / colorDelta);
            int index1 = Math.min(colors.length-1, index0 + 1);
            double localRel = (globalRel - index0 * colorDelta) / colorDelta;

            Color c0 = colors[index0];
            int r0 = c0.getRed();
            int g0 = c0.getGreen();
            int b0 = c0.getBlue();
            int a0 = c0.getAlpha();

            Color c1 = colors[index1];
            int r1 = c1.getRed();
            int g1 = c1.getGreen();
            int b1 = c1.getBlue();
            int a1 = c1.getAlpha();

            int dr = r1-r0;
            int dg = g1-g0;
            int db = b1-b0;
            int da = a1-a0;

            int r = (int)(r0 + localRel * dr);
            int g = (int)(g0 + localRel * dg);
            int b = (int)(b0 + localRel * db);
            int a = (int)(a0 + localRel * da);
            int rgb =
                (a << 24) |
                (r << 16) |
                (g <<  8) |
                (b <<  0);
            colorMap[i] = rgb;
        }
        return colorMap;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ColorMaps1D()
    {
        // Private constructor to prevent instantiation
    }
}