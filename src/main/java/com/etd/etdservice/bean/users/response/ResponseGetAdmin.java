package com.etd.etdservice.bean.users.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetAdmin {
	private String userName;
	private String phone;
	private String email;
}