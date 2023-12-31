package com.gudokjoa5.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gudokjoa5.dto.GroupCreateDTO;
import com.gudokjoa5.dto.GroupDTO;
import com.gudokjoa5.dto.GroupJoinDTO;
import com.gudokjoa5.service.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="GROUP API", description="GROUP API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins="*", allowedHeaders = "*")
public class GroupController {

	@Autowired
	private final GroupService groupService;
	
	@Operation(operationId="groupsdetail", summary="그룹 정보 가져오기", description="하나의 그룹 정보 상세 내용을 제공합니다.")
	@GetMapping("/group/detail")
	public ResponseEntity <GroupDTO> getGroupById(long id) {	
		return groupService.getGroup(id);
	}
	
	@Operation(operationId="groupslist", summary="그룹 목록 가져오기", description=" 사용자가 가입한 그룹 목록을 제공합니다.")
	@GetMapping("/group/mylist")
	public ResponseEntity <List<GroupDTO>> getGroupListById(long id) {	
		return groupService.getGroupList(id);
	}
		
	@Operation(operationId="groupCreate", summary="그룹 생성하기", description="그룹을 생성합니다 ")
	@PostMapping("group/create")
	public ResponseEntity<Object> createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {	
		return groupService.insertGroup(groupCreateDTO);
	} 
	
	@Operation(operationId="groupJoin", summary="그룹 참하기", description="그룹에 참여합니다 ")
	@PostMapping("group/join")
	public ResponseEntity <Object> joinGroup(@RequestBody GroupJoinDTO groupJoinDTO) {	
		return groupService.joinGroup(groupJoinDTO);
	} 


}