package com.enjoyf.platform.serv.search.lucene;

import com.enjoyf.platform.service.search.SearchGiftIndexEntry;
import com.enjoyf.platform.service.search.SearchGiftResultEntry;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class GiftIndexer {
    private static final Logger logger = LoggerFactory.getLogger(GiftIndexer.class);

    private String indexDir;

    private Analyzer analyzer = new IKAnalyzer(false);


    private IndexReader reader = null;
    private IndexWriter writer = null;


    public GiftIndexer(String indexDir) {
        this.indexDir = indexDir;

        try {
            Directory dir = NIOFSDirectory.open(new File(this.indexDir));
            writer = LuceneUtil.openIndexWriter(dir, analyzer);
            reader = IndexReader.open(writer, false);
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " init reader occuerd IOException:", e);
            System.exit(0);
        }
    }

    public int index(List<SearchGiftIndexEntry> entries) throws IOException {
        deleteAll();
        for (SearchGiftIndexEntry entity : entries) {
            indexFile(writer, entity);
        }
        writer.optimize();
        writer.commit();
        return writer.numDocs();
    }


    public void deleteAll() throws IOException {
        writer.deleteAll();
        writer.optimize();
        writer.commit();
    }


    public void deleteByDocId(String idField, String docId) throws IOException {
        Term t = new Term(idField, docId);
        writer.deleteDocuments(t);
    }

    public PageRows<SearchGiftResultEntry> search(List<Map<String, String>> queryList, Pagination p) throws ParseException, IOException {
        PageRows<SearchGiftResultEntry> retrurnValue = new PageRows<SearchGiftResultEntry>();
        BooleanQuery booleanQueryTotal = new BooleanQuery();
        reader = LuceneUtil.getReader(reader);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        List<SearchGiftResultEntry> searchResultList = new ArrayList<SearchGiftResultEntry>();
        try {
            for (Map<String, String> criteriaMap : queryList) {
                BooleanQuery booleanQuery = new BooleanQuery();
                for (Map.Entry<String, String> criteriaMapEntry : criteriaMap.entrySet()) {
                    QueryParser parser = new QueryParser(Version.LUCENE_30, criteriaMapEntry.getKey(), analyzer);
                    String text = criteriaMapEntry.getValue();
                    if (StringUtil.isEmpty(text)) {
                        continue;
                    }
                    Query query = parser.parse(text);
                    booleanQuery.add(query, BooleanClause.Occur.SHOULD);
                }
                if (booleanQuery != null && booleanQuery.getClauses().length > 0) {
                    booleanQueryTotal.add(booleanQuery, BooleanClause.Occur.MUST);
                }
            }

            long start = System.currentTimeMillis();
            Sort updateSort = new Sort(new SortField(SearchGiftIndexEntry.IDX_KEY_GTID, SortField.LONG, true));
            TopDocs hits = indexSearcher.search(booleanQueryTotal, null, p.getStartRowIdx() + p.getPageSize(), updateSort);
            long end = System.currentTimeMillis();

            if (logger.isDebugEnabled()) {
                logger.debug("found " + hits.totalHits + " document(s) (in " + (end - start) + " milliseconds) that matched query '" + queryList + "':");
            }

            p.setTotalRows(hits.totalHits);

            for (int i = p.getStartRowIdx(); i <= p.getEndRowIdx(); i++) {
                if (i + 1 > hits.totalHits) {
                    break;
                }
                Document doc = indexSearcher.doc(hits.scoreDocs[i].doc);
                SearchGiftResultEntry entry = new SearchGiftResultEntry();

                entry.setGtId(doc.get(SearchGiftIndexEntry.IDX_KEY_GTID));
                entry.setGtName(doc.get(SearchGiftIndexEntry.IDX_KEY_GTNAME));

                searchResultList.add(entry);
            }
            retrurnValue.setRows(searchResultList);
            retrurnValue.setPage(p);
        } finally {
            LuceneUtil.closeIndexSearch(indexSearcher);
        }

        return retrurnValue;
    }

    private void indexFile(IndexWriter writer, SearchGiftIndexEntry entity) throws IOException {
        Term t = new Term(SearchGiftIndexEntry.IDX_KEY_GTID, entity.getaId());
        writer.updateDocument(t, getDocument(entity));
    }

    private Document getDocument(SearchGiftIndexEntry entity) {

        Document doc = new Document();

        //for search

        //for display result
        doc.add(new Field(SearchGiftIndexEntry.IDX_KEY_GTID, entity.getaId(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field(SearchGiftIndexEntry.IDX_KEY_GTNAME, entity.getName(), Field.Store.YES, Field.Index.ANALYZED));

        return doc;
    }

}
