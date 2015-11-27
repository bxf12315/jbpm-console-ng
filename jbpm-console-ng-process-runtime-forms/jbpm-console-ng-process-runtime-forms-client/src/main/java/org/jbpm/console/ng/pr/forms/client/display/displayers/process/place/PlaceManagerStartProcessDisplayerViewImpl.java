package org.jbpm.console.ng.pr.forms.client.display.displayers.process.place;

import javax.enterprise.context.Dependent;

import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerViewImpl;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

@Dependent
public class PlaceManagerStartProcessDisplayerViewImpl extends AbstractStartProcessFormDisplayerViewImpl implements PlaceManagerStartProcessDisplayerPresenter.PlaceManagerStartProcessDisplayerPresenterView {

    @Override
    public boolean supportsContent( String content ) {
        try {
            JSONValue jsonValue = JSONParser.parseStrict( content );

            JSONObject jsonObject;

            if ( (jsonObject = jsonValue.isObject()) == null )
                return false;

            jsonValue = jsonObject.get( "handler" );

            if ( jsonValue.isString() == null )
                return false;

            return jsonValue.isString().stringValue().equals( "handledByPlaceManagerFormProvider" );
        } catch ( Exception e ) {
        }
        return false;
    }
    @Override
    public int getPriority() {
        return 2;
    }
}
