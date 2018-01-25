/*
 * ===========================================
 * unittest-utilities
 * ===========================================
 *
 * Project Info:  https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2017 nepatec GmbH & Co. KG
 *
 *  This file is part of unittest-utilities
 *
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.lonzak.common.unittest;

import java.awt.Button;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Class which automatically tests DTO / Entity style classes.<p>
 * Also Exception classes may be tested. The following things are tested:
 * <ul>
 * <li>instantiation of all constructors</li>
 * <li>getter and setter methods (a call to set should return the value in the get method)</li>
 * <li>equals() and hashCode() methods (a change of an attribute must result in different hashCodes())</li>
 * <li>toString() method</li>
 * </ul>
 *
 * The values used for testing are randomly generated. There are different possibilities to adapt the default behavior, so it is possible to pass specific values along or to skip certain attributes.
 *
 * @author TvT
 *
 */
public final class AutoTester {
	
	private static SecureRandom r = new SecureRandom();
	private static boolean enableWarnings = true;

	//only static methods thus no instantiation
	private AutoTester(){
	}
	
	/**
	 * Automatically tests an DTO/Entity/Java Bean style or Exception class.
	 * 
	 * @param dtoClass to test
	 * @throws AssertionError if test fails
	 */
	public static void testDTOClass(Class<?> dtoClass){
		testDTOClass(dtoClass, new ArrayList<Class<?>>(), new ArrayList<String>(), SpecialValueLocator.NONE);
	}
	
