package net.lonzak.common.unittest.examples.dtos;

/**
 * Some coordinates
 * 
 */
public class Coordinates {
	private float x;
	private float y;

	/**
	 * Constructs a 2 dimensional point.
	 * @param x
	 * @param y
	 */
	public Coordinates(float x, float y){
		this.x=x;
		this.y=y;
	}
	
	/**
	 * Constructs a 2 dimensional point.
	 * @param x
	 * @param y
	 */
	public Coordinates(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return this.y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * The interface contract for Object requires that if two objects 
	 * are equal according to equals(), then they must have the 
	 * same hashCode() value. Otherwise the class violates the invariant 
	 * that equal objects must have equal hashcodes.
	 * 
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean equals(Object o){
		
		if (o == null) return false;
		if (this == o) return true;
		
		if(o.getClass()== this.getClass()){

			Coordinates coord = (Coordinates)o; 
			if(Math.abs(this.x - coord.getX()) < .0000001 &&
				Math.abs(this.y - coord.getY()) < .0000001){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { 
	    int hash = 1;
	    hash = hash * 7 + (int)this.x;
	    hash = hash * 7 + (int)this.y; 
	   return hash;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public String toString() {
		return "(x,y)=" + this.x + "," + this.y;
	}
}