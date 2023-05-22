
# GRAPHS AND ALGORITHMS

<p align="justify">
The project is built using <b>Maven</b> for project management and the code is written in <b>Java 14</b>.
</p>

Versions:
- `1.0-beta` - For diploma thesis
- `1.0` - Latest stable release

## Purpose

The project was focused on:

- implementation of unweighted undirected graphs that cannot have multiple edges nor loops.
- implementation of chosen algorithms involved with graph structures.

<p align="justify">
Initially, the project was created to the needs of my diploma thesis 
<i>'Algorithmic aspects of domination problem in graphs'</i>, where I included implementation of <b>approximation algorithm</b> 
for computing <b>maximum connected dominating set</b> of given graph. The implementation of the algorithm required defining 
specific data structure. Despite possibility to use already accessible data structures in Java, I decided to implement the 
structure myself in case of possible improvements and changes I could be willing to deploy - hence I defined Graph class. 
The next case was to decide about vertex implementation. And simply because of an idea: <i>what if there would be a need 
for each or some vertex to store multiple values?</i> - I decided to create a Vertex class as well. However, it was 
important to me to keep it simple for a user. Thus, I decided that Vertex should be an inner non-static class of Graph, 
so the user do not have to worry about having an object instance and use only a number as a reference. To make it possible, 
I implemented methods that map numbers to vertex objects and the other way.
<br>
<br>
After a graduation, I decided to improve the quality of written code and add new features. However, my goal was to keep
the assumption about the implementation I determined in <code>1.0-beta</code> project version. Therefore, I didn't 
implement palpable representation of edges, since the implementation of <b>vertex neighbourhood</b> was satisfactory 
in such case. The outcome of my work has been committed on this repository.
</p>

## Changes in `1.0`

### Improvements

- Code refactoring (cleaner code, improved functionality and efficiency of Graph implementation)
- Replaced some of the methods with lombok annotations
- Changed the order of displayable vertices to ascending
- Changed the displayable sets of vertices to unmodifiable
- Changed return type of some of the methods from void to boolean
- Added new files to resources
- Added javadoc
- Added JetBrains annotations
- Added JUnit unit tests
- Added GraphRunner class
- Added NegativeVertexIndexException class
- Added NoSuchVertexIndexException class

### New Features

- Disconnect vertices
- Get neighbours of given vertex
- Remove multiple vertices from the graph (before: only singular vertex per method invocation)
- Create complete graph of given size,
- Check if graph contains given vertices (before: only singular vertex per method invocation)
- Check if subgraph or graph is bipartite
- Check if subgraph or graph is complete
- Check if given subset of vertices is an independent set of the graph
- Compute maximal independent set in the graph
- Modified implementation of breadth first search algorithm
- Map a graph to a complete graph of the same size
- Map vertices set to vertices indexes set

## Features

- Create an empty graph (_order-zero graph / null graph_)
- Create a graph from given file
- Create a complete graph (including an empty)
- Display graph structure (all vertices and for each their [open] neighbourhood list)
- Get all vertices of the  graph
- Get neighbours of given vertex
- Add singular vertex to the graph
- Add multiple vertices to the graph
- Remove singular vertex from the graph
- Remove multiple vertices from graph
- Connect vertices
- Disconnect vertices
- Check if graph contains given vertex or vertices
- Check if subgraph or graph is connected
- Check if subgraph or graph is bipartite
- Check if subgraph or graph is complete
- Check if given subset of vertices is a connected dominating set of the graph
- Check if given subset of vertices is an independent set of the graph
- Modified implementation of breadth first search algorithm
- Modified implementation of depth first search algorithm
- Compute minimal dominating set in the graph
- Compute minimal connected dominating set in the graph
- Compute maximal independent set in the graph
- Map a graph to a complete graph of the same size

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
