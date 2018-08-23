package net.lonzak.common.unittest.examples.dtos;

/**
 *	Common shape data 
 */
public class Shape {
	
	private TypeEnum type;
	
	private String id;
	
	private int positionX;
	private int positionY;
	private int height;
	private int width;

	private String name;
	private boolean mandatory;
	
	public Shape(
			TypeEnum type, 
			String id, 
			int positionX,
			int positionY, 
			int width, 
			int height, 
			String name,
			boolean mandatory) 
	{
		super();
		this.type = type;
		this.id = id;
		this.positionX = positionX;
		this.positionY = positionY;
		this.height = height;
		this.width = width;
		this.name = name;
		this.mandatory = mandatory;
	}

	public TypeEnum getType() {
		return this.type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getPositionX() {
		return this.positionX;
	}
	
	public int getPositionY() {
		return this.positionY;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isMandatory() {
		return this.mandatory;
	}
}
