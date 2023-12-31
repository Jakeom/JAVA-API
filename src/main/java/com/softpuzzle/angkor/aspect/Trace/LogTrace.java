package com.softpuzzle.angkor.aspect.Trace;

public interface LogTrace {

    // 기존에 했던걸 인터페이스로
    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}