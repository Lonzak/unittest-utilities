package net.lonzak.examples.classes;

public class DtoWithNoToString {

  private String valueA;
  private String valueB;
  private int[] array;

  public DtoWithNoToString(String valueA, String valueB, int[] array) {
    super();
    this.valueA = valueA;
    this.valueB = valueB;
    this.array = array;
  }

  /**
   * @return the valueA
   */
  public String getValueA() {
    return this.valueA;
  }

  /**
   * @param valueA the valueA to set
   */
  public void setValueA(String valueA) {
    this.valueA = valueA;
  }

  /**
   * @return the valueB
   */
  public String getValueB() {
    return this.valueB;
  }

  /**
   * @param valueB the valueB to set
   */
  public void setValueB(String valueB) {
    this.valueB = valueB;
  }

  /**
   * @return the array
   */
  public int[] getArray() {
    return this.array;
  }

  /**
   * @param array the array to set
   */
  public void setArray(int[] array) {
    this.array = array;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("DtoWithNoToString [");
    if (this.valueA != null) {
      builder.append("valueA=");
      builder.append(this.valueA);
      builder.append(", ");
    }
    if (this.valueB != null) {
      builder.append("valueB=");
      builder.append(this.valueB);
      builder.append(", ");
    }
    if (this.array != null) {
      builder.append("array=");
      builder.append(this.array);
    }
    builder.append("]");
    return builder.toString();
  }

}
