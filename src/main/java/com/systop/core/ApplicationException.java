package com.systop.core;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 业务异常基类.带有错误代码与错误信息. 用户在生成异常时既可以直接赋予错误代码与错误信息.
 * 也可以只赋予错误代码与错误信息参数.如ErrorCode=ORDER.LACK_INVENTORY , errorArg=without EJB
 * 系统会从errors.properties中查出 ORDER.LACK_INVENTORY= Book <{0}> lack of inventory
 * 最后返回错误信息为 Book 《without EJB》 lack of inventory.
 * 
 * <b>important:</b><br/>
 * <code>ApplicationException</code>是一个CheckedException，所有caller都必须
 * catch这个异常。所以，只有在下面情况下才可以extends<code>ApplicationException</code>
 * <ul>
 * <li>确定调用者有能力处理这个Exception.</li>
 * <li>所有调用者都必须处理这个Exception，换句话说， Exception相当于方法的另外一个返值。</li>
 * <li>抛出(throws)这个异常，不会造成调用者太大的代码上的负担。</li>
 * <li>以后不会修改这个异常。</li>
 * </ul>
 * 
 * @author nice
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

	/**
	 * Logger
	 */
	protected final Log log = LogFactory.getLog(ApplicationException.class);
	
	/**
	 * The default error code.
	 */
	public static final String UNKNOW_ERROR = "UNKNOW_ERROR";

	/**
	 * 兼容纯错误信息，不含error code,errorArgs的情况.
	 */
	private final String errorMessage;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public ApplicationException() {
		super();
		errorMessage = null;
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public ApplicationException(final String msgPattern, final Object... args) {
		this.errorMessage = MessageFormat.format(msgPattern, args);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
		this.errorMessage = null;
	}

	/**
	 * 获得出错信息.
	 * 
	 * @return 格式化后的错误信息
	 */
	public final String getMessage() {
		return errorMessage;
	}

}
