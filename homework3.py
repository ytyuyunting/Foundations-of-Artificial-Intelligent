import os
import math
from collections import deque
from queue import PriorityQueue as PQ
import heapq

#Design an admissible heuristic for A*
def h(a,b,tmap):
	(x1, y1)= (a[0],a[1])
	(x2, y2)= (b[0],b[1])
	dis2D = math.sqrt(min(abs(x1 - x2), abs(y1 - y2)) ** 2 * 2) + abs((x1 - x2) - (y1 - y2)) * 10
	dis3D = math.floor(dis2D) + abs(tmap[x1][y1]-tmap[x2][y2])
	return dis3D

#Use A* search to find the shortest path from startNode to targetNode.
def A_star_search(tmap,startNode,targetNode):
	frontier = []
	startNode=list(reversed(startNode))
	targetNode=list(reversed(targetNode))	
	parent = {}   # store the root of node
	parent = {str(startNode): None}
	cost_visited ={}   # store the cost of node from startNode
	cost=0
	heapq.heappush(frontier, (cost, startNode))
	cost_visited[str(startNode)]=cost
	while frontier:
		cur = heapq.heappop(frontier)[1]
		if cur == targetNode:
			break
		#traverse the neighbors of current node
		for next in graphNeighbors(arrz_int,cur):
			# Different cost of two kinds of move(2D): diagonal=14, N-S-W-E=10
			# Update the cost of node
			#cost_3D= cost_2D + height
			if next[0]!=cur[0] and next[1]!=cur[1]:
				cost = cost_visited[str(cur)]+ 14 + abs(tmap[next[0]][next[1]]-tmap[cur[0]][cur[1]])
			else:
				cost = cost_visited[str(cur)]+ 10 + abs(tmap[next[0]][next[1]]-tmap[cur[0]][cur[1]])
			# Expand node(find its children which has not been considered to avoid loop)
			# Update cost
			if (str(next) not in cost_visited) or cost< cost_visited[str(next)]:
				cost_visited[str(next)]=cost
				# f(n) = g(n)+h(n)
				# f(n')= max(g(n')+h(n'),f(n))
				priority = cost + h(targetNode,next,tmap),cur[0]
				heapq.heappush(frontier, (priority, next))
				parent[str(next)]= cur
	
	if cur!= targetNode:
		result ="FAIL"
	else:
		#print out the shortest path from the start point.
		# Parent stores the root of each visited node
		cost= cost_visited[str(cur)]
		path= deque()
		path.appendleft(list(reversed(cur)))
		while cost>0:
			child= cur
			cur= parent[str(cur)]
			if cur is not None :
				path.appendleft(list(reversed(cur)))
				cost= cost- (cost_visited[str(child)]- cost_visited[str(cur)])
		result= path
	return result

#Use UCS search to find the shortest path from startNode to targetNode.
def ucs_search(tmap,startNode,targetNode):
	frontier = []
	startNode=list(reversed(startNode))
	targetNode=list(reversed(targetNode))
	cost_visited = {}
	cost=0
    cost_visited[str(startNode)]=cost
	heapq.heappush(frontier, (cost, startNode))
	parent = {}
	parent = {str(startNode): None}
	while frontier:
		cur = heapq.heappop(frontier)[1]
		if cur == targetNode:
			break
		else:
			#traverse the neighbors of current node
			li_iter= iter(graphNeighbors(arrz_int,cur))
			while True:
				try:
					x= next(li_iter)
					# Different cost of two kinds of move: diagonal=14, N-S-W-E=10
					# Update the cost of node
					if x[0]!=cur[0] and x[1]!=cur[1]:
						cost = cost_visited[str(cur)]+14
					else:
						cost = cost_visited[str(cur)]+10
					# Expand node(find its children which has not been considered to avoid loop)
					# Update cost
					if (str(x) not in cost_visited) or cost< cost_visited[str(x)]:
						cost_visited[str(x)]=cost
						heapq.heappush(frontier, (cost, x))
						parent[str(x)]= cur
				except StopIteration:
					break
			
	if cur!= targetNode:
		result ="FAIL"
	else:
		#print out the shortest path from the start point.
		# Parent stores the root of each visited node
		cost= cost_visited[str(cur)]
		path= deque()
		path.appendleft(list(reversed(cur)))
		while cost>0:
			child= cur
			cur= parent[str(cur)]
			if cur is not None :
				path.appendleft(list(reversed(cur)))
				cost= cost- (cost_visited[str(child)]- cost_visited[str(cur)])
		result = path
	return result

