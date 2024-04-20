package pkgMisc;

import java.util.Comparator;

import pkgData.Tool;

public class ToolComparator implements Comparator<Tool> {

	@Override
	public int compare(Tool arg0, Tool arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}

}
