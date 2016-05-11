package com.systop.core.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings({ "rawtypes", "serial" })
public class ConstantUtil extends HashMap implements Map {
	
	public ConstantUtil(){
        
    }  
  
    public Object getValue(String constantPath)throws Exception{  
        String classPath = StringUtils.substringBeforeLast(constantPath, ".");  
        String fieldName = StringUtils.substringAfterLast(constantPath, ".");  
        Field field = Class.forName(classPath).getField(fieldName);
        return field.get( null );  
    }  
      
  
    @Override  
    public Object get( Object key ) {
        try {  
            return getValue( key.toString() );   
        }  
        catch (Exception e) {  
            throw new IllegalArgumentException( " 找不到相关的变量: " + e.getMessage());  
        }  
    }  
}
