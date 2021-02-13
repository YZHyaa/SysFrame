package com.mdt.entity;

import com.mdt.util.Const;
import com.mdt.util.PageData;
import com.mdt.util.Tools;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class Page {

    private long showCount; //每页显示记录数
    private long totalPage;        //总页数
    private long totalResult;    //总记录数
    private long currentPage;    //当前页
    private long currentResult;    //当前记录起始索引
    private boolean entityOrField;    //true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
    private String pageStr;        //最终页面显示的底部翻页导航，详细见：getPageStr();
    private PageData pd = new PageData();
    private int draw;


    public Page() {
        try {
            this.showCount = Integer.parseInt(Tools.readTxtFile(Const.PAGE));
        } catch (Exception e) {
            this.showCount = 15;
        }
    }

    /**
     * 使用dataTables控件时，封装从页面传入的参数
     *
     * @param pd，获取页面参数的PageData
     * @param queryTag，默认为'q_'，查询参数前缀，为区分查询参数与分页参数，需要传入到sql中的查询条件增加一个前缀
     * @return
     * @author panglin
     */
    public static Page parseDataTable2Page(PageData pd, String queryTag) {
        Page page = new Page();
        if (queryTag == null || "".equals(queryTag))
            queryTag = "q_";

        Iterator<String> it = pd.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.startsWith(queryTag)) {
                page.getPd().put(key.substring(2).toUpperCase(), pd.get(key));
            }
        }

        page.setShowCount(Integer.parseInt(pd.get("length").toString()));
        page.setDraw(Integer.parseInt(pd.get("draw").toString()));
        page.setCurrentResult(Integer.parseInt(pd.get("start").toString()));
        page.setCurrentPage(Long.parseLong(pd.get("start").toString()) / page.getShowCount() + 1);
        page.setEntityOrField(true);
        return page;
    }

    /**
     * 使用bootstramp table控件时，封装从页面传入的参数
     *
     * @param pd，获取页面参数的PageData
     * @return
     * @throws UnsupportedEncodingException
     * @author panglin
     */
    public static Page parseBootstrmpTable2Page(PageData pd) {
        Page page = new Page();
        Iterator<String> it = pd.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            try {
                if (StringUtils.isNotBlank(pd.get(key) + "")) {
                    pd.put(key, URLDecoder.decode(URLDecoder.decode(pd.get(key) + "", "UTF-8"), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        page.setPd(pd);

        page.setShowCount(Integer.parseInt((pd.get("limit") == null ? "100000" : pd.get("limit")).toString()));
        //page.setDraw(Integer.parseInt(pd.get("draw").toString()));
        page.setCurrentResult(Integer.parseInt((pd.get("offset") == null ? "0" : pd.get("offset")).toString()));
        page.setCurrentPage(Long.parseLong((pd.get("offset") == null ? "0" : pd.get("offset")).toString()) / page.getShowCount() + 1);
        page.setEntityOrField(true);
        return page;
    }

    /**
     * 将查询结果与分页信息封装为dataTable认可的结构
     *
     * @param page，保存了分页信息
     * @param dbResult，List<Map>结构
     * @return
     * @author panglin
     */
    public static Map parsePage2DataTable(Page page, List dbResult) {
        if (dbResult == null)
            dbResult = new ArrayList();

        Map dataTable = new HashMap();
        dataTable.put("draw", page.getDraw());
        dataTable.put("recordsTotal", page.getTotalResult());
        dataTable.put("recordsFiltered", page.getTotalResult());
        dataTable.put("data", dbResult);

        return dataTable;
    }

    /**
     * 将查询结果与分页信息封装为bootstramp table认可的结构
     *
     * @param page，保存了分页信息
     * @param dbResult，List<Map>结构
     * @return
     * @author panglin
     */
    public static Map parsePage2BootstrmpTable(Page page, List dbResult) {
        if (dbResult == null)
            dbResult = new ArrayList();
        Map dataTable = new HashMap();
        dataTable.put("total", page.getTotalResult());
        dataTable.put("rows", dbResult);
        return dataTable;
    }

    public long getTotalPage() {
        if (totalResult % showCount == 0)
            totalPage = totalResult / showCount;
        else
            totalPage = totalResult / showCount + 1;
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(long totalResult) {
        this.totalResult = totalResult;
    }

    public long getCurrentPage() {
        if (currentPage <= 0)
            currentPage = 1;
        if (currentPage > getTotalPage())
            currentPage = getTotalPage();
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageStr() {
        StringBuffer sb = new StringBuffer();
        if (totalResult > 0) {
            sb.append("	<ul>\n");
            if (currentPage == 1) {
                sb.append("	<li><a>共<font color=red>" + totalResult + "</font>条</a></li>\n");
                sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
                sb.append("	<li><a>首页</a></li>\n");
                sb.append("	<li><a>上页</a></li>\n");
            } else {
                sb.append("	<li><a>共<font color=red>" + totalResult + "</font>条</a></li>\n");
                sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(1)\">首页</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(" + (currentPage - 1) + ")\">上页</a></li>\n");
            }
            int showTag = 5;//分页标签显示数量
            long startTag = 1;
            if (currentPage > showTag) {
                startTag = currentPage - 1;
            }
            long endTag = startTag + showTag - 1;
            for (long i = startTag; i <= totalPage && i <= endTag; i++) {
                if (currentPage == i)
                    sb.append("<li><a><font color='#808080'>" + i + "</font></a></li>\n");
                else
                    sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(" + i + ")\">" + i + "</a></li>\n");
            }
            if (currentPage == totalPage) {
                sb.append("	<li><a>下页</a></li>\n");
                sb.append("	<li><a>尾页</a></li>\n");
            } else {
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(" + (currentPage + 1) + ")\">下页</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(" + totalPage + ")\">尾页</a></li>\n");
            }
            sb.append("	<li><a>第" + currentPage + "页</a></li>\n");
            sb.append("	<li><a>共" + totalPage + "页</a></li>\n");


            sb.append("	<li><select title='显示条数' style=\"width:55px;float:left;\" onchange=\"changeCount(this.value)\">\n");
            sb.append("	<option value='" + showCount + "'>" + showCount + "</option>\n");
            sb.append("	<option value='10'>10</option>\n");
            sb.append("	<option value='20'>20</option>\n");
            sb.append("	<option value='30'>30</option>\n");
            sb.append("	<option value='40'>40</option>\n");
            sb.append("	<option value='50'>50</option>\n");
            sb.append("	<option value='60'>60</option>\n");
            sb.append("	<option value='70'>70</option>\n");
            sb.append("	<option value='80'>80</option>\n");
            sb.append("	<option value='90'>90</option>\n");
            sb.append("	<option value='99'>99</option>\n");
            sb.append("	</select>\n");
            sb.append("	</li>\n");


            sb.append("</ul>\n");
            sb.append("<script type=\"text/javascript\">\n");

            //换页函数
            sb.append("function nextPage(page){");
            sb.append(" top.jzts();");
            sb.append("	if(true && document.forms[0]){\n");
            sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
            sb.append("		if(url.indexOf('?')>-1){url += \"&" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		else{url += \"?" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		url = url + page + \"&" + (entityOrField ? "showCount" : "page.showCount") + "=" + showCount + "\";\n");
            sb.append("		document.forms[0].action = url;\n");
            sb.append("		document.forms[0].submit();\n");
            sb.append("	}else{\n");
            sb.append("		var url = document.location+'';\n");
            sb.append("		if(url.indexOf('?')>-1){\n");
            sb.append("			if(url.indexOf('currentPage')>-1){\n");
            sb.append("				var reg = /currentPage=\\d*/g;\n");
            sb.append("				url = url.replace(reg,'currentPage=');\n");
            sb.append("			}else{\n");
            sb.append("				url += \"&" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";\n");
            sb.append("			}\n");
            sb.append("		}else{url += \"?" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		url = url + page + \"&" + (entityOrField ? "showCount" : "page.showCount") + "=" + showCount + "\";\n");
            sb.append("		document.location = url;\n");
            sb.append("	}\n");
            sb.append("}\n");

            //调整每页显示条数
            sb.append("function changeCount(value){");
            sb.append(" top.jzts();");
            sb.append("	if(true && document.forms[0]){\n");
            sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
            sb.append("		if(url.indexOf('?')>-1){url += \"&" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		else{url += \"?" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		url = url + \"1&" + (entityOrField ? "showCount" : "page.showCount") + "=\"+value;\n");
            sb.append("		document.forms[0].action = url;\n");
            sb.append("		document.forms[0].submit();\n");
            sb.append("	}else{\n");
            sb.append("		var url = document.location+'';\n");
            sb.append("		if(url.indexOf('?')>-1){\n");
            sb.append("			if(url.indexOf('currentPage')>-1){\n");
            sb.append("				var reg = /currentPage=\\d*/g;\n");
            sb.append("				url = url.replace(reg,'currentPage=');\n");
            sb.append("			}else{\n");
            sb.append("				url += \"1&" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";\n");
            sb.append("			}\n");
            sb.append("		}else{url += \"?" + (entityOrField ? "currentPage" : "page.currentPage") + "=\";}\n");
            sb.append("		url = url + \"&" + (entityOrField ? "showCount" : "page.showCount") + "=\"+value;\n");
            sb.append("		document.location = url;\n");
            sb.append("	}\n");
            sb.append("}\n");

            //跳转函数
            sb.append("function toTZ(){");
            sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
            sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
            sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
            sb.append("nextPage(toPaggeVlue);");
            sb.append("}\n");
            sb.append("</script>\n");
        }
        pageStr = sb.toString();
        return pageStr;
    }

    public void setPageStr(String pageStr) {
        this.pageStr = pageStr;
    }

    public long getShowCount() {
        return showCount;
    }

    public void setShowCount(long showCount) {

        this.showCount = showCount;
    }

    public long getCurrentResult() {
        currentResult = (getCurrentPage() - 1) * getShowCount();
        if (currentResult < 0)
            currentResult = 0;
        return currentResult;
    }

    public void setCurrentResult(long currentResult) {
        this.currentResult = currentResult;
    }

    public boolean isEntityOrField() {
        return entityOrField;
    }

    public void setEntityOrField(boolean entityOrField) {
        this.entityOrField = entityOrField;
    }

    public PageData getPd() {
        return pd;
    }

    public void setPd(PageData pd) {
        this.pd = pd;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

}
