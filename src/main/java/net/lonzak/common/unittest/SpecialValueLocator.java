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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sometimes while auto testing classes it is necessary to provide special formats or ranges [...].
 * This can be done using the special value mechanism provides by this class.
 * 
 * @author Lonzak
 *
 */
public final class SpecialValueLocator {
  
  private Map<ConstructorValue, ConstructorValue> specialValues;
  private int numberOfArgumentsConstructor=-1;
  
  /**
   * If no special value should be used.
   */
  public static final SpecialValueLocator NONE = new SpecialValueLocator(new ArrayList<ConstructorValue>());
 
  /**
   * Initializes the list of special values with one value.
   * To add further values use the {@link #addSpecialValue(ConstructorValue)} method. 
   * 
   * @param constructorValue the special value which should be used for the Constructor
   */
  public SpecialValueLocator(ConstructorValue constructorValue) {
    this.specialValues= new HashMap<>();
    this.specialValues.put(constructorValue, constructorValue);
    this.numberOfArgumentsConstructor=constructorValue.getNumberOfArguments();
  }
  
  /**
   * Constructs a special value list to be able to set individual constructor arguments.
   * @param constructorValues a list of special values
   */
  public SpecialValueLocator(List<ConstructorValue> constructorValues) {
    this.specialValues= new HashMap<>();
    
    for(ConstructorValue constructorValue : constructorValues) {
      this.specialValues.put(constructorValue,constructorValue);
    }
  }
  
  /**
   * 
   * @param constructorValue the special value which should be used
   */
  public void addSpecialValue(ConstructorValue constructorValue){
    this.specialValues.put(constructorValue,constructorValue);
  }
  
  Map<ConstructorValue, ConstructorValue> getSpecialValues(){
    return this.specialValues;
  }

  /**
   * Sets the number of arguments of the constructor
   * 
   * This variable is set while iterating over the constructors in the AutoTester.
   * Not to be publicly called.
   * 
   * @param numberOfArgumentsConstructor the number of arguments of that constructor
   */
  void setNumberOfArgumentsConstructor(int numberOfArgumentsConstructor) {
    this.numberOfArgumentsConstructor = numberOfArgumentsConstructor;
  }

  /**
   * 
   * @param location the location of the special value which should be retrieved
   * @return the special object or null
   */
  public Object getSpecialValue(ConstructorValue location) {
    return this.specialValues.get(location);
  }
  
  /**
   * 
   * @param numberOfArguments of the special value to retrieve
   * @param parameterIndex of the special value to retrieve
   * @param dataType of the parameter
   * 
   * @return the special object or null
   */
  public Object getSpecialValue(int numberOfArguments, int parameterIndex, Class<?> dataType){
    return this.specialValues.get(new ConstructorValue(numberOfArguments,parameterIndex,null,dataType));
  }
  
  /**
   * Retrieves the special value by using the parameterIndex.
   * The umberOfArguments must be set in advance. {@link SpecialValueLocator#setNumberOfArgumentsConstructor(int)} 
   * 
   * @param parameterIndex of the special value to retrieve
   * @return the special object or null
   */
  Object getSpecialValue(int parameterIndex,  Class<?> dataType){
    if(this.numberOfArgumentsConstructor!=-1){
      ConstructorValue specialValue = this.specialValues.get(new ConstructorValue(this.numberOfArgumentsConstructor,parameterIndex,null,dataType));
      return specialValue==null ? null : specialValue.getValue();
    }
    else{
      if(this.specialValues.isEmpty()) {
        return null;
      }
      throw new IllegalStateException("The current constructor index has not been set!");
    }
  }
  
  /**
   * 
   * @param parameterIndex 
   * @return the data type or null
   */
  Class<?> getDataType(int parameterIndex, Class<?> dataType){
    if(this.numberOfArgumentsConstructor!=-1){
      ConstructorValue value = this.specialValues.get(new ConstructorValue(this.numberOfArgumentsConstructor,parameterIndex,null,dataType));
      return value==null ? null : value.getDataType();
    }
    else{
      if(this.specialValues.isEmpty()) {
        return null;
      }
      throw new IllegalStateException("The current constructor index has not been set!");
    }
  }
  
  /**
   * Checks whether the SpecialValue locator is used or not (cp. {@link SpecialValueLocator#NONE})
   * @return true if used otherwise false
   */
  public boolean isEmpty() {
    if(this.specialValues.isEmpty()) return true;
    return false;
  }

  
  @Override
  public String toString() {
    return "SpecialValueLocator [specialValues=" + this.specialValues + ", numberOfArguments="+this.numberOfArgumentsConstructor + "]";
  }

  /**
   * The location element of a certain value which should be set.
   * A location is defined by the number of arguments and the index of the parameter.
   * 
   * @author 225010
   *
   */
  public static final class ConstructorValue{
    
    private int numberOfArguments=-1;
    private int parameterIndex=-1;
    private Object value;
    private Class<?> dataType;
    
