package com.mdt.dao;

import com.mdt.util.StringUtil;
import com.mdt.util.mongodb.MongoUtil;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * mongodb的dao类
 *
 * @author "PangLin"
 * @ClassName: MongoDaoSupport
 * @Description: TODO
 * @date 2016年1月4日 上午11:38:02
 */
@Repository("mongoDaoSupport")
public class MongoDaoSupport {

    /**
     * 获取所有数据库名称列表
     *
     * @return
     */
    public MongoIterable<String> getAllDBNames() {
        if (MongoUtil.getMongoClient() != null) {
            MongoIterable<String> s = MongoUtil.getMongoClient().listDatabaseNames();
            return s;
        } else {
            return null;
        }
    }


    /**
     * 查询DB下的所有表名
     */
    public List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = MongoUtil.getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 删除collection
     *
     * @param dbName
     * @param collName
     */
    public void dropCollection(String dbName, String collName) {

        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);

        if (coll != null)
            coll.drop();
    }

    /**
     * 统计collection中记录的总数
     *
     * @param dbName
     * @param collName
     * @return
     */
    public long getCollectionCount(String dbName, String collName) {
        long count = 0;
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        if (coll != null)
            count = coll.count();

        return count;
    }

    /**
     * 统计collection中记录的总数
     *
     * @param dbName
     * @param collName
     * @param filter
     * @return
     */
    public long getCollectionCount(String dbName, String collName, Bson filter) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        long result = 0;

        if (coll != null) {
            result = coll.count(filter);
        }

        return result;
    }

    /**
     * 根据id查找对象
     *
     * @param dbName
     * @param collName
     * @param id
     * @return
     */
    public Document findById(String dbName, String collName, String id) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        Document doc = null;

        if (!(coll == null || StringUtil.isEmpty(id))) {
            ObjectId _id = new ObjectId(id);
            doc = coll.find(eq("_id", _id)).first();
        }

        return doc;
    }


    /**
     * 条件查询
     *
     * @param dbName
     * @param collName
     * @param filter   查询条件
     * @param sort     排序
     * @return
     */
    public FindIterable<Document> find(String dbName, String collName, Bson filter, Bson sort) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        FindIterable<Document> cursor = null;

        if (coll != null) {
            if (sort == null) {
                if (filter == null)
                    cursor = coll.find();
                else
                    cursor = coll.find(filter);
            } else {
                if (filter == null)
                    cursor = coll.find().sort(sort);
                else
                    cursor = coll.find(filter).sort(sort);
                ;
            }
        }

        return cursor;

    }

    /**
     * 按条件查找，并对结果进行投影
     *
     * @param dbName
     * @param collName
     * @param filter
     * @param sort
     * @return
     */
    public FindIterable<Document> findAndProjection(String dbName, String collName, Bson filter, Bson projection) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        FindIterable<Document> cursor = null;

        if (coll != null) {
            if (projection == null) {
                if (filter == null)
                    cursor = coll.find();
                else
                    cursor = coll.find(filter);
            } else {
                if (filter == null)
                    cursor = coll.find().projection(projection);
                else
                    cursor = coll.find(filter).projection(projection);
            }
        }

        return cursor;

    }


    /**
     * 分页查询
     *
     * @param dbName
     * @param collName
     * @param filter
     * @param sort
     * @param pageNo
     * @param pageSize
     * @return
     */
    public FindIterable<Document> findByPage(String dbName, String collName, Bson filter, Bson sort, Long pageNo, Long pageSize) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        FindIterable<Document> cursor = null;
        if (coll != null) {
            if (sort == null) {
                if (filter == null)
                    cursor = coll.find().skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                else
                    cursor = coll.find(filter).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
            } else {
                if (filter == null)
                    cursor = coll.find().sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                else
                    cursor = coll.find(filter).sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
            }

        }
        return cursor;
    }

    public FindIterable<Document> findByPageAndProjection(String dbName, String collName, Bson filter, Bson sort, Long pageNo, Long pageSize, Bson projection) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        FindIterable<Document> cursor = null;
        if (coll != null) {
            if (sort == null) {
                if (filter == null) {
                    if (projection == null)
                        cursor = coll.find().skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                    else
                        cursor = coll.find().projection(projection).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                } else {
                    if (projection == null)
                        cursor = coll.find(filter).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                    else
                        cursor = coll.find(filter).projection(projection).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                }
            } else {
                if (filter == null) {
                    if (projection == null)
                        cursor = coll.find().sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                    else
                        cursor = coll.find().projection(projection).sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                } else {
                    if (projection == null)
                        cursor = coll.find(filter).sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                    else
                        cursor = coll.find(filter).projection(projection).sort(sort).skip((pageNo.intValue() - 1) * pageSize.intValue()).limit(pageSize.intValue());
                }
            }

        }
        return cursor;
    }


    /**
     * 插入单条记录
     *
     * @param dbName
     * @param collName
     * @param doc
     * @return 插入正常返回true，插入失败返回false
     */
    public boolean insert(String dbName, String collName, Document doc) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        boolean ret = true;

        if (coll != null)
            coll.insertOne(doc);
        else
            ret = false;

        return ret;
    }

    /**
     * 批量插入
     *
     * @param dbName
     * @param collName
     * @param docList
     * @return 插入正常返回true，插入失败返回false
     */
    public boolean insertMany(String dbName, String collName, List<Document> docList) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        boolean ret = true;

        if (coll != null)
            coll.insertMany(docList);
        else
            ret = false;

        return ret;
    }

    /**
     * 根据id更新
     *
     * @param dbName
     * @param collName
     * @param id
     * @param newdoc
     * @return
     */
    public UpdateResult updateById(String dbName, String collName, String id, Document newdoc) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        UpdateResult result = null;

        if (!(coll == null || StringUtil.isEmpty(id))) {
            ObjectId _id = new ObjectId(id);
            result = coll.updateOne(eq("_id", _id), new Document("$set", newdoc));
        }

        return result;
    }


    /**
     * 批量更新
     *
     * @param dbName
     * @param collName
     * @param filter   Filter类的方法
     * @param param    Updates类的方法
     * @return
     */
    public UpdateResult updateMany(String dbName, String collName, Bson filter, Bson param) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);

        UpdateResult result = null;

        if (coll != null) {
            result = coll.updateMany(filter, param);
        }

        return result;
    }

    /**
     * 根据id替换doc
     *
     * @param dbName
     * @param collName
     * @param id
     * @param newdoc
     * @return
     */
    public UpdateResult replaceById(String dbName, String collName, String id, Document newdoc) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);
        UpdateResult result = null;

        if (!(coll == null || StringUtil.isEmpty(id))) {
            ObjectId _id = new ObjectId(id);
            result = coll.replaceOne(eq("_id", _id), newdoc);
        }

        return result;
    }

    /**
     * 根据id删除doc
     *
     * @param dbName
     * @param collName
     * @param id
     * @return
     */
    public DeleteResult deleteById(String dbName, String collName, String id) {
        DeleteResult result = null;
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);

        if (!(coll == null || StringUtil.isEmpty(id))) {
            ObjectId _id = new ObjectId(id);
            result = coll.deleteOne(eq("_id", _id));
        }

        return result;
    }

    /**
     * 删除多条
     *
     * @param dbName
     * @param collName
     * @param filter
     * @return
     */
    public DeleteResult deleteMany(String dbName, String collName, Bson filter) {
        DeleteResult result = null;
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);

        if (coll != null) {
            result = coll.deleteMany(filter);
        }

        return result;
    }

    /**
     * pipeline聚合查询
     *
     * @param dbName
     * @param collName
     * @param list
     * @return
     */
    public AggregateIterable aggregate(String dbName, String collName, List<Bson> pipeline) {
        MongoCollection<Document> coll = MongoUtil.getMongoCollection(dbName, collName);

        AggregateIterable result = null;

        if (coll != null) {
            result = coll.aggregate(pipeline);
        }

        return result;
    }

    /**
     * 测试数据库是否能够连接
     *
     * @param dbName
     * @return
     */
    public boolean testDBLink(String dbName) {
        Boolean result = true;
        try {
            getAllCollections(dbName);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

}
