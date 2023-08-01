package com.softpuzzle.angkor.utility;

import com.softpuzzle.angkor.http.request.Paginginfo;
import org.apache.commons.lang3.ObjectUtils;

public class PagingUtil {
	/**
	 * 페이징 기본 셋팅 - sort
	 */
	public static Paginginfo setPaging(Paginginfo pagingInfo) {
		if(ObjectUtils.isEmpty(pagingInfo)) {
			Paginginfo newPagingInfo = new Paginginfo();
			pagingInfo = newPagingInfo;
		}

		// 페이징 페이지 번호가 존재하지 않을 때
		if(ObjectUtils.isEmpty(pagingInfo) || pagingInfo.getPage() == null) {
			pagingInfo.setPage(1);
		}

		// 페이징 사이즈가 존재하지 않을 때
		if(ObjectUtils.isEmpty(pagingInfo) || pagingInfo.getPagingSize() == null) {
			pagingInfo.setPagingSize(20);
		}

		// 오프셋 : (페이지 번호 - 1) * 페이지 사이즈
		pagingInfo.setOffset(pagingInfo.getPagingSize() * (pagingInfo.getPage() - 1));

		return pagingInfo;
	}
}
