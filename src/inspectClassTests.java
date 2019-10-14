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
		String e1 = "\nImplements: java.io.Serializable";
		String e2 = "\nImplements: java.lang.Comparable";
		String e3 = "\nImplements: java.lang.CharSequence";
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
	
	@Test
	public void testArrayValues()
	{
		new Inspector().inspect(new int[] {4, 5, 6}, false);
		String expected = "0: 4\n1: 5\n2: 6";
		assert(outStream.toString().contains(expected));
	}
	
	@Test
	public void testObjectClassDoesntRecurse()
	{
		// Test "fails" if it never completes (infinite recursion)
		new Inspector().inspect(new Object(), true);
	}
	
	@Test
	public void testNullObject()
	{
		new Inspector().inspect(null, false);
		assertEquals("", outStream.toString());
	}
	
	@Test
	public void testBadInspectClassCall()
	{
		Class c = int.class;
		Object o = new String();
		new Inspector().inspectClass(c, o, false, 0);
		String expected = "ERROR: obj null or obj class not assignable to c in call to inspectClass";
		assert(outStream.toString().contains(expected));
	}
	
	@Test
	public void testMultiDimensionalArrayValues()
	{
		int[][] arr = new int[][] { {1, 2}, {3, 4}};
		new Inspector().inspect(arr, false);
		String e1 = " 0: 1\n 1: 2";
		String e2 = " 0: 3\n 1: 4";
		String output = outStream.toString();
		assert(output.contains(e1));
		assert(output.contains(e2));
	}
	
	@Test
	public void testPrivateFieldValue()
	{
		new Inspector().inspect(new TestClass(), false);
		String expected = " Type: boolean\n Modifiers: private\n Value: true";
		assert(outStream.toString().contains(expected));
	}
}
