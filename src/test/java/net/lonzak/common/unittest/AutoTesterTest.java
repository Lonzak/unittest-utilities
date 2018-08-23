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
package net.lonzak.common.unittest;

import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import net.lonzak.common.unittest.AutoTester;
import net.lonzak.common.unittest.PotentialErrorDetected;
import net.lonzak.common.unittest.SpecialValueLocator;
import net.lonzak.common.unittest.SpecialValueLocator.ConstructorValue;
import net.lonzak.common.unittest.examples.classes.ArrayObject;
import net.lonzak.common.unittest.examples.classes.Constructor;
import net.lonzak.common.unittest.examples.classes.Constructor2;
import net.lonzak.common.unittest.examples.classes.Constructor3;
import net.lonzak.common.unittest.examples.classes.ConstructorFailures;
import net.lonzak.common.unittest.examples.classes.DtoWithNoToString;
import net.lonzak.common.unittest.examples.classes.PrivateConstructor;
import net.lonzak.common.unittest.examples.dtos.BlackNumber;
import net.lonzak.common.unittest.examples.dtos.ExampleDTO;
import net.lonzak.common.unittest.examples.dtos.RedNumber;
import net.lonzak.common.unittest.examples.dtos.Triangle;
import net.lonzak.common.unittest.examples.enums.ClassOfColor;
import net.lonzak.common.unittest.examples.enums.LineOfColor;
import net.lonzak.common.unittest.examples.exceptions.DomainException;
import net.lonzak.common.unittest.examples.exceptions.ServiceNotAvailableException;
import net.lonzak.common.unittest.examples.exceptions.TechnicalException;

public class AutoTesterTest {

  @Test
  public void testDTOs() {
    // Default Tests
    AutoTester.testClass(BlackNumber.class);
    AutoTester.testClass(ExampleDTO.class);
    AutoTester.testClass(Triangle.class);
  }

  @Test
  public void testWithPredefinedValues() {
    // Tests with a special value
    AutoTester.testClass(RedNumber.class, null, null,
        new SpecialValueLocator(new ConstructorValue(1, 1, "9910000001111")));

    // Test with several special values
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    customValues.add(new ConstructorValue(3, 2, ClassOfColor.H5));
    customValues.add(new ConstructorValue(1, 1, "1110011111111"));
    AutoTester.testClass(RedNumber.class, null, null, new SpecialValueLocator(customValues));
  }

  @Test
  public void testExceptions() {
    // Tests with Exception classes
    AutoTester.testClass(DomainException.class);
    AutoTester.testClass(ServiceNotAvailableException.class);
    AutoTester.testClass(TechnicalException.class);
  }

  @Test
  public void testExclusion() {
    ArrayList<String> exclusions = new ArrayList<>();
    exclusions.add("floatArray");

    AutoTester.testClass(ExampleDTO.class, null, exclusions, null);
  }

  @Test
  public void testEnum() {
    AutoTester.testClass(LineOfColor.class);
    AutoTester.testClass(ClassOfColor.class);
  }

  @Test
  public void testPrivate() {
    HashMap<Object, Object> map = AutoTester.testPrivateConstructor(PrivateConstructor.class);
    AutoTester.testPrivateMethod(map.keySet().toArray()[0], "setA", "TEST");
  }

//  @Test
//  public void testTimes() {
//    AutoTester.testClass(LocalTime.class);
//    AutoTester.testClass(LocalDate.class);
//    AutoTester.testClass(LocalDateTime.class);
//    AutoTester.testClass(ZoneOffset.class);
//    AutoTester.testClass(DateTimeFormatter.class);
//    AutoTester.testClass(Instant.class);
//    AutoTester.testClass(ZonedDateTime.class);
//  }

  @Test
  public void testArrays() {
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    customValues.add(new ConstructorValue(2, 1, new int[] {1}));
    customValues.add(new ConstructorValue(2, 2, new long[] {2L}));
    AutoTester.testClass(ArrayObject.class, null, null, new SpecialValueLocator(customValues));
  }

