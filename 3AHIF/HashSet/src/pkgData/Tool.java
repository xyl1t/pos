package pkgData;

public class Tool {
	private String name = null;
	private int price = 0;
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Tool [name=" + name + ", price=" + price + "]";
	}

	public Tool(String name, int price) {
		super();
		this.name = name;
		this.price = price;
	}

	@Override
	public int hashCode() {
		System.out.println("------------ in hashcode");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + price;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("------------ in equals");
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
		if (price != other.price)
			return false;
		return true;
	}
	
	
}
