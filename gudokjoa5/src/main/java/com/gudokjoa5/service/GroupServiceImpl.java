package com.gudokjoa5.service;

import java.util.LinkedList;
import java.util.List;

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
import com.gudokjoa5.dto.GroupDTO;
import com.gudokjoa5.dto.SubscribeDTO;
import com.gudokjoa5.model.Group;
import com.gudokjoa5.model.User;

@Service
public class GroupServiceImpl implements GroupService {
	
private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GroupDao groupDao; // Dao 객체
	
	@Autowired
	private UserDao userDao; // Dao 객체
	
	@Autowired
	private SubscribeDao subscribeDao; // Dao 객체
	
	@Override
	@Transactional
	public ResponseEntity<GroupDTO> getGroup(long id) {
		GroupDTO groupDTO = groupDTO(id);
//		try {
//			Group group = null;
//			List<User> users = null;
//			SubscribeDTO subscribeDTO = null;
//			User leaderUser = null;
//			group = groupDao.getGroup(id);
//			subscribeDTO = subscribeDao.getSubscribeDetail(group.getSubscribeserviceId());
//			users = userDao.getUserByGroupId(group.getId());
//			leaderUser = userDao.selectUser(group.getLeaderUser());
//
//			groupDTO = new GroupDTO(
//						group.getId(),
//						group.getGroupName(),
//						group.getBilligDate(),
//						leaderUser.getUsername(),
//						subscribeDTO,
//						users
//					);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return new ResponseEntity<GroupDTO> (groupDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<GroupDTO>> getGroupList() {
		List<GroupDTO> groupDTOList = new LinkedList<>();
		try {
			List<Group> groupList = groupDao.getGroupList();
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
			subscribeDTO = subscribeDao.getSubscribeDetail(group.getSubscribeserviceId());
			users = userDao.getUserByGroupId(group.getId());
			leaderUser = userDao.selectUser(group.getLeaderUser());

			groupDTO = new GroupDTO(
						group.getId(),
						group.getGroupName(),
						group.getBilligDate(),
						leaderUser.getUsername(),
						subscribeDTO,
						users
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupDTO;
	}

}