  @Test
  public void testConstructors() {

    // primitive and java.lang types
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    customValues.add(new ConstructorValue(1, 1, 1));
    customValues.add(new ConstructorValue(1, 1, new Integer(-1)));
    customValues.add(new ConstructorValue(1, 1, 10000000000L));
    customValues.add(new ConstructorValue(1, 1, new Long(-10000000000L)));
    customValues.add(new ConstructorValue(1, 1, -2.0f));
    customValues.add(new ConstructorValue(1, 1, new Float(-3.0f)));
    customValues.add(new ConstructorValue(1, 1, -10.0d));
    customValues.add(new ConstructorValue(1, 1, new Double(-11.0d)));
    customValues.add(new ConstructorValue(1, 1, (short) 1));
    customValues.add(new ConstructorValue(1, 1, new Short((short) -1)));
    customValues.add(new ConstructorValue(1, 1, 'c'));
    customValues.add(new ConstructorValue(1, 1, new Character('C')));
    customValues.add(new ConstructorValue(1, 1, (byte) -1));
    customValues.add(new ConstructorValue(1, 1, new Byte((byte) 1)));
    customValues.add(new ConstructorValue(1, 1, true));
    customValues.add(new ConstructorValue(1, 1, Boolean.TRUE));

    customValues.add(new ConstructorValue(1, 1, new int[] {1}));
    customValues.add(new ConstructorValue(1, 1, new Integer[] {-1}));
    customValues.add(new ConstructorValue(1, 1, new long[] {10000000000L}));
    customValues.add(new ConstructorValue(1, 1, new Long[] {-10000000000L}));
    customValues.add(new ConstructorValue(1, 1, new float[] {-2.0f}));
    customValues.add(new ConstructorValue(1, 1, new Float[] {-3.0f}));
    customValues.add(new ConstructorValue(1, 1, new double[] {-10.0d}));
    customValues.add(new ConstructorValue(1, 1, new Double[] {-11.0d}));
    customValues.add(new ConstructorValue(1, 1, new short[] {(short) 1}));
    customValues.add(new ConstructorValue(1, 1, new Short[] {(short) -1}));
    customValues.add(new ConstructorValue(1, 1, new char[] {'c'}));
    customValues.add(new ConstructorValue(1, 1, new Character[] {'C'}));
    customValues.add(new ConstructorValue(1, 1, new byte[] {(byte) -1}));
    customValues.add(new ConstructorValue(1, 1, new Byte[] {(byte) 1}));
    customValues.add(new ConstructorValue(1, 1, new boolean[] {true}));
    customValues.add(new ConstructorValue(1, 1, new Boolean[] {Boolean.TRUE}));

    customValues.add(new ConstructorValue(1, 1, LineOfColor.GRAY));
    customValues.add(new ConstructorValue(1, 1, BigDecimal.valueOf(10)));

    AutoTester.testClass(Constructor.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test(expected = PotentialErrorDetected.class)
  public void testConstructorsFailures() {

    // primitive and java.lang types
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    // default constructor -> nothing happens
    customValues.add(new ConstructorValue(0, 1, 1));
    // invalid value Integer instead of int
    customValues.add(new ConstructorValue(1, 1, new Integer(1)));

    AutoTester.testClass(ConstructorFailures.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorsFailures2() {

    // primitive and java.lang types
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    // default constructor
    customValues.add(new ConstructorValue(0, 1, 1));
    // invalid parameter index
    customValues.add(new ConstructorValue(1, 0, new Integer(-1)));
    // inexistent constructor
    customValues.add(new ConstructorValue(2, 1, 10000000000L));

    AutoTester.testClass(ConstructorFailures.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorsFailures3() {

    // primitive and java.lang types
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    // default constructor -> use for setters
    customValues.add(new ConstructorValue(0, 1, 1));
    // invalid parameter index
    customValues.add(new ConstructorValue(1, 0, new Integer(-1)));

    AutoTester.testClass(ConstructorFailures.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test(expected = PotentialErrorDetected.class)
  public void testConstructorsFailures4() {

    // primitive and java.lang types
    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    // inexistent constructor
    customValues.add(new ConstructorValue(2, 1, 10000000000L));

    AutoTester.testClass(ConstructorFailures.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test
  public void testConstructorsMultiple() {

    ArrayList<Character> chars = new ArrayList<>();
    chars.add(new Character('*'));

    ArrayList<ConstructorValue> customValues = new ArrayList<>();
    customValues.add(new ConstructorValue(7, 1, 1));
    customValues.add(new ConstructorValue(7, 2, new Double(2.0d)));
    customValues.add(new ConstructorValue(7, 3, "byteArrayXY".getBytes()));
    customValues.add(new ConstructorValue(7, 4, "StringXY"));
    customValues.add(new ConstructorValue(7, 5, chars));
    customValues.add(new ConstructorValue(7, 6, LineOfColor.SEPIA));
    customValues.add(new ConstructorValue(7, 7, new RedNumber<>(1, 12, 1)));

    AutoTester.testClass(Constructor2.class, null, null, new SpecialValueLocator(customValues));

  }

  @Test
  public void testSpecial() {
    AutoTester.testClass(Constructor3.class, null, null,
        new SpecialValueLocator(new ConstructorValue(0, 1, "setter?")));
  }

  @Test(expected = PotentialErrorDetected.class)
  public void testIllegalToString() {
    AutoTester.testClass(DtoWithNoToString.class);
  }
  
  @Test
  public void testRandomMethods(){
    BigDecimal number = AutoTester.getRandomBigDecimal();
	Assert.assertTrue(number.intValue()>=0 && number.intValue()<Integer.MAX_VALUE);
		
	Boolean bool = AutoTester.getRandomBoolean();
	Assert.assertTrue(bool.booleanValue()==true || bool.booleanValue()==false);
		
	Boolean[] bools = AutoTester.getRandomBooleanArray();
	Assert.assertTrue(bools.length>=0 && bools.length<42);
		
	boolean[] primBools = AutoTester.getRandomBooleanArrayPrimitive();
	Assert.assertTrue(primBools.length>=0 && primBools.length<42);
	
	Byte buyte = AutoTester.getRandomByte();
	Assert.assertTrue(buyte.intValue()>-129 && buyte.intValue()<128);
	
	Byte[] bytes = AutoTester.getRandomByteArray();
	Assert.assertTrue(bytes.length>=0 && bytes.length<42);
	
	byte[] primBytes = AutoTester.getRandomByteArrayPrimitive();
	Assert.assertTrue(primBytes.length>=0 && primBytes.length<42);
	
	Character care = AutoTester.getRandomCharacter();
	Assert.assertTrue(care.charValue()>-Character.MIN_VALUE && care.charValue()<=Character.MAX_VALUE);
	
	Character[] cares = AutoTester.getRandomCharacterArray();
	Assert.assertTrue(cares.length>=0 && cares.length<42);
		char[] primCares = AutoTester.getRandomCharArrayPrimitive();
	Assert.assertTrue(primCares.length>=0 && primCares.length<42);
	
	Double doubel = AutoTester.getRandomDouble();
	Assert.assertTrue(doubel.doubleValue()>=Double.MIN_VALUE && doubel.doubleValue()<=Double.MAX_VALUE);
	
	Double[] doubels = AutoTester.getRandomDoubleArray();
	Assert.assertTrue(doubels.length>=0 && doubels.length<42);
	
	double[] primDoubels = AutoTester.getRandomDoubleArrayPrimitive();
	Assert.assertTrue(primDoubels.length>=0 && primDoubels.length<42);
	
	Float flohd = AutoTester.getRandomFloat();
	Assert.assertTrue(flohd.floatValue()>=Float.MIN_VALUE && flohd.floatValue()<=Float.MAX_VALUE);
	
	Float[] flohds = AutoTester.getRandomFloatArray();
	Assert.assertTrue(flohds.length>=0 && flohds.length<42);
	
	float[] primFlohds = AutoTester.getRandomFloatArrayPrimitive();
	Assert.assertTrue(primFlohds.length>=0 && primFlohds.length<42);
	
	Integer ind = AutoTester.getRandomInteger();
	Assert.assertTrue(ind.intValue()>=Integer.MIN_VALUE && ind.intValue()<=Integer.MAX_VALUE);
	
	Integer[] inds = AutoTester.getRandomIntegerArray();
	Assert.assertTrue(inds.length>=0 && inds.length<42);
	
	int[] primInds = AutoTester.getRandomIntArrayPrimitive();
	Assert.assertTrue(primInds.length>=0 && primInds.length<42);
	
	int range = Math.abs(new Random(System.nanoTime()).nextInt());
	int intInRange = AutoTester.getRandomInt(range);
	Assert.assertTrue(intInRange>=0 && intInRange<=intInRange);
	
	Long lon = AutoTester.getRandomLong();
	Assert.assertTrue(lon.longValue()>=Long.MIN_VALUE && lon.longValue()<=Long.MAX_VALUE);
	
	Long[] lons = AutoTester.getRandomLongArray();
	Assert.assertTrue(lons.length>=0 && lons.length<42);
	
	long[] primLons = AutoTester.getRandomLongArrayPrimitive();
	Assert.assertTrue(primLons.length>=0 && primLons.length<42);
	
	Short shoat = AutoTester.getRandomShort();
	Assert.assertTrue(shoat.shortValue()>=Short.MIN_VALUE && shoat.shortValue()<=Short.MAX_VALUE);
	
	Short[] shoats = AutoTester.getRandomShortArray();
	Assert.assertTrue(shoats.length>=0 && shoats.length<42);
	
	short[] primShoats = AutoTester.getRandomShortArrayPrimitive();
	Assert.assertTrue(primShoats.length>=0 && primShoats.length<42);
	
	String string = AutoTester.getRandomString();
	Assert.assertTrue(string.length()>0);
	
	int unsinedShortByte = Byte.parseByte(AutoTester.getRandomUnsignedIntAsString(Byte.MAX_VALUE));
	Assert.assertTrue(unsinedShortByte>=0 && unsinedShortByte<=Byte.MAX_VALUE);
	
	int unsinedShort = Short.parseShort(AutoTester.getRandomUnsignedIntAsString(Short.MAX_VALUE));
	Assert.assertTrue(unsinedShort>=0 && unsinedShort<=Short.MAX_VALUE);
	
	int unsinedInt = Integer.parseInt(AutoTester.getRandomUnsignedIntAsString(Integer.MAX_VALUE));
	Assert.assertTrue(unsinedInt>=0 && unsinedInt<=Integer.MAX_VALUE);
  }
}
