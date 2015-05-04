package application;

public class TabIndex {

	private static TabIndex tabCount = new TabIndex();
	
	public static TabIndex getInstance() {
		return tabCount;
	}
	
	private int index = 0;
	
	public int nextIndex() {
		return index++;
	}
}
