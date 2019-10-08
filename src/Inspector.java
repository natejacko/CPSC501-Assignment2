import java.lang.reflect.Constructor;
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
