package org.wanghailu.detector.risk.handler;

import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.RiskClassResult;
import org.wanghailu.detector.model.RiskType;
import org.wanghailu.detector.risk.check.JspServletCheck;

import javax.servlet.jsp.HttpJspPage;

public class JspOnServerHandler extends BaseHandler {

    public JspOnServerHandler(DetectorContext detectorContext,String info) {
        super(detectorContext,info);
    }

    @Override
    public boolean isEnabled(){
        return detectorContext.getLoadedClass("javax.servlet.jsp.HttpJspPage") != null;
    }

    @Override
    public void doHandle() {
        for (Class allLoadedClass : detectorContext.getAllLoadedClasses()) {
            if(HttpJspPage.class.isAssignableFrom(allLoadedClass)&&allLoadedClass.getName().endsWith("_jsp")){
                detectorContext.addRiskCheckAble(new JspServletCheck(allLoadedClass));
                detectorContext.addRiskClassResult(new RiskClassResult(allLoadedClass, RiskType.jspServlet));
            }
        }
    }
}