    /**
     * The location identifies which parameter of which constructor to set.
     * The first parameter (numberOfArguments) is to identify the constructor.
     * A constructor can be identified by the number of arguments it has.
     * <p>
     * - The argument is defined by its index e.g. the 3rd parameter should be set so its index is 3.<p>
     * - The index is '1' based. Meaning that the first attribute of a 1 argument constructor is 1,1.<p>
     * - If only a default constructor exists and a special set Object should be set then use (0,1) 
     * 
     * @param numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex the index of the parameter of that constructor
     * @param value the special value to set
     * @param dataType of the special value
     * 
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, Object value, Class<?> dataType){
      if(numberOfArguments!=0 && numberOfArguments<parameterIndex) throw new IllegalArgumentException("The number of arguments ("+numberOfArguments+") can not be smaller than the parameter index ("+parameterIndex+")!");
      if(numberOfArguments<0 || parameterIndex <1) throw new IllegalArgumentException("Illegal value for number of constructor arguments ("+numberOfArguments+") or the parameter index("+parameterIndex+")!");
      this.numberOfArguments=numberOfArguments;
      this.parameterIndex=parameterIndex;
      this.value=value;
      this.dataType=dataType;
    }
    
    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex the index of the parameter of that constructor
     * @param value object value
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, Object value){
      this(numberOfArguments,parameterIndex,value,value.getClass());
    }
    
    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, java.lang.Object, java.lang.Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive int parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, int value){
      this(numberOfArguments,parameterIndex,value,int.class);
    }
    
    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive long parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, long value){
      this(numberOfArguments,parameterIndex,value,long.class);
    }

    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive float parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, float value){
      this(numberOfArguments,parameterIndex,value,float.class);
    }

    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive double parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, double value){
      this(numberOfArguments,parameterIndex,value,double.class);
    }

    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive short parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, short value){
      this(numberOfArguments,parameterIndex,value,short.class);
    }

    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive char parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, char value){
      this(numberOfArguments,parameterIndex,value,char.class);
    }
    
    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive byte parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, byte value){
      this(numberOfArguments,parameterIndex,value,byte.class);
    }

    /**
     * Constructor to construct an object parameter. Also can be a standard string, an array or an Enum. 
     * @see #ConstructorValue(int, int, Object, Class)
     * 
     * @param numberOfArguments numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex parameterIndex the index of the parameter of that constructor
     * @param value primitive boolean parameter
     */
    public ConstructorValue(int numberOfArguments, int parameterIndex, boolean value){
      this(numberOfArguments,parameterIndex,value,boolean.class);
    }

    
    /**
     * Internal Constructor to be able to locate a special value
     * @param numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex  the index of the parameter of that constructor
     */
    ConstructorValue(int numberOfArguments, int parameterIndex){
      if(numberOfArguments!=0 && numberOfArguments<parameterIndex) throw new IllegalArgumentException("The number of arguments ("+numberOfArguments+") can not be smaller than the parameter index ("+parameterIndex+")!");
      if(numberOfArguments<0 || parameterIndex <1) throw new IllegalArgumentException("Illegal value for number of constructor arguments ("+numberOfArguments+") or the parameter index("+parameterIndex+")!");
      this.numberOfArguments=numberOfArguments;
      this.parameterIndex=parameterIndex;
    }

    /**
     * Returns which constructor should be used. A constructor can be identified by its number of arguments.
     * @return number of arguments of a constructor
     */
    public final int getNumberOfArguments() {
      return this.numberOfArguments;
    }
   
    /**
     * Returns the index of the parameter. Meaning at which position the parameter is located. 
     * @return the parameter index
     */
    public final int getParameterIndex() {
      return this.parameterIndex;
    }
    
    /**
     * Returns the data type of the special value. This is necessary since primitive types are autoboxed.
     * @return the data type class of the special value
     */
    public final Class<?> getDataType() {
      return this.dataType;
    }
    
    /**
     * 
     * @return the actual 'special' format value which should be used
     */
    public Object getValue() {
      return this.value;
    }

    /**
     * Compares whether a ConstructorValue has the same coordinates (numberOfArguments and parameterIndex)
     * 
     * @param constructorValue to compare this class with
     * 
     * @return true if the coordinates match otherwise false
     */
    public boolean compareCoordinates(ConstructorValue constructorValue) {
      return this.numberOfArguments==constructorValue.getNumberOfArguments() && this.parameterIndex==constructorValue.getParameterIndex();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
      result = prime * result + numberOfArguments;
      result = prime * result + parameterIndex;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ConstructorValue other = (ConstructorValue) obj;
      if (dataType == null) {
        if (other.dataType != null)
          return false;
      } else if (!dataType.equals(other.dataType))
        return false;
      if (numberOfArguments != other.numberOfArguments)
        return false;
      if (parameterIndex != other.parameterIndex)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "Location [numberOfArguments=" + this.numberOfArguments + ", parameterIndex=" + this.parameterIndex + ", value: "+this.value+", data type: "+this.dataType+"]";
    }
  } 
}