#Use BFS search to find the shortest path from startNode to targetNode.
def bfs_search(tmap,startNode,targetNode):
	queue =deque()
	startNode=list(reversed(startNode))
	queue.append(startNode)
	cost_visited = {}
    cost=0
	cost_visited[str(startNode)]=cost
	parent = {}
	parent = {str(startNode): None}
	while queue:
		cur= queue.popleft()
		if cur == list(reversed(targetNode)):
			break
		else:
			cost = cost_visited[str(cur)]+1
			#traverse the neighbors of current node
			li_iter= iter(graphNeighbors(arrz_int,cur))
			while True:
				try:
					x= next(li_iter)
					# Expand node(find its children which has not been considered to avoid loop)
					# Update cost
					if str(x) not in cost_visited:
						queue.append(x)
						cost_visited[str(x)] = cost
						parent[str(x)]= cur
				except StopIteration:
					break
	if cur!= list(reversed(targetNode)):
		result= "FAIL"
	else:
		#print out the shortest path from the start point.
		# Parent stores the root of each visited node
		cost= cost_visited[str(cur)]
		path= deque()
		path.appendleft(list(reversed(cur)))
		while cost>=0:
			cur= parent[str(cur)]
			if cur is not None :
				path.appendleft(list(reversed(cur)))
			cost -= 1
		result = path
	return result

'''Find next safe move in tmap of point
	At mesh-grid, each cell has 8 possible neighbor cells.
	What defines the safety is the maximum slope between any two adjacent cells 
	return a list of safe neighbor cells'''
def graphNeighbors(tmap,point):
	i= point[0]
	j =point[1]
	neighbors=[]
	n=height
	m=width
	#North-South and East-West
	if 0<= i-1< n: 
		if abs(tmap[i-1][j] - tmap[i][j]) <= maxDiff:
			neighbors.append([i-1, j]) #North
	if 0<= i+1< n:
		if abs(tmap[i+1][j] - tmap[i][j]) <= maxDiff:
			neighbors.append([i+1, j]) #South
	if 0<= j-1< m:
		if abs(tmap[i][j-1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i, j-1]) #West
	if 0<= j+1< m: 
		if abs(tmap[i][j+1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i,j+1]) #East
	#diagonal move
	if 0<= i-1< n and 0<= j-1< m :
		if abs(tmap[i-1][j-1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i-1,j-1]) #NorthWest
	if 0<= i-1< n and 0<= j+1< m:  
		if abs(tmap[i-1][j+1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i-1,j+1]) #NorthEast
	if 0<= i+1< n and 0<= j-1< m :
		if abs(tmap[i+1][j-1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i+1,j-1]) #SouthhWest
	if 0<= i+1< n and 0<= j+1< m: 
		if abs(tmap[i+1][j+1] - tmap[i][j]) <= maxDiff:
			neighbors.append([i+1,j+1]) #SouthEast
	return neighbors

#read input file
with open('test0.txt', 'r') as f:
	#read input file
	lines = f.readlines()
	alg = lines[0].rstrip('\n')
	landingArea = lines[1].rstrip('\n').split()
	coordiLanding = lines[2].rstrip('\n').split()
	maxDiff = int(lines[3].rstrip('\n'))
	numTarget = int(lines[4].rstrip('\n'))
	targetSite =list()

	#process parameters of inputcoordiLanding
	width=int(landingArea[0])
	height=int(landingArea[1])
	start=[int(coordiLanding[0]),int(coordiLanding[1])]
	for numN in range(numTarget):
		targetSite.append(lines[4+numN+1].rstrip('\n'))
	targetSite_int=[]
	for i in range(numTarget):
		targetSite[i]=targetSite[i].split()
		targetSite_int.append(list(map(lambda x: int(x), targetSite[i])))
	arrZ =list()
	for numH in range(len(lines)-5-numTarget):
		arrZ.append(lines[5+numTarget+numH].rstrip('\n'))

	i=0
	arrz_int =[]
	while i<len(arrZ):
		arrZ[i]= arrZ[i].split()
		each_line = list(map(lambda x: int(x), arrZ[i]))
		arrz_int.append(each_line)
		i+=1

#create output file
with open('output.txt', 'w') as file:
	output=[]
	for i in range(numTarget):
		string=[]
		if alg=='BFS':
			output.append(bfs_search(arrz_int,start,targetSite_int[i]))
		elif alg=='UCS':
			output.append(ucs_search(arrz_int,start,targetSite_int[i]))
		elif alg=='A*':
			output.append(A_star_search(arrz_int,start,targetSite_int[i]))
		else:
			pass
		if output[0]=='FAIL':
			file.write('FAIL')
		else:
			for x in range(len(output[i])):
				string.append(",".join(str(j) for j in output[i][x])) 
			file.write(" ".join(str(j) for j in string))
			file.write('\n')





