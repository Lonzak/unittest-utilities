package net.lonzak.examples.classes;

public class PrivateConstructor {
  
  private String a;
  private int b;
  private float c;
  
  private PrivateConstructor() {
  }
  
  private PrivateConstructor(String a, int b, float c) {
    this.a=a;
    this.b=b;
    this.c=c;
  }

  private String getA() {
    return this.a;
  }

  private void setA(String a) {
    this.a = a;
  }

  private int getB() {
    return this.b;
  }

  private void setB(int b) {
    this.b = b;
  }

  private float getC() {
    return this.c;
  }

  private void setC(float c) {
    this.c = c;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Constructor [");
    if (this.a != null) {
      builder.append("a=");
      builder.append(this.a);
      builder.append(", ");
    }
    builder.append("b=");
    builder.append(this.b);
    builder.append(", c=");
    builder.append(this.c);
    builder.append("]");
    return builder.toString();
  }
}
