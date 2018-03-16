package com.allen.audi.http;

import java.io.Serializable;

/**
 * @className : BaseResult.java
 * @classDescription : 网络请求返回对象公共抽象类
 * @author : AIDAN SU
 * @createTime : 2014-4-1
 *
 */
public class BaseResponse implements Serializable {

	protected boolean isSuccess;
	protected String result;
	protected String message;

	/**
	 * 设置网络请求返回的结果编码
     *
	 * @param code [200：成功；400：失败]
	 * @throws Exception
	 */
	public void setResponseCode( boolean code )throws Exception {
		isSuccess = code;
	}
	
	/**
	 * 设置网络请求返回的结果
     *
	 * @param result JSON数据
	 * @throws Exception
	 */
	public void setResponseResult( String result )throws Exception {
		this.result = result;
	}

	/**
	 * 设置网络请求返回的消息
     *
	 * @param message 字符串
	 * @throws Exception
	 */
	public void setResponseMessage( String message )throws Exception {
		this.message = message;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "BaseResponse{" +
				"isSuccess=" + isSuccess +
				", result='" + result + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
