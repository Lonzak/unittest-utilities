package net.lonzak.common.unittest.examples.dtos;

/**
 * One button element of an radio button group
 */
public class TriangleElement {

	private Shape dummyShape;
	private Triangle triangleRef = null;
	
	public TriangleElement(
			int positionX,
			int positionY, 
			int width,
			int height,
			String valueChecked) 
	{
		this.dummyShape = new Shape(null, "", positionX, positionY, width, height, "", false);
	}
	
	public TriangleElement(
			Triangle triangleRef, 
			TriangleElement element) 
	{
		this.triangleRef = triangleRef;
		this.dummyShape = new Shape(null, "", element.getPositionX(), element.getPositionY(), element.getWidth(), element.getHeight(), "", false);
	}
	
	public boolean isSomething() {
		return (this.dummyShape.getName().equals(this.triangleRef.getMaterial()));
	}
	
	public int getPositionX() {
		return this.dummyShape.getPositionX();
	}
	
	public int getPositionY() {
		return this.dummyShape.getPositionY();
	}
	
	public int getHeight() {
		return this.dummyShape.getHeight();
	}

	public int getWidth() {
		return this.dummyShape.getWidth();
	}
}
