package org.wanghailu.detector.analyze.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 模拟当前方法栈帧执行时的上下文，包含本地变量表和操作数栈的信息
 */
public class EnvContext {
    
    private final ArrayList<StackItem> localVariables;
    
    private final LinkedList<StackItem> operandStack;
    
    public EnvContext() {
        this.localVariables = new ArrayList<>();
        this.operandStack = new LinkedList<>();
    }
    
    public EnvContext copy() {
        EnvContext newEnvContext = new EnvContext();
        for (StackItem localVariable : localVariables) {
            newEnvContext.addVariable(localVariable.copyOnSameTrace());
        }
        for (StackItem stack : operandStack) {
            newEnvContext.push(stack.copyOnSameTrace());
        }
        return newEnvContext;
    }
    
    public StackItem pop() {
        if(operandStack.size()>0){
            return operandStack.remove(operandStack.size() - 1);
        }
        return null;
    }
    
    public List<StackItem> pop(int num) {
        List<StackItem> result = new ArrayList<>();
        num = num > operandStack.size() ? operandStack.size() : num;
        for (int i = 0; i < num; i++) {
            result.add(operandStack.remove(operandStack.size() - 1));
        }
        return result;
    }
    
    public void push(StackItem item) {
        operandStack.add(item);
    }
    
    public void push() {
        operandStack.add(new StackItem());
    }
    
    public int stackSize() {
        return this.operandStack.size();
    }
    
    public void removeStack(int index) {
        this.operandStack.remove(index);
    }
    
    public void addVariable(StackItem t) {
        this.localVariables.add(t);
    }
    
    public void setVariable(int index, StackItem t) {
        if(t==null){
            t= new StackItem();
        }
        StackItem oldItem = localVariables.get(index);
        if(oldItem!=null){
            t.addTraces(oldItem.getTraces());
        }
        localVariables.set(index, t);
    }
    
    public StackItem getVariable(int index) {
        return localVariables.get(index);
    }
    
    public int variableSize() {
        return this.localVariables.size();
    }
    
    public void removeVariable(int index) {
        this.localVariables.remove(index);
    }
}
