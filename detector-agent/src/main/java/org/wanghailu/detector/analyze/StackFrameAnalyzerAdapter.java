package org.wanghailu.detector.analyze;

import org.wanghailu.detector.analyze.model.StackItem;
import org.wanghailu.detector.analyze.model.EnvContext;
import org.wanghailu.detector.analyze.model.Trace;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.objectweb.asm.ConstantDynamic;
import org.wanghailu.objectweb.asm.Handle;
import org.wanghailu.objectweb.asm.Label;
import org.wanghailu.objectweb.asm.MethodVisitor;
import org.wanghailu.objectweb.asm.Opcodes;
import org.wanghailu.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StackFrameAnalyzerAdapter extends MethodVisitor {

    private final Map<Label, EnvContext> labelContextMap = new HashMap<>();

    private final Set<Label> exceptionHandlerLabels = new HashSet<>();

    protected EnvContext currentContext;

    protected DetectorContext detectorContext;

    protected AnalysisMethodInfo analysisMethodInfo;

    protected Set<Trace> returnTrace = new HashSet<>();


    public StackFrameAnalyzerAdapter(AnalysisMethodInfo analysisMethodInfo, final MethodVisitor methodVisitor, DetectorContext detectorContext) {
        super(Opcodes.ASM9, methodVisitor);
        currentContext = new EnvContext();
        this.detectorContext = detectorContext;
        this.analysisMethodInfo = analysisMethodInfo;
        AnalysisCodeContext.getInvokeMethodList().add(analysisMethodInfo);
        Type[] argumentTypes = Type.getArgumentTypes(analysisMethodInfo.getMethodDescriptor());

        boolean isStatic = (analysisMethodInfo.access & Opcodes.ACC_STATIC) != 0;
        int length = isStatic ? argumentTypes.length : argumentTypes.length + 1;
        List<StackItem> itemList = analysisMethodInfo.getItemList();
        for (int argIndex = 0; argIndex < length; argIndex++) {
            StackItem item;
            if (argIndex < itemList.size() && itemList.get(argIndex) != null) {
                item = itemList.get(argIndex).copyOnSameTrace();
            } else {
                item = new StackItem();
            }
            if(item.type==null){
                if(argIndex==0 && !isStatic){
                    item.type = Type.getObjectType(analysisMethodInfo.getClazz().getName().replace(".","/"));
                }else{
                    item.type = argumentTypes[isStatic?argIndex:argIndex-1];
                }
            }
            for (int i = 0; i < item.type.getSize(); i++) {
                currentContext.addVariable(item.addTrace(Trace.byArgIndex(argIndex, analysisMethodInfo)));
            }
        }
    }

    @Override
    public void visitEnd() {
        for (StackItem item : analysisMethodInfo.getItemList()) {
            for (Trace trace : new ArrayList<>(item.getTraces())) {
                if (analysisMethodInfo.equals(trace.getAnalysisMethodInfo())) {
                    item.getTraces().remove(trace);
                    break;
                }
            }
        }
        AnalysisCodeContext.getInvokeMethodList().remove(analysisMethodInfo);
        super.visitEnd();
    }

    @Override
    public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack,
                           final Object[] stack) {
        int stackSize = 0;
        for (int i = 0; i < numStack; i++) {
            Object typ = stack[i];
            int objectSize = 1;
            if (typ.equals(Opcodes.LONG) || typ.equals(Opcodes.DOUBLE)) {
                objectSize = 2;
            }
            for (int j = currentContext.stackSize(); j < stackSize + objectSize; j++) {
                Type type2 = local[i] instanceof String ? Type.getObjectType((String) local[i]) : null;
                currentContext.push(new StackItem(type2));
            }
            stackSize += objectSize;
        }
        int localSize = 0;
        for (int i = 0; i < numLocal; i++) {
            Object typ = local[i];
            int objectSize = 1;
            if (typ.equals(Opcodes.LONG) || typ.equals(Opcodes.DOUBLE)) {
                objectSize = 2;
            }
            for (int j = currentContext.variableSize(); j < localSize + objectSize; j++) {
                Type type2 = local[i] instanceof String ? Type.getObjectType((String) local[i]) : null;
                currentContext.addVariable(new StackItem(type2));
            }
            localSize += objectSize;
        }
        for (int i = currentContext.stackSize() - stackSize; i > 0; i--) {
            currentContext.removeStack(currentContext.stackSize() - 1);
        }
        for (int i = currentContext.variableSize() - localSize; i > 0; i--) {
            currentContext.removeVariable(currentContext.variableSize() - 1);
        }
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(final int opcode) {
        super.visitInsn(opcode);
        execute(opcode, 0, null);
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        super.visitIntInsn(opcode, operand);
        execute(opcode, operand, null);
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        super.visitVarInsn(opcode, var);
        for (int i = currentContext.variableSize(); i <= var; i++) {
            currentContext.addVariable(new StackItem());
        }
        execute(opcode, var, null);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        super.visitTypeInsn(opcode, type);
        execute(opcode, 0, type);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
        switch (opcode) {
            case Opcodes.GETSTATIC:
                pushDescriptor(descriptor);
                break;
            case Opcodes.PUTSTATIC:
                popByDescriptor(descriptor);
                break;
            case Opcodes.GETFIELD:
                StackItem item = pop();
                pushDescriptor(descriptor, item);
                break;
            case Opcodes.PUTFIELD:
                StackItem item1 = popByDescriptor(descriptor).get(0);
                StackItem item2 = pop();
                item2.addTraces(item1.getTraces());
                break;
            default:
                throw new IllegalArgumentException("Invalid opcode " + opcode);
        }
    }

    @Override
    public void visitMethodInsn(final int opcodeAndSource, final String owner, final String name,
                                final String descriptor, final boolean isInterface) {
        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);

        int opcode = opcodeAndSource & ~Opcodes.SOURCE_MASK;

        List<StackItem> argList = popByDescriptor(descriptor);
        if (opcode != Opcodes.INVOKESTATIC) {
            argList.add(pop());
        }
        Collections.reverse(argList);

        CodeMethodInvokeContext context = new CodeMethodInvokeContext(opcode, owner, name, descriptor, isInterface,
                argList);
        List<StackItem> items = detectorContext.getAnalysisCodeManager().analysisMethodOnCode(context);
        if ("java/lang/Object".equals(owner) && "getClass".equals(name)) {
            StackItem item = argList.get(0);
            if (item != null && item.type != null) {
                Trace trace = Trace.byMethodInfo(context);
                trace.setOtherInfo(item.type.getClassName());
                items.add(new StackItem().addTrace(trace));
            }
        }
        detectorContext.getAnalysisCodeManager().checkSinkMethod(context);
        pushDescriptorWithItemList(descriptor, items, false);
    }


    @Override
    public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle,
                                       final Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        List<StackItem> argList = popByDescriptor(descriptor);
        CodeMethodInvokeContext context = new CodeMethodInvokeContext(Opcodes.INVOKEDYNAMIC, analysisMethodInfo.getClazz().getName().replace(".", "/"), name, descriptor, true,
                argList);
        Collections.reverse(argList);
        Trace trace = Trace.byMethodInfo(context);
        argList.add(new StackItem().addTrace(trace));
        pushDescriptorWithItemList(descriptor, argList, false);
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        super.visitJumpInsn(opcode, label);
        execute(opcode, 0, null);
        saveLabelContext(label);
    }

    @Override
    public void visitLabel(final Label label) {
        super.visitLabel(label);
        EnvContext envContext = labelContextMap.get(label);
        if (envContext != null) {
            this.currentContext = envContext.copy();
        }
        if (exceptionHandlerLabels.contains(label)) {
            currentContext.push();
        }
    }

    @Override
    public void visitLdcInsn(final Object value) {
        super.visitLdcInsn(value);
        if (value instanceof Integer) {
            push(Opcodes.INTEGER);
        } else if (value instanceof Long) {
            push(Opcodes.LONG);
            push(Opcodes.TOP);
        } else if (value instanceof Float) {
            push(Opcodes.FLOAT);
        } else if (value instanceof Double) {
            push(Opcodes.DOUBLE);
            push(Opcodes.TOP);
        } else if (value instanceof String) {
            pushLdc(STRING, (String) value);
        } else if (value instanceof Type) {
            int sort = ((Type) value).getSort();
            if (sort == Type.OBJECT || sort == Type.ARRAY) {
                pushLdc(CLASS, ((Type) value).getClassName());
            } else if (sort == Type.METHOD) {
                pushLdc(METHOD_TYPE, ((Type) value).getDescriptor());
            } else {
                throw new IllegalArgumentException();
            }
        } else if (value instanceof Handle) {
            pushLdc(METHOD_HANDLE, value.toString());
        } else if (value instanceof ConstantDynamic) {
            pushDescriptor(((ConstantDynamic) value).getDescriptor());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        super.visitIincInsn(var, increment);
        execute(Opcodes.IINC, var, null);
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        execute(Opcodes.TABLESWITCH, 0, null);
        saveLabelContext(dflt);
        for (Label label : labels) {
            saveLabelContext(label);
        }
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        execute(Opcodes.LOOKUPSWITCH, 0, null);
        saveLabelContext(dflt);
        for (Label label : labels) {
            saveLabelContext(label);
        }
    }

    @Override
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        execute(Opcodes.MULTIANEWARRAY, numDimensions, descriptor);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        exceptionHandlerLabels.add(handler);
        super.visitTryCatchBlock(start, end, handler, type);
    }

    // -----------------------------------------------------------------------------------------------
    private static StackItem TOP = new StackItem(Type.VOID_TYPE);

    private static StackItem NULL = new StackItem();

    private static StackItem BOOLEAN = new StackItem(Type.BOOLEAN_TYPE);

    private static StackItem CHAR = new StackItem(Type.CHAR_TYPE);

    private static StackItem BYTE = new StackItem(Type.BYTE_TYPE);

    private static StackItem SHORT = new StackItem(Type.SHORT_TYPE);

    private static StackItem INT = new StackItem(Type.INT_TYPE);

    private static StackItem FLOAT = new StackItem(Type.FLOAT_TYPE);

    private static StackItem LONG = new StackItem(Type.LONG_TYPE);

    private static StackItem DOUBLE = new StackItem(Type.DOUBLE_TYPE);

    public static Type STRING = Type.getObjectType("java/lang/String");

    public static Type CLASS = Type.getObjectType("java/lang/Class");

    public static Type METHOD_TYPE = Type.getObjectType("java/lang/invoke/MethodType");

    public static Type METHOD_HANDLE = Type.getObjectType("java/lang/invoke/MethodHandle");

    private void saveLabelContext(Label label) {
        EnvContext oldEnvContext;
        if (labelContextMap.containsKey(label)) {
            EnvContext state = labelContextMap.get(label);
            oldEnvContext = state;
        } else {
            oldEnvContext = currentContext;
        }
        EnvContext newEnvContext = oldEnvContext.copy();
        labelContextMap.put(label, newEnvContext);
    }

    private StackItem get(final int local) {
        return currentContext.getVariable(local);
    }

    private void set(final int local, final StackItem type) {
        while (local >= currentContext.variableSize()) {
            currentContext.addVariable(TOP);
        }
        currentContext.setVariable(local, type);
    }

    private void push(Integer type) {
        if (Opcodes.TOP.equals(type)) {
            currentContext.push(TOP);
        } else if (Opcodes.NULL.equals(type)) {
            currentContext.push(NULL);
        } else if (Opcodes.INTEGER.equals(type)) {
            currentContext.push(INT);
        } else if (Opcodes.LONG.equals(type)) {
            currentContext.push(LONG);
        } else if (Opcodes.FLOAT.equals(type)) {
            currentContext.push(FLOAT);
        } else if (Opcodes.DOUBLE.equals(type)) {
            currentContext.push(DOUBLE);
        } else if (Opcodes.LONG.equals(type)) {
            currentContext.push(LONG);
        } else {
            throw new AssertionError();
        }
    }

    private void push(final StackItem type) {
        currentContext.push(type);
    }

    private void pushLdc(final Type type, String value) {
        currentContext.push(new StackItem(type, value).addTrace(Trace.byLdcValue(type, value)));
    }

    private void pushDescriptor(final String fieldOrMethodDescriptor, StackItem... argItems) {
        if (argItems != null && argItems.length > 0) {
            pushDescriptorWithItemList(fieldOrMethodDescriptor, Arrays.stream(argItems).collect(Collectors.toList()),
                    false);
        } else {
            pushDescriptorWithItemList(fieldOrMethodDescriptor, null, false);
        }

    }

    private Set<Trace> getTraceInfo(List<StackItem> argItems) {
        if (argItems == null || argItems.size() == 0) {
            return null;
        }
        Set<Trace> traces = new HashSet<>();
        for (StackItem argItem : argItems) {
            if(argItem!=null){
                traces.addAll(argItem.getTraces());
            }
        }
        return traces;
    }

    private void pushDescriptorWithItemList(final String fieldOrMethodDescriptor, List<StackItem> argItems,
                                            boolean isCheckCast) {
        String descriptor =
                fieldOrMethodDescriptor.charAt(0) == '(' ? Type.getReturnType(fieldOrMethodDescriptor).getDescriptor()
                        : fieldOrMethodDescriptor;
        switch (descriptor.charAt(0)) {
            case 'V':
                return;
            case 'Z':
                currentContext.push(BOOLEAN);
                return;
            case 'C':
                currentContext.push(CHAR);
                return;
            case 'B':
                currentContext.push(BYTE);
                return;
            case 'S':
                currentContext.push(SHORT);
                return;
            case 'I':
                currentContext.push(INT);
                return;
            case 'F':
                currentContext.push(FLOAT);
                return;
            case 'J':
                currentContext.push(LONG);
                currentContext.push(TOP);
                return;
            case 'D':
                currentContext.push(DOUBLE);
                currentContext.push(TOP);
            case '[':
            case 'L':
                if (isCheckCast) {
                    StackItem item = argItems.get(0);
                    if(item!=null){
                        item.type = Type.getType(descriptor);
                    }
                    currentContext.push(item);
                } else {
                    StackItem item = new StackItem(Type.getType(descriptor));
                    item.addTraces(getTraceInfo(argItems));
                    currentContext.push(item);
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    private StackItem pop() {
        return currentContext.pop();
    }

    private List<StackItem> pop(final int numSlots) {
        return currentContext.pop(numSlots);
    }


    public List<StackItem> popByDescriptor(String descriptor) {
        List<StackItem> result = new ArrayList<>();
        char firstDescriptorChar = descriptor.charAt(0);
        if (firstDescriptorChar == '(') {
            Type[] types = Type.getArgumentTypes(descriptor);
            for (Type type : types) {
                List<StackItem> items= pop(type.getSize());
                if(items.size()>0){
                    result.add(items.get(0));
                }
            }
        } else if (firstDescriptorChar == 'J' || firstDescriptorChar == 'D') {
            List<StackItem> items= pop(2);
            result.add(items.get(0));
        } else {
            result.add(pop());
        }
        return result;
    }

    private void execute(final int opcode, final int intArg, final String stringArg) {
        if (opcode == Opcodes.JSR || opcode == Opcodes.RET) {
            throw new IllegalArgumentException("JSR/RET are not supported");
        }
        StackItem value1;
        StackItem value2;
        StackItem value3;
        StackItem t4;
        switch (opcode) {
            case Opcodes.NOP:
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
            case Opcodes.GOTO:
            case Opcodes.RETURN:
                break;
            case Opcodes.ACONST_NULL:
                push(Opcodes.NULL);
                break;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                push(Opcodes.INTEGER);
                break;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
                push(Opcodes.FLOAT);
                break;
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case Opcodes.ILOAD:
            case Opcodes.FLOAD:
            case Opcodes.ALOAD:
                push(get(intArg));
                break;
            case Opcodes.LLOAD:
            case Opcodes.DLOAD:
                push(get(intArg));
                push(Opcodes.TOP);
                break;
            case Opcodes.LALOAD:
            case Opcodes.D2L:
                pop(2);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case Opcodes.DALOAD:
            case Opcodes.L2D:
                pop(2);
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case Opcodes.AALOAD:
                pop();
                value1 = pop();
                if (value1.type != null) {
                    pushDescriptor(value1.type.getDescriptor().substring(1), value1);
                } else {
                    push(value1.copy());
                }
                break;
            case Opcodes.ISTORE:
            case Opcodes.FSTORE:
            case Opcodes.ASTORE:
                value1 = pop();
                set(intArg, value1);
                if (intArg > 0) {
                    value2 = get(intArg - 1);
                    if (value2 != null && value2.getTypeSize() == 2) {
                        set(intArg - 1, TOP);
                    }
                }
                break;
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
                pop();
                value1 = pop();
                set(intArg, value1);
                set(intArg + 1, TOP);
                if (intArg > 0) {
                    value2 = get(intArg - 1);
                    if (value2.getTypeSize() == 2) {
                        set(intArg - 1, TOP);
                    }
                }
                break;
            case Opcodes.IASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
            case Opcodes.FASTORE:
            case Opcodes.AASTORE:
                List<StackItem> items=pop(3);
                if(items.size()==3){
                    StackItem eleItem = items.get(0);
                    StackItem arrayItem = items.get(2);
                    if(eleItem!=null && arrayItem!=null){
                        arrayItem.addTraces(eleItem.getTraces());
                    }
                }
                break;
            case Opcodes.LASTORE:
            case Opcodes.DASTORE:
                pop(4);
                break;
            case Opcodes.POP:
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
            case Opcodes.ATHROW:
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                pop();
                break;
            case Opcodes.ARETURN:
                StackItem item = pop();
                if (item != null) {
                    returnTrace.addAll(item.getTraces());
                }
                break;
            case Opcodes.POP2:
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.LRETURN:
            case Opcodes.DRETURN:
                pop(2);
                break;
            case Opcodes.DUP:
                value1 = pop();
                push(value1);
                push(value1);
                break;
            case Opcodes.DUP_X1:
                value1 = pop();
                value2 = pop();
                push(value1);
                push(value2);
                push(value1);
                break;
            case Opcodes.DUP_X2:
                value1 = pop();
                value2 = pop();
                value3 = pop();
                push(value1);
                push(value3);
                push(value2);
                push(value1);
                break;
            case Opcodes.DUP2:
                value1 = pop();
                value2 = pop();
                push(value2);
                push(value1);
                push(value2);
                push(value1);
                break;
            case Opcodes.DUP2_X1:
                value1 = pop();
                value2 = pop();
                value3 = pop();
                push(value2);
                push(value1);
                push(value3);
                push(value2);
                push(value1);
                break;
            case Opcodes.DUP2_X2:
                value1 = pop();
                value2 = pop();
                value3 = pop();
                t4 = pop();
                push(value2);
                push(value1);
                push(t4);
                push(value3);
                push(value2);
                push(value1);
                break;
            case Opcodes.SWAP:
                value1 = pop();
                value2 = pop();
                push(value1);
                push(value2);
                break;
            case Opcodes.IALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
            case Opcodes.IADD:
            case Opcodes.ISUB:
            case Opcodes.IMUL:
            case Opcodes.IDIV:
            case Opcodes.IREM:
            case Opcodes.IAND:
            case Opcodes.IOR:
            case Opcodes.IXOR:
            case Opcodes.ISHL:
            case Opcodes.ISHR:
            case Opcodes.IUSHR:
            case Opcodes.L2I:
            case Opcodes.D2I:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
                pop(2);
                push(Opcodes.INTEGER);
                break;
            case Opcodes.LADD:
            case Opcodes.LSUB:
            case Opcodes.LMUL:
            case Opcodes.LDIV:
            case Opcodes.LREM:
            case Opcodes.LAND:
            case Opcodes.LOR:
            case Opcodes.LXOR:
                pop(4);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case Opcodes.FALOAD:
            case Opcodes.FADD:
            case Opcodes.FSUB:
            case Opcodes.FMUL:
            case Opcodes.FDIV:
            case Opcodes.FREM:
            case Opcodes.L2F:
            case Opcodes.D2F:
                pop(2);
                push(Opcodes.FLOAT);
                break;
            case Opcodes.DADD:
            case Opcodes.DSUB:
            case Opcodes.DMUL:
            case Opcodes.DDIV:
            case Opcodes.DREM:
                pop(4);
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.LUSHR:
                pop(3);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case Opcodes.IINC:
                set(intArg, new StackItem(Type.INT_TYPE));
                break;
            case Opcodes.I2L:
            case Opcodes.F2L:
                pop();
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case Opcodes.I2F:
                pop();
                push(Opcodes.FLOAT);
                break;
            case Opcodes.I2D:
            case Opcodes.F2D:
                pop();
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case Opcodes.F2I:
            case Opcodes.ARRAYLENGTH:
            case Opcodes.INSTANCEOF:
                pop();
                push(Opcodes.INTEGER);
                break;
            case Opcodes.LCMP:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                pop(4);
                push(Opcodes.INTEGER);
                break;
            case Opcodes.NEW:
                pushDescriptor(Type.getObjectType(stringArg).getDescriptor());
                break;
            case Opcodes.NEWARRAY:
                pop();
                switch (intArg) {
                    case Opcodes.T_BOOLEAN:
                        pushDescriptor("[Z");
                        break;
                    case Opcodes.T_CHAR:
                        pushDescriptor("[C");
                        break;
                    case Opcodes.T_BYTE:
                        pushDescriptor("[B");
                        break;
                    case Opcodes.T_SHORT:
                        pushDescriptor("[S");
                        break;
                    case Opcodes.T_INT:
                        pushDescriptor("[I");
                        break;
                    case Opcodes.T_FLOAT:
                        pushDescriptor("[F");
                        break;
                    case Opcodes.T_DOUBLE:
                        pushDescriptor("[D");
                        break;
                    case Opcodes.T_LONG:
                        pushDescriptor("[J");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid array type " + intArg);
                }
                break;
            case Opcodes.ANEWARRAY:
                pop();
                pushDescriptor("[" + Type.getObjectType(stringArg));
                break;
            case Opcodes.CHECKCAST:
                StackItem item2 = pop();
                pushDescriptorWithItemList(Type.getObjectType(stringArg).getDescriptor(), Arrays.asList(item2), true);
                break;
            case Opcodes.MULTIANEWARRAY:
                pop(intArg);
                pushDescriptor(stringArg);
                break;
            default:
                throw new IllegalArgumentException("Invalid opcode " + opcode);
        }
    }

    public EnvContext getCurrentContext() {
        return currentContext;
    }

    public Set<Trace> getReturnTrace() {
        return returnTrace;
    }

    public AnalysisMethodInfo getAnalysisMethodInfo() {
        return analysisMethodInfo;
    }
}
