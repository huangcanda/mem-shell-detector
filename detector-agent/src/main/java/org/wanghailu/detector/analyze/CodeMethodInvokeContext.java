package org.wanghailu.detector.analyze;

import org.wanghailu.detector.analyze.model.StackItem;
import org.wanghailu.objectweb.asm.Opcodes;
import org.wanghailu.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CodeMethodInvokeContext {
    protected int opcode;
    protected String owner;
    protected String name;
    protected String descriptor;
    protected boolean isInterface;
    
    protected List<StackItem> argList;

    public CodeMethodInvokeContext() {
    }

    public CodeMethodInvokeContext(int opcode, String owner, String name, String descriptor, boolean isInterface,List<StackItem> argList) {
        this.opcode = opcode;
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.isInterface = isInterface;

        Type[] argumentTypes = Type.getArgumentTypes(descriptor);
        boolean isStatic = opcode == Opcodes.INVOKESTATIC;
        int length = isStatic ? argumentTypes.length : argumentTypes.length + 1;
        List<StackItem> list = new ArrayList<>();
        for (int argIndex = 0; argIndex < length; argIndex++) {
            StackItem item;
            if (argIndex < argList.size() && argList.get(argIndex) != null) {
                item = argList.get(argIndex);
            } else {
                item = new StackItem();
            }
            if (item.type == null) {
                if (argIndex == 0 && !isStatic) {
                    item.type = Type.getObjectType(owner);
                } else {
                    item.type = argumentTypes[isStatic ? argIndex : argIndex - 1];
                }
            }
            list.add(item);
        }
        this.argList = list;
    }
    
    public int getOpcode() {
        return opcode;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescriptor() {
        return descriptor;
    }
    
    public boolean isInterface() {
        return isInterface;
    }
    
    public List<StackItem> getArgList() {
        return argList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeMethodInvokeContext that = (CodeMethodInvokeContext) o;
        return Objects.equals(owner, that.owner) &&
                Objects.equals(name, that.name) &&
                Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }
}
