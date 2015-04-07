package liquibase.change.custom.ext;

import java.lang.reflect.Method;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class MethodLoader implements CustomTaskChange {
	
	// class name to load the method from 
	private String className;
	
	// method name to run
	private String methodName;
	

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUp() throws SetupException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Database database)
			throws CustomChangeException {
		try 
		{
			Class<?> classDefinition = Class.forName(className);

			//@@Smart2K.Converters.GenerateShortName@@"
			//If there is no method name, we will just make new instances of the class
			if(methodName == null)
			{
				System.out.println("Executing class: "+className);
				classDefinition.newInstance();
			}
			//If there's a method name, we invoke the method without parameters (for now).
			//This way, we don't need to create a new class for one purpose
			//We can have a generic class and create different methods
			//@@Smart2K.Converters.DatabaseUpdaterMethods@@updateRecurringMeetingTemplatesGenerator@@"
			else
			{
				Class<String> strType = String.class;
				System.out.println("Executing class: "+className+"."+methodName);
				Method method = classDefinition.getDeclaredMethod(methodName, strType);
				method.invoke(classDefinition, "");
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CustomChangeException(e.getMessage());
		}
	}

}
