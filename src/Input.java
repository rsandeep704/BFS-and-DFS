import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input {

	String algorithmToBeUsed;
	int numberOfPipes = 0;
	int startTime = 0;
	String sourceNode;
	String middleNodes;
	String destinationNodes;
	String[] graphs;

	public String getAlgorithmToBeUsed() {
		return algorithmToBeUsed;
	}

	public void setAlgorithmToBeUsed(String algorithmToBeUsed) {
		this.algorithmToBeUsed = algorithmToBeUsed.trim();
	}

	public int getNumberOfPipes() {
		return numberOfPipes;
	}

	public void setNumberOfPipes(int numberOfPipes) {
		this.numberOfPipes = numberOfPipes;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String getSourceNode() {
		return sourceNode.trim();
	}

	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}

	public String getMiddleNodes() {
		return middleNodes;
	}

	public void setMiddleNodes(String middleNodes) {
		this.middleNodes = middleNodes.trim();
	}

	public String getDestinationNodes() {
		return destinationNodes;
	}

	@Override
	public String toString() {
		String newLine = "\n";
		String text = algorithmToBeUsed + newLine;
		text += sourceNode + newLine;
		text += destinationNodes + newLine;
		text += middleNodes + newLine;
		text += graphs.length + newLine;
		for (String graph : graphs) {
			text += graph + newLine;
		}
		text += startTime + newLine;
		text += newLine;
		return text;
	}

	public void setDestinationNodes(String destinationNodes) {
		this.destinationNodes = destinationNodes.trim();
	}

	public String[] getGraphs() {
		return graphs;
	}

	public void setGraphs(String[] graphs) {
		this.graphs = graphs;
	}

	public Node<String> getRootFromInput() {
		Map<String, Node<String>> nodeMap = new HashMap<String, Node<String>>();
		Node<String> root = new Node<String>(sourceNode);
		nodeMap.put(sourceNode, root);

		for (String middleNode : middleNodes.split(" ")) {
			nodeMap.put(middleNode, new Node<String>(middleNode));
		}
		for (String destinationNode : destinationNodes.split(" ")) {
			nodeMap.put(destinationNode, new Node<String>(destinationNode));
		}
		for (String graph : graphs) {
			String[] items = graph.split(" ");
			Node<String> node = nodeMap.get(items[0]);
			if (node == null) {
				node = new Node<String>(items[0]);
				nodeMap.put(items[0], node);
			}
			Node<String> child = nodeMap.get(items[1]);
			if (child == null) {
				child = new Node<String>(items[1]);
				nodeMap.put(items[1], child);
			}
			node.addChild(child, Integer.parseInt(items[2]));
			if (items[3] != null && Integer.parseInt(items[3]) != 0) {
				List<TimeInterval> timesDoesNotWork = new ArrayList<TimeInterval>(Integer.parseInt(items[3]));
				for (int r = 4; r < items.length; r++) {
					timesDoesNotWork.add(new TimeInterval(Integer.parseInt(items[r].split("-")[0]),
							Integer.parseInt(items[r].split("-")[1])));
				}
				node.getTimesNotWorking().put(nodeMap.get(items[1]), timesDoesNotWork);
			} else {
				node.getTimesNotWorking().put(nodeMap.get(items[1]), null);
			}
		}
		return root;
	}

}
