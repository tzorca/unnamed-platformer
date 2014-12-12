package unnamed_platformer.game.other;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.entities.Entity;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class SpatialHash
{
	private final static int BOX_SIZE = 96;
	private final static int PADDING = 1;
	
	 private static HashMap<Entity, List<Pair<Integer, Integer>>>
	 previousHashes = Maps
	 .newHashMap();

	private static Multimap<Pair<Integer, Integer>, Entity> map = HashMultimap
			.create();

	public static void remove(Entity entity) {
		removePreviousHash(entity);
	}

	public static boolean has(Entity entity) {
		return previousHashes.containsKey(entity);
	}
	
	public static void insert(Entity entity) {
		List<Pair<Integer, Integer>> entityHash = getEntityHashPoints(entity, PADDING);

		removePreviousHash(entity);

		for (Pair<Integer, Integer> pointHash : entityHash) {
			map.put(pointHash, entity);
		}
		previousHashes.put(entity, entityHash);
	}
	

	public static void clear() {
		previousHashes.clear();
		map.clear();
	}

	public static Collection<Entity> getNearbyEntities(Entity entity) {
		List<Pair<Integer, Integer>> entityHash = getEntityHashPoints(entity, PADDING);
		Set<Entity> nearbyEntities = Sets.newHashSet();

		for (Pair<Integer, Integer> hash : entityHash) {
			if (map.containsKey(hash)) {
				nearbyEntities.addAll(map.get(hash));
			}
		}

		return nearbyEntities;
	}

	private static void removePreviousHash(Entity entity) {
		if (previousHashes.containsKey(entity)) {
			List<Pair<Integer, Integer>> previousEntityHash = previousHashes
					.get(entity);
			for (Pair<Integer, Integer> pointHash : previousEntityHash) {
				map.remove(pointHash, entity);
			}
		}
	}

	private static List<Pair<Integer, Integer>> getEntityHashPoints(
			Entity entity, int padding) {

		Rectangle box = entity.getOriginalBox();

		return hashRange((int) box.getMinX() - padding, (int) box.getMaxX()
				+ padding, (int) box.getMinY() - padding, (int) box.getMaxY()
				+ padding);
	}

	private static List<Pair<Integer, Integer>> hashRange(int minX, int maxX,
			int minY, int maxY) {

		minX /= BOX_SIZE;
		maxX /= BOX_SIZE;
		minY /= BOX_SIZE;
		maxY /= BOX_SIZE;

		List<Pair<Integer, Integer>> hashes = Lists.newArrayList();
		Set<Integer> xRange = ContiguousSet.create(Range.closed(minX, maxX),
				DiscreteDomain.integers());
		Set<Integer> yRange = ContiguousSet.create(Range.closed(minY, maxY),
				DiscreteDomain.integers());

		for (int x : xRange) {
			for (int y : yRange) {
				hashes.add(Pair.of(x, y));
			}
		}
		return hashes;
	}




}
