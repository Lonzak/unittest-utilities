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
  private int currentConstructorIndex=-1;
 
  /**
   * Initializes the list of special values with one value.
   * To add further values use the {@link #addSpecialValue(Location, Object)} or {@link #setSpecialValues(Map)} methods. 
   * 
   * @param location of the special value 
   * @param specialValue the special value which should be used for the Constructor
   */
  public SpecialValueLocator(Location location, Object specialValue) {
    this.specialValues= new HashMap<SpecialValueLocator.Location, Object>();
    this.specialValues.put(location, specialValue);
  }
  
  /**
   * Constructs a special value list to be able to set individual constructor arguments.
   * @param specialValues a list of special values
   */
  public SpecialValueLocator(Map<Location, Object> specialValues) {
    this.specialValues = specialValues;
  }
  
  public static final SpecialValueLocator NONE = new SpecialValueLocator(new HashMap<Location, Object>());
  
  public void addSpecialValue(Location location, Object specialValue){
    this.specialValues.put(location, specialValue);
  }
  
  public void setSpecialValues(Map<Location, Object> specialValues){
    this.specialValues.putAll(specialValues);
  }

  public int getCurrentConstructorIndex() {
    return this.currentConstructorIndex;
  }

  public void setCurrentConstructorIndex(int currentConstructorIndex) {
    this.currentConstructorIndex = currentConstructorIndex;
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
   * @param constructorIndex of the special value to retrieve
   * @param parameterIndex of the special value to retrieve
   * @return the special object or null
   */
  public Object getSpecialValue(int constructorIndex, int parameterIndex){
    return this.specialValues.get(new Location(constructorIndex,parameterIndex));
  }
  
  /**
   * Retrieves the special value by using the parameterIndex.
   * The constructor index is used from the {@link #currentConstructorIndex} 
   * 
   * @param parameterIndex of the special value to retrieve
   * @return the special object or null
   */
  public Object getSpecialValue(int parameterIndex){
    if(this.currentConstructorIndex!=-1){
      return this.specialValues.get(new Location(this.currentConstructorIndex,parameterIndex));
    }
    else{
      throw new IllegalStateException("The current constructor index has not been set!");
    }
  }
  
  @Override
  public String toString() {
    return "SpecialValueLocator [specialValues=" + this.specialValues + ", currentConstructorIndex="+this.currentConstructorIndex + "]";
  }

  /**
   * The location element of a special value.
   * A location is defined by the constructor and the parameter of the constructor.
   * The index of a constructor is the same as the definition order in the class file.
   * The first constructor starts at 1.
   * 
   * @author 225010
   *
   */
  public static final class Location{
    
    private int constructorIndex=-1;
    private int parameterIndex=-1;
    
    /**
     * The location for the parameter is '1' based. Meaning that the first attribute of the first constructor is located at 1,1.
     * @param constructorIndex the number of the constructor in the file
     * @param parameterIndex the number of the parameter of the constructor
     */
    public Location(int constructorIndex, int parameterIndex){
      this.constructorIndex=constructorIndex;
      this.parameterIndex=parameterIndex;
    }

    public final int getConstructorIndex() {
      return this.constructorIndex;
    }
   
    public final int getParameterIndex() {
      return this.parameterIndex;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.constructorIndex;
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
      if (this.constructorIndex != other.constructorIndex)
        return false;
      return (this.parameterIndex == other.parameterIndex);
    }

    @Override
    public String toString() {
      return "Location [constructorIndex=" + this.constructorIndex + ", parameterIndex=" + this.parameterIndex + "]";
    }
  } 
}