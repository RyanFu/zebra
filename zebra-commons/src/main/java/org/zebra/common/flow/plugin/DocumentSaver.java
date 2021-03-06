package org.zebra.common.flow.plugin;

import org.apache.log4j.Logger;
import org.zebra.common.*;
import org.zebra.common.flow.*;
import org.zebra.common.domain.*;
import org.zebra.common.domain.dao.*;
import org.zebra.common.utils.ProcessorUtil;

public class DocumentSaver implements Processor {
    private static Logger logger = Logger.getLogger(DocumentSaver.class);
    private DocumentDao documentDao = null;

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @Override
    public boolean initialize() {
        logger.info("DocumentSave initialized.");
        return true;
    }

    @Override
    public boolean destroy() {
        logger.info("DocumentSave destroy.");
        return true;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean process(CrawlDocument doc, Context context) {
        if (null == doc || null == context) {
            logger.warn("invalid parameter. document or context is empty.");
            return false;
        }
        if (null == this.documentDao) {
            logger.warn("internal error, documentDao is empty");
            return false;
        }
        Document document = new Document();
        String title = (String) context.getVariable(ProcessorUtil.COMMON_PROP_TITLE);
        String description = (String) context.getVariable(ProcessorUtil.COMMON_PROP_DESCRIPTION);
        String articleText = (String) context.getVariable(ProcessorUtil.COMMON_PROP_MAINBODY);
        document.setUrl(doc.getUrl());
        document.setUrlMd5(org.zebra.common.utils.StringUtil.computeMD5(doc.getUrl()));
        document.setSourceUrl((String)doc.getUrlInfo().getFeature(ProcessorUtil.COMMON_PROP_SEEDURL));
        document.setTitle(title);
        document.setDescription(description);
        document.setArticleText(articleText);
        document.setDownloadTime(doc.getFetchTime().getTime());
        document.setChannel((String)doc.getUrlInfo().getFeature(ProcessorUtil.COMMON_PROP_CHANNEL));
        logger.debug("save document:" + document.toString());
        this.documentDao.save(document);
        return true;
    }
}
