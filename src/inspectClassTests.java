import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class inspectClassTests 
{
	private static final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

	@BeforeClass
	public static void setUpOutput()
	{
		System.setOut(new PrintStream(outStream));
	}
	
	@After
	public void resetOutputStream()
	{
		outStream.reset();
	}
	
	@Test
	public void testStringClassName() 
	{
		new Inspector().inspect(new String(), false);
		String expected = "\nClass Name: java.lang.String";
		assert(outStream.toString().contains(expected));
	}
	
	@Test
	public void testWrongStringClassName() 
	{
		new Inspector().inspect(new String(), false);
		String notExpected = "\nClass Name: String";
		assertFalse(outStream.toString().contains(notExpected));
	}

	@Test
	public void testStringSuperClassName() 
	{
		new Inspector().inspect(new String(), false);
		String expected = "\nSuper-class Name: java.lang.Object";
		assert(outStream.toString().contains(expected));
	}
	
	@Test
	public void testWrongStringSuperClassName() 
	{
		new Inspector().inspect(new String(), false);
		String notExpected = "\nSuper-class Name: Object";
		assertFalse(outStream.toString().contains(notExpected));
	}
	
	@Test
	public void testStringInterfaceNames()
	{
		new Inspector().inspect(new String(), false);
		String e1 = "\nImplements: interface java.io.Serializable";
		String e2 = "\nImplements: interface java.lang.Comparable";
		String e3 = "\nImplements: interface java.lang.CharSequence";
		String output = outStream.toString();
		assert(output.contains(e1));
		assert(output.contains(e2));
		assert(output.contains(e3));
	}
	
	@Test
	public void testWrongStringInterfaceName()
	{
		new Inspector().inspect(new String(), false);
		String notExpected = "\nImplements: interface java.lang.Number";
		assertFalse(outStream.toString().contains(notExpected));
	}
	
	@Test
	public void testStringConstructorCount()
	{
		new Inspector().inspect(new String(), false);
		int constructorCount = 16;
		String expected = "\nConstructor name: java.lang.String";
		String output = outStream.toString();
		int idx = output.indexOf(expected);
		while (idx >= 0)
		{
			constructorCount--;
			System.out.println(constructorCount);
			idx = output.indexOf(expected, idx + 1);
		}
		
		assertEquals(0, constructorCount);
	}
	
	@Test
	public void testWrongStringConstructorCount()
	{
		new Inspector().inspect(new String(), false);
		int constructorCount = 10;
		String expected = "\nConstructor name: java.lang.String";
		String output = outStream.toString();
		int idx = output.indexOf(expected);
		do
		{
			idx = output.indexOf(expected, idx + 1);
			constructorCount--;
		}
		while (idx > 0);
		
		assertFalse(constructorCount == 0);
	}
	
	@Test
	public void testStringConstructorParametersAndModifier()
	{
		new Inspector().inspect(new String(), false);
		// Looking for the Constructor public String(Byte[], int)
		String e1 = "\n Parameter type: [B";
		String e2 = "\n Parameter type: int";
		String e3 = "\n Modifiers: public";
		
		assert(outStream.toString().contains(e1 + e2 + e3));
	}
	
	@Test
	public void testPrivateAndProtectedConstructors()
	{
		new Inspector().inspect(new TestClass(), false);
		String e1 = "\n Modifiers: protected";
		String e2 = "\n Modifiers: private";
		String output = outStream.toString();
		assert(output.contains(e1));
		assert(output.contains(e2));
	}
	
	@Test
	public void testTestClasstestMethod1()
	{
		new Inspector().inspect(new TestClass(), false);
		String e1 = "\nMethod name: testMethod1";
		String e2 = "\n Throws: java.lang.NoSuchFieldException";
		String e3 = "\n Throws: java.lang.NumberFormatException";
		String e4 = "\n Parameter type: int";
		String e5 = "\n Parameter type: java.lang.String";
		String e6 = "\n Return type: [[B";
		String e7 = "\n Modifiers: public static";
		
		assert(outStream.toString().contains(e1 + e2 + e3 + e4 + e5 + e6 + e7));
	}
}
