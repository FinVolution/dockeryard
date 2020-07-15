package com.ppdai.dockeryard.admin.cleanup.handler.invoker;

import com.ppdai.dockeryard.admin.cleanup.handler.Handler;
import org.springframework.util.Assert;

import java.util.List;

public class ImageHandlerInvoker {

    private List<Handler> handlers;

    public ImageHandlerInvoker(List<Handler> handlers) {
        Assert.notNull(handlers, "handlers not null");
        this.handlers = handlers;
    }

    public void execute() {
        handlers.forEach(Handler::handle);
    }

}
