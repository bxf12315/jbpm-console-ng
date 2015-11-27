package org.jbpm.console.ng.pr.forms.display.process.api;

import java.util.Map;

import org.jbpm.console.ng.ga.forms.display.GeneriFormDisplayerPresenter;
import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.pr.model.ProcessDefinitionKey;

public interface StartProcessFormDisplayerPresenter extends GeneriFormDisplayerPresenter<ProcessDefinitionKey> {
    void startProcessFromDisplayer();

    void startProcess(Map<String, Object> params);

    void addResizeFormContent(FormContentResizeListener resizeListener);
    
//    StartProcessFormDisplayerPresenterView getView();
    
}
