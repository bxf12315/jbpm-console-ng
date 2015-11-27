/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.console.ng.gc.forms.client.display.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jbpm.console.ng.ga.forms.display.GeneriFormDisplayerPresenter;
import org.jbpm.console.ng.ga.forms.display.GenericFormDisplayer;
import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.ga.forms.display.view.StartFormDisplayerView;
import org.uberfire.mvp.Command;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmbeddedStartFormDisplayView implements StartFormDisplayerView {

    @Inject
    private VerticalPanel formContainer;

    private GeneriFormDisplayerPresenter currentDisplayer;

    private Command onCloseCommand;

    private FormContentResizeListener resizeListener;

    @PostConstruct
    public void init() {
        formContainer.setWidth("100%");
    }


    @Override
    public void display(GeneriFormDisplayerPresenter displayer) {
        currentDisplayer = displayer;
        formContainer.clear();
        formContainer.add(displayer.getView().getContainer());
        if (displayer.getView().getOpener() == null) formContainer.add(displayer.getView().getFooter());
    }

    public Widget getView() {
        return formContainer;
    }

    @Override
    public Command getOnCloseCommand() {
        return onCloseCommand;
    }

    @Override
    public void setOnCloseCommand(Command onCloseCommand) {
        this.onCloseCommand = onCloseCommand;
    }

    @Override
    public FormContentResizeListener getResizeListener() {
        return resizeListener;
    }

    @Override
    public void setResizeListener(FormContentResizeListener resizeListener) {
        this.resizeListener = resizeListener;
    }

    @Override
    public GeneriFormDisplayerPresenter getCurrentDisplayer() {
        return currentDisplayer;
    }
}
