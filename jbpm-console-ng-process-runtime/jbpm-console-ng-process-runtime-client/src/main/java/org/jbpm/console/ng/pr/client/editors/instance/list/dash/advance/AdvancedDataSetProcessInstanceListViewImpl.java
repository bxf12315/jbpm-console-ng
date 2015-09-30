package org.jbpm.console.ng.pr.client.editors.instance.list.dash.advance;

import java.util.List;

import javax.enterprise.context.Dependent;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jbpm.console.ng.gc.client.experimental.grid.base.ExtendedPagedTable;
import org.jbpm.console.ng.pr.client.editors.instance.list.dash.BaseDataSetProcessInstanceListViewImpl;
import org.jbpm.console.ng.pr.model.ProcessInstanceSummary;
import org.kie.api.runtime.process.ProcessInstance;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class AdvancedDataSetProcessInstanceListViewImpl extends BaseDataSetProcessInstanceListViewImpl
        implements AdvancedDataSetProcessInstanceListPresenter.AdvancedDataSetProcessInstanceListView {

    interface AdvancedDataSetProcessInstanceListViewBinder extends UiBinder<Widget, AdvancedDataSetProcessInstanceListViewImpl> {
    }

    @Override
    protected void controlBulkOperations() {
        if ( selectedProcessInstances != null && selectedProcessInstances.size() > 0 ) {
            bulkAbortNavLink.setEnabled( false );
            bulkSignalNavLink.setEnabled( false );
        } else {
            bulkAbortNavLink.setEnabled( true );
            bulkSignalNavLink.setEnabled( true );
        }
    }

    private class SignalActionHasCell implements HasCell<ProcessInstanceSummary, ProcessInstanceSummary> {

        private ActionCell<ProcessInstanceSummary> cell;

        public SignalActionHasCell(String text,
                Delegate<ProcessInstanceSummary> delegate) {
            cell = new ActionCell<ProcessInstanceSummary>( text, delegate ) {

                @Override
                public void render( Cell.Context context,
                        ProcessInstanceSummary value,
                        SafeHtmlBuilder sb ) {
                    if ( value.getState() == ProcessInstance.STATE_ACTIVE ) {
                        SafeHtmlBuilder mysb = new SafeHtmlBuilder();
                        mysb.appendHtmlConstant("<a href='javascript:;' class='btn btn-mini' style='margin-right:5px;' title='"+constants.Signal()+"'>"+constants.Signal()+"</a>");
                        sb.append( mysb.toSafeHtml() );
                    }
                }
            };
        }

        @Override
        public Cell<ProcessInstanceSummary> getCell() {
            return cell;
        }

        @Override
        public FieldUpdater<ProcessInstanceSummary, ProcessInstanceSummary> getFieldUpdater() {
            return null;
        }

        @Override
        public ProcessInstanceSummary getValue( ProcessInstanceSummary object ) {
            return object;
        }
    }
    
    @Override
    protected void initSpecificBulkActionsDropDown( final ExtendedPagedTable extendedPagedTable ) {
        bulkSignalNavLink = new AnchorListItem( constants.Bulk_Signal() );
        bulkSignalNavLink.setIcon( IconType.BELL );
        bulkSignalNavLink.addClickHandler( new ClickHandler() {

            @Override
            public void onClick( ClickEvent event ) {
                ((AdvancedDataSetProcessInstanceListPresenter) presenter).bulkSignal( selectedProcessInstances );
                selectedProcessInstances.clear();
                extendedPagedTable.redraw();
            }
        } );

    }

    @Override
    protected void initSpecificCells( final List<HasCell<ProcessInstanceSummary, ?>> cells ) {
        cells.add( new SignalActionHasCell( constants.Signal(), new Delegate<ProcessInstanceSummary>() {

            @Override
            public void execute( ProcessInstanceSummary processInstance ) {

                PlaceRequest placeRequestImpl = new DefaultPlaceRequest( "Signal Process Popup" );
                placeRequestImpl.addParameter( "processInstanceId", Long.toString( processInstance.getProcessInstanceId() ) );

                placeManager.goTo( placeRequestImpl );
            }
        } ) );        
        
    }
}