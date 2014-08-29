package unnamed_platformer.res_mgt;

import java.io.IOException;
import java.util.Collection;

import unnamed_platformer.globals.Ref;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.reflect.ClassPath;

public final class ClassLookup {

	// Data structures for quick lookup
	private static Multimap<String, Class<?>> classesInPackages = HashMultimap
			.create();
	private static Multimap<String, String> classNamesInPackages = HashMultimap
			.create();
	private static Table<String, String, Class<?>> classesByClassNameInPackages = HashBasedTable
			.create();

	// Initialize quick lookup structures
	static {
		ClassPath classpath = null;
		try {
			classpath = ClassPath.from(ClassLoader.getSystemClassLoader());
			for (ClassPath.ClassInfo classInfo : classpath.getAllClasses()) {
				// if (classInfo.getClass())
				try {
					String packageName = classInfo.getPackageName();

					if (!packageName.startsWith(Ref.BASE_PACKAGE_NAME)) {
						continue;
					}
					Class<?> clazz = classInfo.load();
					String className = classInfo.getSimpleName().toLowerCase();
					classesInPackages.put(packageName, clazz);
					classNamesInPackages.put(packageName, className);
					classesByClassNameInPackages.put(packageName, className,
							clazz);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static Collection<Class<?>> getClassesInPackage(String packageName) {
		return classesInPackages.get(packageName);
	}

	public static Collection<String> getClassNamesInPackage(String packageName) {
		return classNamesInPackages.get(packageName);
	}

	public static Class<?> getClass(String packageName, String className) {
		return classesByClassNameInPackages.get(packageName,
				className.toLowerCase());
	}

	public static boolean classExists(String packageName, String className) {
		return classesByClassNameInPackages.contains(packageName,
				className.toLowerCase());
	}

	public static Object instantiate(Class<?> clazz) {
		try {
			return clazz.getConstructor().newInstance();
		} catch (Exception e) {
			System.out.println("Error: Could not instantiate '" + clazz.getName()
					+ "': " + e.getMessage());
		}
		return null;
	}

	public static Object instantiate(String packageName, String className) {
		return instantiate(getClass(packageName, className.toLowerCase()));
	}
}