	/**
	 * Tests a DTO/Entity/Java Bean style or Exception class.
	 * Please note, that all values which are used for the tests are randomly generated.
	 * In case the default test method {@link #testDTOClass(Class)} fails there are several options:
	 * 
	 * <ul>
	 * <li>1. provide specific values for certain parameters (e.g. special string formatting, specific numbers ...)</li>
	 * <li>2. provide implementations for abstract parameters (e.g. ArrayList for List )</li>
	 * <li>3. exclude certain attributes of the class and thus the corresponding getter/setter methods</li>
	 * </ul>
	 * 
	 * <b>1. Special Values usage</b><p>
	 * Additionally a special valueLocator can be specified which is able to pass values used for testing.<p>
     * The identification of an attribute is as follows, for example:<p>
     * A class has two constructors:
     * <pre>
     * {@code}
     * class A {
     *   A(String a){...}
     *   A(String a, String b) {...}
     * 
     * </pre>
     * To target the first value of the first constructor (the order is the order as the constructors are defined in the file) use:<p>
     *  <code>new SpecialValueLocator(new Location(1,1),"value");</code><p>
     * To target the second parameter of the second constructor:<p>
     *  <code>new SpecialValueLocator(new Location(2,2),"value");</code>
	 *
	 * 
	 * If for some reason some attributes should be ignored a list of 'ignorePropertiesForGetSetTest' can be specified.
	 * 
	 * @param dtoClass the class to test
	 * @param implOfAbstractClasses In case the class to test contains abstract parameters a list with implementation classes could be specified.
	 * @param ignorePropertiesForGetSetTest the name of the attribute which should be excluded
	 * @param specialValues for the constructors/set methods to use
	 */
	public static void testDTOClass(Class<?> dtoClass, List<Class<?>> implOfAbstractClasses, List<String> ignorePropertiesForGetSetTest, SpecialValueLocator specialValues){
		if(implOfAbstractClasses==null) implOfAbstractClasses = new ArrayList<>();
		if(ignorePropertiesForGetSetTest==null) ignorePropertiesForGetSetTest = new ArrayList<>();
		if(specialValues==null) specialValues = SpecialValueLocator.NONE;
			  
		//abstract classes or interfaces can not be instantiated
		if(Modifier.isAbstract(dtoClass.getModifiers())) throw new AssertionError(dtoClass.getSimpleName()+" is an abstract class or an interface and can thus not be instantiated. Use on of its subclasses instead!");
		
		//check whether equals and hashCode was overwritten
		boolean equalsExists = classImplementsEquals(dtoClass);
		boolean hashCodeExists = classImlementsHashCode(dtoClass);
		
		if(equalsExists == !hashCodeExists){
			throw new AssertionError(dtoClass.getSimpleName()+" implements only one method: equals(Object o) or hashCode(). This violates the invariant that equal objects must have equal hashcodes. (Interface contract for Object states: if two objects are equal according to equals(), then they must have the same hashCode() value.)");
		}
		
		try{
		    //create all constructors and check equals
			HashMap<Object, Object> constructors = createObjects(new ArrayList<Class<?>>(), dtoClass, implOfAbstractClasses, specialValues,true);
			//create all set methods and call them for each constructor
			if(equalsExists && hashCodeExists){
				checkEqualsAndHashCode(new ArrayList<Class<?>>(), dtoClass, constructors, implOfAbstractClasses, specialValues);
			}
			checkGettersAndSetters(new ArrayList<Class<?>>(), dtoClass, constructors, implOfAbstractClasses, ignorePropertiesForGetSetTest, specialValues);
			checkToString(dtoClass, constructors);
		}
		catch (IllegalArgumentException iae) {
			throw new RuntimeException("An IllegalArgumentException occured. There are several possible reasons: \n 1. The class can not be automatically tested \n 2. There is an error in the AutoTester (please inform the unittest-utilities project) \n 3. There is an error in your implementation of the DTO class (e.g. faulty equals/hashCode Implementation ...) \n => Please exclude the class '"+dtoClass.getName()+"' from automatic testing (for now) and see stacktrace for details ("+iae.getMessage()+")",iae);		
		}
		catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("An ClassNotFoundException occured. There are several possible reasons: \n 1. The class can not be automatically tested \n 2. There is an error in the AutoTester (please inform the unittest-utilities project) \n 3. There is an error in your implementation of the DTO class (e.g. faulty equals/hashCode Implementation ...) \n => Please exclude the class '"+dtoClass.getName()+"' from automatic testing (for now) and see stacktrace for details ("+cnfe.getMessage()+")",cnfe);
		}
		catch (InstantiationException ie) {
			throw new RuntimeException("An InstantiationException occured. There are several possible reasons: \n 1. The class can not be automatically tested \n 2. There is an error in the AutoTester (please inform the unittest-utilities project) \n 3. There is an error in your implementation of the DTO class (e.g. faulty equals/hashCode Implementation ...) \n => Please exclude the class '"+dtoClass.getName()+"' from automatic testing (for now) and see stacktrace for details ("+ie.getMessage()+")",ie);
		}
		catch (IllegalAccessException iae) {
			throw new RuntimeException("An IllegalAccessException occured. There are several possible reasons: \n 1. The class can not be automatically tested \n 2. There is an error in the AutoTester (please inform the unittest-utilities project) \n 3. There is an error in your implementation of the DTO class (e.g. faulty equals/hashCode Implementation ...) \n => Please exclude the class '"+dtoClass.getName()+"' from automatic testing (for now) and see stacktrace for details ("+iae.getMessage()+")",iae);
		}
		catch (InvocationTargetException ite) {
			throw new RuntimeException("An InvocationTargetException occured. There are several possible reasons: \n 1. The class can not be automatically tested \n 2. There is an error in the AutoTester (please inform the unittest-utilities project) \n 3. There is an error in your implementation of the DTO class (e.g. faulty equals/hashCode Implementation ...) \n => Please exclude the class '"+dtoClass.getName()+"' from automatic testing (for now) and see stacktrace for details ("+ite.getMessage()+")",ite);
		}
	}
	
	/**
	 * Enable warnings
	 * @return true if warnings are enabled otherwise false
	 */
	public static boolean isEnableWarnings() {
		return AutoTester.enableWarnings;
	}

	/**
	 * En-or disable the warnings
	 * @param enableWarnings true to enable warnings and false to disable
	 */
	public static void setEnableWarnings(boolean enableWarnings) {
		AutoTester.enableWarnings = enableWarnings;
	}

	/**
	 * Checks if the equals method works as expected.
	 * 
	 * @param constructedClasses list of all classes which are about to be created
	 * @param dtoClass to test
	 * @param implOfAbstractClasses
	 * @param specialValues
	 * @param allConstructors
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws  
	 * @throws AssertionError if test fails
	 */
	private static HashMap<Object, Object> createObjects(ArrayList<Class<?>> constructedClasses, Class<?> dtoClass, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues, boolean allConstructors) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		//create return Map: two objects for each constructor
		HashMap<Object, Object> returnObjects = new HashMap<>();
		
		//exclude java.lang.* classes because:
		//otherwise all Integer, Float, String ... constructors will be called, filled with Random numbers etc.
		//and it also crashes since the random numbers are invalid values for the constructors (numbers, size, index etc)
		if(dtoClass.getName().startsWith("java.lang") && ClassUtils.getAllInterfaces(dtoClass).contains(Comparable.class)){
			Class[] parameters = new Class[]{dtoClass};
			Class[] paramListLeft = new Class[1];
			Object[] argListLeft = new Object[1];
			Class[] paramListRight = new Class[1];
			Object[] argListRight = new Object[1];
				
			fillJavaLangType(parameters, paramListLeft, argListLeft, paramListRight, argListRight, 0, specialValues);
			returnObjects.put(argListLeft[0], argListRight[0]);
			return returnObjects;
		}
		else if(dtoClass.getName().startsWith("java.") || dtoClass.getName().startsWith("javax.")){
			Class[] parameters = new Class[]{dtoClass};
			Class[] paramListLeft = new Class[1];
			Object[] argListLeft = new Object[1];
			Class[] paramListRight = new Class[1];
			Object[] argListRight = new Object[1];
				
			fillSpecialObject(parameters, paramListLeft, argListLeft, paramListRight, argListRight, 0, specialValues);
			returnObjects.put(argListLeft[0], argListRight[0]);
			return returnObjects;
		}
		else if(Modifier.isAbstract(dtoClass.getModifiers())){
			
			Class<?> implementationClass=null;
			
			for(Class<?> clazz : implOfAbstractClasses){
				//check for interfaces and abstract classes
				List<Class<?>> superClasses = ClassUtils.getAllSuperclasses(clazz);
				superClasses.addAll( ClassUtils.getAllInterfaces(clazz));
				
				if(superClasses.contains(dtoClass)){
					implementationClass=clazz;
					break;
				}
			}
				
			if(implementationClass==null){
				throw new AssertionError("No implementation found for abstract class '"+dtoClass.getName()+"'. Please use the testDTOClass(Class<?>, List implementationsOfAbstractClasses) method and supply a suitable implementation.");
			}
			
			//overwrite DTO class
			dtoClass = implementationClass;
		}

		//only public constructors are relevant
		List<Constructor<?>> constructors =  new ArrayList<>(Arrays.asList(dtoClass.getConstructors()));

		//check for constructors which are instantiated with the same class -> results in infinite loop
		ArrayList<Constructor<?>> toBeRemoved = new ArrayList<>();
		for (Constructor<?> constructor : constructors) {
			
			Class<?>[] parameters = constructor.getParameterTypes();
			
			for (int j = 0; j < parameters.length; j++) {
				if(parameters[j].isAssignableFrom(dtoClass)){
					toBeRemoved.add(constructor);
				}
			}
		}
		
		if(!toBeRemoved.isEmpty()){
			constructors.removeAll(toBeRemoved);
		}
		
		//if object is not instantiable use private constructor
		if(constructors.isEmpty()){
			constructors=Arrays.asList(dtoClass.getDeclaredConstructors());
		}
		
	    //it is necessary to store all classes to avoid circular object creations which otherwise results in an SOE
        if(!constructedClasses.contains(dtoClass)){
          constructedClasses.add(dtoClass);
        }

        try{
			constructObjects(constructedClasses, constructors, returnObjects, implOfAbstractClasses, specialValues,allConstructors);
		}
		catch(InvocationTargetException ite){
			if(ite.getCause() instanceof NumberFormatException || ite.getTargetException() instanceof IllegalArgumentException){
				throw new InvocationTargetException(ite.getTargetException(),"It seems that the constructor of the class needs a special format. Try to use the special value mechanism!");
			}
			if(ite.getTargetException() instanceof NullPointerException){
			  throw new InvocationTargetException(ite.getTargetException(),"There seems to be a problem while creating the object.");
			}
			else{
				throw ite;
			}
		}
		return returnObjects;
	}
	
	private static void checkEqualsAndHashCode(ArrayList<Class<?>> constructedClasses, Class<?> dtoClass, HashMap<Object, Object> constructedObjects, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		//first check equality on all constructed objects
		Set<Entry<Object,Object>> consts = constructedObjects.entrySet();

		for (Entry<Object,Object> entry : consts) {
			Object constLeft = entry.getKey();
			Object constRight = entry.getValue();
			executeEquals(constLeft, constRight,false);
		}
		
		//second check if equals and hashCode are correctly implemented:
		//call a setter method and verify that different hashCodes result in unequal objects and the same hashCode results in equal objects
		Method[] methods = dtoClass.getMethods();
		
		try{
			constructSetMethodsAndCheckEquals(constructedClasses, dtoClass, constructedObjects, methods, implOfAbstractClasses, specialValues);
		}
		catch(InvocationTargetException ite){
			if(ite.getCause() instanceof NumberFormatException){
			  throw new InvocationTargetException(ite, "It seems that the class needs a special format. Try to use the special value mechanism!");
			}
			if(ite.getTargetException() instanceof NullPointerException){
              throw new InvocationTargetException(ite.getTargetException(), "The check equals() or hashCode() seems to have a problem. Check the implementation!");
            }
			else{
				throw ite;
			}
		}
	}
	
	private static void checkGettersAndSetters(ArrayList<Class<?>> constructedClasses, Class<?> dtoClass, HashMap<Object, Object> constructedObjects, List<Class<?>> implOfAbstractClasses, List<String> propertiesToIgnore, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

		//all public methods are relevant
		List<Method> publicMethods = Arrays.asList(dtoClass.getMethods());
		
		//all protected methods are relevant
		ArrayList<Method> allMethods = getInheritedProtectedMethods(dtoClass);
		
		//merge into one array
		allMethods.addAll(publicMethods);
		
		
		// remove the ignoredProperties
		List<Method> toRemove = new ArrayList<>();
		for (Method current : allMethods) {
			if (current.getName().startsWith("get") || current.getName().startsWith("set")) {
				String name = current.getName().substring(3);
				
				name = name.subSequence(0, 1).toString().toLowerCase() + name.substring(1);
				if (propertiesToIgnore.contains(name)) {
					toRemove.add(current);
				}
			}
		}
		
		for (Method method : toRemove) {
			allMethods.remove(method);
		}
		
		
		try{
			constructSetMethods(constructedClasses, dtoClass, constructedObjects, allMethods, implOfAbstractClasses, specialValues);
		}
		catch(InvocationTargetException ite){
			if(ite.getCause() instanceof NumberFormatException){
			  throw new InvocationTargetException(ite, "It seems that the class needs a special format. Try to use the special value mechanism!");
			}
			if(ite.getTargetException() instanceof NullPointerException){
              throw new InvocationTargetException(ite.getTargetException(), "There seems to be a problem with a getter or setter method. Check your implementation.");
            }
			else{
				throw ite;
			}
		}
	}
	
	private static void checkToString(Class<?> dtoClass, HashMap<Object, Object> constructors) throws IllegalAccessException,InvocationTargetException {

	  try{
		//only public methods are relevant
		Method[] methods = dtoClass.getMethods();
		
		for (int i = 0; i < methods.length; i++) {
			
			//call toString method if overridden
			if(methods[i].getName().startsWith("toString") && methods[i].getDeclaringClass().equals(dtoClass)){
				Method method = methods[i];
				
				Class<?>[] parameters = method.getParameterTypes();
				Class<?> returnType = method.getReturnType();
				
				//<String toString()> has no parameter and String as return value
				if(parameters.length==0 && returnType.isAssignableFrom(String.class)){
				  
					//call method for every constructed constructor
					Set<Entry<Object,Object>> consts = constructors.entrySet();
					
					for (Entry<Object, Object> entry : consts) {
						Object constLeft = entry.getKey();
						Object constRight = entry.getValue();
						
						nullifyUnorderedCollections(constLeft);
						nullifyUnorderedCollections(constRight);
						
	                      //invoke toString
                        Object returnLeft = method.invoke(constLeft,(Object[])null);
                        Object returnRight = method.invoke(constRight,(Object[])null);
						
						//result of toString() should be equals, too
						try{
						  executeEquals(returnLeft,returnRight,false);
						}
						catch(AssertionError ae){
						  throw new AssertionError("Two identical objects should have the same toString() method result. The reason for that are usually object addresses (SomeObject@383534aa...) of attributes which don't overwrite toString(). For debugging and clean error logs this should be avoided!", ae);
						}
					}
				}
				else{
					System.err.println(dtoClass.getSimpleName()+" does not overwrite the object <String toString()> method although it has a <'"+returnType.getSimpleName()+" "+ method.getName()+"> method!");
				}
			}
		}
	  }
	  catch(InvocationTargetException ite){
	    if(ite.getTargetException() instanceof NullPointerException){
	      throw new AssertionError("A NullpointerException occured which indicates that a bug was found in the 'toString()' method.",ite.getTargetException());
	    }
	  }
	}

	/**
	 * If a method call (e.g. toString()) should be comparable some Collection types must be nullified (e.g. HashSet, HashMap) 
	 * because the ordering is always different. 
	 * 
	 * @param workPiece
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static void nullifyUnorderedCollections(Object workPiece) throws IllegalArgumentException, IllegalAccessException{
      Field[] fields = workPiece.getClass().getDeclaredFields();
      
      for (Field field : fields) {
        //intentionally used "getSimpleName().equals("DateTime")" to apply to org.joda.time.* and java.util.time
        if(field.getType().isAssignableFrom(HashSet.class) || field.getType().isAssignableFrom(HashMap.class) || field.getType().getSimpleName().equals("DateTime") || field.getType().getSimpleName().equals("Date")){
          field.setAccessible(true);
          field.set(workPiece, null);
        }
      }
	}
	
	private static void fillPrimitiveType(Class<?>[] parameters, Class<?>[] paramListLeft, Object[] argListLeft, Class<?>[] paramListRight, Object[] argListRight, int parameterIndex, SpecialValueLocator specialValues){
		//primitive types
		if(parameters[parameterIndex].isAssignableFrom(byte.class)){
			paramListLeft[parameterIndex] = Byte.TYPE;
			paramListRight[parameterIndex] = Byte.TYPE;
			
			Byte b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b =  (Byte)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomByte();
            }
			
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b;
		}
		else if(parameters[parameterIndex].isAssignableFrom(short.class)){
			paramListLeft[parameterIndex] = Short.TYPE;
			paramListRight[parameterIndex] = Short.TYPE;
			
			Short s;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
			  s =  (Short)specialValues.getSpecialValue(parameterIndex+1);
			}
			else{
			  s = getRandomShort();
			}
			argListLeft[parameterIndex]= s;
			argListRight[parameterIndex]= s;
		}
		else if(parameters[parameterIndex].isAssignableFrom(int.class)){
			paramListLeft[parameterIndex] = Integer.TYPE;
			paramListRight[parameterIndex] = Integer.TYPE;
			
			Integer in;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
			  in = (Integer)specialValues.getSpecialValue(parameterIndex+1);
			}
            //this is a special case for setting a random scale for a bigDecimal e.g.(1906457549,-619243059): 
            //a negative scale of such a big int would take too long to calculate
            //TODO: there should be another way (special value mechanism?) However how to point the user the the exact problem? 
            //In theory start a new thread which calculates the numbers and if it takes too long abort it...?
            //If this is happening another time then 
            else if(parameters.length>1 && parameterIndex>0 && argListLeft[parameterIndex-1] instanceof BigDecimal){
              //use a smaller number
              in = getRandomShort().intValue();
            }
			else{
			  in = getRandomInteger();
			}
			argListLeft[parameterIndex]= in; 
			argListRight[parameterIndex]= in;
		}
		else if(parameters[parameterIndex].isAssignableFrom(long.class)){
			paramListLeft[parameterIndex] = Long.TYPE;
			paramListRight[parameterIndex] = Long.TYPE;
			
			Long l;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              l = (Long)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              l = getRandomLong();
            }
			
			argListLeft[parameterIndex]= l;
			argListRight[parameterIndex]= l;
		}
		else if(parameters[parameterIndex].isAssignableFrom(float.class)){
			paramListLeft[parameterIndex] = Float.TYPE;
			paramListRight[parameterIndex] = Float.TYPE;

			Float f;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              f = (Float)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              f = getRandomFloat();
            }
			
			argListLeft[parameterIndex]= f;  
			argListRight[parameterIndex]= f;
		}
		else if(parameters[parameterIndex].isAssignableFrom(double.class)){
			paramListLeft[parameterIndex] = Double.TYPE;
			paramListRight[parameterIndex] = Double.TYPE;
			
			Double d;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              d = (Double)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              d = getRandomDouble();
            }
			
			argListLeft[parameterIndex]= d;
			argListRight[parameterIndex]= d;
		}
		else if(parameters[parameterIndex].isAssignableFrom(boolean.class)){
			paramListLeft[parameterIndex] = Boolean.TYPE;
			paramListRight[parameterIndex] = Boolean.TYPE;
			
			Boolean b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (Boolean)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomBoolean();
            }
			
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b;
		}
		else if(parameters[parameterIndex].isAssignableFrom(char.class)){
			paramListLeft[parameterIndex] = Character.TYPE;
			paramListRight[parameterIndex] = Character.TYPE;
			
			Character c;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              c = (Character)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              c = getRandomCharacter();
            }
			
			argListLeft[parameterIndex]= c;
			argListRight[parameterIndex]= c;
		}
		else{
			throw new AssertionError("Unknown primitive java type: "+parameters[parameterIndex].getName());
		}
	}
	
	/**
	 * 
	 * @param parameters
	 * @param paramListLeft
	 * @param argListLeft
	 * @param paramListRight
	 * @param argListRight
	 * @param parameterIndex
	 * @param specialValues
	 * @return true if the data type could be filled otherwise false
	 */
	private static boolean fillJavaLangType(Class<?>[] parameters, Class<?>[] paramListLeft, Object[] argListLeft,Class<?>[] paramListRight, Object[] argListRight, int parameterIndex, SpecialValueLocator specialValues){
		
		//check String
	    if(parameters[parameterIndex].isAssignableFrom(String.class)){
			paramListLeft[parameterIndex] = String.class;
			paramListRight[parameterIndex] = String.class;
			
			String s;
			Object clazz = specialValues.getSpecialValue(parameterIndex+1);
			if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
			  s = (String)specialValues.getSpecialValue(parameterIndex+1);
			}
			else{
			  s = getRandomString();
			}
			argListLeft[parameterIndex]= s;
			argListRight[parameterIndex]= s;
		}
		//objects of primitive types
	    else if(parameters[parameterIndex].isAssignableFrom(Byte.class)){
			paramListLeft[parameterIndex] = Byte.class;
			paramListRight[parameterIndex] = Byte.class;
			
			Byte b;
			Object clazz = specialValues.getSpecialValue(parameterIndex+1);
			if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (Byte)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomByte();
            }
			
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Short.class)){
			paramListLeft[parameterIndex] = Short.class;
			paramListRight[parameterIndex] = Short.class;
			
			Short s;
			Object clazz = specialValues.getSpecialValue(parameterIndex+1);
			if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              s = (Short)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              s = getRandomShort();
            }
			
			argListLeft[parameterIndex]= s;
			argListRight[parameterIndex]= s;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Integer.class)){
			paramListLeft[parameterIndex] = Integer.class;
			paramListRight[parameterIndex] = Integer.class;
			
			Integer in;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              in = (Integer)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              in = getRandomInteger();
            }
			
			argListLeft[parameterIndex]= in; 
			argListRight[parameterIndex]= in;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Long.class)){
			paramListLeft[parameterIndex] = Long.class;
			paramListRight[parameterIndex] = Long.class;
			
			Long l;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              l = (Long)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              l = getRandomLongAsObject();
            }
			
			argListLeft[parameterIndex]= l;
			argListRight[parameterIndex]= l;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Float.class)){
			paramListLeft[parameterIndex] = Float.class;
			paramListRight[parameterIndex] = Float.class;
			
			Float f;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              f = (Float)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              f = getRandomFloatAsObject();
            }
			argListLeft[parameterIndex]= f;  
			argListRight[parameterIndex]= f;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Double.class)){
			paramListLeft[parameterIndex] = Double.class;
			paramListRight[parameterIndex] = Double.class;
			
			Double d;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              d = (Double)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              d = getRandomDoubleAsObject();
            }
			argListLeft[parameterIndex]= d;
			argListRight[parameterIndex]= d;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Boolean.class)){
			paramListLeft[parameterIndex] = Boolean.class;
			paramListRight[parameterIndex] = Boolean.class;
			
			Boolean b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (Boolean)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomBoolean();
            }
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b;
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Character.class)){
			paramListLeft[parameterIndex] = Character.class;
			paramListRight[parameterIndex] = Character.class;
			
			Character c;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              c = (Character)specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              c = getRandomCharacter();
            }
			argListLeft[parameterIndex]= c;
			argListRight[parameterIndex]= c;
		}
		else{
			return false;
		}
		return true;
	}
	
	private static void fillArray(ArrayList<Class<?>> constructedObjects, Class<?>[] parameters, Type[] types, Class<?>[] paramListLeft,Object[] argListLeft,Class<?>[] paramListRight,Object[] argListRight, int parameterIndex, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
	  
		//primitive Arrays 
	   if(parameters[parameterIndex].isAssignableFrom(int[].class)){
			paramListLeft[parameterIndex] = int[].class;
			paramListRight[parameterIndex] = int[].class;
			
			int [] in;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              in = (int[])specialValues.getSpecialValue(parameterIndex+1+1);
            }
            else{
              in = getRandomIntArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= in;
			argListRight[parameterIndex]= in.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(long[].class)){
			paramListLeft[parameterIndex] = long[].class;
			paramListRight[parameterIndex] = long[].class;
			
			long [] l;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              l = (long[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              l = getRandomLongArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= l;
			argListRight[parameterIndex]= l.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(float[].class)){
			paramListLeft[parameterIndex] = float[].class;
			paramListRight[parameterIndex] = float[].class;
			
			float [] f;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              f = (float[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              f = getRandomFloatArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= f;
			argListRight[parameterIndex]= f.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(double[].class)){
			paramListLeft[parameterIndex] = double[].class;
			paramListRight[parameterIndex] = double[].class;
			
			double [] d;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){

              d = (double[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              d = getRandomDoubleArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= d;
			argListRight[parameterIndex]= d.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(boolean[].class)){
			paramListLeft[parameterIndex] = boolean[].class;
			paramListRight[parameterIndex] = boolean[].class;
			
			boolean [] b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (boolean[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomBooleanArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(byte[].class)){
			paramListLeft[parameterIndex] = byte[].class;
			paramListRight[parameterIndex] = byte[].class;
			
			byte [] b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (byte[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomByteArrayPrimitive();
            }
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(short[].class)){
			paramListLeft[parameterIndex] = short[].class;
			paramListRight[parameterIndex] = short[].class;
			
			short [] s;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              s = (short[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              s = getRandomShortArrayPrimitive();
            }
			
			argListLeft[parameterIndex]= s;
			argListRight[parameterIndex]= s.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(char[].class)){
			paramListLeft[parameterIndex] = char[].class;
			paramListRight[parameterIndex] = char[].class;
			
			char [] c;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              c= (char[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              c = getRandomCharArrayPrimitive();
            }
			argListLeft[parameterIndex]= c;
			argListRight[parameterIndex]= c.clone();
		}
		
		//object Arrays 
	    else if(parameters[parameterIndex].isAssignableFrom(Integer[].class)){
			paramListLeft[parameterIndex] = Integer[].class;
			paramListRight[parameterIndex] = Integer[].class;
			
			Integer [] in;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              in= (Integer[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              in = getRandomIntegerArray();
            }
			argListLeft[parameterIndex]= in;
			argListRight[parameterIndex]= in.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Long[].class)){
			paramListLeft[parameterIndex] = Long[].class;
			paramListRight[parameterIndex] = Long[].class;
			
			Long [] l;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              l= (Long[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              l = getRandomLongArray();
            }
			argListLeft[parameterIndex]= l;
			argListRight[parameterIndex]= l.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Float[].class)){
			paramListLeft[parameterIndex] = Float[].class;
			paramListRight[parameterIndex] = Float[].class;
			
			Float [] f;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              f= (Float[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              f = getRandomFloatArray();
            }
			argListLeft[parameterIndex]= f;
			argListRight[parameterIndex]= f.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Double[].class)){
			paramListLeft[parameterIndex] = Double[].class;
			paramListRight[parameterIndex] = Double[].class;
			
			Double [] d;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              d= (Double[])specialValues.getSpecialValue(parameterIndex+1+1);
            }
            else{
              d = getRandomDoubleArray();
            }
			argListLeft[parameterIndex]= d;
			argListRight[parameterIndex]= d.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Boolean[].class)){
			paramListLeft[parameterIndex] = Boolean[].class;
			paramListRight[parameterIndex] = Boolean[].class;
			
			Boolean [] b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (Boolean[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomBooleanArray();
            }
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Byte[].class)){
			paramListLeft[parameterIndex] = Byte[].class;
			paramListRight[parameterIndex] = Byte[].class;
			
			Byte [] b;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              b = (Byte[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              b = getRandomByteArray();
            }
			argListLeft[parameterIndex]= b;
			argListRight[parameterIndex]= b.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Short[].class)){
			paramListLeft[parameterIndex] = Short[].class;
			paramListRight[parameterIndex] = Short[].class;
			
			Short [] s;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              s = (Short[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              s = getRandomShortArray();
            }
			argListLeft[parameterIndex]= s;
			argListRight[parameterIndex]= s.clone();
		}
	    else if(parameters[parameterIndex].isAssignableFrom(Character[].class)){
			paramListLeft[parameterIndex] = Character[].class;
			paramListRight[parameterIndex] = Character[].class;
			
			Character [] c;
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
              c = (Character[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              c = getRandomCharacterArray();
            }
			argListLeft[parameterIndex]= c;
			argListRight[parameterIndex]= c.clone();
		}
		else if(parameters[parameterIndex].getName().startsWith("[[")){
			throw new AssertionError("Multidimensional Arrays are not supported yet:"+parameters[parameterIndex].getName());
		}
		else{
			//object array
			paramListLeft[parameterIndex] = Class.forName(parameters[parameterIndex].getName());
			paramListRight[parameterIndex] = Class.forName(parameters[parameterIndex].getName());
			
			//detect object type
			Class<?> arrayType = ((Class<?>)types[parameterIndex]).getComponentType();

			//create objects for the array
			HashMap<Object, Object> map = createObjects(constructedObjects, Class.forName(arrayType.getName()), implOfAbstractClasses, specialValues, false);
			Iterator<Entry<Object,Object>> entries = map.entrySet().iterator();
			
			Object[] leftList;
			Object[] rightList;
			
            Object clazz = specialValues.getSpecialValue(parameterIndex+1);
            if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
			  leftList = (Object[])specialValues.getSpecialValue(parameterIndex+1);
			  rightList = (Object[])specialValues.getSpecialValue(parameterIndex+1);
            }
            else{
              leftList = (Object[])Array.newInstance(arrayType,map.size());
              rightList = (Object[])Array.newInstance(arrayType,map.size());
              
              for(int i=0; entries.hasNext();i++){
                Entry entry = entries.next();
                leftList[i]=entry.getKey();
                rightList[i]=entry.getValue();
              }
            }
				
			argListLeft[parameterIndex]= leftList;
			argListRight[parameterIndex]= rightList;
		}
	}
	
	private static void fillEnum(Class<?>[] parameters, Class<?>[] paramListLeft, Object[] argListLeft, Class<?>[] paramListRight, Object[] argListRight, int parameterIndex, SpecialValueLocator specialValues) throws ClassNotFoundException{
		//Enums can not be instantiated
		Class<?> object = Class.forName(parameters[parameterIndex].getName());
		Object[] objects = object.getEnumConstants();
		
		if(objects.length>0){
  		//randomly select value
          int enumValue = getRandomIntIncludingZero(objects.length);
  		
          paramListLeft[parameterIndex] = objects[enumValue].getClass();
          paramListRight[parameterIndex] = objects[enumValue].getClass();
  		
          Object clazz = specialValues.getSpecialValue(parameterIndex+1);
          if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
            argListLeft[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
            argListRight[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
          }
          else{
            argListLeft[parameterIndex]= objects[enumValue];
            argListRight[parameterIndex]= objects[enumValue];
          }
		}
		else{
		  throw new IllegalArgumentException(object.getCanonicalName()+" is an empty enum and can thus not be tested. Add a value or exclude it from the tests.");
		}
	}

	private static void fillCollections(ArrayList<Class<?>> constructedObjects, Class<?>[] parameters, Type[] types, Class<?>[] paramListLeft,Object[] argListLeft,Class<?>[] paramListRight, Object[] argListRight, int parameterIndex, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		
		Class<?> parameter = parameters[parameterIndex];
		
		paramListLeft[parameterIndex] = Class.forName(parameter.getName());
		paramListRight[parameterIndex] = Class.forName(parameter.getName());

        Object clazz = specialValues.getSpecialValue(parameterIndex+1);
        if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
          argListLeft[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
          argListRight[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
		}
		else{
		  
	        Collection<Object> leftList;
	        Collection<Object> rightList;
    		//concrete Collection class will be instantiated
    		if(!parameter.isInterface()){
    			leftList = (Collection<Object>)paramListLeft[parameterIndex].newInstance();
    			rightList = (Collection<Object>)paramListRight[parameterIndex].newInstance();
    		}
    		else if(parameter.isAssignableFrom(List.class)){
    			//default implementation for List = ArrayList
    			leftList = new ArrayList<>();
    			rightList = new ArrayList<>();
    		}
    		else if(parameter.isAssignableFrom(Set.class)){
    			//default implementation for List = HashSet
    			leftList = new HashSet<>();
    			rightList = new HashSet<>();
    		}
    		else if(parameter.isAssignableFrom(Queue.class)){
    			//default implementation for List = PriorityQueue
    			leftList = new PriorityQueue<>();
    			rightList = new PriorityQueue<>();
    		}
    		else{
    			throw new AssertionError("Unsupported Collection type:"+parameter.getName());
    		}
    				
    		ParameterizedType type = (ParameterizedType) types[parameterIndex];
    		//retrieve type of class, in a java.util.List there is only one type thus use [0]
    		Class<?> type2 = (Class<?>)type.getActualTypeArguments()[0];
    		
    		//create objects for the List
    		HashMap<Object, Object> map = createObjects(constructedObjects, Class.forName(type2.getName()), implOfAbstractClasses, specialValues, false);
    		Set<Entry<Object,Object>> entries = map.entrySet();
    					
    		for(Entry<Object,Object> entry : entries){
    				
    			//classes of SortedSet must implement comparable interface
    			if(ClassUtils.getAllInterfaces(parameter).contains(SortedSet.class) && !ClassUtils.getAllInterfaces(entry.getKey().getClass()).contains(Comparable.class)){
    				throw new AssertionError("The class ("+entry.getKey().getClass().getName()+") which is used in a SortedSet ("+parameter.getName()+") must implement the Comparable interface!");
    			}
    				
    			leftList.add(entry.getKey());
    			rightList.add(entry.getValue());
    		}
    		argListLeft[parameterIndex]= leftList;
    		argListRight[parameterIndex]= rightList;	
		}
	}
	
	private static void fillMaps(ArrayList<Class<?>> constructedObjects, Class<?>[] parameters, Type[] types, Class<?>[] paramListLeft, Object[] argListLeft,Class<?>[] paramListRight, Object[] argListRight, int parameterIndex, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		
		Class<?> parameter = parameters[parameterIndex];
		
		paramListLeft[parameterIndex] = Class.forName(parameter.getName());
		paramListRight[parameterIndex] = Class.forName(parameter.getName());
		
        Object clazz = specialValues.getSpecialValue(parameterIndex+1);
        if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
          argListLeft[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
          argListRight[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
        }
        else{
		
    		Map<Object,Object> leftList;
    		Map<Object,Object> rightList;
    		
    		//concrete Map class will be instantiated
    		if(!parameter.isInterface()){
    			leftList = (Map<Object,Object>)paramListLeft[parameterIndex].newInstance();
    			rightList = (Map<Object,Object>)paramListRight[parameterIndex].newInstance();
    		}
    		else {
    			//default implementation for Map = HashMap
    			leftList = new HashMap<>();
    			rightList = new HashMap<>();
    		}
    				
    		//retrieve types of the classes <key,value>
    		//key
    		ParameterizedType type = (ParameterizedType) types[parameterIndex];
    		Class<?> keyType = (Class<?>)type.getActualTypeArguments()[0];
    		//value
    		Class<?> valueType = (Class<?>)type.getActualTypeArguments()[1];
    		
    		//create objects for the Map
    		HashMap<Object, Object> values = createObjects(constructedObjects, Class.forName(valueType.getName()), implOfAbstractClasses, specialValues, false);
    		Set<Entry<Object,Object>> entriesV = values.entrySet();
    
    		for(Entry<Object,Object> entryV : entriesV){
    
    			HashMap<Object, Object> keys = createObjects(constructedObjects, Class.forName(keyType.getName()), implOfAbstractClasses, specialValues, false);
    			Set<Entry<Object,Object>> entriesK = keys.entrySet();
    
    			for (Entry<Object,Object> entryK : entriesK) {
    				
    				//classes of SortedMap keys must implement comparable interface
    				if(ClassUtils.getAllInterfaces(parameter).contains(SortedMap.class) && !ClassUtils.getAllInterfaces(entryK.getKey().getClass()).contains(Comparable.class)){
    					throw new AssertionError("The key class ("+entryK.getKey().getClass().getName()+") which is used in a SortedMap ("+parameter.getName()+") must implement the Comparable interface!");
    				}
    				
    				leftList.put(entryK.getKey(),entryV.getKey());
    				rightList.put(entryK.getValue(),entryV.getValue());
    				break;
    			}
    		}
    				    
    		argListLeft[parameterIndex]= leftList;
    		argListRight[parameterIndex]= rightList;
        }
	}

	private static void fillObject(ArrayList<Class<?>> constructedObjects, Class<?>[] parameters, Class<?>[] paramListLeft,Object[] argListLeft,Class<?>[] paramListRight,Object[] argListRight, int parameterIndex, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		//Is a normal object
		Class<?> object = Class.forName(parameters[parameterIndex].getName());
			
		//recursively check equals
		HashMap<Object, Object> map = createObjects(constructedObjects, object, implOfAbstractClasses,specialValues, false);
		Set<Entry<Object,Object>> entries = map.entrySet();
			
		for(Entry<Object,Object> entry : entries){
			paramListLeft[parameterIndex] = entry.getKey().getClass();
			paramListRight[parameterIndex] = entry.getValue().getClass();
			
	         Object clazz = specialValues.getSpecialValue(parameterIndex+1);
	         if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
	          argListLeft[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
	          argListRight[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
	        }
	        else{
    			argListLeft[parameterIndex]= entry.getKey();
    			argListRight[parameterIndex]= entry.getValue();
    			
    			//1st condition: calling equals on URL objects is not a good idea cp. http://javaantipatterns.wordpress.com/2007/11/24/comparing-urls-with-urlequals
    			//2nd condition: If a class does not override equals then don't call it otherwise objects with the same values are not equals
    			if(!object.isAssignableFrom(URL.class) && classImplementsEquals(entry.getKey().getClass())){
    				//since only one pair is taken also check equals here for that parameter object (but only a warning is printed out)
    				executeEquals(entry.getKey(), entry.getValue(),true);
    			}
	        }
		}
	}
	
	private static void fillSpecialObject(Class<?>[] parameters, Class<?>[] paramListLeft,Object[] argListLeft,Class<?>[] paramListRight,Object[] argListRight, int parameterIndex, SpecialValueLocator specialValues){
		try{
		  
          Object clazz = specialValues.getSpecialValue(parameterIndex+1);
          if(clazz !=null && parameters[parameterIndex].isAssignableFrom(clazz.getClass())){
		    paramListLeft[parameterIndex] = specialValues.getSpecialValue(parameterIndex+1).getClass();
            paramListRight[parameterIndex] = specialValues.getSpecialValue(parameterIndex+1).getClass();
		    
            argListLeft[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
            argListRight[parameterIndex]= specialValues.getSpecialValue(parameterIndex+1);
          }
          else{
            if(parameters[parameterIndex].isAssignableFrom(URL.class)){
				paramListLeft[parameterIndex] = URL.class;
				paramListRight[parameterIndex] = URL.class;
				
				URL url = new URL("http://www."+getRandomString()+".de");
				
				argListLeft[parameterIndex]= url;
				argListRight[parameterIndex]= url;
			}
            else if(parameters[parameterIndex].isAssignableFrom(URI.class)){
				paramListLeft[parameterIndex] = URI.class;
				paramListRight[parameterIndex] = URI.class;
				
				URI uri = new URI("file://C:/"+getRandomString()+".txt");
				
				argListLeft[parameterIndex]= uri;
				argListRight[parameterIndex]= uri;
			}
            else if(parameters[parameterIndex].isAssignableFrom(Date.class)){
				paramListLeft[parameterIndex] = Date.class;
				paramListRight[parameterIndex] = Date.class;
				
				Date date = new Date(System.currentTimeMillis());
				
				argListLeft[parameterIndex]= date;
				argListRight[parameterIndex]= date.clone();
			}
            else if(parameters[parameterIndex].isAssignableFrom(Calendar.class)){
				paramListLeft[parameterIndex] = Calendar.class;
				paramListRight[parameterIndex] = Calendar.class;
				
				Calendar cal = Calendar.getInstance();
				
				argListLeft[parameterIndex]= cal;
				argListRight[parameterIndex]= cal.clone();
			}
            else if(parameters[parameterIndex].isAssignableFrom(Serializable.class)){
				paramListLeft[parameterIndex] = Serializable.class;
				paramListRight[parameterIndex] = Serializable.class;
				
				Serializable ser = getRandomString();
				
				argListLeft[parameterIndex]= ser;
				argListRight[parameterIndex]= ser;
			}
            else if(parameters[parameterIndex].isAssignableFrom(BigDecimal.class)){
				paramListLeft[parameterIndex] = BigDecimal.class;
				paramListRight[parameterIndex] = BigDecimal.class;
				
				BigDecimal ser = getRandomBigDecimal();
				
				argListLeft[parameterIndex]= ser;
				argListRight[parameterIndex]= ser;
			}
            else if(parameters[parameterIndex].isAssignableFrom(Color.class)){
				paramListLeft[parameterIndex] = Color.class;
				paramListRight[parameterIndex] = Color.class;
				
				float red= r.nextFloat();
				float green= r.nextFloat();
				float blue= r.nextFloat();
				
				argListLeft[parameterIndex]= new Color(red,green,blue);
				argListRight[parameterIndex]= new Color(red,green,blue);
			}
            else if(parameters[parameterIndex].isAssignableFrom(Image.class)){
				paramListLeft[parameterIndex] = Image.class;
				paramListRight[parameterIndex] = Image.class;
				
				argListLeft[parameterIndex]= new BufferedImage(256, 256,BufferedImage.TYPE_INT_RGB);
				argListRight[parameterIndex]= new BufferedImage(256, 256,BufferedImage.TYPE_INT_RGB);
			}
            else if(parameters[parameterIndex].isAssignableFrom(ImageObserver.class)){
				paramListLeft[parameterIndex] = ImageObserver.class;
				paramListRight[parameterIndex] = ImageObserver.class;
				
				argListLeft[parameterIndex]= new Button("Button");
				argListRight[parameterIndex]= new Button("Button");
			}
            else if(parameters[parameterIndex].isAssignableFrom(KeyStore.class)){
				paramListLeft[parameterIndex] = KeyStore.class;
				paramListRight[parameterIndex] = KeyStore.class;
				
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				
				argListLeft[parameterIndex]= ks;
				argListRight[parameterIndex]= ks;
			}
            else if(parameters[parameterIndex].isAssignableFrom(PrivateKey.class)){
				paramListLeft[parameterIndex] = PrivateKey.class;
				paramListRight[parameterIndex] = PrivateKey.class;
				
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				keyGen.initialize(512, random);
				
				KeyPair pair = keyGen.generateKeyPair();
				PrivateKey priv = pair.getPrivate();
				
				argListLeft[parameterIndex]= priv;
				argListRight[parameterIndex]= priv;
			}
            else if(parameters[parameterIndex].isAssignableFrom(Certificate.class)){
				paramListLeft[parameterIndex] = Certificate.class;
				paramListRight[parameterIndex] = Certificate.class;
				
				//alternative way to get a certificate, however need to access com.sun.* classes
				//argListLeft[j]= new X509CertImpl();
				//argListRight[j]= new X509CertImpl();
				argListLeft[parameterIndex]= readCertificate();
				argListRight[parameterIndex]= readCertificate();
				
			}
			else if(parameters[parameterIndex].isAssignableFrom(X509Certificate.class)){
				paramListLeft[parameterIndex] = X509Certificate.class;
				paramListRight[parameterIndex] = X509Certificate.class;
				
				//alternative way to get a certificate, however need to access com.sun.* classes
				//argListLeft[j]= new X509CertImpl();
				//argListRight[j]= new X509CertImpl();
				argListLeft[parameterIndex]= readCertificate();
				argListRight[parameterIndex]= readCertificate();
			}
			else if(parameters[parameterIndex].isAssignableFrom(Pattern.class)){
			
				paramListLeft[parameterIndex] = Pattern.class;
				paramListRight[parameterIndex] = Pattern.class;
				
				argListLeft[parameterIndex]= Pattern.compile(".");
				argListRight[parameterIndex]= Pattern.compile(".");
			}
			else if(parameters[parameterIndex].isAssignableFrom(StringBuilder.class)){
			
				paramListLeft[parameterIndex] = StringBuilder.class;
				paramListRight[parameterIndex] = StringBuilder.class;
				
				String rnd = getRandomString(); 
				
				argListLeft[parameterIndex]= new StringBuilder(rnd);
				argListRight[parameterIndex]= new StringBuilder(rnd);
			}
			else if(parameters[parameterIndex].isAssignableFrom(StringBuffer.class)){
			    paramListLeft[parameterIndex] = StringBuffer.class;
				paramListRight[parameterIndex] = StringBuffer.class;
				
				String rnd = getRandomString(); 
				
				argListLeft[parameterIndex]= new StringBuffer(rnd);
				argListRight[parameterIndex]= new StringBuffer(rnd);
			}
			else if(parameters[parameterIndex].isAssignableFrom(InputStream.class)){
			    paramListLeft[parameterIndex] = InputStream.class;
				paramListRight[parameterIndex] = InputStream.class;
				
				byte[] rnd = getRandomByteArrayPrimitive();
				
				argListLeft[parameterIndex]= new ByteArrayInputStream(rnd);
				argListRight[parameterIndex]= new ByteArrayInputStream(rnd);
				
				if(enableWarnings){
					System.err.println("Warning: There is an InputStream parameter. A random inputstream is created however if a specific file is expected this will probably fail. Maybe in future the Tester will be extended to support this.");
				}
			}
			else if(parameters[parameterIndex].isAssignableFrom(Blob.class)){
				paramListLeft[parameterIndex] = Blob.class;
				paramListRight[parameterIndex] = Blob.class;
				
				byte[] rnd = getRandomByteArrayPrimitive(); 
				
				try {
					argListLeft[parameterIndex]= new SerialBlob(rnd);
					argListRight[parameterIndex]= new SerialBlob(rnd);
				}
				catch (SerialException e) {
					throw new RuntimeException("Error creating random Blob: "+e.getMessage(),e);
				}
				catch (SQLException e) {
					throw new RuntimeException("Error creating random Blob!"+e.getMessage(),e);
				}
			}
			else if(parameters[parameterIndex].isAssignableFrom(StackTraceElement.class)){
			  paramListLeft[parameterIndex] = StackTraceElement.class;
              paramListRight[parameterIndex] = StackTraceElement.class;
              
              String declaringClass = getRandomString();
              String methodName = getRandomString();
              String fileName = getRandomString();
              int line = getRandomInt();
              
              argListLeft[parameterIndex]= new StackTraceElement(declaringClass,methodName,fileName,line);
              argListRight[parameterIndex]= new StackTraceElement(declaringClass,methodName,fileName,line);
			}
			else if(parameters[parameterIndex].isAssignableFrom(XMLGregorianCalendar.class)){
              paramListLeft[parameterIndex] = XMLGregorianCalendar.class;
              paramListRight[parameterIndex] = XMLGregorianCalendar.class;
              
              GregorianCalendar cal = new GregorianCalendar();
              try {
                argListLeft[parameterIndex]= DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                argListRight[parameterIndex]= DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
              }
              catch (DatatypeConfigurationException e) {
                throw new RuntimeException("Error creating XMLGregorianCalendar!"+e.getMessage(),e);
              }
			}
			else if(parameters[parameterIndex].isAssignableFrom(LocalDate.class)){
			  paramListLeft[parameterIndex] = LocalDate.class;
              paramListRight[parameterIndex] = LocalDate.class;
              
              argListLeft[parameterIndex]= LocalDate.now();
              argListRight[parameterIndex]= LocalDate.now();
			}
			else if(parameters[parameterIndex].isAssignableFrom(LocalTime.class)){
              paramListLeft[parameterIndex] = LocalTime.class;
              paramListRight[parameterIndex] = LocalTime.class;
              
              argListLeft[parameterIndex]= LocalTime.now();
              argListRight[parameterIndex]= LocalTime.now();
            }
			else{
				throw new AssertionError("Unsupported class: "+parameters[parameterIndex].getName()+" - report this to the unittest-utilities project! (And for now disable automatic testing for that class)");
			}
          }
		}
		catch (URISyntaxException use) {
			throw new RuntimeException("Error creating URI due to: "+use.getMessage(),use);
		}
		catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException("Error creating Keystore due to: "+nsae.getMessage(),nsae);
		}
		catch (MalformedURLException mue) {
			throw new RuntimeException("Error creating URL due to: "+mue,mue);
		}
		catch (KeyStoreException kse) {
			throw new RuntimeException("Error creating Keystore due to: "+kse.getMessage(),kse);
		}
	}
	
	private static void fillEverything(ArrayList<Class<?>> constructedClasses, Class<?>[] parameters, Type[] types, Object[] argListLeft, Object[] argListRight, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		Class<?>[] paramListLeft = new Class[parameters.length];
		Class<?>[] paramListRight = new Class[parameters.length];
		
		for (int j = 0; j < parameters.length; j++) {
			
			//detect the different types
			if(parameters[j].isPrimitive()){
				fillPrimitiveType(parameters, paramListLeft, argListLeft, paramListRight, argListRight, j, specialValues);
			}
			else if(parameters[j].isArray()){
				fillArray(constructedClasses, parameters, types, paramListLeft, argListLeft, paramListRight, argListRight, j, implOfAbstractClasses, specialValues);
			}
			else if(parameters[j].isEnum()){
				fillEnum(parameters, paramListLeft, argListLeft, paramListRight, argListRight, j, specialValues);
			}
			//check for primitive Object types like Integer, Long, Float etc. and String
			else if(fillJavaLangType(parameters, paramListLeft, argListLeft, paramListRight, argListRight, j, specialValues)){
				continue;
			}
			else if(parameters[j].equals(Collection.class) || ClassUtils.getAllInterfaces(parameters[j]).contains(Collection.class)){
				fillCollections(constructedClasses, parameters, types, paramListLeft, argListLeft, paramListRight, argListRight, j, implOfAbstractClasses,specialValues);
			}
			else if(parameters[j].equals(Map.class) || ClassUtils.getAllInterfaces(parameters[j]).contains(Map.class)){
				fillMaps(constructedClasses, parameters, types, paramListLeft, argListLeft, paramListRight, argListRight, j, implOfAbstractClasses,specialValues);
			}
			else{
				fillObject(constructedClasses, parameters, paramListLeft, argListLeft, paramListRight, argListRight, j, implOfAbstractClasses,specialValues);
			}
		}
	}

	private static void constructObjects(ArrayList<Class<?>> constructedClasses, List<Constructor<?>> constructors, HashMap<Object, Object> returnObjects, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues, boolean allConstructors) throws InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
	  Throwable stored = null;
		for (int i=0; i<constructors.size(); i++) {
		    Constructor<?> constructor = constructors.get(i);
		    
		    //in case special values need to be set
		    specialValues.setNumberOfArgumentsConstructor(constructor.getParameterTypes().length);
			
		    Class<?>[] parameters = constructor.getParameterTypes();
			
			//if no public constructor was available use private one (cp. comment above) and make it accessible
			//constructor.getModifiers()==Modifier.PRIVATE || constructor.getModifiers()==Modifier.PROTECTED ||
			if(!constructor.isAccessible()){
				constructor.setAccessible(true);
			}
			
			//clear constructedClasses for new constructors (recursively called but with 'allConstructors' one is able to identify the initial loop)
			if(allConstructors && constructedClasses.size()>1){
			  constructedClasses.subList(1, constructedClasses.size()).clear();
			}
			
			Object newObjLeft;
			Object newObjRight;
			
			//check if its a enum, then directly instantiate it
			if(constructor.getDeclaringClass().isEnum()){
			  //Class<?> object = Class.forName();
			  Object[] objects = constructor.getDeclaringClass().getEnumConstants();
		        
		      if(objects.length>0){
		      //randomly select value
		        int enumValue = getRandomIntIncludingZero(objects.length);
		        newObjLeft = objects[enumValue];
		        newObjRight = objects[enumValue];
		      }
		      else{
		        throw new IllegalArgumentException(constructor.getDeclaringClass().getCanonicalName()+" is an empty enum and can thus not be tested. Add a value or exclude it from the tests.");
		      }
			}
			else{
			
    			try{
        			//handle no-argument constructors differently otherwise an exception is thrown
        			if(parameters.length>0){
        				Object[] argListLeft=new Object[parameters.length];
        				Object[] argListRight=new Object[parameters.length];
        				
        				Type[] types = constructor.getGenericParameterTypes();
        				
        				//iterate parameters to avoid circular class creation which results in an StackOverflow Error
        				boolean foundCycle=false;
        				for(Class<?> parameterClass : parameters){
        				  
        				  for(Class<?> constructedClass : constructedClasses){
            				  if(parameterClass.isAssignableFrom(constructedClass)){
            				    //Skip constructor: A class creation cycle has been detected. The class '"+constructedClass.getSimpleName()+"' should be created however is needed as parameter for "+constructor.getDeclaringClass().getSimpleName()+" at the same time
            				    foundCycle=true;
            				    break;
            				  }
        				  }
        				}
        				if(foundCycle) {
        				  continue;
        				}
        				
        				fillEverything(constructedClasses, parameters, types, argListLeft, argListRight, implOfAbstractClasses, specialValues);
        				//call constructor
        				newObjLeft = constructor.newInstance(argListLeft);
        				newObjRight = constructor.newInstance(argListRight);
        			}
        			else{
        				//call constructor
        				newObjLeft = constructor.newInstance();
        				newObjRight = constructor.newInstance();
        			}
    			}
    			//allConstructors==false then only one constructor call should succeed (e.g. if parameters should be instantiated just an exemplary object is needed) => skipping failed constructors
    			catch(InstantiationException | IllegalAccessException | InvocationTargetException | AssertionError e){
                  if(!allConstructors){
                    stored = e;
                    continue;
                  }
                  else{
                    throw e;
                  }
                }
			}
			//return newly created objects
			returnObjects.put(newObjLeft,newObjRight);
		}
		if(returnObjects.isEmpty() && stored!=null){
		  throw new RuntimeException(stored);
		}
		else if(returnObjects.isEmpty() && enableWarnings){
		  System.err.println("None of the constructors of the class "+constructors.get(0).getDeclaringClass().getSimpleName()+" could be instantiated (due to creation cycle(s)). Consider revising your application design.");
		}
	}
	
	private static void constructSetMethodsAndCheckEquals(ArrayList<Class<?>> constructedClasses, Class<?> dtoClass, HashMap<Object, Object> constructedObjects, Method[] methods, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for (int i = 0; i < methods.length; i++) {
			
			if(methods[i].getName().startsWith("set")){
				
				Method method = methods[i];
				
				Class<?>[] parameters = method.getParameterTypes();
				Type[] types = method.getGenericParameterTypes();
				
				Object[] argListLeft = new Object[parameters.length];
				Object[] argListRight = new Object[parameters.length];
				
				fillEverything(constructedClasses, parameters, types, argListLeft, argListRight, implOfAbstractClasses, specialValues);

				//call method for every constructed constructor
				Set<Entry<Object,Object>> consts = constructedObjects.entrySet();
				
				for (Entry<Object, Object> entry : consts) {
					Object constLeft = entry.getKey();
					Object constRight = entry.getValue();
					
					//extract old value for comparison with new to see if it was changed
					ExtractionValue oldHashCode = extractValueFromHashCode(dtoClass, constLeft);

					//only continue when the hashCode could be extracted
					if(!oldHashCode.isCouldExtractValue()){
						if(enableWarnings){
							System.err.println(dtoClass.getSimpleName()+": The "+dtoClass.getSimpleName()+" has no 'int hashCode()' method thus it is skipped!");
						}
						continue;
					}
					
					//invoke one setter
					method.invoke(constLeft,argListLeft);
					
					ExtractionValue newHashCode=extractValueFromHashCode(dtoClass, constLeft);
					
					//setter changed hashCode thus should also change result of equals
					if(objectHasChanged(oldHashCode.getExtractedValue(), newHashCode.getExtractedValue())){
						if(!objectHasChanged(constLeft, constRight)){
							throw new AssertionError("Error in "+dtoClass.getSimpleName()+": called ("+method.getName()+") with "+Arrays.toString(argListLeft)+" and the hashCode did change however the result of equals did not! This violates the invariant that equal objects must have equal hashcodes. (Interface contract for Object states: if two objects are equal according to equals(), then they must have the same hashCode() value.)");
						}
					}
					else{
						if(objectHasChanged(constLeft, constRight)){
							throw new AssertionError("Error in "+dtoClass.getSimpleName()+": called ("+method.getName()+") with "+Arrays.toString(argListLeft)+" and the hashCode did not change however the result of equals did! This violates the invariant that equal objects must have equal hashcodes. (Interface contract for Object states: if two objects are equal according to equals(), then they must have the same hashCode() value.)");
						}
					}
					
					//also invoke one other object to keep objects the same
					method.invoke(constRight,argListRight);
				}
			}
		}
	}
	
	private static void constructSetMethods(ArrayList<Class<?>> constructedClasses, Class<?> dtoClass, HashMap<Object, Object> constructedObjects, ArrayList<Method> allMethods, List<Class<?>> implOfAbstractClasses, SpecialValueLocator specialValues) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for (Method method : allMethods) {
			
			if(method.getName().startsWith("set")){

				Class<?>[] parameters = method.getParameterTypes();
				
				//check that it is a 'simple' setters with one parameter
				if(parameters.length>1){
					if(enableWarnings){
						System.err.println(dtoClass.getSimpleName()+": The "+method.getName()+" has more than one parameter thus it is skipped. Only <field>, <setField(...)>, <getField()> type methods, following the java beans code convention, are supported!");
					}
					continue;
				}
				
				Object[] argListLeft = new Object[parameters.length];
				Object[] argListRight = new Object[parameters.length];
				
				Type[] types = method.getGenericParameterTypes();
				
				fillEverything(constructedClasses, parameters, types, argListLeft, argListRight, implOfAbstractClasses, specialValues);

				//call set method for every constructed constructor
				Set<Entry<Object,Object>> consts = constructedObjects.entrySet();
				
				for (Entry<Object, Object> entry : consts) {
					Object constLeft = entry.getKey();
					Object constRight = entry.getValue();
					
					compareOldAndNew(dtoClass, method, argListLeft, constLeft);
					compareOldAndNew(dtoClass, method, argListRight, constRight);
				}
			}
		}
	}

	/**
	 * 
	 * @param dtoClass
	 * @param fields
	 * @param method
	 * @param constructedObject
	 * @param value of the actual field
	 * @returns true when the field could be found otherwise false
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static ExtractionValue extractValueFromField(Class<?> dtoClass, Method method, Object constructedObject) throws IllegalAccessException{
		
		Field[] fields = dtoClass.getDeclaredFields();
		
		//check if method has a corresponding field
		for (int j = 0; j < fields.length; j++) {
			if(StringUtils.uncapitalize(method.getName().substring(3)).equals(fields[j].getName())){
				Field field = fields[j];
				field.setAccessible(true);
				return new ExtractionValue(true, field.get(constructedObject));
			}
		}
		
		//if no value is found in the direct class, check super classes
		List<Field> inheritedFields = getInheritedFields(dtoClass);
		
		for(Field field : inheritedFields){
			if(StringUtils.uncapitalize(method.getName().substring(3)).equals(field.getName())){
				field.setAccessible(true);
				return new ExtractionValue(true, field.get(constructedObject));
			}
		}
		
		return new ExtractionValue(false, null);
	}
	
	/**
	 * 
	 * @param dtoClass
	 * @param fields
	 * @param method
	 * @param constructedObject
	 * @param returnValue of the getter method
	 * @return true when the field could be found otherwise false
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static ExtractionValue extractValueFromGetter(Class<?> dtoClass, Method method, Object constructedObject) throws IllegalAccessException, InvocationTargetException{
		
		Method[] getter = dtoClass.getMethods();
		for (int j = 0; j < getter.length; j++) {
			if(getter[j].getName().equals("get"+method.getName().substring(3)) || getter[j].getName().equals("is"+method.getName().substring(3))){
				try{
					return new ExtractionValue(true, getter[j].invoke(constructedObject, (Object[])null));
				}
				catch(InvocationTargetException ite){
					if(ite.getCause() instanceof EmptyStackException){
						if(enableWarnings){
							System.err.println("Warning @ "+dtoClass.getSimpleName()+": The "+method.getName()+" could not be invoked. The most probable reason is, that it relies on a different internal object which hasn't been instantiated yet. Thus it is skipped!");
						}
						return new ExtractionValue(false, null);
					}
					else if(ite.getCause() instanceof RuntimeException){
						//TODO TvT: Special adaption for DocumentData.class -> when there is no DocumentCache a runtime exception is thrown
						return new ExtractionValue(true, null);
					}
					else{
						throw ite;
					}
				}
			}
		}
		return new ExtractionValue(false, null);
	}
	
	/**
	 * 
	 * @param dtoClass
	 * @param fields
	 * @param method
	 * @param constructedObject
	 * @param returnValue of the getter method
	 * @return true when the field could be found otherwise false
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static ExtractionValue extractValueFromHashCode(Class<?> dtoClass, Object constructedObject) throws IllegalAccessException, InvocationTargetException{
		
		Method[] methods = dtoClass.getMethods();
		for (int j = 0; j < methods.length; j++) {
			if("hashCode".equals(methods[j].getName())){
				if(Modifier.isPublic(methods[j].getModifiers())){
					Class<?>[] parameters = methods[j].getParameterTypes();
					if(parameters.length==0){
						Class<?> returnType = methods[j].getReturnType();
						if(returnType.isAssignableFrom(int.class)){
							return new ExtractionValue(true, methods[j].invoke(constructedObject, (Object[])null));
						}
					}
				}
			}
		}
		return new ExtractionValue(false, null);
	}
	
	private static void executeEquals(Object left, Object right, boolean warningOnly){
		
		Object nill = null;
		//equals to null
		if(left.equals(nill) || right.equals(nill)){
		  if(warningOnly && enableWarnings){
		    System.err.println("Error testEquals() - objects match null!");
		  }
		  else {
		    throw new AssertionError("Error testEquals() - objects match null!");
		  }
		}
		//equals on itself
		if(!left.equals(left) || !right.equals(right)){
		  if(warningOnly && enableWarnings){
		    System.err.println("Error testEquals() - object ("+left.getClass().getName()+") isn't equals to itself!");
		  }
		  else{
		    throw new AssertionError("Error testEquals() - object ("+left.getClass().getName()+") isn't equals to itself!");
		  }
		}
		//equals on a different object
		if(left.equals(new Object()) || right.equals(new Object())){
		  if(warningOnly && enableWarnings){
		    System.err.println("Error testEquals() - object ("+left.getClass().getName()+") always returns true!");
		  }
		  else{
		    throw new AssertionError("Error testEquals() - object ("+left.getClass().getName()+") always returns true!");
		  }
		}
		//equals to a different object with same values
		if(!left.equals(right)){
		  if(warningOnly && enableWarnings){
		    System.err.println("(Parameter) objects should be equals but in fact they are not ("+left.getClass().getName()+") Values: "+left+" vs. "+right+"! An exception may be time related classes which may contain a different timestamp.");
		  }
		  else{
			throw new AssertionError("(Parameter) objects should be equals but in fact they are not ("+left.getClass().getName()+") Values: "+left+" vs. "+right+"!  An exception may be time related classes which may contain a different timestamp.");
		  }
		}
	}
	
	private static boolean objectHasChanged(Object left, Object right){
		if(left == null && right==null){
			return false;
		}
		else if(left == null || right == null){
			return true;
		}
		else{
			return !left.equals(right);
		}
	}
	
	private static void compareOldAndNew(Class<?> dtoClass, Method method, Object[] argList, Object constructor) throws IllegalAccessException, InvocationTargetException{
		
		//extract the old values for a later comparison (old=value after creating object with constrcutor)
		ExtractionValue oldValueOfTheField = extractValueFromField(dtoClass, method, constructor);
		ExtractionValue oldValueOfGetter = extractValueFromGetter(dtoClass, method, constructor);
		
		//invoke one setter on the left object
		method.invoke(constructor,argList);
		
		//only continue when objects could be extracted
		if(oldValueOfTheField.isCouldExtractValue()){
			if(objectHasChanged(oldValueOfTheField.getExtractedValue(), argList[0])){
				
				//extract new value from
				 ExtractionValue newValueOfTheField = extractValueFromField(dtoClass, method, constructor);
				
				if(!objectHasChanged(oldValueOfTheField.getExtractedValue(),newValueOfTheField.getExtractedValue())){
					throw new AssertionError("Error @ "+dtoClass.getSimpleName()+": Called the setter ("+method.getName()+") but the corresponding field ("+StringUtils.uncapitalize(method.getName().substring(3))+") didn't change!");
				}
			}
			else{
				//extract new value from
				ExtractionValue sameValueOfTheField = extractValueFromField(dtoClass, method, constructor);
				
				if(objectHasChanged(oldValueOfTheField.getExtractedValue(),sameValueOfTheField.getExtractedValue())){
					throw new AssertionError("Error @ "+dtoClass.getSimpleName()+": Called the setter ("+method.getName()+") with the same value however the corresponding field ("+StringUtils.uncapitalize(method.getName().substring(3))+") now has a different value!");
				}
			}
		}
		else{
			if(enableWarnings){
				System.err.println("Warning @ "+dtoClass.getSimpleName()+": The "+method.getName()+" has no corresponding field ("+StringUtils.uncapitalize(method.getName().substring(3))+") thus it is skipped. Please follow the java beans code convention!");
			}
		}
		
		//only continue when getter could be extracted
		if(oldValueOfGetter.isCouldExtractValue()){
			if(objectHasChanged(oldValueOfGetter.getExtractedValue(), argList[0])){
				ExtractionValue newValueOfGetter=extractValueFromGetter(dtoClass, method, constructor);
				
				if(!objectHasChanged(oldValueOfGetter.getExtractedValue(),newValueOfGetter.getExtractedValue())){
					throw new AssertionError("Error @ "+dtoClass.getSimpleName()+": called the setter ("+method.getName()+") but the return value of the corresponding (get"+method.getName().substring(3)+") didn't change!");
				}
			}
			else{
				//extract new value from
				ExtractionValue sameValueOfGetter =extractValueFromGetter(dtoClass, method, constructor);
				
				if(objectHasChanged(oldValueOfTheField.getExtractedValue(),sameValueOfGetter.getExtractedValue())){
					throw new AssertionError("Error @ "+dtoClass.getSimpleName()+": called the setter ("+method.getName()+") with the same value however the corresponding (get"+method.getName().substring(3)+") now has a different value!");
				}
			}
		}
		else{
			if(enableWarnings){
				System.err.println("Warning @ "+dtoClass.getSimpleName()+": The "+method.getName()+" has no getter ("+"is/get"+method.getName().substring(3)+") thus it is skipped. Please follow the java beans code convention!");
			}
		}
	}
	
	/**
	 * Since getDeclaredFields() does not return inherited fields this method
	 * recursively collects all super class fields and returns them. 
	 * 
	 * @param classToCheck
	 * @return
	 */
	private static List<Field> getInheritedFields(Class<?> clazz) {
		 List<Field> fields = new ArrayList<>();
		 
		 Class<?> classToCheck = clazz;
		 
		 while(classToCheck.getSuperclass()!=null){
			 fields.addAll(Arrays.asList(classToCheck.getSuperclass().getDeclaredFields()));
			 classToCheck=classToCheck.getSuperclass();
		 }

		 return fields;
	}
	
	/**
	 * Since getDeclaredFields() does not return inherited protected fields this method
	 * recursively collects all super class fields and returns them. 
	 * 
	 * @param classToCheck
	 * @return
	 */
	private static ArrayList<Method> getInheritedProtectedMethods(Class<?> clazz) {
		ArrayList<Method> methods = new ArrayList<>();
		ArrayList<Method> protectedMethods = new ArrayList<>();
		 
		Class<?> classToCheck = clazz;
		
		 //exclude Object methods. To include object protected methods change to: classToCheck!=null
		 while(classToCheck.getSuperclass()!=null){
			 methods.addAll(Arrays.asList(classToCheck.getDeclaredMethods()));
			 classToCheck=classToCheck.getSuperclass();
		 }
		 
		 //clear public and private methods
		 for(Method method : methods){
			 if(Modifier.isProtected(method.getModifiers())){
				 method.setAccessible(true);
				 protectedMethods.add(method);
			 }
		 }

		 return protectedMethods;
	 }
	
	private static boolean classImplementsEquals(Class<?> dtoClass){
		//only public methods for the DTO class are relevant 
		List<Method> methods = Arrays.asList(dtoClass.getDeclaredMethods()); 
		
		for (Method method : methods) {
			//public boolean equals(Object o)
			if("equals".equals(method.getName())){
				if(Modifier.isPublic(method.getModifiers())){
					Class<?>[] parameters = method.getParameterTypes();
					if(parameters.length==1 && parameters[0].isAssignableFrom(Object.class)){
						Class<?> returnType = method.getReturnType();
						if(returnType.isAssignableFrom(boolean.class)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private static boolean classImlementsHashCode(Class<?> dtoClass){
		
		//only public methods for the DTO class are relevant 
		List<Method> methods = Arrays.asList(dtoClass.getDeclaredMethods()); 
		
		for (Method method : methods) {
			//public int hashCode() 
			if("hashCode".equals(method.getName())){
				if(Modifier.isPublic(method.getModifiers())){
					Class<?>[] parameters = method.getParameterTypes();
					if(parameters.length==0){
						Class<?> returnType = method.getReturnType();
						if(returnType.isAssignableFrom(int.class)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private static X509Certificate readCertificate(){
		
		X509Certificate cert = null;
		InputStream inStream = null;
		
		try {
			inStream = AutoTester.class.getResourceAsStream("/Test.cer");
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate)cf.generateCertificate(inStream);
			inStream.close();
			inStream=null;
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException("Could not load example Certificate: "+fnfe.getMessage(),fnfe);
		}
		catch (CertificateException ce) {
			throw new RuntimeException("Could not instantiate Certificate: "+ce.getMessage(),ce);
		}
		catch (IOException ioe) {
			throw new RuntimeException("Could not load example Certificate: "+ioe.getMessage(),ioe);
		}
		finally{
			if(inStream!=null){
				try {
					inStream.close();
				}
				catch (IOException e) {
					//swallow to not overwrite original exception
				}
			}
		}
		
		return cert;
	}
	
	static Byte getRandomByte(){
		byte[] b = new byte[1];
		AutoTester.r.nextBytes(b);
		return Byte.valueOf(b[0]);
	}
	
	static byte[] getRandomByteArrayPrimitive(){
		
		byte[] b = new byte[AutoTester.getRandomInt(42)];
		AutoTester.r.nextBytes(b);
		
		if(b.length>0){
			//add EOF to the end (used e.g. for byteArrayInputStream)
			b[b.length-1]=0xffffffff;
		}
		
		return b;
	}
	
	static Byte[] getRandomByteArray(){
		byte[] b = new byte[AutoTester.getRandomInt(42)];
		AutoTester.r.nextBytes(b);
		
		Byte[] objectByteArray = new Byte[b.length];
		for(int i = 0; i < b.length; i++){
			objectByteArray[i] = Byte.valueOf(b[i]);
		}
		return objectByteArray;
	}
	
	static Integer getRandomInteger(){
	  return Integer.valueOf(getRandomInt());
	}
		
	static int getRandomInt(){
      return AutoTester.getRandomInt(Integer.MAX_VALUE);
    }
	
	static int getRandomInt(int n){
	   int i = 0;
	   while(i==0) i = AutoTester.r.nextInt(n);

	   return i;
	}
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	static int getRandomIntIncludingZero(int range){
	  return AutoTester.r.nextInt(range);
   }
	
	static String getRandomUnsignedIntAsString(int range){
		return String.valueOf(Math.abs(getRandomInt(range)));
	}
	
	static int[] getRandomIntArrayPrimitive(){
		int[] b = new int[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < b.length; i++){
			b[i] = AutoTester.getRandomInt();
		}
		return b;
	}
	
	static Integer[] getRandomIntegerArray(){
		Integer[] b = new Integer[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < b.length; i++){
			b[i] = AutoTester.getRandomInteger();
		}
		return b;
	}
	
	/**
	 * 
	 * @return a primitive random float number except 0
	 */
	static float getRandomFloat(){
	  float f = 0f;
      while(f==0f) f = AutoTester.r.nextFloat();
	  
      return f;
    }
	
	static Float getRandomFloatAsObject(){
		return Float.valueOf(getRandomFloat());
	}
	
	static float[] getRandomFloatArrayPrimitive(){
		float[] f = new float[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < f.length; i++){
			f[i] = AutoTester.getRandomFloat();
		}
		return f;
	}
	
	static Float[] getRandomFloatArray(){
		Float[] f = new Float[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < f.length; i++){
			f[i] = AutoTester.getRandomFloatAsObject();
		}
		return f;
	}
	
	static double getRandomDouble(){
	   double d = 0d;
	   while(d==0d) d = AutoTester.r.nextDouble();
	      
	   return d;
	}
	
	static Double getRandomDoubleAsObject(){
		return Double.valueOf(AutoTester.getRandomDouble());
	}
	
	static double[] getRandomDoubleArrayPrimitive(){
		double[] d = new double[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < d.length; i++){
			d[i] = AutoTester.getRandomDouble();
		}
		return d;
	}
	
	static Double[] getRandomDoubleArray(){
		Double[] d = new Double[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < d.length; i++){
			d[i] = AutoTester.getRandomDoubleAsObject();
		}
		return d;
	}

	static long getRandomLong(){
	  long l = 0; 
	  while(l==0) {
	    l = AutoTester.r.nextLong();
	  }
	  return l;
	}
	
	static Long getRandomLongAsObject(){
		return Long.valueOf(AutoTester.getRandomLong());
	}
	
	static long[] getRandomLongArrayPrimitive(){
		long[] l = new long[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < l.length; i++){
			l[i] = AutoTester.getRandomLong();
		}
		return l;
	}
	
	static Long[] getRandomLongArray(){
		Long[] l = new Long[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < l.length; i++){
			l[i] = AutoTester.getRandomLongAsObject();
		}
		return l;
	}
	
	static Boolean getRandomBoolean(){
		return Boolean.valueOf(AutoTester.r.nextBoolean());
	}
	
	static boolean[] getRandomBooleanArrayPrimitive(){
		boolean[] l = new boolean[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < l.length; i++){
			l[i] = getRandomBoolean().booleanValue();
		}
		return l;
	}
	
	static Boolean[] getRandomBooleanArray(){
		Boolean[] b = new Boolean[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < b.length; i++){
			b[i] = getRandomBoolean();
		}
		return b;
	}
	
	static Short getRandomShort(){
		return Short.valueOf((short)(AutoTester.getRandomInt(Short.MIN_VALUE*(-2)) - Short.MIN_VALUE));
	}
	
	static short[] getRandomShortArrayPrimitive(){
		short[] s = new short[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < s.length; i++){
			s[i] = getRandomShort().shortValue();
		}
		return s;
	}
	
	static Short[] getRandomShortArray(){
		Short[] s = new Short[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < s.length; i++){
			s[i] = getRandomShort();
		}
		return s;
	}
	
	static Character getRandomCharacter(){
		return Character.valueOf((char)AutoTester.getRandomInt(Character.MAX_VALUE+1));
	}
	
	static char[] getRandomCharArrayPrimitive(){
		char[] c = new char[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < c.length; i++){
			c[i] = getRandomCharacter().charValue();
		}
		return c;
	}
	
	static Character[] getRandomCharacterArray(){
		Character[] c = new Character[AutoTester.getRandomInt(42)];
		
		for(int i = 0; i < c.length; i++){
			c[i] = getRandomCharacter();
		}
		return c;
	}
	
	static String getRandomString(){
		return UUID.randomUUID().toString();
	}
	
	static BigDecimal getRandomBigDecimal(){
		return new BigDecimal(Math.abs(AutoTester.getRandomInt(Integer.MAX_VALUE)));
	}
	
	private static class ExtractionValue{
		private boolean couldExtractValue;
		private Object extractedValue;
		
		public ExtractionValue(boolean couldExtractValue, Object value){
			this.couldExtractValue=couldExtractValue;
			this.extractedValue=value;
		}
		public boolean isCouldExtractValue() {
			return this.couldExtractValue;
		}
		public Object getExtractedValue() {
			return this.extractedValue;
		}
    
		@Override
    public String toString() {
      return "ExtractionValue [couldExtractValue=" + this.couldExtractValue + ", extractedValue=" + this.extractedValue + "]";
    }
		
		
	}
}