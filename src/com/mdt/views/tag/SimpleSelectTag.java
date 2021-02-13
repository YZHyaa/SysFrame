package com.mdt.views.tag;

import com.mdt.util.StringUtil;

import javax.servlet.jsp.JspException;
import java.util.Map;


public class SimpleSelectTag extends AbstractSelectTag {
	
	private String size;
	
	public String getSize()
	  {
	    return this.size;
	  }
	  
	  public void setSize(String size)
	  {
	    this.size = size;
	  }
	
	public SimpleSelectTag() {}

	@Override
	protected String createSelectTag() throws JspException {
		Map localMap = getOptionData(getData());
	    StringBuffer stringBuffer = new StringBuffer();
	    stringBuffer.append("<select");
	    createId(stringBuffer);
	    createName(stringBuffer);
	    createCssClass(stringBuffer);
	    createStyle(stringBuffer);
	    createOnChange(stringBuffer);
	    createMultiple(stringBuffer);
	    createValidator(stringBuffer);
	    
	    
	    if (!StringUtil.stringIsNull(getSize())) {
	      if ("all".equalsIgnoreCase(getSize()))
	      {
	        stringBuffer.append(" size=\"" + localMap.size() + "\"");
	      }
	      else
	      {
	        int i = 1;
	        try
	        {
	          i = Integer.parseInt(getSize());
	        }
	        catch (NumberFormatException localNumberFormatException)
	        {
	          throw new JspException("simpleSelect标签中size属性的值为数字或字符串“all”");
	        }
	        stringBuffer.append(" size=\"" + i + "\"");
	      }
	    }
	    stringBuffer.append(">");
	    createHeader(stringBuffer);
	    createOrderAndShow(localMap, stringBuffer);
	    stringBuffer.append("</select>");
	    return stringBuffer.toString();
	}

}
