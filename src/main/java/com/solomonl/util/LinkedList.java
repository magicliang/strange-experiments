package com.solomonl.util;

/**
 * Created by magicliang on 2016/8/2.
 */


import java.util.Iterator;
import java.util.function.Consumer;

/**
 * TODO:
 * 1. add unit test for this class, for now we use main instead
 * 2. encapsulate this into a util class
 * 3. synchronized?
 * 4. 接口化？
 */

public class LinkedList<T> implements List {

    private Node<T> head;

    public LinkedList() {
        super();
        head = new Node<>();
        head.setNextNode(head);
        head.setPreviousNode(head);
    }

    public Node<T> getHead() {
        return head;
    }

    public void addNodeAtHead(Node<T> node) {
        Node<T> nextNode = head.getNextNode();
        node.setNextNode(nextNode);
        nextNode.setPreviousNode(node);
        head.setNextNode(node);
    }

    public void addNodeAtTail(Node<T> node) {
        Node<T> tail = head.getPreviousNode();
        node.setNextNode(head);
        head.setPreviousNode(node);
        tail.setNextNode(node);
        //Head 不能变，但tail总是可变
        tail = node;
    }

    @Override
    public String toString() {
        Node<T> node = head.getNextNode();
        StringBuilder sb = new StringBuilder("com.solomonl.util.LinkedList: \n" + head);
        while (node != head) {
            sb.append("\n => " + node);
            node = node.getNextNode();
        }

        return sb.toString();
    }

    @Override
    public Iterator iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }

        @Override
        public void remove() {

        }

        @Override
        public void forEachRemaining(Consumer action) {

        }
    }

    public static void main(String[] args) {
        testCase1();
        testCase2();
        testCase3();
    }

    private static void testCase1() {
        System.out.println("testCase1");
        LinkedList<String> linkedList = new LinkedList();
        for (int i = 0; i < 10; i++) {
            linkedList.addNodeAtTail(new Node<>((new Integer(i)).toString()));
        }
        System.out.println(linkedList);
    }

    private static void testCase2() {
        System.out.println("testCase2");
        LinkedList<String> linkedList = new LinkedList();
        for (int i = 10; i > 0; i--)
            linkedList.addNodeAtHead(new Node<>((new Integer(i))
                    .toString()));
        System.out.println(linkedList);
    }

    private static void testCase3() {
        System.out.println("testCase3");
        LinkedList<String> linkedList = new LinkedList();
        System.out.println(linkedList);
    }
}
