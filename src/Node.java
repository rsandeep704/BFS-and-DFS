import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Node<T> implements Comparable<Node<T>> {

	public T key;
	public Map<Node<T>, Integer> children = new TreeMap<Node<T>, Integer>();
	private Map<Node<T>, List<TimeInterval>> timesNotWorking = new HashMap<Node<T>, List<TimeInterval>>();
	private int distanceFromRoot = 0;
	private Node<T> parent;
	private boolean visited = false;
	private int notWorkingVisits = 0;

	public int getNotWorkingVisits() {
		return notWorkingVisits;
	}

	public void setNotWorkingVisits(int notWorkingVisits) {
		this.notWorkingVisits = notWorkingVisits;
	}

	public Node() {
		super();
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Node(T key) {
		super();
		this.key = key;
	}

	public Node(Node<T> node) {
		this.key = node.getKey();
		this.distanceFromRoot = node.getDistanceFromRoot();
		this.parent = node.getParent();
		this.timesNotWorking = node.getTimesNotWorking();
		this.visited = node.visited;
		this.notWorkingVisits = node.getNotWorkingVisits();
		this.children = node.children;
	}

	public int getDistanceFromRoot() {
		return distanceFromRoot;
	}

	public void setDistanceFromRoot(int distanceFromRoot) {
		this.distanceFromRoot = distanceFromRoot;
	}

	public void addChild(Node<T> node, int cost) {
		children.put(node, cost);
	}

	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public Map<Node<T>, Integer> getChildren() {
		return children;
	}

	public Map<Node<T>, List<TimeInterval>> getTimesNotWorking() {
		return timesNotWorking;
	}

	public void setTimesNotWorking(Map<Node<T>, List<TimeInterval>> timesNotWorking) {
		this.timesNotWorking = timesNotWorking;
	}

	public boolean isWorking(float currentTime) {
		return true;
	}

	public boolean equals(Node<T> node) {
		if (node.getKey().equals(this.key))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return (String) key;
	}

	@Override
	public int compareTo(Node<T> arg0) {
		if (arg0.getKey().getClass().equals(String.class)) {
			String key1 = (String) this.getKey();
			String key2 = (String) arg0.getKey();
			return key1.compareTo(key2);
		}
		// throw an exception in the real implementation
		return 0;
	}
}
