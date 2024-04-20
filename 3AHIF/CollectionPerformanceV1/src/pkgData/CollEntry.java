package pkgData;

public class CollEntry {
    private String word;
    private int counter = 1;

    public CollEntry(String word) {
        this.word = word;
    }

	public void increaseCounter() {
        counter++;
    }

    @Override
	public String toString() {
		return "...[word=" + word + ", counter=" + counter + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollEntry other = (CollEntry) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

}
