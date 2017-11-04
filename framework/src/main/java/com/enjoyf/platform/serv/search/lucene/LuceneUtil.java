package com.enjoyf.platform.serv.search.lucene;

import com.enjoyf.platform.util.log.GAlerter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class LuceneUtil {
    private static final Logger Logger = LoggerFactory.getLogger(LuceneUtil.class);

    public static IndexReader getReader(IndexReader reader) {
        boolean needClose = false;
        try {
            if (!reader.isCurrent()) {
                IndexReader newReader = reader.reopen();
                Logger.info("reopen reader success.");
                needClose = true;
                return newReader;
            }
            return reader;
        } catch (IOException e) {
            GAlerter.lab("get Reader occured Exception.", e);
            return reader;
        } finally {
            if (needClose) {
                closeIndexReader(reader);
            }
        }
    }

    public static IndexWriter openIndexWriter(Directory dir, Analyzer analyzer) throws IOException {
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_30, analyzer);
        writerConfig.setMergeScheduler(new ConcurrentMergeScheduler());
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(dir, writerConfig);
    }

    public static void closeIndexWriter(IndexWriter iw) {
        if (iw != null) {
            try {
                iw.close();
            } catch (IOException e) {
                Logger.error("closeIndexWriter occured IOException:", e);
            }
        }
    }

    public static void closeDirectory(Directory dir) {
        if (dir != null) {
            try {
                dir.close();
            } catch (IOException e) {
                Logger.error("closeIndexWriter occured IOException:", e);
            }
        }
    }


    public static void closeIndexSearch(IndexSearcher is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                Logger.error("closeIndexWriter occured IOException:", e);
            }
        }
    }

    public static void closeIndexReader(IndexReader ir) {
        if (ir != null) {
            try {
                ir.close();
            } catch (IOException e) {
                Logger.error("closeIndexWriter occured IOException:", e);
            }
        }
    }
}
