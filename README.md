unittest-utilities
===============================
# Automated junit tests for DTO, Entity, JavaBeans style classes to increase test coverage

The following things are tested:
--------------------------------

- instantiation of all constructors
- getter and setter methods (a call to set should return the value in the get method)
- equals() and hashCode() methods (a change of an attribute must result in different hashCodes())
- toString() method

Note, that the values used for testing are randomly generated. 
There are different possibilities to adapt the default behavior, so it is possible to pass specific values along or to skip certain attributes.
For abstract classes example implementations can be passed along

Usage:
------

Default usage
```java
AutoTester.testDTOClass(ExampleDTO.class);
```

Tests with a special value for the first parameter of the second constructor
```java
AutoTester.testDTOClass(RedNumber.class, null, null, new SpecialValueLocator(new Location(2,1), "9910000001111"));
```
Test with several special values for a) second parameter in the first constructor and b) the first parameter of the second constructor
```java
HashMap<Location, Object> customValues = new HashMap<SpecialValueLocator.Location, Object>();
customValues.put(new Location(1,2), ClassOfColor.H5);
customValues.put(new Location(2,1), "1110011111111");

AutoTester.testDTOClass(RedNumber.class, null,null, new SpecialValueLocator(customValues));
```

Exclusion of an attribute of a class (getter/setter check exclusion)
```java
ArrayList<String> exclusions = new ArrayList<String>();
exclusions.add("floatArray");
AutoTester.testDTOClass(ExampleDTO.class,null,exclusions,null);
```

Contributions
-------------

If you would like to contribute you are welcome to do so - just create an issue (with an attached patch) or do a pull request and it will be integrated. "You have to fix my bug" or "you have to implement this feature I need" or similar request will not be answered.