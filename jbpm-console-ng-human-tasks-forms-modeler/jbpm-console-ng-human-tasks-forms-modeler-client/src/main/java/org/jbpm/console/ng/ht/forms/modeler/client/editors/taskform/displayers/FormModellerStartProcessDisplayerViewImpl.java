package org.jbpm.console.ng.ht.forms.modeler.client.editors.taskform.displayers;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerViewImpl;
import org.jbpm.formModeler.renderer.client.FormRendererWidget;

import com.google.gwt.user.client.DOM;

@Dependent
public class FormModellerStartProcessDisplayerViewImpl extends AbstractStartProcessFormDisplayerViewImpl implements FormModellerStartProcessDisplayerPresenter.FormModellerStartProcessDisplayerView{

    @Inject
    private FormRendererWidget formRenderer;
    

    @Override
    public void initDisplayerView() {
        formRenderer.loadContext( formContent );

        formRenderer.setVisible( true );

        final PanelGroup accordion = new PanelGroup();
        accordion.setId( DOM.createUniqueId() );

        accordion.add( new Panel() {{
            final PanelCollapse collapse = new PanelCollapse() {{
                setIn( false );
                addHideHandler( new HideHandler() {
                    @Override
                    public void onHide( final HideEvent hideEvent ) {
                        hideEvent.stopPropagation();
                    }
                } );
                add( new PanelBody() {{
                    add( correlationKey );
                }} );
            }};
            add( new PanelHeader() {{
                add( new Heading( HeadingSize.H4 ) {{
                    add( new Anchor() {{
                        setText( constants.Correlation_Key() );
                        setDataToggle( Toggle.COLLAPSE );
                        setDataParent( accordion.getId() );
                        setDataTargetWidget( collapse );
                    }} );
                }} );
            }} );
            add( collapse );
        }} );

        accordion.add( new Panel() {{
            final PanelCollapse collapse = new PanelCollapse() {{
                setIn( true );
                addHideHandler( new HideHandler() {
                    @Override
                    public void onHide( final HideEvent hideEvent ) {
                        hideEvent.stopPropagation();
                    }
                } );
                add( new PanelBody() {{
                    add( formRenderer.asWidget() );
                }} );
            }};
            add( new PanelHeader() {{
                add( new Heading( HeadingSize.H4 ) {{
                    add( new Anchor() {{
                        setText( constants.Form() );
                        setDataToggle( Toggle.COLLAPSE );
                        setDataParent( accordion.getId() );
                        setDataTargetWidget( collapse );
                    }} );
                }} );
            }} );
            add( collapse );
        }} );

        formContainer.add( accordion );
        
    }

    @Override
    public boolean supportsContent( String content ) {
        return formRenderer.isValidContextUID( content );
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public FormRendererWidget getFormRenderer() {
        return formRenderer;
    }
}
