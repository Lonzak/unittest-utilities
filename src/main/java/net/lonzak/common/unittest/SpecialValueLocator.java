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

import java.util.HashMap;
import java.util.Map;

public final class SpecialValueLocator {
  
  private Map<Location, Object> specialValues;
  private int numberOfArgumentsConstructor=-1;
  
  public static final SpecialValueLocator NONE = new SpecialValueLocator(new HashMap<Location, Object>());
 
  /**
   * Initializes the list of special values with one value.
   * To add further values use the {@link #addSpecialValue(Location, Object)} or {@link #setSpecialValues(Map)} methods. 
   * 
   * @param location of the special value 
   * @param specialValue the special value which should be used for the Constructor
   */
  public SpecialValueLocator(Location location, Object specialValue) {
    this.specialValues= new HashMap<>();
    this.specialValues.put(location, specialValue);
    this.numberOfArgumentsConstructor=location.getNumberOfArguments();
  }
  
  /**
   * Constructs a special value list to be able to set individual constructor arguments.
   * @param specialValues a list of special values
   */
  public SpecialValueLocator(Map<Location, Object> specialValues) {
    this.specialValues = specialValues;
  }
  
  public void addSpecialValue(Location location, Object specialValue){
    this.specialValues.put(location, specialValue);
  }
  
  public void setSpecialValues(Map<Location, Object> specialValues){
    this.specialValues.putAll(specialValues);
  }

  public int getNumberOfArgumentsConstructor() {
    return this.numberOfArgumentsConstructor;
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
  public Object getSpecialValue(Location location) {
    return this.specialValues.get(location);
  }
  
  /**
   * 
   * @param numberOfArguments of the special value to retrieve
   * @param parameterIndex of the special value to retrieve
   * @return the special object or null
   */
  public Object getSpecialValue(int numberOfArguments, int parameterIndex){
    return this.specialValues.get(new Location(numberOfArguments,parameterIndex));
  }
  
  /**
   * Retrieves the special value by using the parameterIndex.
   * The constructor is used from the {@link #numberOfArgumentsConstructor} 
   * 
   * @param parameterIndex of the special value to retrieve
   * @return the special object or null
   */
  public Object getSpecialValue(int parameterIndex){
    if(this.numberOfArgumentsConstructor!=-1){
      return this.specialValues.get(new Location(this.numberOfArgumentsConstructor,parameterIndex));
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
  public static final class Location{
    
    private int numberOfArguments=-1;
    private int parameterIndex=-1;
    
    /**
     * The location identifies which parameter of which constructor to set.
     * The first parameter (numberOfArguments) is to identify the constructor.
     * A constructor can be identified by the number of arguments it has.
     * <p>
     * - The argument is defined by its index e.g. the 3rd parameter should be set so its index is 3.<p>
     * - The location is '1' based. Meaning that the first attribute of a 1 argument constructor is 1,1.<p>
     * - If only a default constructor exists and a special set Object should be set then use (0,1) 
     * 
     * @param numberOfArguments the number of the arguments of a constructor
     * @param parameterIndex the index of the parameter of that constructor
     */
    public Location(int numberOfArguments, int parameterIndex){
      this.numberOfArguments=numberOfArguments;
      this.parameterIndex=parameterIndex;
    }

    public final int getNumberOfArguments() {
      return this.numberOfArguments;
    }
   
    public final int getParameterIndex() {
      return this.parameterIndex;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.numberOfArguments;
      result = prime * result + this.parameterIndex;
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
      Location other = (Location) obj;
      if (this.numberOfArguments != other.numberOfArguments)
        return false;
      return (this.parameterIndex == other.parameterIndex);
    }

    @Override
    public String toString() {
      return "Location [numberOfArguments=" + this.numberOfArguments + ", parameterIndex=" + this.parameterIndex + "]";
    }
  } 
}