package com.mdt.dao;

import com.mdt.util.PageData;
import com.mdt.util.postgres.PostgresTemplate;
import com.mdt.util.postgres.PostgresTemplateCallBack;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Postgresql dao类
 *
 * @author "PangLin"
 * @ClassName: PostgresDaoSupport
 * @Description: TODO
 * @date 2016年4月15日 下午1:44:14
 */
@Repository("postgresDaoSupport")
public class PostgresDaoSupport {

    public List<PageData> queryForList(String sql) {

        PostgresTemplate<List<PageData>> template = PostgresTemplate.getPostgresTemplate();

        return template.query(sql, new PostgresTemplateCallBack<List<PageData>>() {
            @Override
            public List<PageData> purseRs(ResultSet rs) {
                List<PageData> retList = new ArrayList<PageData>();
                try {
                    if (rs != null) {
                        ResultSetMetaData rsm = rs.getMetaData();
                        int colLen = rsm.getColumnCount();
                        while (rs.next()) {
                            PageData pd = new PageData();
                            for (int i = 1; i <= colLen; i++) {
                                pd.put(rsm.getColumnName(i), rs.getObject(i));
                            }
                            retList.add(pd);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return retList;
            }
        });
    }


    public PageData queryForObject(String sql) {

        PostgresTemplate<PageData> template = PostgresTemplate.getPostgresTemplate();

        return template.query(sql, new PostgresTemplateCallBack<PageData>() {
            @Override
            public PageData purseRs(ResultSet rs) {
                PageData retPd = new PageData();
                try {
                    if (rs != null) {
                        ResultSetMetaData rsm = rs.getMetaData();
                        int colLen = rsm.getColumnCount();
                        if (rs.next()) {
                            for (int i = 1; i <= colLen; i++) {
                                retPd.put(rsm.getColumnName(i), rs.getObject(i));
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return retPd;
            }
        });
    }

}
