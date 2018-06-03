package com.johnfnash.learn.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.johnfnash.learn.domain.JsonResult;
import com.johnfnash.learn.domain.User;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class UserController {

	// �����̰߳�ȫ��Map
	static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<Integer, User>());
	
	/**
	 * ����û�
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "�����û�", notes = "����User���󴴽��û�")
	@ApiImplicitParam(name = "user", value = "�û���ϸʵ�� User", required = true)
	@PostMapping("user")
	public ResponseEntity<JsonResult> add (@RequestBody User user){
		JsonResult r = new JsonResult();
		
		try {
			users.put(user.getId(), user);
			r.setResult(user.getId());
			r.setStatus("ok");
		} catch (Exception e) {
			r.setResult(e.getClass().getName() + ":" + e.getMessage());
			r.setStatus("error");
	
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(r);
	}
	
	@ApiOperation(value="��ȡ�û��б�", notes="��ȡ�û��б�")
	@GetMapping("users")
	public ResponseEntity<JsonResult> getUserList (){
		JsonResult r = new JsonResult();
		
		try {
			List<User> userList = new ArrayList<User>(users.values());
			r.setResult(userList);
			r.setStatus("ok");
		} catch (Exception e) {
			r.setResult(e.getClass().getName() + ":" + e.getMessage());
			r.setStatus("error");
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(r);
	}
	
}
