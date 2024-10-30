# Word Search

Demo:
[![Watch the Video!](https://img.youtube.com/vi/XuZnOt4sTQ0/0.jpg)](https://www.youtube.com/watch?v=XuZnOt4sTQ0)

This Java program allows users to search for a path between two words of the same length using Dijkstra's algorithm. The program uses a graph representation where each word is a vertex, and edges exist between words that differ by only one letter.

## Features

- **Word Search**: Finds a path between two words of the same length using Dijkstra's algorithm.
- **User Interaction**: Prompts the user to enter words and displays the path between them.

## Usage

### Search for a Path Between Words

1. Enter the first word (must be in the English dictionary).
2. Enter the second word (must have the same length as the first word).
3. The program will display the path between the two words.
4. You can choose to enter another word or exit the program.

## Code Overview

### Main Class: `WordSearch`

- **Main Method**: Handles user input and initiates the word search.
- **genMap Method**: Generates the initial map of words.
- **genAdjList Method**: Generates the adjacency list for the graph.
- **dijkstrasStart Method**: Implements Dijkstra's algorithm to find the shortest path between words.
- **traverse Method**: Traverses the path found by Dijkstra's algorithm and displays it.

### Dependencies

- **HashMap**: Used for storing the graph representation.
- **LinkedList**: Used for storing vertices.
- **ArrayList**: Used for storing adjacency lists.
- **PriorityQueue**: Used for implementing Dijkstra's algorithm.
