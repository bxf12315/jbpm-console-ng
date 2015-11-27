package org.jbpm.console.ng.ga.forms.display;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;


public interface GenericFormDisplayerView  {
    boolean supportsContent(String content);

    Panel getContainer();

    IsWidget getFooter();

    int getPriority();

    String getOpener();
}
