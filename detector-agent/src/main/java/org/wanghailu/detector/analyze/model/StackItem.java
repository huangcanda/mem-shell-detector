package org.wanghailu.detector.analyze.model;

import org.wanghailu.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

public class StackItem {

    public Type type;

    public String value;

    public Set<Trace> traces = new HashSet<>();

    public StackItem() {
    }

    public StackItem(Type type) {
        this.type = type;
    }

    public StackItem(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public StackItem addTrace(Trace trace) {
        if (trace != null) {
            traces.add(trace);
        }
        return this;
    }

    public StackItem addTraces(Set<Trace> traces) {
        if (traces != null && traces.size() > 0) {
            this.traces.addAll(traces);
        }
        return this;
    }

    public Set<Trace> getTraces() {
        return traces;
    }

    public StackItem copyOnSameTrace() {
        StackItem item = new StackItem();
        item.type = type;
        item.value = value;
        item.traces = traces;
        return item;
    }

    public StackItem copy() {
        StackItem item = new StackItem();
        item.type = type;
        item.value = value;
        item.traces.addAll(traces);
        return item;
    }

    public int getTypeSize() {
        if (type != null) {
            return type.getSize();
        } else {
            return 1;
        }
    }

}
