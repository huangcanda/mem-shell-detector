package org.wanghailu.detector.model;

import org.wanghailu.detector.analyze.AnalysisCodeManager;
import org.wanghailu.detector.analyze.rule.MethodRuleConfig;
import org.wanghailu.detector.analyze.rule.MethodRuleOnContext;
import org.wanghailu.detector.risk.check.RiskCheckable;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.stream.Collectors;


public class DetectorContext {
    
    private Instrumentation inst;
    
    private MapArgs mapArgs;
    
    private Class[] allLoadedClasses;
    
    private Map<String, Class> classNameMap;
    
    private List<MethodRuleOnContext> commonRuleList = new ArrayList<>();
    
    private Map<String, List<MethodRuleOnContext>> commonRuleMap = new HashMap<>();
    
    private List<MethodRuleOnContext> sinkRuleList = new ArrayList<>();
    
    private Map<String, List<MethodRuleOnContext>> sinkRuleMap = new HashMap<>();
    
    private List<RiskCheckable> riskCheckList = new ArrayList<>();
    
    private AnalysisCodeManager analysisCodeManager;

    private int maxLengthOnSinkMethodName = 0;
    
    public DetectorContext(Instrumentation inst, MapArgs mapArgs) {
        this.inst = inst;
        this.mapArgs = mapArgs;
        this.allLoadedClasses = inst.getAllLoadedClasses();
        this.classNameMap = Arrays.stream(allLoadedClasses).collect(Collectors
                .groupingBy(Class::getName, Collectors.collectingAndThen(Collectors.toList(), e -> e.get(0))));
        
        initMethodRule(MethodRuleConfig.ruleList, commonRuleList, commonRuleMap);
        initMethodRule(MethodRuleConfig.sinkRuleList, sinkRuleList, sinkRuleMap);

        for (MethodRuleOnContext ruleOnContext : sinkRuleList) {
            int length= ruleOnContext.getRule().getMethodName().length();
            if(length > maxLengthOnSinkMethodName){
                maxLengthOnSinkMethodName = length;
            }
        }

        analysisCodeManager = new AnalysisCodeManager(this);
    }
    
    private void initMethodRule(List<MethodRule> rules, List<MethodRuleOnContext> ruleListOnContext,
            Map<String, List<MethodRuleOnContext>> ruleMapOnContext) {
        List<MethodRuleOnContext> commonRuleList = new ArrayList<>();
        Map<String, List<MethodRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(x -> x.getClassName()));
        for (String classType : ruleMap.keySet()) {
            String className = classType.replace("/", ".");
            Class baseClass = classNameMap.get(className);
            if (baseClass == null) {
                continue;
            }
            List<Class> childClassList = classNameMap.values().stream().filter(baseClass::isAssignableFrom)
                    .collect(Collectors.toList());
            for (Class childClass : childClassList) {
                for (MethodRule methodRule : ruleMap.get(classType)) {
                    //是否继续判断，子类没有实现父类的方法的情况
                    commonRuleList.add(new MethodRuleOnContext(methodRule, childClass));
                }
            }
        }
        Map<String, List<MethodRuleOnContext>> commonRuleMap = commonRuleList.stream()
                .collect(Collectors.groupingBy(x -> x.getClazzType() + "." + x.getRule().getMethodName()));
        ruleListOnContext.addAll(commonRuleList);
        ruleMapOnContext.putAll(commonRuleMap);
    }
    
    public DetectorContext() {
    }
    
    public Class[] getAllLoadedClasses() {
        return allLoadedClasses;
    }
    
    public Class getLoadedClass(String className) {
        return classNameMap.get(className);
    }

    
    public void addRiskCheckAble(RiskCheckable riskCheckable) {
        riskCheckList.add(riskCheckable);
    }
    
    public void addRiskClassResult(RiskClassResult riskClassResult) {

    }
    
    public List<RiskCheckable> getRiskCheckList() {
        return riskCheckList;
    }
    
    public MapArgs getMapArgs() {
        return mapArgs;
    }
    
    public Instrumentation getInst() {
        return inst;
    }
    
    public Map<String, Class> getClassNameMap() {
        return classNameMap;
    }
    
    public List<MethodRuleOnContext> getCommonRuleList() {
        return commonRuleList;
    }
    
    public Map<String, List<MethodRuleOnContext>> getCommonRuleMap() {
        return commonRuleMap;
    }
    
    public List<MethodRuleOnContext> getSinkRuleList() {
        return sinkRuleList;
    }
    
    public Map<String, List<MethodRuleOnContext>> getSinkRuleMap() {
        return sinkRuleMap;
    }

    public int getMaxLengthOnSinkMethodName() {
        return maxLengthOnSinkMethodName;
    }

    public AnalysisCodeManager getAnalysisCodeManager() {
        return analysisCodeManager;
    }
}
