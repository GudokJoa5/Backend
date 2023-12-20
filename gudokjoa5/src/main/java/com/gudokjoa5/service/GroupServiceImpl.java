package com.gudokjoa5.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gudokjoa5.dao.GroupDao;
import com.gudokjoa5.dao.SubscribeDao;
import com.gudokjoa5.dao.UserDao;
import com.gudokjoa5.dto.GroupCreateDTO;
import com.gudokjoa5.dto.GroupDTO;
import com.gudokjoa5.dto.GroupJoinDTO;
import com.gudokjoa5.dto.SubscribeDTO;
import com.gudokjoa5.model.Group;
import com.gudokjoa5.model.User;
import com.gudokjoa5.model.UserGroup;

@Service
public class GroupServiceImpl implements GroupService {
	
private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SubscribeDao subscribeDao;
	
	@Override
	@Transactional
	public ResponseEntity<GroupDTO> getGroup(long id) {
		GroupDTO groupDTO = groupDTO(id);		
		return new ResponseEntity<GroupDTO> (groupDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<GroupDTO>> getGroupList(long id) {
		List<GroupDTO> groupDTOList = new LinkedList<>();
		try {
			List<Group> groupList = groupDao.getGroupList(id);
			for(Group group: groupList) {
				groupDTOList.add(groupDTO(group.getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<GroupDTO>> (groupDTOList, HttpStatus.OK);
	}
	
	@Transactional
	private GroupDTO groupDTO(long id) {
		GroupDTO groupDTO = null;
		try {
			Group group = null;
			List<User> users = null;
			SubscribeDTO subscribeDTO = null;
			User leaderUser = null;
			group = groupDao.getGroup(id);
			subscribeDTO = subscribeDao.getSubscribeDetail(group.getSubscribeId());
			users = userDao.getUserByGroupId(group.getId());
			leaderUser = userDao.selectUser(group.getLeaderUser());

			groupDTO = new GroupDTO(
						group.getId(),
						group.getGroupName(),
						group.getBillingDate(),
						leaderUser.getUsername(),
						subscribeDTO,
						users
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupDTO;
	}

	@Override
	@Transactional
	public ResponseEntity<String> insertGroup(GroupCreateDTO groupCreateDTO) {
		String invitationCode = null;
		
		try {
			Group group = null;
			UserGroup userGroup = null;
			SubscribeDTO subscribeDTO = subscribeDao.getSubscribeByName(groupCreateDTO.getGroupName()); 			
			group = new Group(
					0,
					subscribeDTO.getServiceId(),
					groupCreateDTO.getGroupAccount(),
					groupCreateDTO.getLeaderUser(),
					1,
					groupCreateDTO.getGroupName(),
					groupCreateDTO.getBillingDate(),
					invitationCode
			);
			
			groupDao.insertGroup(group);
			
			Group createdGroup = groupDao.getGroupByGroupName(groupCreateDTO.getGroupName());
			userGroup = new UserGroup(
					0,
					groupCreateDTO.getLeaderUser(),
					createdGroup.getId(),
					1
			);
			
			groupDao.insertUserGroup(userGroup);
			invitationCode = randomString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String> (invitationCode, HttpStatus.OK);	
	}
	
	private String randomString() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 16;
		Random random = new Random();

		String generatedString = random.ints(leftLimit,rightLimit + 1)
		  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  .limit(targetStringLength)
		  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  .toString();

		return generatedString;
	}

	@Override
	@Transactional
	public ResponseEntity<String> joinGroup(GroupJoinDTO groupJoinDTO) {
		String msg = "";
		UserGroup userGroup = null;
		try {
			Group createdGroup = groupDao.getGroupByInvitationCode(groupJoinDTO.getInvitationCode());
			if(createdGroup == null){ 
				return null;
			}
			
			userGroup = new UserGroup(
					0,
					groupJoinDTO.getId(),
					createdGroup.getId(),
					1
			);
			
			groupDao.insertUserGroup(userGroup);
			msg = "그룹 들어가기 성공 ";			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String> (msg, HttpStatus.OK);
	}

}
