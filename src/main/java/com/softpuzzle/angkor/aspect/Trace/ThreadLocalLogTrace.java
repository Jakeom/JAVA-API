package com.softpuzzle.angkor.aspect.Trace;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<X-";

	private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

	@Override
	public TraceStatus begin(String message) {
		syncTraceId();
		TraceId traceId = traceIdHolder.get();

		Long startTimeMs = System.currentTimeMillis();

		// 로그 출력
		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	private void syncTraceId() {
		TraceId traceId = traceIdHolder.get();

		if (traceId == null) { // 맨 처음이라면 새로 생성
			log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------->");
			traceIdHolder.set(new TraceId());
		} else {
			traceIdHolder.set(traceId.createNextId());
		}
	}

	@Override
	public void end(TraceStatus status) {
		complete(status, null);
	}

	@Override
	public void exception(TraceStatus status, Exception e) {
		complete(status, e);
	}

	private void complete(TraceStatus status, Exception e) {
		Long stopTimeMs = System.currentTimeMillis();
		float resultTimeMs = (float)(stopTimeMs - status.getStartTimeMs()) / (float)1000;
		
		TraceId traceId = status.getTraceId();

		if (e == null) {
			log.info("[{}] {}{} 소요시간(time) = {} 초", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
		} else {
			log.info("[{}] {}{} 소요시간(time) = {} 초 ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
		}

		
		
		releaseTraceId();
	}

	private void releaseTraceId() {
		TraceId traceId = traceIdHolder.get();
		if (traceId.isFirstLevel()) { // 들어갔다 나오는. 마지막 로그인 상태
			traceIdHolder.remove(); // remove 필요 (destroy)
			if(traceIdHolder.get() == null) {
				log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------->");
			}
		} else {
			traceIdHolder.set(traceId.createPreviousId());
		}
		
	}

	private static String addSpace(String prefix, int level) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < level; i++) {
			sb.append((i == level - 1) ? "     |" + prefix : "|   ");
		}

		return sb.toString();
	}
}