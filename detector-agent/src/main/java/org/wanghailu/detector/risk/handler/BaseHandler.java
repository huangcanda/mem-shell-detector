package org.wanghailu.detector.risk.handler;

import org.wanghailu.detector.model.DetectorContext;

public abstract class BaseHandler {

    protected DetectorContext detectorContext;

    protected String info;

    public BaseHandler(DetectorContext detectorContext,String info) {
        this.detectorContext = detectorContext;
        this.info = info;
    }


    public boolean isEnabled(){
        return true;
    }

    public abstract void doHandle();

    public String getInfo() {
        return info;
    }
}
