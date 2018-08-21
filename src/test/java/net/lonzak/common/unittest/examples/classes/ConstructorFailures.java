package net.lonzak.common.unittest.examples.classes;


public class ConstructorFailures {

  private ConstructorFailures() {}

  public ConstructorFailures(int i) {
    if (i != 1) {
      throw new IllegalArgumentException("Wrong int constructor called: " + i);
    }
  }
}
