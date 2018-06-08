package com.lmh.soap.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private AuthRepositroy authRepository;

	public List<Auth> getAllArticles(){
		List<Auth> list = new ArrayList<>();
		authRepository.findAll().forEach(e -> list.add(e));
		return list;
	}
	

	
	public String getSecret(String name){  
		String userRecords=null;
		List<Auth> allRecords = getAllArticles();
		System.out.println("value is:"+name);
		for(Auth ar:allRecords) {
			if(ar.getUser_name().equals(name)) {
				userRecords=ar.getSecret(); 
			}
		}
        return userRecords;  
    } 
	
	
}
