package org.jbpm.console.ng.ga.forms.display;

import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.ga.service.ItemKey;
import org.uberfire.mvp.Command;

public interface GeneriFormDisplayerPresenter<T extends ItemKey> {

    void init(FormDisplayerConfig<T> config, Command onCloseCommand, Command onRefreshCommand, FormContentResizeListener formContentResizeListener);
    
    void addOnCloseCallback( Command callback );

    void addOnRefreshCallback( Command callback );
    
    void close();
    
    GenericFormDisplayerView getView();
}
