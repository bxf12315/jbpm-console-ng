package org.jbpm.console.ng.ht.forms.modeler.client.editors.taskform.displayers;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.console.ng.ht.forms.modeler.service.FormModelerProcessStarterEntryPoint;
import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerPresenter;
//import org.jbpm.console.ng.pr.forms.display.process.api.StartProcessFormDisplayerPresenterView;
import org.jbpm.formModeler.api.events.FormSubmittedEvent;
import org.jbpm.formModeler.api.events.ResizeFormcontainerEvent;
import org.jbpm.formModeler.renderer.client.FormRendererWidget;

@Dependent
public class FormModellerStartProcessDisplayerPresenter extends AbstractStartProcessFormDisplayerPresenter {

    public interface FormModellerStartProcessDisplayerView {
        void initDisplayerView();
        FormRendererWidget getFormRenderer();
    }
    
    private Caller<FormModelerProcessStarterEntryPoint> renderContextServices;
    private static final String ACTION_START_PROCESS = "startProcess";
    protected String action;
    private FormModellerStartProcessDisplayerView view;
    
    @Inject
    public FormModellerStartProcessDisplayerPresenter( Caller<FormModelerProcessStarterEntryPoint> renderContextServices ,FormModellerStartProcessDisplayerView view ){
        this.renderContextServices = renderContextServices;
        this.view = view;
        
    }

    @Override
    protected void initDisplayer() {
       view.initDisplayerView();

    }

    public void startProcessFromDisplayer() {
        submitForm( ACTION_START_PROCESS );
    }

    protected void submitForm( String action ) {
        this.action = action;
        view.getFormRenderer().submitFormAndPersist();
    }


    @Override
    public void close() {
        renderContextServices.call( new RemoteCallback<Void>() {
            @Override
            public void callback( Void response ) {
                ((FormModellerStartProcessDisplayerViewImpl)view).setFormContent( null );
                FormModellerStartProcessDisplayerPresenter.super.close();
            }
        } ).clearContext( ((FormModellerStartProcessDisplayerViewImpl)view).getFormContent() );
    }


    public void onFormSubmitted( @Observes FormSubmittedEvent event ) {
        if ( event.isMine( ((FormModellerStartProcessDisplayerViewImpl)view).getFormContent() ) ) {
            if ( event.getContext().getErrors() == 0 ) {
                if ( ACTION_START_PROCESS.equals( action ) ) {
                    renderContextServices.call( getStartProcessRemoteCallback(), getUnexpectedErrorCallback() )
                            .startProcessFromRenderContext( ((FormModellerStartProcessDisplayerViewImpl)view).getFormContent(), deploymentId, processDefId,((FormModellerStartProcessDisplayerViewImpl)view).getCorrelationKey(), parentProcessInstanceId );
                }
            }
        }
    }

    public void onFormResized( @Observes ResizeFormcontainerEvent event ) {
        if ( event.isMine( ((FormModellerStartProcessDisplayerViewImpl)view).getFormContent() ) ) {
            view.getFormRenderer().resize( event.getWidth(), event.getHeight() );
            if ( resizeListener != null ) {
                resizeListener.resize( event.getWidth(), event.getHeight() );
            }
        }
    }
    
    @Override
    public AbstractStartProcessFormDisplayerView getView() {
        return ((FormModellerStartProcessDisplayerViewImpl)view);
    }

    @Override
    public AbstractStartProcessFormDisplayerView getSpecificView() {
        return ((FormModellerStartProcessDisplayerViewImpl)view);
    }
}
