package com.mdt.views.tag;

import com.mdt.util.StringUtil;
import com.mdt.util.cache.DictionaryCacheUtil;
import org.stringtree.json.BufferErrorListener;
import org.stringtree.json.JSONValidator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 选择框基类
 * 
 * @ClassName: AbstractSelectTag
 * @Description: TODO
 * @author "PangLin"
 * @date 2015年12月1日 上午9:24:38
 *
 */
public abstract class AbstractSelectTag extends TagSupport {
	private String name;
	private String data;
	private String dicCode;
	private String selectedData;
	private String asc;
	private String cssClass;
	private String style;
	private String headerValue;
	private String headerText;
	private String validator;
	private String orderByText;
	private String onchange;
	private String multiple;

	public AbstractSelectTag() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		if(null==data||"".equals(data)){
			String dicCode = getDicCode();
			if(!(dicCode==null||"".equals(dicCode))){
				data = DictionaryCacheUtil.getDictionarieStrCache(dicCode);
			}else{
				data = "";
			}
		}
		
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	
	public String getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(String selectedData) {
		this.selectedData = selectedData;
	}

	public String getAsc() {
		return asc;
	}

	public void setAsc(String asc) {
		this.asc = asc;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getOrderByText() {
		return orderByText;
	}

	public void setOrderByText(String orderByText) {
		this.orderByText = orderByText;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public boolean validatorJSON(String paramString) {
		JSONValidator localJSONValidator = new JSONValidator(
				new BufferErrorListener());
		return localJSONValidator.validate(paramString);
	}

	public Map getOptionData(String paramString) throws JspException {
		if (StringUtil.stringIsNull(paramString)) {
			paramString = "{}";
		}
		Map option = new LinkedHashMap();
		if (paramString.startsWith("{")) {
			if (!paramString.endsWith("}")) {
				throw new JspException("data属性的数据格式不正确！");
			}
			String str = paramString.substring(1, paramString.length() - 1);
			if (!StringUtil.stringIsNull(str)) {
				String[] arrayOfString1 = str.split(",");
				for (int i = 0; i < arrayOfString1.length; i++) {
					String[] arrayOfString2 = arrayOfString1[i].split(":");
					if (arrayOfString2.length != 2) {
						throw new JspException("请用\":\"分隔键值对");
					}
					option.put(arrayOfString2[0], arrayOfString2[1]);
				}
			}
		} else {
			option = (Map) this.pageContext.getRequest().getAttribute(
					paramString);
			if (option == null) {
				throw new JspException("request中不存在" + paramString + "属性");
			}
		}
		return option;
	}

	public int doStartTag() throws JspException {
		try {
			this.pageContext.getOut().write(createSelectTag());
		} catch (IOException localIOException) {
			throw new JspException(localIOException);
		}
		return super.doStartTag();
	}

	protected abstract String createSelectTag() throws JspException;

	protected void createHeader(StringBuffer paramStringBuffer) {
		if ((getHeaderValue() != null) && (getHeaderText() != null)) {
			paramStringBuffer.append("<option value=\"" + getHeaderValue()
					+ "\">" + getHeaderText() + "</option>");
		} else if (getHeaderValue() != null) {
			paramStringBuffer.append("<option value=\"" + getHeaderValue()
					+ "\">请选择</option>");
		} else if (getHeaderText() != null) {
			paramStringBuffer.append("<option value=\"\">" + getHeaderText()
					+ "</option>");
		}
	}

	protected void createId(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getId())) {
			paramStringBuffer.append(" id=\"" + getId() + "\"");
		}
	}

	protected void createName(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getName())) {
			paramStringBuffer.append(" name=\"" + getName() + "\"");
		}
	}

	protected void createStyle(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getStyle())) {
			paramStringBuffer.append(" style=\"" + getStyle() + "\"");
		}
	}

	protected void createCssClass(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getCssClass())) {
			paramStringBuffer.append(" class=\"" + getCssClass() + "\"");
		}
	}

	protected void createOnChange(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getOnchange())) {
			paramStringBuffer.append(" onchange=\"" + getOnchange() + "\"");
		}
	}

	protected void createOrderAndShow(Map paramMap,
			StringBuffer paramStringBuffer) {
		String selectedVal = getSelectedData();

		if (!StringUtil.stringIsNull(getOrderByText())) {
			ArrayList valueList = new ArrayList(paramMap.keySet());
			if ("true".equalsIgnoreCase(getOrderByText())) {
				if ((!StringUtil.stringIsNull(getAsc()))
						&& ("false".equalsIgnoreCase(getAsc()))) {
					Collections.sort(valueList, new SelectComparatorByText(
							false));
				} else {
					Collections.sort(valueList,
							new SelectComparatorByText(true));
				}
			} else {
				if ((!StringUtil.stringIsNull(getAsc()))
						&& ("false".equalsIgnoreCase(getAsc()))) {
					Collections.sort(valueList, new SelectComparatorByValue(
							false));
				} else {
					Collections.sort(valueList, new SelectComparatorByValue(
							true));
				}
			}

			for (int i = 0; i < valueList.size(); i++) {
				String value = (String) valueList.get(i);
				if ((selectedVal != null) && (selectedVal.equals(value))) {
					paramStringBuffer.append("<option value=\"" + value
							+ "\" selected=\"selected\">" + paramMap.get(value)
							+ "</option>");
				} else {
					paramStringBuffer.append("<option value=\"" + value + "\">"
							+ paramMap.get(value) + "</option>");
				}
			}

		} else {
			Object[] arrayOfObject = paramMap.keySet().toArray();
			for (int i = 0; i < arrayOfObject.length; i++) {
				if ((selectedVal != null)
						&& (selectedVal.equals(arrayOfObject[i]))) {
					paramStringBuffer.append("<option value=\""
							+ arrayOfObject[i] + "\" selected=\"selected\">"
							+ paramMap.get(arrayOfObject[i]) + "</option>");
				} else {
					paramStringBuffer.append("<option value=\""
							+ arrayOfObject[i] + "\">"
							+ paramMap.get(arrayOfObject[i]) + "</option>");
				}
			}
		}

	}

	protected void createMultiple(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getMultiple())&&"true".equals(getMultiple())) {
			paramStringBuffer.append(" multiple=\"multiple\"");
		}
	}
	
	protected void createValidator(StringBuffer paramStringBuffer) {
		if (!StringUtil.stringIsNull(getValidator())) {
			paramStringBuffer.append(" validator=\"" + getValidator() + "\"");
		}
	}

}
