import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DynamicLoadingDriver {

    public static void main(String[] args) throws Exception {
    	if (args.length != 3)
    	{
    		System.out.println("Error: Three arguments must be provided");
    		printUsage();
    		System.exit(-1);
    	}
    	
    	Object inspector = getClassObject(args[0]);
    	
    	Object toInspect = getClassObject(args[1]);
    	
    	boolean recursive = Boolean.parseBoolean(args[2]);
    	
    	runTest(inspector, toInspect, recursive);
    }
    
    private static Object getClassObject(String className)
    {
    	Object obj = null;
    	try 
    	{
    		Class c = Class.forName(className);
    		Constructor con = c.getDeclaredConstructor(null);
    		if (!Modifier.isPublic(con.getModifiers()))
    		{
    			con.setAccessible(true);
    		}
    		obj = con.newInstance(null);
    		
    	}
    	catch (ClassNotFoundException e)
    	{
    		System.out.println("Error: Class " + className + " not found");
    		printUsage();
    		System.exit(-1);
    	}
    	catch (NoSuchMethodException e)
    	{
    		System.out.println("Error: Could not find a no-arg constructor for class " + className);
    		printUsage();
    		System.exit(-1);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error: " + e);
    		printUsage();
    		System.exit(-1);
    	}
		return obj;
    }
    
    private static void runTest(Object inspector, Object toInspect, boolean recursive)
    {
    	try
    	{
    		Class c = inspector.getClass();
    		Method m = c.getMethod("inspect", new Class[] { Object.class, boolean.class });
    		if (!Modifier.isPublic(m.getModifiers()))
    		{
    			m.setAccessible(true);
    		}
    		m.invoke(inspector, new Object[] { toInspect, recursive });
    	}
    	catch (NoSuchMethodException e)
    	{
    		System.out.println("Error: inspect(Object, boolean) not found in " + inspector.getClass().getName());
    		printUsage();
    		System.exit(-1);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error: " + e);
    		printUsage();
    		System.exit(-1);
    	}
    }
    
    private static void printUsage()
    {
    	System.out.println("Usage: java DynamicLoadingDriver <classWithInspectMethod> <classToInspect> <recursionFlag true|false>");
    	System.out.println("\tIf recursiveFlag cannot be parsed, false will be used by default");
    }
}
