/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.platform.search.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.bridge.event.DocumentDeletedEvent;
import org.xwiki.bridge.event.DocumentUpdatedEvent;
import org.xwiki.context.Execution;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.SpaceReference;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.platform.search.IndexerProcess;
import org.xwiki.platform.search.Search;
import org.xwiki.platform.search.SearchException;
import org.xwiki.platform.search.SearchIndexingException;
import org.xwiki.platform.search.SearchResponse;
import org.xwiki.platform.search.SolrQuery;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.internal.event.AttachmentAddedEvent;
import com.xpn.xwiki.internal.event.AttachmentDeletedEvent;
import com.xpn.xwiki.internal.event.AttachmentUpdatedEvent;

/**
 * @version $Id$
 */
public abstract class AbstractSearch implements Search, EventListener
{
    @Inject
    protected Logger logger;

    @Inject
    protected Execution execution;

    protected IndexerProcess indexerProcessor;

    private static final List<Event> EVENTS = Arrays.<Event> asList(new DocumentUpdatedEvent(),
        new DocumentCreatedEvent(), new DocumentDeletedEvent(), new AttachmentAddedEvent(),
        new AttachmentDeletedEvent(), new AttachmentUpdatedEvent());

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.platform.search.Search#indexWiki()
     */
    @Override
    public int indexWiki() throws SearchIndexingException, XWikiException
    {
        int docsCount = indexWiki(getXWikiContext().getWiki().getName());
        return docsCount;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.platform.search.Search#indexWikiFarm()
     */
    @Override
    public int indexWikiFarm() throws SearchIndexingException, XWikiException
    {
        XWikiContext xcontext = getXWikiContext();
        int totalDocCount = 0;
        if (xcontext.getWiki().isVirtualMode()) {
            List<String> wikis = xcontext.getWiki().getVirtualWikisDatabaseNames(xcontext);
            for (String wikiName : wikis) {
                totalDocCount += indexWiki(wikiName);
            }
        }
        return totalDocCount;
    }


    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.observation.EventListener#getEvents()
     */
    @Override
    public List<Event> getEvents()
    {
        return EVENTS;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.observation.EventListener#getName()
     */
    @Override
    public String getName()
    {
        return "XWiki Search";
    }

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.observation.EventListener#onEvent(org.xwiki.observation.event.Event, java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        // TODO Auto-generated method stub

    }

    protected XWikiContext getXWikiContext()
    {
        return (XWikiContext) this.execution.getContext().getProperty(XWikiContext.EXECUTIONCONTEXT_KEY);
    }

    protected abstract int indexWiki(String wikiName) throws XWikiException;

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.platform.search.Search#getCurrentIndexerProcess()
     */
    @Override
    public IndexerProcess getCurrentIndexerProcess()
    {
        return indexerProcessor;
    }
}
