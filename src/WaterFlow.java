import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

public class WaterFlow {

	public static void main(String args[]) {
		Scanner sc = null;
		try {
			String filename = null;
			if (args[0].equals("-i"))
				filename = args[1];
			sc = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int numberOftestCases = Integer.parseInt(sc.nextLine());
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < numberOftestCases; i++) {
			Input input = new Input();

			input.setAlgorithmToBeUsed(sc.nextLine());

			input.setSourceNode(sc.nextLine());
			input.setDestinationNodes(sc.nextLine());
			input.setMiddleNodes(sc.nextLine());

			input.setNumberOfPipes(Integer.parseInt(sc.nextLine()));

			String[] graphs = new String[input.getNumberOfPipes()];
			for (int j = 0; j < input.getNumberOfPipes(); j++) {
				graphs[j] = sc.nextLine();
			}
			input.setGraphs(graphs);

			input.setStartTime(Integer.parseInt(sc.nextLine()));

			String result = null;

			try {
				result = search(input);
			} catch (Exception e) {
			}
			if (result == null) {
				output.append("None");
			} else {
				output.append(result);
			}
			if (i != numberOftestCases - 1) {
				output.append("\n");
			}
			try {
				sc.nextLine();
			} catch (Exception e) {
			}
		}

		sc.close();

		PrintWriter outputWriter = null;
		try {
			outputWriter = new PrintWriter("output.txt");
		} catch (FileNotFoundException e) {
			File f = new File("output.txt");
			try {
				f.createNewFile();
				outputWriter = new PrintWriter("output.txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		outputWriter.print(output.toString());
		outputWriter.close();
	}

	static Map<String, Boolean> visited;

	private static String search(Input input) {
		Node<String> root = input.getRootFromInput();
		root.setDistanceFromRoot(input.getStartTime());
		LinkedList<Node<String>> frontier = new LinkedList<Node<String>>();
		visited = new HashMap<String, Boolean>();
		frontier.addFirst(root);

		while (!frontier.isEmpty()) {
			// expand
			Node<String> nodeToBeTested = frontier.peekFirst();
			if (input.getAlgorithmToBeUsed().equals("UCS")) {
				if (visited.get(nodeToBeTested.getKey()) != null)
					nodeToBeTested.setVisited(visited.get(nodeToBeTested.getKey()));
				if (nodeToBeTested.isVisited()) {
					frontier.removeFirst();
					continue;
				}
				List<TimeInterval> times = null;
				try {
					for (Entry<Node<String>, List<TimeInterval>> entry : nodeToBeTested.getParent().getTimesNotWorking()
							.entrySet()) {
						if (entry.getKey().equals(nodeToBeTested)) {
							times = entry.getValue();
							break;
						}
					}
				} catch (Exception e) {
					times = new ArrayList<>();
				}
				boolean working = false;
				try {
					working = isWorking(nodeToBeTested.getParent().getDistanceFromRoot(), times);
				} catch (Exception e) {
					working = true;
				}
				if (!working) {
					visited.put(nodeToBeTested.getKey(), false);
					frontier.removeFirst();
					continue;
				}
				visited.put(nodeToBeTested.getKey(), true);
			}
			// expand end

			// goal test start
			for (String destinationNode : input.getDestinationNodes().split(" ")) {
				if (nodeToBeTested.toString().equals(destinationNode)) {
					return nodeToBeTested.toString() + " " + nodeToBeTested.getDistanceFromRoot() % 24;
				}
			}
			// goal test end

			// queueing function
			queuingFunction(frontier, input.getAlgorithmToBeUsed());
		}
		return null;
	}

	private static void queuingFunction(LinkedList<Node<String>> frontier, String algorithmToBeUsed) {
		Node<String> front = frontier.removeFirst();
		front.setVisited(true);
		switch (algorithmToBeUsed) {
		case "UCS":
			if (visited.get(front) != null && visited.get(front)) {
				return;
			}
			for (Entry<Node<String>, Integer> child : front.getChildren().entrySet()) {
				child.getKey().setDistanceFromRoot((child.getValue() + front.getDistanceFromRoot()));
				child.getKey().setParent(front);

				int index = 0;
				while (index < frontier.size()
						&& (frontier.get(index).getDistanceFromRoot() < child.getKey().getDistanceFromRoot())) {
					index++;
				}
				while (index < frontier.size()
						&& frontier.get(index).getDistanceFromRoot() == child.getKey().getDistanceFromRoot()) {
					if (child.getKey().getKey().compareTo(frontier.get(index).getKey()) <= 0) {
						break;
					}
					index++;
				}
				frontier.add(index, new Node<String>(child.getKey()));
			}
			break;
		case "BFS":
			for (Entry<Node<String>, Integer> child : front.getChildren().entrySet()) {
				if (child.getKey().isVisited()) {
					if (child.getKey().getDistanceFromRoot() > front.getDistanceFromRoot() + 1) {
						child.getKey().setDistanceFromRoot(1 + front.getDistanceFromRoot());
						child.getKey().setParent(front);
					}
				} else {
					child.getKey().setDistanceFromRoot(1 + front.getDistanceFromRoot());
					frontier.addLast(child.getKey());
					child.getKey().setParent(front);
					child.getKey().setVisited(true);
				}
			}
			break;
		case "DFS":
			NavigableMap<Node<String>, Integer> reverseMap = ((TreeMap<Node<String>, Integer>) front.getChildren())
					.descendingMap(); // insert in reverse order for DFS
			for (Entry<Node<String>, Integer> child : reverseMap.entrySet()) {
				if (child.getKey().isVisited()) {
					if (frontier.removeFirstOccurrence(child.getKey())) {
						frontier.addFirst(child.getKey());
						child.getKey().setParent(front);
						child.getKey().setDistanceFromRoot(1 + front.getDistanceFromRoot());
					}
				} else {
					child.getKey().setDistanceFromRoot(1 + front.getDistanceFromRoot());
					frontier.addFirst(child.getKey());
					child.getKey().setVisited(true);
					child.getKey().setParent(front);
				}
			}
			break;
		}
	}

	private static boolean isWorking(int currentTime, List<TimeInterval> timesNotWorking) {
		currentTime = currentTime % 24;
		if (timesNotWorking == null) {
			return true;
		}
		for (TimeInterval timeNotWorking : timesNotWorking) {
			if (currentTime >= timeNotWorking.getStartTime() && currentTime <= timeNotWorking.getEndTime()) {
				return false;
			}
		}
		return true;
	}

}
