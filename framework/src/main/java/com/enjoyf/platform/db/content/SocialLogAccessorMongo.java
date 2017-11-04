package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseMongoAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.content.social.SocialLog;
import com.enjoyf.platform.service.content.social.SocialLogCategory;
import com.enjoyf.platform.service.content.social.SocialLogType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppShareChannel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-23
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class SocialLogAccessorMongo extends AbstractBaseMongoAccessor<SocialLog>{
    private static final String KEY_COLLECTION_NAME = "social_log_";
    private static final String KEY_COLLECTION_SUFFIX_FMT = "yyyyMM";

    @Override
    public SocialLog insert(SocialLog socialLog, DB db) throws DbException {
        WriteResult writeResult = db.getCollection(getCollection(socialLog.getCreateDate() == null ? new Date() : socialLog.getCreateDate())).insert(entryToDBObject(socialLog));
        if (writeResult.getN() > 0) {
            return socialLog;
        }
        return null;
    }

    @Override
    protected SocialLog dbObjToEntry(DBObject dbObject) {
        SocialLog socialLog = new SocialLog();
        socialLog.setSLogId((ObjectId) dbObject.get("_id"));
        socialLog.setForeignId((Long) dbObject.get("foreign_id"));
        socialLog.setUno((String) dbObject.get("uno"));
        socialLog.setContentId((Long) dbObject.get("content_id"));
        socialLog.setLogType(SocialLogType.getByCode((Integer) dbObject.get("log_type")));
        socialLog.setLogCategory(SocialLogCategory.getByCode((Integer) dbObject.get("log_category")));
        socialLog.setCreateDate(new Date((Long) dbObject.get("create_time")));
        socialLog.setPlatform(AppPlatform.getByCode((Integer) dbObject.get("platform")));
        socialLog.setShareChannel(AppShareChannel.getByCode((String) dbObject.get("share_channel")));
        return socialLog;
    }

    @Override
    protected BasicDBObject entryToDBObject(SocialLog entry) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("foreign_id", entry.getForeignId());
        basicDBObject.put("uno", entry.getUno());
        basicDBObject.put("content_id", entry.getContentId());
        basicDBObject.put("log_type", entry.getLogType().getCode());
        basicDBObject.put("log_category", entry.getLogCategory().getCode());
        basicDBObject.put("platform", entry.getPlatform().getCode());
        basicDBObject.put("share_channel", entry.getShareChannel() == null ? "" : entry.getShareChannel().getCode());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        basicDBObject.put("create_date", df.format(entry.getCreateDate() == null ? new Date() : entry.getCreateDate()));
        basicDBObject.put("create_time", entry.getCreateDate() == null ? System.currentTimeMillis() : entry.getCreateDate().getTime());
        return basicDBObject;
    }

    private String getCollection(Date date) {
        return KEY_COLLECTION_NAME + TableUtil.getTableDateSuffix(date, KEY_COLLECTION_SUFFIX_FMT);
    }
}
