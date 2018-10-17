package com.github.cxt.MyJavaAgent.ByteBuddy;


public interface EnhancedInstance {
    Object getTestDynamicField();

    void setTestDynamicField(Object value);
}
