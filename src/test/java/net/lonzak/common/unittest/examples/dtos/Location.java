package net.lonzak.common.unittest.examples.dtos;

/**
 * The location of a shape 
 *
 */
public class Location {
	
	private Coordinates left;
	private Coordinates right;
	private int page;
	
	public Location(Coordinates left, Coordinates right, int page){
		this.left=left;
		this.right=right;
		this.page=page;
	}
	
	public Coordinates getRight() {
		return this.right;
	}

	public Coordinates getLeft() {
		return this.left;
	}
	
	public int getPage() {
		return this.page;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.left == null) ? 0 : this.left.hashCode());
		result = prime * result + this.page;
		result = prime * result + ((this.right == null) ? 0 : this.right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Location other = (Location) obj;
		if (this.left == null) {
			if (other.left != null) return false;
		}
		else if (!this.left.equals(other.left)) return false;
		if (this.page != other.page) return false;
		if (this.right == null) {
			if (other.right != null) return false;
		}
		else if (!this.right.equals(other.right)) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return " left "+this.left+" right "+this.right+" p."+this.page;
	}
}