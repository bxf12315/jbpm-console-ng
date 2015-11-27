package org.jbpm.console.ng.pr.forms.client.display.displayers.process.ftl;

import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jbpm.console.ng.bd.service.KieSessionEntryPoint;
import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerPresenter;
import org.jbpm.console.ng.pr.forms.display.process.api.StartProcessFormDisplayerPresenterView;

import com.google.gwt.core.client.JavaScriptObject;

@Dependent
public class FTLStartProcessDisplayerPresenter extends AbstractStartProcessFormDisplayerPresenter {
    
    private FTLStartProcessDisplayerPresenterView view;
    
    public interface FTLStartProcessDisplayerPresenterView {
        void initDisplayerView();
    } 
    private Caller<KieSessionEntryPoint> sessionServices;
    
    @Inject
    public FTLStartProcessDisplayerPresenter ( FTLStartProcessDisplayerPresenterView view, Caller<KieSessionEntryPoint> sessionServices ){
        this.view = view;
        this.sessionServices = sessionServices;
    }

    @Override
    protected void initDisplayer() {
        publish( this );
        jsniHelper.publishGetFormValues();
        ((FTLStartProcessDisplayerViewImpl)view).getFormContainer().clear();
        view.initDisplayerView();
        jsniHelper.injectFormValidationsScripts( ((FTLStartProcessDisplayerViewImpl)view).getFormContent() );

    }
    
    @Override
    public native void startProcessFromDisplayer() /*-{
        try {
            if ($wnd.eval("taskFormValidator()")) $wnd.startProcess($wnd.getFormValues($doc.getElementById("form-data")));
        } catch (err) {
            alert("Unexpected error: " + err);
        }
    }-*/;

    public void startProcess( JavaScriptObject values ) {
        final Map<String, Object> params = jsniHelper.getParameters( values );
        sessionServices.call( getStartProcessRemoteCallback(), getUnexpectedErrorCallback() )
                .startProcess( deploymentId, processDefId, ((FTLStartProcessDisplayerViewImpl)view).getCorrelationKey(), params );
    }

    protected native void publish( FTLStartProcessDisplayerPresenter ftl )/*-{
        $wnd.startProcess = function (form) {
            ftl.@org.jbpm.console.ng.pr.forms.client.display.displayers.process.ftl.FTLStartProcessDisplayerPresenter::startProcess(Lcom/google/gwt/core/client/JavaScriptObject;)(form);
        }
    }-*/;

    @Override
    public StartProcessFormDisplayerPresenterView getView() {
        return ((FTLStartProcessDisplayerViewImpl)view);
    }

    @Override
    public AbstractStartProcessFormDisplayerView getSpecificView() {
        return ((FTLStartProcessDisplayerViewImpl)view);
    }

}
