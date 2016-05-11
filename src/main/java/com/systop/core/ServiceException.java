package com.systop.core;

import java.text.MessageFormat;

/**
 * Service层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String messagePattern, Object... args) {
		super(MessageFormat.format(messagePattern, args));
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
