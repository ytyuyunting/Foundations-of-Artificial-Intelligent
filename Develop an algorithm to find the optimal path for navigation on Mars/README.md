# Foundations-of-Artificial-Intelligent
561

Project description 

In this project, we twist the problem of path planning a little bit just to give you the opportunity to deepen your understanding of search
algorithms by modifying search techniques to fit the criteria of a realistic problem. To give you a realistic context for expanding your 
ideas about search algorithms, we invite you to take part in a Mars exploration mission. The goal of this mission is to send a sophisticated
mobile lab to Mars to study the surface of the planet more closely. We are invited to develop an algorithm to find the optimal path for
navigation of the rover based on a particular objective.

Search for the optimal paths  
Our task is to move the rover from its landing site to one of the target sites for experiments and soil sampling. For an ideal rover that 
can cross every place, usually the shortest geometrical path is defined as the optimal path; however, since in this project we have some 
operational concerns, our objective is first to avoid steep areas and thus we want to minimize the path from A to B under those constraints.
Thus, our goal is, roughly, finding the shortest path among the safe paths. What defines the safety of a path is the maximum slope between 
any two adjacent cells along that path.
