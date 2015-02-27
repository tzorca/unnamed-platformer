package unnamed_platformer.game.editor;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class CategoryLookup
{
	private static Multimap<String, String> categoryToTextures = HashMultimap.create(); 
	private static Map<String, String> texturesToCategories = Maps.newHashMap(); 
	
	public static void map(String category, String texture) {
		categoryToTextures.put(category, texture);
		texturesToCategories.put(texture,  category);
	}
	
	public static Collection<String> listCategories() {
		return categoryToTextures.keySet();
	}
	
	public static Collection<String> getTextures(String category){
		return categoryToTextures.get(category);
	}
	
	public static String getCategory(String texture) {
		return texturesToCategories.get(texture);
	}
}
