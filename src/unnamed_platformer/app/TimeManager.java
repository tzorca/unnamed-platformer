package unnamed_platformer.app;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class TimeManager {

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

	public static final Map<Integer, Long> samples = new HashMap<Integer, Long>();

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

	private static Table<Integer, Integer, Long> registeredPeriods = HashBasedTable.create();

	/**
	 * Register a (every ... seconds) period given a hashcode and a period ID
	 */
	public static void registerPeriod(int hashCode, int periodId) {
		registeredPeriods.put(hashCode, periodId, time());
	}

	/**
	 * Returns true when a period of the specified length has finished since the
	 * last call to this method.
	 */
	public static boolean periodElapsed(int hashCode, int periodId, float seconds) {
		if (!registeredPeriods.contains(hashCode, periodId)) {
			registerPeriod(hashCode, periodId);
		}
		long currTime = time();
		if ((currTime - registeredPeriods.get(hashCode, periodId))/1000f > seconds) {
			registeredPeriods.put(hashCode, periodId, currTime);
			return true;
		}
		return false;
	}
}
