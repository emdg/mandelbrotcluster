package mandelbrot;

import util.SerializedImage;
import api.Result;
import api.Task;

public class MandelbrotResult extends Result<SerializedImage> {

	public MandelbrotResult(int seqNr, SerializedImage value) {
		super(seqNr, value);
	}

}
