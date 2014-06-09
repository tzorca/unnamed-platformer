package unnamed_platformer.game.structures;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;

import unnamed_platformer.app.App;
import unnamed_platformer.game.parameters.Ref.BlueprintField;
import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class Blueprint extends EnumMap<BlueprintField, Object> {
	private static final long serialVersionUID = -7682315731084504119L;

	private static transient FSTConfiguration conf = FSTConfiguration
			.createDefaultConfiguration();

	public Blueprint() {
		super(BlueprintField.class);
	}

	public boolean save(String filename) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
			FSTObjectOutput out = conf.getObjectOutput(stream);
			out.writeObject(this, Blueprint.class);
			out.flush();
			return true;
		} catch (Exception e) {
			App.print(e.toString());
			return false;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Blueprint load(String filename, boolean loadHeader) {
		Blueprint bp = null;
		FileInputStream stream = null;
		FSTObjectInput in = null;
		try {
			stream = new FileInputStream(filename);
			in = conf.getObjectInput(stream);
			bp = (Blueprint) in.readObject(Blueprint.class);
		} catch (Exception e) {
			App.print(e.toString());
		}

		// close stream
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			App.print(e.toString());
		}

		return bp;
	}

}
