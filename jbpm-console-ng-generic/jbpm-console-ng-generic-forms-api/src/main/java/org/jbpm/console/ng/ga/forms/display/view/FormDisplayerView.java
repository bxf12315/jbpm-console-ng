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

package org.jbpm.console.ng.ga.forms.display.view;

import org.jbpm.console.ng.ga.forms.display.GenericFormDisplayer;
import org.jbpm.console.ng.ga.service.ItemKey;
import org.uberfire.mvp.Command;

public interface FormDisplayerView {
    void display(GenericFormDisplayer<? extends ItemKey> display);

    Command getOnCloseCommand();

    void setOnCloseCommand(Command onCloseCommand);

    FormContentResizeListener getResizeListener();

    void setResizeListener(FormContentResizeListener resizeListener);

    GenericFormDisplayer getCurrentDisplayer();
}
