package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.Friendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Long,List<Long>> adjList;
    private Map<Long, Boolean> visitedList;
    private List<List<Long>> components;
    private List<Long> ids;
    private final Iterable<Friendship> friendships;

    public Graph(Iterable<Friendship> f, List<Long> ids) {
        this.visitedList = new HashMap<>();
        this.friendships = f;
        this.ids = ids;
        prepare();
        components = new ArrayList<>();
        dfs();
    }

    private void prepare() {
        adjList = new HashMap<>();
        for(Long id : ids) {
            adjList.put(id, new ArrayList<>());
            visitedList.put(id, false);
        }
        for(Friendship f : friendships) {
            adjList.get(f.getId().getLeft()).add(f.getId().getRight());
            adjList.get(f.getId().getRight()).add(f.getId().getLeft());
        }

    }

    public int getComponents() { return components.size(); }

    private void dfs() {
        for(Long i : adjList.keySet()) {
            if(!visitedList.get(i)) {
                components.add(new ArrayList<>());
                visit(i);
            }
        }
    }

    private void visit(Long i) {
        visitedList.put(i, true);
        components.getLast().add((long) i);
        for(int j = 0; j < adjList.get(i).size(); j++) {
            if(!visitedList.get(adjList.get(i).get(j))) {
                visit(adjList.get(i).get(j));
            }
        }
    }

    public List<Long> longestPath() {
        int longestPath = 0;
        List<Long> socialComunity = new ArrayList<>();
        for(List<Long> c : components) {
            if(c.size() > longestPath) {
                longestPath = c.size();
                socialComunity = c;
            }
        }
        return socialComunity;
    }
}
