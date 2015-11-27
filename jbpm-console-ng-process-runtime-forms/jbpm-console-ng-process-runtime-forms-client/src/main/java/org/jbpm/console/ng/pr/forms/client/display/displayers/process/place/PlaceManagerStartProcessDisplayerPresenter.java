package org.jbpm.console.ng.pr.forms.client.display.displayers.process.place;

import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jbpm.console.ng.bd.service.KieSessionEntryPoint;
import org.jbpm.console.ng.gc.forms.client.display.displayers.util.PlaceManagerFormActivitySearcher;
import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerPresenter;
import org.jbpm.console.ng.pr.forms.display.process.api.StartProcessFormDisplayerPresenterView;
import org.uberfire.ext.widgets.common.client.forms.GetFormParamsEvent;
import org.uberfire.ext.widgets.common.client.forms.RequestFormParamsEvent;
import org.uberfire.ext.widgets.common.client.forms.SetFormParamsEvent;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

@Dependent
public class PlaceManagerStartProcessDisplayerPresenter extends AbstractStartProcessFormDisplayerPresenter {

    private Caller<KieSessionEntryPoint> sessionServices;

    private PlaceManagerFormActivitySearcher placeManagerFormActivitySearcher;

    private Event<SetFormParamsEvent> setFormParamsEvent;

    private Event<RequestFormParamsEvent> requestFormParamsEvent;
    
    private PlaceManagerStartProcessDisplayerPresenterView view;

    public interface PlaceManagerStartProcessDisplayerPresenterView {

    }

    @Inject
    public PlaceManagerStartProcessDisplayerPresenter(Caller<KieSessionEntryPoint> sessionServices,
            PlaceManagerFormActivitySearcher placeManagerFormActivitySearcher,
            Event<SetFormParamsEvent> setFormParamsEvent,
            Event<RequestFormParamsEvent> requestFormParamsEvent,
            PlaceManagerStartProcessDisplayerPresenterView view) {
        this.sessionServices = sessionServices;
        this.placeManagerFormActivitySearcher = placeManagerFormActivitySearcher;
        this.setFormParamsEvent = setFormParamsEvent;
        this.requestFormParamsEvent = requestFormParamsEvent;
        this.view = view;

    }

    @Override
    protected void initDisplayer() {
        JSONValue jsonValue = JSONParser.parseStrict( ((PlaceManagerStartProcessDisplayerViewImpl)view).getFormContent() );

        JSONObject jsonObject = jsonValue.isObject();

        if ( jsonObject != null ) {
            ((PlaceManagerStartProcessDisplayerViewImpl)view).getFormContainer().setWidth( "100%" );
            ((PlaceManagerStartProcessDisplayerViewImpl)view).getFormContainer().setHeight( "400px" );

            JSONValue jsonDestination = jsonObject.get( "destination" );

            if ( jsonDestination == null )
                return;

            String destination = jsonDestination.isString().stringValue();

            JSONObject jsonParams = jsonObject.get( "params" ).isObject();

            if ( jsonParams == null )
                return;

            Map<String, String> params = jsniHelper.parseParams( jsonParams );

            placeManagerFormActivitySearcher.findFormActivityWidget( destination, ((PlaceManagerStartProcessDisplayerViewImpl)view).getFormContainer() );
            setFormParamsEvent.fire( new SetFormParamsEvent( params, false ) );
        }
    }

   
    @Override
    public void close() {
        super.close();
    }

    public void startProcess() {
        requestFormParamsEvent.fire( new RequestFormParamsEvent( "startProcess" ) );
    }

    public void startProcessCallback( @Observes GetFormParamsEvent event ) {

        if ( processDefId == null || deploymentId == null )
            return;

        if ( event.getAction().equals( "startProcess" ) ) {
            sessionServices.call( getStartProcessRemoteCallback(), getUnexpectedErrorCallback() )
                    .startProcess( deploymentId, processDefId, event.getParams() );
        }
    }

    @Override
    public void startProcessFromDisplayer() {
        startProcess();
    }

    @Override
    public StartProcessFormDisplayerPresenterView getView() {
        return ((PlaceManagerStartProcessDisplayerViewImpl)view);
    }

    @Override
    public AbstractStartProcessFormDisplayerView getSpecificView() {
        return ((PlaceManagerStartProcessDisplayerViewImpl)view);
    }
}
