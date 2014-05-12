package app;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;

public class TimeManager {

	private static long lastTime;
	@SuppressWarnings("unused")
	private static long totalMilliseconds = 0;

	public static void init() {
		lastTime = time();
	}

	public static long tick() {
		long time = time();
		long delta = time - lastTime;
		totalMilliseconds += delta;
		lastTime = time;
		return delta;
	}

	public static long time() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static Map<Integer, Long> samples = new HashMap<Integer, Long>();

	public static long lastSample(int hashCode) {
		if (!samples.containsKey(hashCode)) {
			return 0;
		}
		return samples.get(hashCode);
	}

	public static long sample(int hashCode) {
		long time = time();
		samples.put(hashCode, time);
		return time;
	}
}
