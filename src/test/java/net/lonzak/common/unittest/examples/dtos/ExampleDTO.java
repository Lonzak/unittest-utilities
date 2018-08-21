/*
 * =========================================== unittest-utilities ===========================================
 *
 * Project Info: https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2017 nepatec GmbH & Co. KG
 *
 * This file is part of unittest-utilities
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details. You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package net.lonzak.common.unittest.examples.dtos;

import java.math.BigDecimal;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExampleDTO {

  private List<String> stringValues;
  private Set<Integer> integerValues;

  private Map<BigDecimal, BigDecimal> numberValues;

  private int[] intArray;
  private long[] longArray;
  private float[] floatArray;
  private double[] doubleArray;
  private String[] stringArray;
  private BigDecimal[] bigDecimalArray;
  private byte primitiveByte;
  private Certificate cert;

  public ExampleDTO(List<String> stringValues, Set<Integer> integerValues, Map<BigDecimal, BigDecimal> numberValues,
      int[] intArray, long[] longArray, float[] floatArray, double[] doubleArray, String[] stringArray,
      BigDecimal[] bigDecimalArray) {
    super();
    this.stringValues = stringValues;
    this.integerValues = integerValues;
    this.numberValues = numberValues;
    this.intArray = intArray;
    this.longArray = longArray;
    this.floatArray = floatArray;
    this.doubleArray = doubleArray;
    this.stringArray = stringArray;
    this.bigDecimalArray = bigDecimalArray;
  }

  public ExampleDTO(List<String> stringValues, Set<Integer> integerValues, Map<BigDecimal, BigDecimal> numberValues) {
    super();
    this.stringValues = stringValues;
    this.integerValues = integerValues;
    this.numberValues = numberValues;
  }

  public List<String> getStringValues() {
    return this.stringValues;
  }

  public void setStringValues(List<String> stringValues) {
    this.stringValues = stringValues;
  }

  public Set<Integer> getIntegerValues() {
    return this.integerValues;
  }

  public void setIntegerValues(Set<Integer> integerValues) {
    this.integerValues = integerValues;
  }

  public Map<BigDecimal, BigDecimal> getNumberValues() {
    return this.numberValues;
  }

  public void setNumberValues(Map<BigDecimal, BigDecimal> numberValues) {
    this.numberValues = numberValues;
  }

  public int[] getIntArray() {
    return this.intArray;
  }

  public void setIntArray(int[] intArray) {
    this.intArray = intArray;
  }

  public long[] getLongArray() {
    return this.longArray;
  }

  public void setLongArray(long[] longArray) {
    this.longArray = longArray;
  }

  public float[] getFloatArray() {
    return this.floatArray;
  }

  public void setFloatArray(float[] floatArray) {
    this.floatArray = floatArray;
  }

  public double[] getDoubleArray() {
    return this.doubleArray;
  }

  public void setDoubleArray(double[] doubleArray) {
    this.doubleArray = doubleArray;
  }

  public String[] getStringArray() {
    return this.stringArray;
  }

  public void setStringArray(String[] stringArray) {
    this.stringArray = stringArray;
  }

  public BigDecimal[] getBigDecimalArray() {
    return this.bigDecimalArray;
  }

  public void setBigDecimalArray(BigDecimal[] bigDecimalArray) {
    this.bigDecimalArray = bigDecimalArray;
  }

  public byte getPrimitiveByte() {
    return this.primitiveByte;
  }

  public void setPrimitiveByte(byte primitiveByte) {
    this.primitiveByte = primitiveByte;
  }

  public Certificate getCert() {
    return this.cert;
  }

  public void setCert(Certificate cert) {
    this.cert = cert;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(this.bigDecimalArray);
    result = prime * result + Arrays.hashCode(this.doubleArray);
    result = prime * result + Arrays.hashCode(this.floatArray);
    result = prime * result + Arrays.hashCode(this.intArray);
    result = prime * result + ((this.integerValues == null) ? 0 : this.integerValues.hashCode());
    result = prime * result + Arrays.hashCode(this.longArray);
    result = prime * result + ((this.numberValues == null) ? 0 : this.numberValues.hashCode());
    result = prime * result + Arrays.hashCode(this.stringArray);
    result = prime * result + ((this.stringValues == null) ? 0 : this.stringValues.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    ExampleDTO other = (ExampleDTO) obj;
    if (!Arrays.equals(this.bigDecimalArray, other.bigDecimalArray)) {
      return false;
    }
    if (!Arrays.equals(this.doubleArray, other.doubleArray)) {
      return false;
    }
    if (!Arrays.equals(this.floatArray, other.floatArray)) {
      return false;
    }
    if (!Arrays.equals(this.intArray, other.intArray)) {
      return false;
    }
    if (this.integerValues == null) {
      if (other.integerValues != null) {
        return false;
      }
    } else if (!this.integerValues.equals(other.integerValues)) {
      return false;
    }
    if (!Arrays.equals(this.longArray, other.longArray)) {
      return false;
    }
    if (this.numberValues == null) {
      if (other.numberValues != null) {
        return false;
      }
    } else if (!this.numberValues.equals(other.numberValues)) {
      return false;
    }
    if (!Arrays.equals(this.stringArray, other.stringArray)) {
      return false;
    }
    if (this.stringValues == null) {
      if (other.stringValues != null) {
        return false;
      }
    } else if (!this.stringValues.equals(other.stringValues)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ExampleDTO [stringValues=" + this.stringValues + ", integerValues=" + this.integerValues + ", numberValues="
        + this.numberValues + ", intArray=" + Arrays.toString(this.intArray) + ", longArray="
        + Arrays.toString(this.longArray) + ", floatArray=" + Arrays.toString(this.floatArray) + ", doubleArray="
        + Arrays.toString(this.doubleArray) + ", stringArray=" + Arrays.toString(this.stringArray)
        + ", bigDecimalArray=" + Arrays.toString(this.bigDecimalArray) + "]";
  }
}
