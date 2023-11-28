
# GRAPHS AND ALGORITHMS

![release](https://img.shields.io/badge/release-v1.1-brightgreen.svg)
![language](https://img.shields.io/badge/language-Java%2014-yellow.svg)
[![Documentation](https://img.shields.io/badge/javadoc-Read-orange.svg)](https://lucasmalara.github.io/graphs-and-algorithms/)
[![author](https://img.shields.io/badge/author-lucasmalara-blue.svg)](https://github.com/lucasmalara)

## Versions

- `v1.0-beta`
- `v1.0` 
- `v1.1` - Latest stable release &bull; [**What's new?**](https://github.com/lucasmalara/graphs-and-algorithms/releases/tag/v1.1)

## Goal

The project was focused on:

- implementation of unweighted undirected graphs that cannot have multiple edges nor loops.
- implementation of chosen algorithms involved with graphs.

<p align="justify">
    Initially, the project was created to the needs of my diploma thesis 
    <code>'Algorithmic aspects of domination problem in graphs'</code>, where I included implementation of an 
    <code>approximation algorithm</code> for computing <code>maximum connected dominating set</code> in given graph.
    The implementation of the algorithm required defining specific <b>data structure</b>. Despite possibility to use already 
    accessible data structures in <b>Java</b>, I decided to implement the structure myself in case of possible improvements 
    and changes I could be willing to deploy - hence I defined <code>Graph</code> class.
    The next case was to decide about vertex implementation. And simply because of an idea:
</p>

<blockquote>what if there would be a need for each or some vertex to store multiple values?</blockquote>

<p align="justify">
    ...I decided to create a <code>Vertex</code> class as well. <b>However, it was important to me to keep it simple for a user.</b> 
    Thus, I decided that <b>Vertex</b> should be an inner non-static class of <b>Graph</b>, so the user do not have to worry about 
    having an object instance and use only a number as a reference. To make it possible, I implemented methods that map numbers to 
    vertex objects and the other way.
</p>

<p align="justify">
    After a graduation, I decided to improve the quality of written code and add new features. However, my goal was to keep
    the assumption about the implementation I determined in <code>v1.0-beta</code>. Therefore, I didn't implement palpable 
    representation of edges, since the implementation of <b>vertex neighbourhood</b> was satisfactory in such case. 
    The outcome of my work has been committed on this repository.
</p>


## Features

- Create an empty graph (_order-zero graph / null graph_)
- Create a graph from given file
- Create a complete graph (including an _empty_)
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

## Run Configuration

To run this project, it is recommended to add an **environment variable** to the run configuration:

`ALLOW`
and set its value to `true` or `false` - depending on wanted behaviour.

```
if(ALLOW) -> runs basic and com.graphs.exceptions test of the Graph implementation
else -> runs only basic test of Graph implementation
```

_Note that if you do not add environment variable to the run configuration of a project you can expect to run the basic test only._

## Dependencies

[Lombok](https://projectlombok.org/) (1.18.30)

[JetBrains Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) (24.1.0)

[JUnit Jupiter API](https://junit.org/junit5/docs/5.10.1/api/org.junit.jupiter.api/module-summary.html) (5.10.1)

[JUnit Jupiter Params](https://junit.org/junit5/docs/5.10.1/api/org.junit.jupiter.params/module-summary.html) (5.10.1)
