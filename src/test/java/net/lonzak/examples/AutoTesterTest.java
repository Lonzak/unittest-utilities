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
package net.lonzak.examples;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import net.lonzak.common.unittest.AutoTester;
import net.lonzak.common.unittest.PotentialErrorDetected;
import net.lonzak.common.unittest.SpecialValueLocator;
import net.lonzak.common.unittest.SpecialValueLocator.ConstructorValue;
import net.lonzak.examples.classes.ArrayObject;
import net.lonzak.examples.classes.Constructor;
import net.lonzak.examples.classes.Constructor2;
import net.lonzak.examples.classes.Constructor3;
import net.lonzak.examples.classes.ConstructorFailures;
import net.lonzak.examples.classes.PrivateConstructor;
import net.lonzak.examples.dtos.ExampleDTO;
import net.lonzak.examples.dtos.RedNumber;
import net.lonzak.examples.enums.ClassOfColor;
import net.lonzak.examples.enums.LineOfColor;
import net.lonzak.examples.exceptions.DomainException;
import net.lonzak.examples.exceptions.ServiceNotAvailableException;
import net.lonzak.examples.exceptions.TechnicalException;

public class AutoTesterTest {

    @Test
    public void testDTOs() {
      //Default Tests
      //AutoTester.testClass(BlackNumber.class);
      AutoTester.testClass(ExampleDTO.class);
    }
    
    @Test
    public void testWithPredefinedValues(){
      //Tests with a special value
      AutoTester.testClass(RedNumber.class, null, null, new SpecialValueLocator(new ConstructorValue(1,1,"9910000001111")));
      
      //Test with several special values
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      customValues.add(new ConstructorValue(3,2, ClassOfColor.H5));
      customValues.add(new ConstructorValue(1,1,"1110011111111"));
      AutoTester.testClass(RedNumber.class, null,null, new SpecialValueLocator(customValues));
    }
    
    @Test
    public void testExceptions(){
      //Tests with Exception classes
      AutoTester.testClass(DomainException.class);
      AutoTester.testClass(ServiceNotAvailableException.class);
      AutoTester.testClass(TechnicalException.class);
    }
    
    @Test
    public void testExclusion(){
      ArrayList<String> exclusions = new ArrayList<>();
      exclusions.add("floatArray");
      
      AutoTester.testClass(ExampleDTO.class,null,exclusions,null);
    }
    
    @Test
    public void testEnum(){
      AutoTester.testClass(LineOfColor.class);
      AutoTester.testClass(ClassOfColor.class);
    }

    @Test
    public void testPrivate(){
      HashMap<Object, Object> map = AutoTester.testPrivateConstructor(PrivateConstructor.class);
      AutoTester.testPrivateMethod(map.keySet().toArray()[0], "setA", "TEST");
    }

    @Test
    public void testTimes() {
      AutoTester.testClass(LocalTime.class);
      AutoTester.testClass(LocalDate.class);
      AutoTester.testClass(LocalDateTime.class);
      AutoTester.testClass(ZoneOffset.class);
      AutoTester.testClass(DateTimeFormatter.class);
      AutoTester.testClass(Instant.class);
      AutoTester.testClass(ZonedDateTime.class);
    }
    
    @Test
    public void testArrays() {
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      customValues.add(new ConstructorValue(2,1, new int [] {1}));
      customValues.add(new ConstructorValue(2,2, new long [] {2L}));
      AutoTester.testClass(ArrayObject.class, null,null, new SpecialValueLocator(customValues));
    }
    
