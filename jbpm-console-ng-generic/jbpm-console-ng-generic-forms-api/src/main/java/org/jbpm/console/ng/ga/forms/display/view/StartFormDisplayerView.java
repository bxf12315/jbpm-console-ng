package org.jbpm.console.ng.ga.forms.display.view;

import org.jbpm.console.ng.ga.forms.display.GeneriFormDisplayerPresenter;
import org.jbpm.console.ng.ga.service.ItemKey;
import org.uberfire.mvp.Command;


public interface StartFormDisplayerView {
    void display(GeneriFormDisplayerPresenter<? extends ItemKey> display);

    Command getOnCloseCommand();

    void setOnCloseCommand(Command onCloseCommand);

    FormContentResizeListener getResizeListener();

    void setResizeListener(FormContentResizeListener resizeListener);

    GeneriFormDisplayerPresenter getCurrentDisplayer();
}
