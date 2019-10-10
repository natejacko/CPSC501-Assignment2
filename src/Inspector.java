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

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) 
    {
    	printWithTabs("Entered new class inspection", depth);
    	printWithTabs("----------------------------", depth);
    	
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
        		// TODO inspect arrays	
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

    private void printWithTabs(String output, int tabs)
    {
    	while(tabs-- > 0)
    	{
    		System.out.print("\t");
    	}
    	System.out.println(output);
    }
}
