package com.ssafy.trip.common.wordsearch;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchEngine {
    private static final TrieNode root = new TrieNode();

    public static void insert(String word, Long id) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.id = id;
    }

    public static List<Node> search(String prefix) {
        List<Node> results = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return results;
            }
        }
        dfs(node, prefix, results);
        return results;
    }
    private static void dfs(TrieNode node, String currentWord, List<Node> result) {
        if (result.size() > 10) return;
        if (node.isEndOfWord) result.add(new Node(currentWord, node.id));
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), currentWord + entry.getKey(), result);
        }
    }

    @Getter
    public static class Node {
        String word;
        Long id;
        public Node(String word, Long id) {
            this.word = word;
            this.id = id;
        }
    }
}
