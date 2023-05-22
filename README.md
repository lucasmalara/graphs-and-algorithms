
# GRAPHS AND ALGORITHMS

<p align="justify">
The present repository contains <b>GRAPHS_AND_ALGORITHMS</b> project. The project is built using 
<code>Maven</code> for project management and the code is written in <code>Java 14</code>.
</p>

Versions:
- `1.0-beta` - For diploma thesis (source code unavailable)
- `1.0` - Current version

## Purpose

The project was focused on:

- implementation of unweighted undirected graphs that cannot have multiple edges nor loops.
- implementation of chosen algorithms involved with graph structures.

<p align="justify">
Initially, the project was created to the needs of my diploma thesis 
<b>"Algorithmic aspects of domination problem in graphs"</b>, where I included implementation of 
<code>approximation algorithm</code> for computing <code>maximum connected dominating set</code> of given graph. 
The implementation of the algorithm required defining specific data structure. Despite possibility to use already accessible 
data structures in <code>Java</code>, I decided to implement the structure myself in case of possible improvements and changes 
I could be willing to deploy - hence I defined <code>Graph class</code>. The next case was to decide about vertex implementation. 
And simply because of an idea: <code>what if there would be a need for each or some vertex to store multiple values?</code> - I decided to 
create a <code>Vertex class</code> as well. However, it was important to me to keep it simple for a user.
Thus, I decided <code>Vertex</code> should be an <code>inner non-static class</code> of <code>Graph</code>, so the user 
do not have to worry about having an object instance of <code>Vertex class</code> and use only a number as a reference. 
To make it possible, I implemented methods that map numbers to <code>Vertex</code> objects and the other way.
<br>
<br>
After a graduation, I decided to improve the quality of written code and add new features. However, my goal was to keep
the assumption about the implementation I determined in <code>1.0-beta</code> project version. Therefore, I didn't 
implement palpable representation of edges, since the implementation of <b>vertex neighbourhood</b> was satisfactory in such case. 
The outcome of my work has been committed on this repository.
</p>

## Changes in `1.0`

Below list corresponds to the changes provided into project after thesis defence:
- Code refactoring (cleaner code, improved functionality and efficiency of `Graph` implementation);
- Replaced some of the methods with `lombok annotations`;
- Changed the order of displayable vertices to `ascending`;
- Changed the displayable sets of vertices to `unmodifiable`;
- Changed return type of some of the methods from `void` to `boolean`;
- Added new files to resources;
- Added `javadoc`;
- Added `JetBrains annotations`;
- Added `JUnit unit tests`;
- Added `GraphRunner` class;
- Added `NegativeVertexIndexException` class;
- Added `NoSuchVertexIndexException` class;
- Added new features:
  - Disconnect vertices,
  - Get neighbours of given vertex,
  - Remove multiple vertices from the graph(before you could remove only singular vertex per method invocation),
  - Create complete graph of given size,
  - Check if graph contains given vertices(before you could check for only singular vertex per method invocation),
  - Check if subgraph or graph is bipartite,
  - Check if subgraph or graph is complete,
  - Check if given subset of vertices is an independent set of the graph,
  - Compute maximal independent set in the graph,
  - Modified implementation of breadth first search algorithm,
  - Map a graph to a complete graph of the same size,
  - Map vertices set to vertices indexes set;

## Features

Main features:
- Create an empty graph (_order-zero graph / null graph_);
- Create a graph from given file;
- Create a complete graph (including an empty);
- Display graph structure (all vertices and for each their [open] neighbourhood list);
- Get all vertices of the  graph;
- Get neighbours of given vertex;
- Add singular vertex to the graph;
- Add multiple vertices to the graph;
- Remove singular vertex from the graph;
- Remove multiple vertices from graph;
- Connect vertices;
- Disconnect vertices;
- Check if graph contains given vertex or vertices;
- Check if subgraph or graph is connected;
- Check if subgraph or graph is bipartite;
- Check if subgraph or graph is complete;
- Check if given subset of vertices is a connected dominating set of the graph;
- Check if given subset of vertices is an independent set of the graph;
- Modified implementation of breadth first search algorithm;
- Modified implementation of depth first search algorithm;
- Compute minimal dominating set in the graph;
- Compute minimal connected dominating set in the graph;
- Compute maximal independent set in the graph;
- Map a graph to a complete graph of the same size;

Others:
<br>
Other features are responsible for interaction between a user and implemented Graph logic.

## Environment Variables

To run this project, it is recommended to add an **environment variable** to the **run configuration**:

`ALLOW`
and set its value to `true` or `false` - depending on wanted behaviour.

```
if(ALLOW) -> runs basic and exceptions test of Graph implementation
else -> runs only basic test of Graph implementation
```

_Note that if you do not add environment variable to run configuration of a project you can expect to run the basic test only._

## Used Dependencies

Dependencies were managed using _Maven_ dependency mechanism. They were kept up to date during the work on project.

[Lombok](https://projectlombok.org/) (1.18.24)

[JetBrains Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) (23.0.0)

[JUnit Jupiter API](https://junit.org/junit5/docs/5.9.0/api/org.junit.jupiter.api/module-summary.html) (5.9.0)

[JUnit Jupiter Params](https://junit.org/junit5/docs/5.9.0/api/org.junit.jupiter.params/module-summary.html) (5.9.0)

## Java Documentation

[Read documentation online](https://lucasmalara.github.io/graphs-and-algorithms/ "Java documentation")

## Author

[@Łukasz Malara](https://github.com/lucasmalara "author")
