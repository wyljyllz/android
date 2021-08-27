package com.example.dto;



/**
 * ajax回应
 * @author 徐骏
 * 2019-7-12
 */
public class MyResponse
{
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 状态码，默认20000表示OK
	 */
	public int code = ResponseCode.OK;
	public String error;
	public Object data = new Object();
}
