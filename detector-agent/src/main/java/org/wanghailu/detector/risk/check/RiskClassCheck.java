package org.wanghailu.detector.risk.check;

import org.wanghailu.detector.model.DetectorContext;

public class RiskClassCheck implements RiskCheckable {
    
    private Class clazz;
    
    public RiskClassCheck(Class clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public void check(DetectorContext detectorContext) {
        detectorContext.getAnalysisCodeManager().startDoAnalysis(clazz, null, null);
    }
}
