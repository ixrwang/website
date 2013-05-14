package name.ixr.website.tools.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 对类的操作工具包
 * @author IXR
 */
public class ClassUtils{
	/**
	 * 获取类的泛型类型
	 * @param cls 要获取的类
	 * @return 类的泛型类型
	 * @author IXR
	 */
	public static Type[] getActualTypeArguments(Class<?> cls){
		ParameterizedType type = ((ParameterizedType)cls.getGenericSuperclass());
		return type.getActualTypeArguments();
	}
	/**
	 * 获取指定调用次序的命名空间+方法名
	 * @param idx 层次
	 * @return
	 */
	public static String getStatementName(int idx){
	    Exception exception = new Exception();
	    StackTraceElement[] stackTraces = exception.getStackTrace();
	    if(idx > stackTraces.length -1){
	        return null;
	    }
	    StringBuilder statementName = new StringBuilder();
	    statementName.append(stackTraces[idx].getClassName());
	    statementName.append(".");
	    statementName.append(stackTraces[idx].getMethodName());
	    return statementName.toString();
	}
}
