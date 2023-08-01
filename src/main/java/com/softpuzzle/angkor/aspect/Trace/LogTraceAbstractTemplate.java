package com.softpuzzle.angkor.aspect.Trace;

//LogTraceAbstractTemplate
// <T>: 타입에 대한 정보를 객체를 생성하는 시점으로 미룸
public abstract class LogTraceAbstractTemplate<T> {

	private final LogTrace trace;

	protected LogTraceAbstractTemplate(LogTrace trace) {
		this.trace = trace;
	}

	public Object execute(String message) {

		TraceStatus status = null;
		// 정상 실행시
		try { 
			status = trace.begin(message);

			// 로직 호출
			Object result = call();
			trace.end(status);
			return result;
		} catch (Exception e) { // 예외 발생시
			trace.exception(status, e); // -> 예외를 먹음(?)
			throw e; // (먹었으니 정상 흐름이 되어버림 그래서 )예외를 꼭 다시 던져주어야 한다.
		}
	}

	protected abstract Object call();
}