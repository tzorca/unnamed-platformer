package unnamed_platformer.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import unnamed_platformer.input.InputManager.GameKey;
import unnamed_platformer.input.InputManager.PlrGameKey;

public class TestInputManager
{

	@Test
	public void testPlrGameKeyFromString_PLR1_START() {
		PlrGameKey testGameKey = PlrGameKey.fromString("PLR1_START");
		
		assertEquals(testGameKey.getPlayerNo(), 1);
		assertEquals(testGameKey.getGameKey(), GameKey.START);
	}

	
	@Test
	public void testPlrGameKeyFromString_PLR4_SECONDARY_UP() {
		PlrGameKey testGameKey = PlrGameKey.fromString("PLR4_SECONDARY_UP");
		
		assertEquals(testGameKey.getPlayerNo(), 4);
		assertEquals(testGameKey.getGameKey(), GameKey.SECONDARY_UP);
	}
}
