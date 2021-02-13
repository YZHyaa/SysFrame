package com.mdt.util;

import com.mdt.dao.DaoSupport;
import com.mdt.listener.WebAppContextListener;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName: ExcelUtil
 * @Description: excel工具类
 */
public class ExcelUtil {

    /**
     * 将查询得到的结果导出为excel
     *
     * @param list
     * @param sheetName 文件名及页签名
     * @param sheetSize 每页数量，默认65535
     * @param response
     * @param cols      导出列，结构为数据库字段名1:显示名称1，数据库字段名2:显示名称2，数据库字段名3:显示名称3,若为空则以list的第一行为表头
     * @return
     */
    public static boolean exportExcel(List<PageData> list, String sheetName, int sheetSize,
                                      HttpServletResponse response, String cols) {

        if (list.size() == 0)
            return false;

        List<String> excelCol = new ArrayList<String>();
        List<String> dataCol = new ArrayList<String>();


        if (StringUtil.isEmpty(cols) || cols.indexOf(",") < 0) {
            PageData pd = list.get(0);
            Iterator<String> it = pd.keySet().iterator();
            while (it.hasNext()) {
                String col = it.next();
                excelCol.add(col);
                dataCol.add(col);
            }
        } else {
            String[] colArr = cols.split(",");
            for (String str : colArr) {
                String[] strArr = str.split(":");
                dataCol.add(strArr[0]);
                excelCol.add(strArr[1]);
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();

        // excel2003最大行数65536
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);
        for (int index = 0; index <= sheetNo; index++) {
            HSSFSheet sheet = workbook.createSheet();
            if (index == 0)
                workbook.setSheetName(index, sheetName);
            else
                workbook.setSheetName(index, sheetName + index);

            HSSFRow row;
            HSSFCell cell;

            row = sheet.createRow(0);

            for (int col = 0; col < excelCol.size(); col++) {
                cell = row.createCell(col);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(excelCol.get(col));
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                PageData vo = list.get(i);
                for (int j = 0; j < dataCol.size(); j++) {
                    String field = dataCol.get(j);

                    try {
                        cell = row.createCell(j);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        Object val = vo.get(field);
                        cell.setCellValue(val == null ? "" : val.toString());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {

            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            String filename = URLEncoder.encode(sheetName, "UTF-8");
            response.setHeader("Content-Disposition", "inline;filename=\"" + filename + ".xls\"");

            OutputStream output = response.getOutputStream();
            workbook.write(output);
            output.flush();
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 传入mapper语句，将查询得到的结果导出为excel
     *
     * @param maperStr  Mapper文件
     * @param param     传入参数
     * @param sheetName 文件名及页签名
     * @param sheetSize 每页数量，默认65535
     * @param response
     * @param cols      导出列，结构为数据库字段名1:显示名称1，数据库字段名2:显示名称2，数据库字段名3:显示名称3,若为空则以list的第一行为表头
     * @return
     * @throws Exception
     */
    public static boolean exportExcel(String maperStr, PageData param, String sheetName, int sheetSize,
                                      HttpServletResponse response, String cols) throws Exception {

        DaoSupport dao = (DaoSupport) WebAppContextListener.getApplicationContext().getBean("daoSupport");

        List<PageData> list = (List<PageData>) dao.findForList(maperStr, param);

        if (list.size() == 0)
            return false;

        List<String> excelCol = new ArrayList<String>();
        List<String> dataCol = new ArrayList<String>();


        if (StringUtil.isEmpty(cols) || cols.indexOf(",") < 0) {
            PageData pd = list.get(0);
            Iterator<String> it = pd.keySet().iterator();
            while (it.hasNext()) {
                String col = it.next();
                excelCol.add(col);
                dataCol.add(col);
            }
        } else {
            String[] colArr = cols.split(",");
            for (String str : colArr) {
                String[] strArr = str.split(":");
                dataCol.add(strArr[0]);
                excelCol.add(strArr[1]);
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();

        // excel2003最大行数65536
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);
        for (int index = 0; index <= sheetNo; index++) {
            HSSFSheet sheet = workbook.createSheet();
            if (index == 0)
                workbook.setSheetName(index, sheetName);
            else
                workbook.setSheetName(index, sheetName + index);

            HSSFRow row;
            HSSFCell cell;

            row = sheet.createRow(0);

            for (int col = 0; col < excelCol.size(); col++) {
                cell = row.createCell(col);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(excelCol.get(col));
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                PageData vo = list.get(i);
                for (int j = 0; j < dataCol.size(); j++) {
                    String field = dataCol.get(j);

                    try {
                        cell = row.createCell(j);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        Object val = vo.get(field);
                        cell.setCellValue(val == null ? "" : val.toString());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {

            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            String filename = URLEncoder.encode(sheetName, "UTF-8");
            response.setHeader("Content-Disposition", "inline;filename=\"" + filename + ".xls\"");

            OutputStream output = response.getOutputStream();
            workbook.write(output);
            output.flush();
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 主要用于导出指定的字符串，首行为标题
     *
     * @param list      作为导出内容的list,每个String都以逗号分隔
     * @param sheetName 文件名及页签名
     * @param sheetSize 每页数量，默认65535
     * @param response
     * @return
     */
    public static boolean exportExcel(List<String> list, String sheetName, int sheetSize,
                                      HttpServletResponse response) {

        if (list.size() == 0)
            return false;

        HSSFWorkbook workbook = new HSSFWorkbook();

        // excel2003最大行数65536
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);
        for (int index = 0; index <= sheetNo; index++) {
            HSSFSheet sheet = workbook.createSheet();
            if (index == 0)
                workbook.setSheetName(index, sheetName);
            else
                workbook.setSheetName(index, sheetName + index);

            HSSFRow row;
            HSSFCell cell;

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i - startNo);
                String vo = list.get(i);
                String[] voArr = vo.split(",");
                for (int j = 0; j < voArr.length; j++) {
                    try {
                        cell = row.createCell(j);
                        Object val = voArr[j];
                        if (voArr[j].toString().matches("^(-?\\d+)(\\.\\d+)?$")) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(val == null ? 0 : Double.parseDouble(val.toString()));
                        } else {
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(val == null ? "" : val.toString());
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {

            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            String filename = URLEncoder.encode(sheetName, "UTF-8");
            response.setHeader("Content-Disposition", "inline;filename=\"" + filename + ".xls\"");

            OutputStream output = response.getOutputStream();
            workbook.write(output);
            output.flush();
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 从request获取excel文件并转换为list
     *
     * @param request
     * @param sheetName
     * @param cols      导入列，结构为数据库字段名1:Excel列A，数据库字段名2:Excel列B，数据库字段名3:Excel列C
     * @return
     */
    public static List<PageData> purseExcelToList(HttpServletRequest request, String sheetName, String cols) {
        List<PageData> list = null;
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        //判断 request 是否有文件上传,即多部分请求  
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();
            if (iter.hasNext()) {
                //取得上传文件  
                List<MultipartFile> files = multiRequest.getFiles(iter.next());
                if (files != null && files.size() > 0) {
                    MultipartFile multipartFile = files.get(0);
                    //取得当前上传文件的文件名称
                    String fileName = multipartFile.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (fileName.trim() != "" && ("xls".equals(fileName.toLowerCase()) || "xlsx".equals(fileName.toLowerCase()))) {
                        String fileType = fileName.substring(fileName.lastIndexOf("."));
                        //文件需要为excel文件
                        if ("xls".equals(fileName.toLowerCase()) || "xlsx".equals(fileName.toLowerCase())) {
                            try {
                                list = purseExcelToList(sheetName, multipartFile.getInputStream(), cols);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        if (list == null)
            list = new ArrayList<PageData>();
        return list;
    }

    /**
     * 获取excel文件输入流，转换为list
     *
     * @param sheetName
     * @param input
     * @param cols      导入列，结构为数据库字段名1:Excel列A，数据库字段名2:Excel列B，数据库字段名3:Excel列C
     * @return List
     * @throws
     * @Title: importExcel
     * @Description: TODO:excel导入
     */
    public static List purseExcelToList(String sheetName, InputStream input, String cols) {
        List<PageData> list = new ArrayList<PageData>();
        try {
            Workbook book = Workbook.getWorkbook(input);
            Sheet sheet = null;
            if (!StringUtil.isEmpty(sheetName)) {
                sheet = book.getSheet(sheetName);// 根据名称获取sheet
            }
            if (sheet == null) {
                sheet = book.getSheet(0);// 根据索引获取sheet
            }
            int rows = sheet.getRows();// 获取行数
            if (rows > 0) {
                Map<Integer, String> fieldsMap = new HashMap<Integer, String>();

                String[] allFields = cols.split(",");

                for (String field : allFields) {
                    String[] fieldArr = field.split(":");
                    int col = purseExcelColtoNum(fieldArr[1]);
                    fieldsMap.put(col, fieldArr[0]);
                }

                for (int i = 1; i < rows; i++) {
                    Cell[] cells = sheet.getRow(i);
                    PageData pd = new PageData();
                    for (int j = 0; j < cells.length; j++) {
                        String c = cells[j].getContents();
                        if (c.equals("")) {
                            continue;
                        }

                        pd.put(fieldsMap.get(j), c);
                    }

                    if (!(pd == null || pd.isEmpty())) {
                        list.add(pd);
                    }

                }
            }

        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取Excel列对应的数字列数,从0开始
     *
     * @param @param  col
     * @param @return 设定文件
     * @return int    返回类型
     * @throws
     * @Title: getExcelCol
     * @Description: TODO:获取excel转化到数字 A->1 B->2
     */
    public static int purseExcelColtoNum(String col) {
        col = col.toUpperCase();
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }
}
