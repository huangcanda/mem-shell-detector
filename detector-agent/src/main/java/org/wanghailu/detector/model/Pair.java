package org.wanghailu.detector.model;

import java.util.Objects;

/**
 * 二元组
 */
public class Pair<A, B> {

    private A first;

    private B second;

    public Pair(A a, B b) {
        first = a;
        second = b;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
