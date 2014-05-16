package game.structures;

import game.parameters.Ref.BlueprintComponent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.EnumMap;

import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class Blueprint extends EnumMap<BlueprintComponent, Object> {
	private static final long serialVersionUID = -7682315731084504119L;

	private static transient FSTConfiguration conf = FSTConfiguration
			.createDefaultConfiguration();

	public Blueprint() {
		super(BlueprintComponent.class);
	}

	public boolean save(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			FSTObjectOutput out = conf.getObjectOutput(fileOut);
			out.writeObject(this, Blueprint.class);
			out.close();
			fileOut.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	public static Blueprint load(String filename) {
		Blueprint bp;
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			FSTObjectInput in = conf.getObjectInput(fileIn);
			bp = (Blueprint) in.readObject(Blueprint.class);
			in.close();
			fileIn.close();
			return bp;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
