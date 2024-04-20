package pkgData;

// Der Typ der für ein TreeSet muss Compareable implementieren!!!
public class Tool implements Comparable<Tool> {
	private String name = null;
	private int price = 0;
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Tool [name=" + name + ", price=" + price + "]";
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("---equals");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tool other = (Tool) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
			
		return true;
	}

	public Tool(String name, int price) {
		super();
		this.name = name;
		this.price = price;
	}

	@Override
	public int compareTo(Tool otherTool) {
		int returnValue = this.getName().compareTo(otherTool.getName());
		if (returnValue == 0) {
			returnValue = otherTool.price - this.price ;			
		}
		
		return returnValue;
	}
}
