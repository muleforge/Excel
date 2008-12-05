/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.routing.outbound;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.config.i18n.CoreMessages;

public class XlsRowMessageSplitter extends AbstractMessageSplitter
{
    private XlsSplitter splitter;

    private String      sheetName;

    private int         startRow;

    private int         endRow;

    private boolean     failIfNoMatch = true;

    public XlsSplitter getSplitter()
    {
        return this.splitter;
    }

    public void setSplitter( XlsSplitter splitter )
    {
        this.splitter = splitter;
    }

    public boolean isFailIfNoMatch()
    {
        return this.failIfNoMatch;
    }

    public void setFailIfNoMatch( boolean failIfNoMatch )
    {
        this.failIfNoMatch = failIfNoMatch;
    }

    public String getSheetName()
    {
        return this.sheetName;
    }

    @Override
    protected SplitMessage getMessageParts( MuleMessage message, List endpoints )
    {
        if (message.getPayload() instanceof InputStream || message.getPayload() instanceof File)
        {
            splitter = new SimpleXlsSplitter((InputStream) message.getPayload(), sheetName);
        } else
        {
            throw new IllegalArgumentException(
                    "The payload for this router must be of type java.io.InputStream or java.io.File");
        }

        SplitMessage splitMessage = new SplitMessage();

        // Cache the properties here because for some message types getting the
        // properties can be expensive
        Map props = new HashMap();
        for (Iterator iterator = message.getPropertyNames().iterator(); iterator.hasNext();)
        {
            String propertyKey = (String) iterator.next();
            props.put(propertyKey, message.getProperty(propertyKey));
        }

        while (splitter.hasNext())
        {
            Map row = splitter.getNextRow();

            MuleMessage part = new DefaultMuleMessage(row, props);
            boolean matchFound = false;
            OutboundEndpoint endpoint;
            // If there is no filter assume that the endpoint can accept the
            // message. Endpoints will be processed in order to only the last
            // (if any) of the the endpoints may not have a filter
            // Try each endpoint in the list. If there is no match for any of
            // them
            // we drop out and throw an exception
            for (int j = 0; j < endpoints.size(); j++)
            {
                endpoint = (OutboundEndpoint) endpoints.get(j);

                if (endpoint.getFilter() == null || endpoint.getFilter().accept(part))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Endpoint filter matched. Routing message over: "
                                + endpoint.getEndpointURI().toString());
                    }
                    splitMessage.addPart(part, endpoint);
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound)
            {
                if (isFailIfNoMatch())
                {
                    throw new IllegalStateException(CoreMessages.splitMessageNoEndpointMatch(endpoints, row)
                            .getMessage());
                } else
                {
                    logger.info("No splitter match for message part. 'failIfNoMatch=false' ingoring message part.");
                }
            }
        }
        // if (enableCorrelation != ENABLE_CORRELATION_NEVER)
        // {
        // // always set correlation group size, even if correlation id
        // // has already been set (usually you don't have group size yet
        // // by this point.
        // final int groupSize = payload.size();
        // message.setCorrelationGroupSize(groupSize);
        // if (logger.isDebugEnabled())
        // {
        // logger.debug("java.util.List payload detected, setting correlation
        // group size to "
        // + groupSize);
        // }
        // }
        return splitMessage;

    }

    public void setSheetName( String sheetName )
    {
        this.sheetName = sheetName;
    }

    public int getStartRow()
    {
        return startRow;
    }

    public void setStartRow( int startRow )
    {
        this.startRow = startRow;
    }

    public int getEndRow()
    {
        return endRow;
    }

    public void setEndRow( int endRow )
    {
        this.endRow = endRow;
    }

}
