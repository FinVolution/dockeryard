package com.ppdai.dockeryard.proxy.ssh;

import java.io.IOException;

public interface Commander {

    void exec(String command) throws IOException, InterruptedException;

}
