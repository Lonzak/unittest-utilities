package net.lonzak.examples.classes;

public class Constructor3 {
  private String name;
  
  public Constructor3() {
    this.name="default";
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name=name;
    if(!"setter?".equals(name)) throw new IllegalArgumentException("Setter with special value has not been called!");
  } 
}