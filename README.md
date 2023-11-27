
# GRAPHS AND ALGORITHMS

![release](https://img.shields.io/badge/release-v1.0-brightgreen.svg)
![language](https://img.shields.io/badge/language-Java%2014-yellow.svg)
[![Documentation](https://img.shields.io/badge/javadoc-Read-orange.svg)](https://lucasmalara.github.io/graphs-and-algorithms/)
[![author](https://img.shields.io/badge/author-lucasmalara-blue.svg)](https://github.com/lucasmalara)

## Versions

- `v1.0-beta`
- `v1.0` - Latest stable release &bull; [**What's new?**](https://github.com/lucasmalara/graphs-and-algorithms/releases/tag/v1.0)

## Goal

The project was focused on:

- implementation of unweighted undirected graphs that cannot have multiple edges nor loops.
- implementation of chosen algorithms involved with com.graphs.struct structures.

<p align="justify">
Initially, the project was created to the needs of my diploma thesis 
<i>'Algorithmic aspects of domination problem in graphs'</i>, where I included implementation of <b>approximation algorithm</b> 
for computing <b>maximum connected dominating set</b> of given com.graphs.struct. The implementation of the algorithm required defining 
specific data structure. Despite possibility to use already accessible data structures in Java, I decided to implement the 
structure myself in case of possible improvements and changes I could be willing to deploy - hence I defined Graph class. 
The next case was to decide about vertex implementation. And simply because of an idea: <i>what if there would be a need 
for each or some vertex to store multiple values?</i> - I decided to create a Vertex class as well. However, it was 
important to me to keep it simple for a user. Thus, I decided that Vertex should be an inner non-static class of Graph, 
so the user do not have to worry about having an object instance and use only a number as a reference. To make it possible, 
I implemented methods that map numbers to vertex objects and the other way.
<br>
After a graduation, I decided to improve the quality of written code and add new features. However, my goal was to keep
the assumption about the implementation I determined in <code>v1.0-beta</code>. Therefore, I didn't implement palpable 
representation of edges, since the implementation of <b>vertex neighbourhood</b> was satisfactory in such case. 
The outcome of my work has been committed on this repository.
</p>

## Features

- Create an empty com.graphs.struct (_order-zero com.graphs.struct / null graph_)
- Create a com.graphs.struct from given file
- Create a complete com.graphs.struct (including an _empty_)
- Display com.graphs.struct structure (all vertices and for each their [open] neighbourhood list)
- Get all vertices of the  com.graphs.struct
- Get neighbours of given vertex
- Add singular vertex to the com.graphs.struct
- Add multiple vertices to the com.graphs.struct
- Remove singular vertex from the com.graphs.struct
- Remove multiple vertices from com.graphs.struct
- Connect vertices
- Disconnect vertices
- Check if com.graphs.struct contains given vertex or vertices
- Check if subgraph or com.graphs.struct is connected
- Check if subgraph or com.graphs.struct is bipartite
- Check if subgraph or com.graphs.struct is complete
- Check if given subset of vertices is a connected dominating set of the com.graphs.struct
- Check if given subset of vertices is an independent set of the com.graphs.struct
- Modified implementation of breadth first search algorithm
- Modified implementation of depth first search algorithm
- Compute minimal dominating set in the com.graphs.struct
- Compute minimal connected dominating set in the com.graphs.struct
- Compute maximal independent set in the com.graphs.struct
- Map a com.graphs.struct to a complete com.graphs.struct of the same size

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

[Lombok](https://projectlombok.org/) (1.18.26)

[JetBrains Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) (24.0.1)

[JUnit Jupiter API](https://junit.org/junit5/docs/5.9.0/api/org.junit.jupiter.api/module-summary.html) (5.9.2)

[JUnit Jupiter Params](https://junit.org/junit5/docs/5.9.0/api/org.junit.jupiter.params/module-summary.html) (5.9.2)
