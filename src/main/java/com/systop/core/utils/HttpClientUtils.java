package com.systop.core.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientUtils {

	/**
	 * 提交get请求
	 * @param requestUrl
	 * @return
	 */
	public static JsonNode doGetRequest(String requestUrl){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(requestUrl);
		CloseableHttpResponse response = null;
		JsonNode root = null;
		try {
			response = httpclient.execute(httpget);
			String reqponseEntity = EntityUtils.toString(response.getEntity(), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();  
			root = mapper.readTree(reqponseEntity);  
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// 需要关闭请求链接
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return root;
	}
	
	
	/**
	 * 提交POST请求,同时需要组织提交的数据包
	 * @param requestUrl
	 * @return
	 */
	public static JsonNode doPostRequest(String requestUrl, HttpEntity entity){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(requestUrl);
		CloseableHttpResponse response = null;
		JsonNode root = null;
		try {
			httpPost.setEntity(entity);
			response = httpclient.execute(httpPost);
			String reqponseEntity = EntityUtils.toString(response.getEntity(), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();  
			root = mapper.readTree(reqponseEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}
}
