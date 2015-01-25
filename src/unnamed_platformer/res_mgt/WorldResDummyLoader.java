package unnamed_platformer.res_mgt;


public class WorldResDummyLoader extends ResLoader {

	protected WorldResDummyLoader() {
		super("game", ".json");
	}

	@Override
	protected Object load(String name) throws Exception {
		throw new Exception("Cannot load worlds from a resource loader. Use World class internal methods.");
	}

}
