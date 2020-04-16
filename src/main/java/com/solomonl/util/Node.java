package com.solomonl.util;

/**
 * Created by magicliang on 2016/8/2.
 */
// 虽然是一个单独的文件，但它依然是只是包可见的
// 泛型化
class Node<T> {
    // 还是放在这里吧，在内存铺排的时候，这些field反正都是null了
    private Node<T> nextNode;
    private Node<T> previousNode;
    private T value;

    Node() {
        super();
    }

    Node(T value) {
        this();
        this.value = value;
    }

    Node getNextNode() {
        return nextNode;
    }

    void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    Node<T> getPreviousNode() {
        return previousNode;
    }

    void setPreviousNode(Node<T> previousNode) {
        this.previousNode = previousNode;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                '}';
    }
}
