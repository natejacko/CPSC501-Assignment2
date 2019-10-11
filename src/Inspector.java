import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class Inspector 
{

    public void inspect(Object obj, boolean recursive) 
    {    			
        Class c = obj.getClass();
        System.out.println("Begin Inspection:\n");
        inspectClass(c, obj, recursive, 0);
    }

    public void inspectClass(Class c, Object obj, boolean recursive, int depth) 
    {
		// Type checking
		if (obj.getClass() != c)
		{
			System.out.println("Invalid call to inspectClass(Class, Object, boolean, int)");
			return;
		}
    	
    	if (c.isArray())
    	{
    		inspectArray(c, obj, recursive, depth, 0);
    		return;
    	}
    	if (c.isPrimitive())
    	{
    		printWithTabs("Primitive inspection not complete yet", depth);
    		return;
    	}
    	
    	// TODO if this itself is java.lang.Object, make sure not not inspect super-class, interfaces, etc.
    	
    	// Class Name
    	printWithTabs("Class Name: " + c.getName(), depth);
    	
    	// Super-class
    	//TODO inspect super-class
    	printWithTabs("Super-class Name: " + c.getSuperclass().getName(), depth);
    	
    	// Interfaces
    	//TODO inspect interfaces recursively
    	for (Class i : c.getInterfaces())
    	{
    		printWithTabs("Implements: " + i, depth);
    	}
    	
    	// Constructors
    	for (Constructor con : c.getDeclaredConstructors())
    	{
    		printWithTabs("Constructor name: " + con.getName(), depth);
    		for (Parameter p : con.getParameters())
    		{
    			printWithTabs(" Parameter type: " +p.getType().getName(), depth);
    		}
    		int mod = con.getModifiers();
    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
    		
    	}
    	
    	// Methods
    	for (Method m : c.getDeclaredMethods())
    	{
    		printWithTabs("Method name: " + m.getName(), depth);
    		for (Class e : m.getExceptionTypes())
    		{
    			printWithTabs(" Throws: " + e.getName(), depth);
    		}
    		for (Class p : m.getParameterTypes())
    		{
    			printWithTabs(" Parameter type: " + p.getName(), depth);
    		}
    		printWithTabs(" Return type: " + m.getReturnType().getName(), depth);
    		int mod = m.getModifiers();
    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
    	}
    	
    	// Fields
    	for (Field f: c.getDeclaredFields())
    	{
    		printWithTabs("Field name: " + f.getName(), depth);
    		Class type = f.getType();
    		printWithTabs(" Type: " + type.getName(), depth);
    		int mod = f.getModifiers();
    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
			// TODO Check if the modifier check needs to be done on everything, or just non-public?
    		f.setAccessible(true);
    		if (type.isPrimitive())
    		{
				try 
				{
	    			Object value = null;
	    			if (type.equals(boolean.class))
	    			{
	    				value = f.getBoolean(obj);
	    			}
	    			else if (type.equals(byte.class))
	    			{
	    				value = f.getByte(obj);
	    			}
	    			else if (type.equals(char.class))
	    			{
	    				value = f.getChar(obj);
	    			}
	    			else if (type.equals(double.class))
	    			{
	    				value = f.getDouble(obj);
	    			}
	    			else if (type.equals(float.class))
	    			{
	    				value = f.getFloat(obj);
	    			}
	    			else if (type.equals(int.class))
	    			{
	    				value = f.getInt(obj);
	    			}
	    			else if (type.equals(long.class))
	    			{
	    				value = f.getLong(obj);
	    			}
	    			else if (type.equals(short.class))
	    			{
	    				value = f.getShort(obj);
	    			}
	    			printWithTabs(" Value: " + value.toString(), depth);
				} 
				catch (IllegalArgumentException | IllegalAccessException e) 
				{
					e.printStackTrace();
				} 
    		}
    		else if (type.isArray())
    		{
    			try
				{
    				// Give arrDepth of 1 for some better indentation in output
					inspectArray(type, f.get(obj), recursive, depth, 1);
				} 
    			catch (IllegalArgumentException | IllegalAccessException e1)
				{
					e1.printStackTrace();
				}
    		}
    		else
    		{
        		// TODO inspect objects (with recursion if flagged)
    			try 
    			{
    				// If the field is an object that is not null, print the reference to it
    				// i.e. the object's class name and the objects hash code
    				Object referenceObj = f.get(obj);
    				if (referenceObj != null)
    				{
    					printWithTabs(" Reference value: " + referenceObj.getClass().getName() + "@" + referenceObj.hashCode() , depth);
    				}
    				else
    				{
    					printWithTabs(" Reference value: null", depth);
    				}
				} 
    			catch (IllegalArgumentException | IllegalAccessException e) 
    			{
					e.printStackTrace();
				}
    		}
    	}
    }
    
	public void inspectArray(Class c, Object obj, boolean recursive, int depth, int arrDepth)
	{
		// Type checking
		if (!c.isArray() || !obj.getClass().isArray() || obj.getClass() != c)
		{
			System.out.println("Invalid call to inspectArray(Class, Object, boolean, int, int)");
			return;
		}
		
		// Component type
		Class compType = c.getComponentType();
		printWithTabsAndSpaces("Component type: " + compType.getName(), depth, arrDepth);
		
		// Length
		int length = Array.getLength(obj);
		printWithTabsAndSpaces("Length: " + String.valueOf(length), depth, arrDepth);
		
		// Array Contents
		if (length > 0)
		{
			printWithTabsAndSpaces("Array contents:", depth, arrDepth);
		}
		
		for (int i = 0; i < length; i++)
		{
			Object arrayObj = Array.get(obj, i);
			if (arrayObj == null)
			{
				printWithTabsAndSpaces(i + ": null", depth, arrDepth);
			}
			else if (compType.isPrimitive())
			{
				printWithTabsAndSpaces(i + ": " + arrayObj, depth, arrDepth);
			}
			else if (compType.isArray())
			{
				printWithTabsAndSpaces(i + ": Array", depth, arrDepth);
				inspectArray(arrayObj.getClass(), arrayObj, recursive, depth, arrDepth + 1);
			}
			else
			{
				// TODO recursively check objects if flag is on
				printWithTabsAndSpaces(i + ": " + arrayObj.getClass().getName() + "@" + arrayObj.hashCode() , depth, arrDepth);
			}
		}
	}

    private void printWithTabs(String output, int tabs)
    {
    	printWithTabsAndSpaces(output, tabs, 0);
    }
    
    private void printWithTabsAndSpaces(String output, int tabs, int spaces)
    {
    	while(tabs-- > 0)
    	{
    		System.out.print('\t');
    	}
    	while(spaces-- > 0)
    	{
    		System.out.print(' ');
    	}
    	System.out.println(output);
    }
}
