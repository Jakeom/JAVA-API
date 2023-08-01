
package com.softpuzzle.angkor.controller.api.code;

import com.softpuzzle.angkor.http.request.ORGReq001;
import com.softpuzzle.angkor.http.request.ORGReq002;
import com.softpuzzle.angkor.http.request.ORGReq003;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.code.CodeService;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = {"코드 관리 (admin)"}, description = "Code")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ankochat/api/code")
@Slf4j
public class CodeController {
	@Autowired
	private CodeService service;

	@ApiOperation(value = "그룹코드 리스트 조회", notes = "그룹코드 리스트 조회한다.")
	@GetMapping("/getCodeAllList")
	@ResponseBody
	public ResCommDTO getCodeAllList(@RequestBody ORGReq001 rdto) {
		
		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getCodeAllList(CommonUtil.Object2Hashmap(rdto));
		log.info("User list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "개별코드 조회", notes = "개별코드 리스트 조회한다.")
	@GetMapping("/getListByCode")
	@ResponseBody
	public ResCommDTO getListByCode(@RequestBody ORGReq001 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getListByCode(CommonUtil.Object2Hashmap(rdto));
		log.info("User list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "코드 세부정보", notes = "입력한 그룹 코드의 세부 코드들의 리스트를 조회한다. 그룹코드는 G001 부터 시작한다.")
	@GetMapping("/info/{code}")
	@ResponseBody
	public ResCommDTO info(@PathVariable(name = "code") ORGReq002 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getInfoCode(CommonUtil.Object2Hashmap(rdto));
		log.info("User list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "코드 등록", notes = "그룹 코드를 등록한다.")
	@PostMapping("/add")
	@ResponseBody
	public ResTrDTO addGroupCode(@RequestBody ORGReq003 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.addGroupCode(CommonUtil.Object2Hashmap(rdto));
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "코드 삭제", notes = "그룹 코드 삭제.")
	@DeleteMapping("/delete/{code}")
	@ResponseBody
	public ResTrDTO deleteGroupCode(@PathVariable String code) {

		log.info("request params : ________________ :" +code);
		HashMap<String, Object> params = new HashMap<>();
		params.put("code", code);
		ResTrDTO res  = service.deleteGroupCode(params);
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}
	
}