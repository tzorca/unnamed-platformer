package unnamed_platformer.app;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public final class TimeManager
{

	private static long lastTime;

	public static void init() {
		lastTime = time();
	}

	public static long tick() {
		long time = time();
		long delta = time - lastTime;
		lastTime = time;
		return delta;
	}

	public static long time() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static final Map<Object, Long> SAMPLES = new HashMap<Object, Long>();

	public static long lastSample(Object obj) {
		if (!SAMPLES.containsKey(obj)) {
			return 0;
		}
		return SAMPLES.get(obj);
	}

	public static long sample(Object obj) {
		long time = time();
		SAMPLES.put(obj, time);
		return time;
	}

	private static Table<Object, String, Long> registeredPeriods = HashBasedTable
			.create();

	/**
	 * Register a (every ... seconds) period given a hashcode and a period ID
	 */
	private static void registerPeriod(Object obj, String periodId) {
		registeredPeriods.put(obj, periodId, time());
	}

	/**
	 * Returns true when a period of the specified length has finished since the
	 * last call to this method.
	 */
	public static boolean periodElapsed(Object obj, String periodId,
			float seconds) {
		if (!registeredPeriods.contains(obj, periodId)) {
			registerPeriod(obj, periodId);
		}
		long currTime = time();
		if ((currTime - registeredPeriods.get(obj, periodId)) / 1000f > seconds) {
			registeredPeriods.put(obj, periodId, currTime);
			return true;
		}
		return false;
	}

	public static float secondsSince(long comparisonTime) {
		return (time() - comparisonTime) / 1000f;
	}

	public static long millisecondsSince(long comparisonTime) {
		return (time() - comparisonTime);
	}
}
