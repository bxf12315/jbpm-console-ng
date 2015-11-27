package org.jbpm.console.ng.pr.forms.client.display.displayers.process;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.console.ng.bd.service.DataServiceEntryPoint;
import org.jbpm.console.ng.bd.service.KieSessionEntryPoint;
import org.jbpm.console.ng.ga.forms.display.FormDisplayerConfig;
import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.gc.forms.client.display.displayers.util.ActionRequest;
import org.jbpm.console.ng.gc.forms.client.display.displayers.util.JSNIHelper;
import org.jbpm.console.ng.pr.forms.display.process.api.StartProcessFormDisplayerPresenter;
import org.jbpm.console.ng.pr.forms.display.process.api.StartProcessFormDisplayerPresenterView;
import org.jbpm.console.ng.pr.model.ProcessDefinitionKey;
import org.jbpm.console.ng.pr.model.ProcessSummary;
import org.jbpm.console.ng.pr.model.events.NewProcessInstanceEvent;
import org.uberfire.client.workbench.widgets.common.ErrorPopupPresenter;
import org.uberfire.mvp.Command;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

public abstract class AbstractStartProcessFormDisplayerPresenter implements StartProcessFormDisplayerPresenter {

    
    public static final String ACTION_START_PROCESS = "startProcess";

    public interface AbstractStartProcessFormDisplayerView extends StartProcessFormDisplayerPresenterView {

        void init( FormDisplayerConfig<ProcessDefinitionKey> config, final AbstractStartProcessFormDisplayerPresenter presenter );

        FlowPanel getFormContainer();

        FlowPanel getFooterButtons();

        String getCorrelationKey();
        
        void setFormContent(String formContent);
        
        String getFormContent();
        
        void setParentProcessInstanceId(Long parentProcessInstanceId);
        
    }

    @Inject
    protected ErrorPopupPresenter errorPopup;

    @Inject
    protected Caller<DataServiceEntryPoint> dataServices;

    @Inject
    protected Event<NewProcessInstanceEvent> newProcessInstanceEvent;

    @Inject
    protected Caller<KieSessionEntryPoint> sessionServices;

    @Inject
    protected JSNIHelper jsniHelper;


    protected String deploymentId;
    protected String processDefId;
    protected String processName;
    protected String opener;
    protected FormContentResizeListener resizeListener;
    protected Long parentProcessInstanceId;
    
    private Command onClose;

    private Command onRefresh;

    @PostConstruct
    protected void init() {
        getView().getContainer().getElement().setId( "form-data" );
    }

    public void init( FormDisplayerConfig<ProcessDefinitionKey> config, Command onCloseCommand, Command onRefreshCommand, FormContentResizeListener formContentResizeListener ) {
        this.deploymentId = config.getKey().getDeploymentId();
        this.processDefId = config.getKey().getProcessId();
        this.onClose = onCloseCommand;
        this.onRefresh = onRefreshCommand;
        this.resizeListener = formContentResizeListener;
        getSpecificView().init( config, this );

        dataServices.call( new RemoteCallback<ProcessSummary>() {

            @Override
            public void callback( ProcessSummary summary ) {
                processName = summary.getProcessDefName();
                FocusPanel wrapperFlowPanel = new FocusPanel();
                wrapperFlowPanel.setStyleName( "wrapper form-actions" );

                if ( opener != null ) {
                    injectEventListener( AbstractStartProcessFormDisplayerPresenter.this );
                }

                initDisplayer();

                doResize();
            }
        } ).getProcessDesc( config.getKey().getDeploymentId(), config.getKey().getProcessId() );
    }

    protected abstract void initDisplayer();

    public void doResize() {
        if ( resizeListener != null )
            resizeListener.resize( getSpecificView().getFormContainer().getOffsetWidth(), getSpecificView().getFormContainer().getOffsetHeight() );
    }

    public ErrorCallback<Message> getUnexpectedErrorCallback() {
        return new ErrorCallback<Message>() {

            @Override
            public boolean error( Message message, Throwable throwable ) {
                String notification = "Unexpected error encountered : " + throwable.getMessage();
                errorPopup.showMessage( notification );
                jsniHelper.notifyErrorMessage( opener, notification );
                return true;
            }
        };
    }

    @Override
    public void startProcess( Map<String, Object> params ) {
        if ( parentProcessInstanceId > 0 ) {
            sessionServices.call( getStartProcessRemoteCallback(), getUnexpectedErrorCallback() )
                    .startProcess( deploymentId, processDefId, getSpecificView().getCorrelationKey(), params, parentProcessInstanceId );

        } else {
            sessionServices.call( getStartProcessRemoteCallback(), getUnexpectedErrorCallback() )
                    .startProcess( deploymentId, processDefId, getSpecificView().getCorrelationKey(), params );
        }
    }

    public RemoteCallback<Long> getStartProcessRemoteCallback() {
        return new RemoteCallback<Long>() {

            @Override
            public void callback( Long processInstanceId ) {
                newProcessInstanceEvent.fire( new NewProcessInstanceEvent( deploymentId, processInstanceId, processDefId, processName, 1 ) );
                jsniHelper.notifySuccessMessage( opener, "Process Id: " + processInstanceId + " started!" );
                close();
            }
        };
    }

    @Override
    public void addOnCloseCallback( Command callback ) {
        this.onClose = callback;
    }

    @Override
    public void addOnRefreshCallback( Command callback ) {
        this.onRefresh = callback;
    }

    @Override
    public void addResizeFormContent( FormContentResizeListener resizeListener ) {
        this.resizeListener = resizeListener;
    }

    @Override
    public void close() {
        if ( this.onClose != null ) {
            this.onClose.execute();
        }
        clearStatus();
    }

    protected void clearStatus() {
        getSpecificView().setFormContent( null );
        opener = null;
        deploymentId = null;
        processDefId = null;
        processName = null;

        getSpecificView().getContainer().clear();
        getSpecificView().getFormContainer().clear();
        getSpecificView().getFooterButtons().clear();

        onClose = null;
        onRefresh = null;
        resizeListener = null;
    }

    protected void eventListener( String origin, String request ) {
        if ( origin == null || !origin.endsWith( "//" + opener ) ) {
            return;
        }

        ActionRequest actionRequest = JsonUtils.safeEval( request );

        if ( ACTION_START_PROCESS.equals( actionRequest.getAction() ) ) {
            startProcessFromDisplayer();
        }
    }
    
    public abstract AbstractStartProcessFormDisplayerView getSpecificView();

    private native void injectEventListener(AbstractStartProcessFormDisplayerPresenter fdp) /*-{
            function postMessageListener(e) {
                fdp.@org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerPresenter::eventListener(Ljava/lang/String;Ljava/lang/String;)(e.origin, e.data);
            }

            if ($wnd.addEventListener) {
                $wnd.addEventListener("message", postMessageListener, false);
            } else {
                $wnd.attachEvent("onmessage", postMessageListener, false);
            }
        }-*/;
    }

