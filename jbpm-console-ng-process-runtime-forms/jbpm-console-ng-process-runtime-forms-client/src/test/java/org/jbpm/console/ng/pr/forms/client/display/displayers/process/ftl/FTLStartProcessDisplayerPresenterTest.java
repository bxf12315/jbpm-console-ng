package org.jbpm.console.ng.pr.forms.client.display.displayers.process.ftl;

import static org.mockito.Mockito.*;
import org.jboss.errai.common.client.api.Caller;
import org.jbpm.console.ng.bd.service.KieSessionEntryPoint;
import org.jbpm.console.ng.ga.forms.display.FormDisplayerConfig;
import org.jbpm.console.ng.ga.forms.display.view.FormContentResizeListener;
import org.jbpm.console.ng.pr.forms.display.process.api.ProcessDisplayerConfig;
import org.jbpm.console.ng.pr.model.ProcessDefinitionKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mvp.Command;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class FTLStartProcessDisplayerPresenterTest {


    @Mock
    protected Caller<KieSessionEntryPoint> sessionServices;
    
    @Mock
    FTLStartProcessDisplayerViewImpl view;

    @Mock
    KieSessionEntryPoint kieSessionEntryPoint;

    @InjectMocks
    private FTLStartProcessDisplayerPresenter presenter;

    private static String domainId = "FtlFormtest";
    private static String processId = "com.test";

    @Before
    public void setupMocks() {
        sessionServices = new CallerMock<KieSessionEntryPoint>( kieSessionEntryPoint );
        presenter = new FTLStartProcessDisplayerPresenter( view, sessionServices );
        ProcessDefinitionKey key = new ProcessDefinitionKey( domainId, processId );
        FormDisplayerConfig<ProcessDefinitionKey> config = new ProcessDisplayerConfig( key, "test" );
        presenter.init( config, any( Command.class ), any( Command.class ), any( FormContentResizeListener.class ) );
    }

    @Test
    public void testAfterStartProcess() {
////        when( kieSessionEntryPoint.startProcess( domainId, processId, any( String.class ), any( Map.class ) ) ).thenReturn( any( Long.class ) );
        JavaScriptObject jso = mock( JavaScriptObject.class );
//        when( jsniHelper.getParameters( jso ) ).thenReturn( mock(Map.class) );
//        when(sessionServices.call( presenter.getStartProcessRemoteCallback(), presenter.getUnexpectedErrorCallback() ).startProcess( domainId, processId )).thenReturn( mock(Long.class) );
//        when(newProcessInstanceEvent.f)
//        presenter.startProcess(jso );
//
//        verify( presenter ).close();
    }
}
