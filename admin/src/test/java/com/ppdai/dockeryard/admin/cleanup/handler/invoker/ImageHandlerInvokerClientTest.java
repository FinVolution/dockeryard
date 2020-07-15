package com.ppdai.dockeryard.admin.cleanup.handler.invoker;

import com.ppdai.dockeryard.admin.AbstractUnitTest;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageOnlyMarkHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRemoveHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRepositoryGarbageCollectHandler;
import com.ppdai.dockeryard.admin.constants.CleanPolicyType;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

/**
 * Created by chenlang on 2020/5/14
 **/
public class ImageHandlerInvokerClientTest extends AbstractUnitTest {

    @Mock
    private ApplicationContext applicationContext;
    @InjectMocks
    private ImageHandlerInvokerClient imageHandlerInvokerClient;

    @Test
    public void onlyMakInvoke() {
        ImageOnlyMarkHandler markHandler = mock(ImageOnlyMarkHandler.class);
        ImageRemoveHandler removeHandler = mock(ImageRemoveHandler.class);
        ImageRepositoryGarbageCollectHandler collectHandler = mock(ImageRepositoryGarbageCollectHandler.class);
        when(applicationContext.getBean(ImageOnlyMarkHandler.class)).thenReturn(markHandler);
        when(applicationContext.getBean(ImageRemoveHandler.class)).thenReturn(removeHandler);
        when(applicationContext.getBean(ImageRepositoryGarbageCollectHandler.class)).thenReturn(collectHandler);
        imageHandlerInvokerClient.invoke(CleanPolicyType.ONLY_MARK);
        verify(markHandler, times(1)).handle();
        verify(removeHandler, times(0)).handle();
        verify(collectHandler, times(0)).handle();

    }

    @Test
    public void onlyCleanInvoke() {
        ImageOnlyMarkHandler markHandler = mock(ImageOnlyMarkHandler.class);
        ImageRemoveHandler removeHandler = mock(ImageRemoveHandler.class);
        ImageRepositoryGarbageCollectHandler collectHandler = mock(ImageRepositoryGarbageCollectHandler.class);
        when(applicationContext.getBean(ImageOnlyMarkHandler.class)).thenReturn(markHandler);
        when(applicationContext.getBean(ImageRemoveHandler.class)).thenReturn(removeHandler);
        when(applicationContext.getBean(ImageRepositoryGarbageCollectHandler.class)).thenReturn(collectHandler);
        imageHandlerInvokerClient.invoke(CleanPolicyType.ONLY_CLEAN);
        verify(markHandler, times(0)).handle();
        verify(removeHandler, times(1)).handle();
        verify(collectHandler, times(0)).handle();
    }

    @Test
    public void markCleanInvoke() {
        ImageOnlyMarkHandler markHandler = mock(ImageOnlyMarkHandler.class);
        ImageRemoveHandler removeHandler = mock(ImageRemoveHandler.class);
        ImageRepositoryGarbageCollectHandler collectHandler = mock(ImageRepositoryGarbageCollectHandler.class);
        when(applicationContext.getBean(ImageOnlyMarkHandler.class)).thenReturn(markHandler);
        when(applicationContext.getBean(ImageRemoveHandler.class)).thenReturn(removeHandler);
        when(applicationContext.getBean(ImageRepositoryGarbageCollectHandler.class)).thenReturn(collectHandler);
        imageHandlerInvokerClient.invoke(CleanPolicyType.MARK_CLEAN);

        InOrder inOrder = inOrder(markHandler, removeHandler);
        inOrder.verify(markHandler).handle();
        inOrder.verify(removeHandler).handle();
        verify(markHandler, times(1)).handle();
        verify(removeHandler, times(1)).handle();
        verify(collectHandler, times(0)).handle();

    }


    @Test
    public void collectInvoke() {
        ImageOnlyMarkHandler markHandler = mock(ImageOnlyMarkHandler.class);
        ImageRemoveHandler removeHandler = mock(ImageRemoveHandler.class);
        ImageRepositoryGarbageCollectHandler collectHandler = mock(ImageRepositoryGarbageCollectHandler.class);
        when(applicationContext.getBean(ImageOnlyMarkHandler.class)).thenReturn(markHandler);
        when(applicationContext.getBean(ImageRemoveHandler.class)).thenReturn(removeHandler);
        when(applicationContext.getBean(ImageRepositoryGarbageCollectHandler.class)).thenReturn(collectHandler);
        imageHandlerInvokerClient.invoke(CleanPolicyType.GARBAGE_COLLECT);
        verify(markHandler, times(0)).handle();
        verify(removeHandler, times(0)).handle();
        verify(collectHandler, times(1)).handle();

    }

}