    @Test
    public void testConstructors() {
      
      //primitive and java.lang types
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      customValues.add(new ConstructorValue(1,1, 1));
      customValues.add(new ConstructorValue(1,1, new Integer(-1)));
      customValues.add(new ConstructorValue(1,1, 10000000000L));
      customValues.add(new ConstructorValue(1,1, new Long(-10000000000L)));
      customValues.add(new ConstructorValue(1,1, -2.0f));
      customValues.add(new ConstructorValue(1,1, new Float(-3.0f)));
      customValues.add(new ConstructorValue(1,1, -10.0d));
      customValues.add(new ConstructorValue(1,1, new Double(-11.0d)));        
      customValues.add(new ConstructorValue(1,1, (short)1));
      customValues.add(new ConstructorValue(1,1, new Short((short)-1)));
      customValues.add(new ConstructorValue(1,1, 'c'));
      customValues.add(new ConstructorValue(1,1, new Character('C')));
      customValues.add(new ConstructorValue(1,1, (byte)-1));
      customValues.add(new ConstructorValue(1,1, new Byte((byte)1)));
      customValues.add(new ConstructorValue(1,1, true));
      customValues.add(new ConstructorValue(1,1, Boolean.TRUE));
      
      customValues.add(new ConstructorValue(1,1, new int[] {1}));
      customValues.add(new ConstructorValue(1,1, new Integer[]{-1}));
      customValues.add(new ConstructorValue(1,1, new long [] {10000000000L}));
      customValues.add(new ConstructorValue(1,1, new Long[] {-10000000000L}));
      customValues.add(new ConstructorValue(1,1, new float[] {-2.0f}));
      customValues.add(new ConstructorValue(1,1, new Float[] {-3.0f}));
      customValues.add(new ConstructorValue(1,1, new double[] {-10.0d}));
      customValues.add(new ConstructorValue(1,1, new Double[] {-11.0d}));        
      customValues.add(new ConstructorValue(1,1, new short[] {(short)1}));
      customValues.add(new ConstructorValue(1,1, new Short[] {(short)-1}));
      customValues.add(new ConstructorValue(1,1, new char[] {'c'}));
      customValues.add(new ConstructorValue(1,1, new Character[] {'C'}));
      customValues.add(new ConstructorValue(1,1, new byte[] {(byte)-1}));
      customValues.add(new ConstructorValue(1,1, new Byte[] {(byte)1}));
      customValues.add(new ConstructorValue(1,1, new boolean[] {true}));
      customValues.add(new ConstructorValue(1,1, new Boolean[] {Boolean.TRUE}));
      
      customValues.add(new ConstructorValue(1,1, LineOfColor.GRAY));
      customValues.add(new ConstructorValue(1,1, BigDecimal.valueOf(10)));
      
      AutoTester.testClass(Constructor.class,null,null, new SpecialValueLocator(customValues));
      
    }
    
    @Test(expected = PotentialErrorDetected.class)
    public void testConstructorsFailures() {
      
      //primitive and java.lang types
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      //default constructor -> nothing happens
      customValues.add(new ConstructorValue(0,1, 1));
      //invalid value Integer instead of int
      customValues.add(new ConstructorValue(1,1, new Integer(1)));
      
      AutoTester.testClass(ConstructorFailures.class,null,null, new SpecialValueLocator(customValues));
      
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsFailures2() {
      
      //primitive and java.lang types
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      //default constructor
      customValues.add(new ConstructorValue(0,1, 1));
      //invalid parameter index
      customValues.add(new ConstructorValue(1,0, new Integer(-1)));
      //inexistent constructor
      customValues.add(new ConstructorValue(2,1, 10000000000L));
      
      AutoTester.testClass(ConstructorFailures.class,null,null, new SpecialValueLocator(customValues));
      
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsFailures3() {
      
      //primitive and java.lang types
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      //default constructor -> use for setters
      customValues.add(new ConstructorValue(0,1, 1));
      //invalid parameter index
      customValues.add(new ConstructorValue(1,0, new Integer(-1)));
      
      AutoTester.testClass(ConstructorFailures.class,null,null, new SpecialValueLocator(customValues));
      
    }
    
    @Test(expected = PotentialErrorDetected.class)
    public void testConstructorsFailures4() {
      
      //primitive and java.lang types
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      //inexistent constructor
      customValues.add(new ConstructorValue(2,1, 10000000000L));
      
      AutoTester.testClass(ConstructorFailures.class,null,null, new SpecialValueLocator(customValues));
      
    }
    
    @Test
    public void testConstructorsMultiple() {
      
      ArrayList<Character> chars = new ArrayList<>();
      chars.add(new Character('*'));
     
      ArrayList<ConstructorValue> customValues = new ArrayList<>();
      customValues.add(new ConstructorValue(7,1, 1));
      customValues.add(new ConstructorValue(7,2, new Double(2.0d)));
      customValues.add(new ConstructorValue(7,3, "byteArrayXY".getBytes()));
      customValues.add(new ConstructorValue(7,4, "StringXY"));
      customValues.add(new ConstructorValue(7,5, chars));
      customValues.add(new ConstructorValue(7,6, LineOfColor.SEPIA));
      customValues.add(new ConstructorValue(7,7, new RedNumber<Integer>(1,12,1)));
      
      AutoTester.testClass(Constructor2.class,null,null, new SpecialValueLocator(customValues));
    
    }
    
    @Test
    public void testSpecial() {
      AutoTester.testClass(Constructor3.class,null,null, new SpecialValueLocator(new ConstructorValue(0,1, "setter?")));
    }
}