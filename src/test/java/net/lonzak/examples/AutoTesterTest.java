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

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import net.lonzak.common.unittest.AutoTester;
import net.lonzak.common.unittest.SpecialValueLocator;
import net.lonzak.common.unittest.SpecialValueLocator.Location;
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
      //AutoTester.testDTOClass(BlackNumber.class);
      AutoTester.testDTOClass(ExampleDTO.class);
    }
    
    @Test
    public void testWithPredefinedValues(){
      //Tests with a special value
      AutoTester.testDTOClass(RedNumber.class, null, null, new SpecialValueLocator(new Location(1,1), "9910000001111"));
      
      //Test with several special values
      HashMap<Location, Object> customValues = new HashMap<>();
      customValues.put(new Location(3,2), ClassOfColor.H5);
      customValues.put(new Location(1,1), "1110011111111");
      AutoTester.testDTOClass(RedNumber.class, null,null, new SpecialValueLocator(customValues));
    }
    
    @Test
    public void testExceptions(){
      //Tests with Exception classes
      AutoTester.testDTOClass(DomainException.class);
      AutoTester.testDTOClass(ServiceNotAvailableException.class);
      AutoTester.testDTOClass(TechnicalException.class);
    }
    
    @Test
    public void testExclusion(){
      ArrayList<String> exclusions = new ArrayList<>();
      exclusions.add("floatArray");
      
      AutoTester.testDTOClass(ExampleDTO.class,null,exclusions,null);
    }
    
    @Test
    public void testEnum(){
      AutoTester.testDTOClass(LineOfColor.class);
      AutoTester.testDTOClass(ClassOfColor.class);
    }
}