package net.lonzak.common.unittest.examples.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Triangle which contains Triangle elements.
 */
public class Triangle extends Shape {

	private String material;
	private ArrayList<TriangleElement> radiobuttonElements = new ArrayList<TriangleElement>();

	/**
	 * Creates the button description.
	 * 
	 * @param id
	 * @param name
	 * @param mandatory
	 * @param material
	 * @param triangleElements
	 */
	public Triangle(
			String id,
			String name,
			boolean mandatory, 
			String material,
			List<TriangleElement> triangleElements) 
	{
		super(TypeEnum.TRIANGLE, id, 0, 0, 0, 0, name, mandatory);
		
		this.material = material;
		for (TriangleElement radiobuttonElement : this.radiobuttonElements) {
			this.radiobuttonElements.add(new TriangleElement(this, radiobuttonElement));
		}
	}
	
	public List<TriangleElement> getElements() {
		return this.radiobuttonElements;
	}
	
	public String getMaterial() {
		return this.material;
	}
}
