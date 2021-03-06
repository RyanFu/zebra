package org.zebra.silkworm.plugin.writer;

import org.zebra.common.*;

public class DocumentWriter {
    private WritableContent content = null;
    private Persistence persistence = null;
    public boolean write(CrawlDocument doc) {
    	if (null == content || null == persistence) {
    		return false;
    	}
    	content.reset(doc);
    	byte[] bytes = content.getBytes();
    	return persistence.writeBytes(bytes);
    }
}
