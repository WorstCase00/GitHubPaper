package edu.bocmst.graph;

import java.util.Set;

public interface IDirectedGraph {

	Set<IDirectedEdge> getEdges();

	IDirectedEdge getEdge(int source, int target);

	Set<Set<IDirectedEdge>> getCycleStructures();
	
	Set<Integer> getSuccessors(int activity);
	
	Set<Integer> getPredecessors(int activity);

	Set<Integer> getVertexSet();

}