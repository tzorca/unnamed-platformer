package model.structures;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import model.Ref.BlueprintComponent;

public class Blueprint extends HashMap<BlueprintComponent, Object> {
	private static final long serialVersionUID = -7682315731084504119L;

	public Blueprint() {
		super();
	}

	public boolean save(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
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
			ObjectInputStream in = new ObjectInputStream(fileIn);
			bp = (Blueprint) in.readObject();
			in.close();
			fileIn.close();
			return bp;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
