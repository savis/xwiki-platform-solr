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
package org.xwiki.platform.search;

import java.util.List;

import org.xwiki.model.reference.DocumentReference;

/**
 * Statistics corresponding document indexing.
 * 
 * @version $Id$
 */
public interface IndexerProcess
{

    /**
     * @return the completion time stamp as a String.
     */
    String getEstimatedCompletionTime();

    /**
     * @return the indexing speed of the documents.
     */
    String getIndexingSpeed();

    /**
     * @param count number of documents
     * @return the list of indexed documents.
     */
    List<DocumentReference> getLastIndexedDocuments(int count);

    /**
     * @return next {@link IndexerProcess} in the case of multiple index requests.
     */
    IndexerProcess getNextIndexerProcess();

    /**
     * @return pre-queue size of the indexing process.
     */
    int getPreQueueSize();

    /**
     * @return queue size of the indexing process.
     */
    int getQueueSize();
}
