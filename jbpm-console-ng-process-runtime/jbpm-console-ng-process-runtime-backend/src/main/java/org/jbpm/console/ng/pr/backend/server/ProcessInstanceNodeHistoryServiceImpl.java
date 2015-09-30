package org.jbpm.console.ng.pr.backend.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.console.ng.ga.model.QueryFilter;
import org.jbpm.console.ng.pr.model.NodeInstanceLogKey;
import org.jbpm.console.ng.pr.model.NodeInstanceLogSummary;
import org.jbpm.console.ng.pr.service.ProcessInstanceNodeHistoryService;
import org.jbpm.services.api.RuntimeDataService;
import org.kie.internal.query.QueryContext;
import org.uberfire.paging.PageResponse;

@Service
@ApplicationScoped
public class ProcessInstanceNodeHistoryServiceImpl implements ProcessInstanceNodeHistoryService {

    @Inject
    private RuntimeDataService dataService;

    @Override
    public PageResponse<NodeInstanceLogSummary> getData( QueryFilter filter ) {
        PageResponse<NodeInstanceLogSummary> response = new PageResponse<NodeInstanceLogSummary>();
        Long processInstanceId = -1L;
        if ( filter.getParams() != null ) {
            processInstanceId = Long.parseLong( filter.getParams().get( "processInstanceId" ).toString() );
        }
        List<NodeInstanceLogSummary> nodeInstanceLogSummariesByStart = NodeInstanceLogHelper.adaptCollection( dataService.
                getProcessInstanceFullHistoryByType( processInstanceId, RuntimeDataService.EntryType.START, new QueryContext( 0, 100 ) ) );
        List<NodeInstanceLogSummary> nodeInstanceLogSummariesByCompleted = NodeInstanceLogHelper.adaptCollection( dataService.
                getProcessInstanceFullHistoryByType( processInstanceId, RuntimeDataService.EntryType.END, new QueryContext( 0, 100 ) ) );
        List<NodeInstanceLogSummary> nodeInstanceLogSummaries = mergeAllStatusNodeInstanceLogSymmaries( nodeInstanceLogSummariesByStart, nodeInstanceLogSummariesByCompleted );
        response.setStartRowIndex( filter.getOffset() );
        response.setTotalRowSize( nodeInstanceLogSummaries.size() - 1 );
        if ( nodeInstanceLogSummaries.size() > filter.getCount() ) {
            response.setTotalRowSizeExact( false );
        } else {
            response.setTotalRowSizeExact( true );
        }
        if ( !nodeInstanceLogSummaries.isEmpty() && nodeInstanceLogSummaries.size() > (filter.getCount() + filter.getOffset()) ) {
            response.setPageRowList( new ArrayList<NodeInstanceLogSummary>( nodeInstanceLogSummaries.subList( filter.getOffset(), filter.getOffset() + filter.getCount() ) ) );
            response.setLastPage( false );

        } else {
            response.setPageRowList( new ArrayList<NodeInstanceLogSummary>( nodeInstanceLogSummaries ) );
            response.setLastPage( true );
        }
        return response;
    }

    @Override
    public NodeInstanceLogSummary getItem( NodeInstanceLogKey key ) {
        return null;
    }

    @Override
    public NodeInstanceLogSummary getItem( Long workItemId ) {
        return NodeInstanceLogHelper.adapt( dataService.getNodeInstanceForWorkItem( workItemId ) );
    }

    private List<NodeInstanceLogSummary> mergeAllStatusNodeInstanceLogSymmaries( List<NodeInstanceLogSummary> nodeInstanceLogSummariesByStart, List<NodeInstanceLogSummary> nodeInstanceLogSummariesByCompleted ) {
        CollectionUtils.filter( nodeInstanceLogSummariesByStart, new MegrePredicate( nodeInstanceLogSummariesByCompleted ) );
        nodeInstanceLogSummariesByCompleted.addAll( 0, nodeInstanceLogSummariesByStart );
        return nodeInstanceLogSummariesByCompleted;
    }

    private class MegrePredicate implements Predicate {

        List<NodeInstanceLogSummary> nodeInstanceLogSummariesByCompleted;

        MegrePredicate(List<NodeInstanceLogSummary> nodeInstanceLogSummariesByCompleted) {
            this.nodeInstanceLogSummariesByCompleted = nodeInstanceLogSummariesByCompleted;
        }

        @Override
        public boolean evaluate( Object object ) {
            NodeInstanceLogSummary nodeLog = (NodeInstanceLogSummary) object;
            for ( NodeInstanceLogSummary node : nodeInstanceLogSummariesByCompleted ) {
                if ( node.getNodeId() == nodeLog.getNodeId() ) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public List<NodeInstanceLogSummary> getAll( QueryFilter filter ) {
        return null;
    }
}