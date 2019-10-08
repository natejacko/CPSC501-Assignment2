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
    	int i = 0;
    	for (Constructor con : c.getDeclaredConstructors())
    	{
    		String conPrefix = "Constructor " + i++; 
    		printWithTabs(conPrefix + " name: " + con.getName(), depth);
    		for (Parameter p : con.getParameters())
    		{
    			printWithTabs(conPrefix + " parameter type: " +p.getType().getName(), depth);
    		}
    		int mod = con.getModifiers();
    		printWithTabs(conPrefix + " modifiers: " + Modifier.toString(mod), depth);
    		
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
