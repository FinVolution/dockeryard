package com.ppdai.dockeryard.proxy.api;

import com.ppdai.dockeryard.proxy.ssh.Commander;
import com.ppdai.dockeryard.proxy.ssh.ShellCommander;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class SSHCommanderControllerApi {

    @Autowired
    private Commander commander;
    @Value("${dockeryard.image.repository.clean.commder:}")
    private String cleanCommand;

    @GetMapping("/image/clean")
    public void execute(@RequestParam(value = "command", required = false) String command) throws IOException, InterruptedException {
        command = StringUtils.defaultIfBlank(cleanCommand, command);
        String[] commands = command.split(";");
        for (String s : commands) {
            commander.exec(s);
        }
    }

}
