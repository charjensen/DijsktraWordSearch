import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;

public class WordSearch {

	public static void main(String[] args) {

		HashMap<Integer, LinkedList<Vertex>> map = genMap();
		HashMap<String, ArrayList<Vertex>> adjList = genAdjList(map);

		boolean run = true;

		Scanner keyboard = new Scanner(System.in);
		do {
			String fInput = null;
			Vertex word1 = null;
			do {
				System.out.println(
						"Please enter the first word you would like to search (Must be in english dicitonary)");
				fInput = keyboard.next().toLowerCase().strip();
				word1 = new Vertex(0, fInput);
			} while (adjList.get(fInput) == null);
			String sInput = null;
			Vertex word2 = null;
			do {
				System.out.println("Please enter the second word, it must have the same length as the first");
				sInput = keyboard.next().toLowerCase().strip();
				word2 = new Vertex(Integer.MAX_VALUE, sInput);
			} while (adjList.get(sInput) == null || fInput.length() != sInput.length());
			
			PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();
			q.add(word1);
			addToQueue(adjList, q, adjList.get(word1.getWord()), q.poll());

			Vertex goal = dijkstrasStart(adjList, q, word1, word2);

			traverse(goal, word1);

			System.out.println("Would you like to enter another word? y / n");

			if (keyboard.next().equals("n"))
				run = false;

		} while (run);
		keyboard.close();
	}

	private static HashMap<String, ArrayList<Vertex>> genAdjList(HashMap<Integer, LinkedList<Vertex>> map) {

		HashMap<String, ArrayList<Vertex>> adjList = new HashMap<String, ArrayList<Vertex>>();

		for (LinkedList<WordSearch.Vertex> l : map.values()) {
			for (Vertex v : l) {

				String word = v.getWord();
				adjList.put(word, new ArrayList<Vertex>());

				for (Vertex s : l) {

					String word2 = s.getWord();

					if ((findWordDiff(word, word2)))
						adjList.get(word).add(s);

				}

			}

		}
		return adjList;
	}

	private static HashMap<Integer, LinkedList<Vertex>> genMap() {
		HashMap<Integer, LinkedList<Vertex>> map = new HashMap<Integer, LinkedList<Vertex>>();
		ArrayList<String> words = new ArrayList<String>();
		File f = new File("Dict.txt");
		Scanner fReader = null;

		try {
			fReader = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (fReader.hasNextLine())
			words.add(fReader.nextLine());

		for (String word : words) {

			if (map.get(word.length()) == null)
				map.put(word.length(), new LinkedList<Vertex>());

			map.get(word.length()).add(new Vertex(Integer.MAX_VALUE, word));

		}

		fReader.close();
		return map;
	}

	public static boolean findWordDiff(String a, String b) {

		char[] first = a.toCharArray();
		char[] second = b.toCharArray();

		int diffs = 0;

		for (int i = 0; i < a.length(); i++) {

			if (first[i] != second[i])
				diffs++;

			if (diffs > 1)
				return false;

		}

		if (diffs == 0)
			return false;

		return true;

	}

	public static int weightDiff(String a, String b) {

		char[] first = a.toCharArray();
		char[] second = b.toCharArray();

		for (int i = 0; i < a.length(); i++) {

			if (first[i] != second[i])
				return Math.abs(first[i] - second[i]);

		}

		return 0;

	}

	public static void addToQueue(HashMap<String, ArrayList<Vertex>> h, PriorityQueue<Vertex> q,
			ArrayList<Vertex> bucket, Vertex word) {

		boolean flag;

		for (Vertex v : bucket) {

			flag = true;

			for (Vertex v2 : q) {

				if (v.getWord().equals(v2.getWord()))
					flag = false;

			}

			if (flag) {
				q.add(v);
				addToQueue(h, q, h.get(v.getWord()), v);
			}
		}
	}

	public static Vertex dijkstrasStart(HashMap<String, ArrayList<Vertex>> h, PriorityQueue<Vertex> q, Vertex word,
			Vertex goal) {

		return dijkstras(h, q, word, goal);

	}

	public static Vertex dijkstras(HashMap<String, ArrayList<Vertex>> h, PriorityQueue<Vertex> q, Vertex word,
			Vertex goal) {

		if (word.getWord().equals(goal.getWord())) {
			relax(h, word, q);
			return word;
		}

		if (q.isEmpty() || word == null)
			return null;

		relax(h, word, q);
		return dijkstras(h, q, q.poll(), goal);

	}

	public static void relax(HashMap<String, ArrayList<Vertex>> h, Vertex v, PriorityQueue<Vertex> q) {

		int w = v.getWeight();
		ArrayList<Vertex> l = h.get(v.getWord());

		for (Vertex connected : l) {

			int newW = 0;

			if (!(connected.getWord().equals(v.getWord()))) {

				newW = w + weightDiff(connected.getWord(), v.getWord());

				if (newW < connected.getWeight()) {
					connected.setWeight(newW);
					connected.setPred(v);
					q.remove(connected);
					q.add(connected);
				}
			}
		}
	}

	public static void traverse(Vertex word, Vertex strt) {

		if (word == null) {
			System.out.println("There is no path between the words");
			return;
		}

		Stack<Vertex> s = new Stack<Vertex>();
		Vertex currentWord = word;
		s.add(currentWord);

		while (currentWord.getPred() != null) {

			currentWord = currentWord.getPred();
			s.add(currentWord);

		}

		if (!currentWord.getWord().equals(strt.getWord())) {
			System.out.println("There is no path between the words");
			return;
		}

		while (s.size() > 1) {
			Vertex sWord = s.pop();
			System.out.print(sWord.getWord() + " ---> ");
			sWord.setWeight(Integer.MAX_VALUE);
			sWord.setPred(null);
		}

		System.out.println(s.pop().getWord());

	}

	public static class Vertex implements Comparable<Vertex> {

		private int weight = Integer.MAX_VALUE;
		private String word;
		private Vertex pred = null;

		public Vertex() {

			weight = Integer.MAX_VALUE;
			word = null;

		}

		public Vertex(int w, String s) {

			weight = w;
			word = s;

		}

		public Vertex(Vertex v) {

			this.weight = v.getWeight();
			this.word = v.getWord();
			this.pred = v.getPred();

		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public Vertex getPred() {
			return pred;
		}

		public void setPred(Vertex v) {
			this.pred = v;
		}

		@Override
		public String toString() {

			return "(" + word + ", weight: " + weight + ")";

		}

		@Override
		public int compareTo(WordSearch.Vertex o) {

			if (this.getWeight() < o.getWeight()) {
				return -1;
			} else if (this.getWeight() > o.getWeight()) {
				return 1;
			} else {
				return 0;
			}

		}

		@Override
		public int hashCode() {
			return Objects.hash(weight, word);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			return Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight)
					&& Objects.equals(word, other.word);
		}

	}

}
