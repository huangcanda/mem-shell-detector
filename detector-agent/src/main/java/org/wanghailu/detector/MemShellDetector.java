package org.wanghailu.detector;

import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.ClassByteHolder;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.MapArgs;
import org.wanghailu.detector.risk.RiskClassCatcher;
import org.wanghailu.detector.risk.check.RiskCheckable;
import org.wanghailu.detector.util.ClassUtil;

import java.lang.instrument.Instrumentation;

public class MemShellDetector {
    
    private DetectorContext context;

    private RiskClassCatcher riskClassCatcher;
    
    public MemShellDetector(Instrumentation inst, MapArgs mapArgs) {
        this.context = new DetectorContext(inst,mapArgs);
        riskClassCatcher = new RiskClassCatcher(context);
    }
    
    public void doDetect() {
        riskClassCatcher.filterRiskClassOnServer();
        ClassUtil.onGetClassContext(context.getInst(), ()->doRiskCheck());
        LogUtils.info("处理结束！");
    }

    private void doRiskCheck(){
        for (RiskCheckable riskCheckable : context.getRiskCheckList()) {
            try{
                riskCheckable.check(context);
            }finally {
                ClassByteHolder.clean();
            }
        }
    }

}
