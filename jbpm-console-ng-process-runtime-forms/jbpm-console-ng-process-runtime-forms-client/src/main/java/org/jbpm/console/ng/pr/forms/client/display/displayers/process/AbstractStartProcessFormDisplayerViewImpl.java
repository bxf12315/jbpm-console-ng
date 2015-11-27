package org.jbpm.console.ng.pr.forms.client.display.displayers.process;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.jbpm.console.ng.ga.forms.display.FormDisplayerConfig;
import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.pr.forms.client.display.displayers.process.AbstractStartProcessFormDisplayerPresenter.AbstractStartProcessFormDisplayerView;
import org.jbpm.console.ng.pr.forms.client.i18n.Constants;
import org.jbpm.console.ng.pr.model.ProcessDefinitionKey;
import org.uberfire.mvp.Command;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public abstract class AbstractStartProcessFormDisplayerViewImpl implements AbstractStartProcessFormDisplayerView {

    protected Constants constants = GWT.create( Constants.class );

    protected FormPanel container = GWT.create( FormPanel.class );
    protected FlowPanel formContainer = GWT.create( FlowPanel.class );
    protected FlowPanel footerButtons = GWT.create( FlowPanel.class );

    protected TextBox correlationKey = GWT.create(TextBox.class);
    protected Label correlationKeyLabel;

    protected String formContent;

    protected String deploymentId;
    protected String processDefId;
    protected String processName;
    protected String opener;
    protected FormContentResizeListener resizeListener;
    protected Long parentProcessInstanceId;

    private AbstractStartProcessFormDisplayerPresenter presenter;

    public void init( FormDisplayerConfig<ProcessDefinitionKey> config, final AbstractStartProcessFormDisplayerPresenter presenter ) {
        this.deploymentId = config.getKey().getDeploymentId();
        this.processDefId = config.getKey().getProcessId();
        this.formContent = config.getFormContent();
        this.opener = config.getFormOpener();
        this.presenter = presenter;

        container.clear();
        formContainer.clear();
        footerButtons.clear();

        container.add( formContainer );

        correlationKey = new TextBox();

        Button startButton = new Button( constants.Submit(), new ClickHandler() {

            @Override
            public void onClick( ClickEvent event ) {
                presenter.startProcessFromDisplayer();
            }
        } );
        startButton.setType( ButtonType.PRIMARY );
        footerButtons.add( startButton );

    }

    @Override
    public Panel getContainer() {
        return container;
    }

    @Override
    public IsWidget getFooter() {
        return footerButtons;
    }

    @Override
    public FlowPanel getFormContainer() {
        return formContainer;
    }

    @Override
    public FlowPanel getFooterButtons() {
        return footerButtons;
    }

    @Override
    public String getOpener() {
        return opener;
    }
    @Override
    public String getCorrelationKey() {
        return correlationKey.getText();
    }

    public Long getParentProcessInstanceId() {
        return parentProcessInstanceId;
    }

    public void setParentProcessInstanceId(Long parentProcessInstanceId) {
        this.parentProcessInstanceId = parentProcessInstanceId;
    }

    public void setFormContent( String formContent ){
        this.formContent = formContent;
    }
    
    public String getFormContent(){
        return formContent;
    }
}
