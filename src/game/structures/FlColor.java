package game.structures;

public class FlColor {
	private float rgba[] = new float[] { 1f, 1f, 1f, 1f };

	public float[] rgb() {
		return new float[] { rgba[0], rgba[1], rgba[2] };
	}

	public float[] rgba() {
		return rgba;
	}
	
	public FlColor(float r, float g, float b, float a) {
		this.rgba = new float[] { r, g, b, a };
	}

	public FlColor(float r, float g, float b) {
		this.rgba = new float[] { r, g, b, 1f };
	}


	public void set(float r, float g, float b, float a) {
		this.rgba = new float[] { r, g, b, a };
	}

	public void set(float r, float g, float b) {
		this.rgba = new float[] { r, g, b, 1f };
	}

	public float r() {
		return rgba[0];
	}

	public float g() {
		return rgba[1];
	}

	public float b() {
		return rgba[2];
	}

	public float a() {
		return rgba[3];
	}
}
