package org.wanghailu.detector.risk.handler;

import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.RiskClassResult;
import org.wanghailu.detector.model.RiskType;
import org.wanghailu.detector.risk.check.RiskClassCheck;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class RiskClassOnServerHandler {
    
    private DetectorContext detectorContext;
    
    public RiskClassOnServerHandler(DetectorContext detectorContext) {
        this.detectorContext = detectorContext;
    }
    
    public void doHandle() {
        for (Class allLoadedClass : detectorContext.getAllLoadedClasses()) {
            if(allLoadedClass.getName().startsWith("net.rebeyond.")||allLoadedClass.getName().startsWith("com.metasploit.")){
                detectorContext.addRiskCheckAble(new RiskClassCheck(allLoadedClass));
                detectorContext.addRiskClassResult(new RiskClassResult(allLoadedClass, RiskType.sinkClass));
            }
        }
    }
}
