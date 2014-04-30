

package app;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;

public class TimeManager {

	private static long lastTime;
	@SuppressWarnings("unused")
	private static long totalDelta = 0;

	public static void init() {
		lastTime = time();
	}

	public static long tick() {
		long time = time();
		long delta = time - lastTime;
		totalDelta += delta;
		lastTime = time;
		return delta;
	}

	public static long time() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static Map<Integer, Long> samples = new HashMap<Integer, Long>();

	public static float lastSample(int hashCode) {
		if (!samples.containsKey(hashCode)) {
			return 0;
		}
		return samples.get(hashCode);
	}

	public static float sample(int hashCode) {
		long time = time();
		samples.put(hashCode, time);
		return time;
	}
}
