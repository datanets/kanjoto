
package com.andrewsummers.kanjoto.model;

/**
 * Vertex is a model for Vertex objects.
 */
public class Vertex {
    private long id;
    private int node;

    /**
     * getId gets Vertex id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Vertex id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getNode gets Vertex node.
     * 
     * @return <code>int</code> of Vertex node.
     */
    public int getNode() {
        return node;
    }

    /**
     * setNode sets Vertex node.
     * 
     * @param node New Vertex node.
     */
    public void setNode(int node) {
        this.node = node;
    }

    /**
     * toString override to return Vertex node.
     * 
     * @return <code>String</code> of Vertex node.
     */
    @Override
    public String toString() {
        return String.valueOf(node);
    }
}
