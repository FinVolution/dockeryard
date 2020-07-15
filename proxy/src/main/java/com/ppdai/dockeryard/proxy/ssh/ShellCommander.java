package com.ppdai.dockeryard.proxy.ssh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Component
public class ShellCommander implements Commander {

    @Override
    public void exec(String command) throws IOException, InterruptedException {
        log.info(String.format("命令%s执行开始", command));
        long start = System.currentTimeMillis();
        Process process = Runtime.getRuntime().exec(command);
        StreamGobbler streamGobblerInfo = new StreamGobbler(process.getInputStream(), log::info);
        StreamGobbler streamGobblerError = new StreamGobbler(process.getErrorStream(),log::error);
        CompletableFuture.runAsync(streamGobblerInfo);
        CompletableFuture.runAsync(streamGobblerError);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        log.info("命令执行结束，耗时:{}", System.currentTimeMillis() - start);

    }

    private class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}
