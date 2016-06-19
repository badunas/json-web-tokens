package org.badun.jwtdemo.web;

import org.badun.jwtdemo.web.model.HeartBeatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by badun on 6/19/16.
 */

@RestController
@RequestMapping(value = "/heartbeat")
public class HeartbeatController {

    @RequestMapping(method = RequestMethod.GET)
    public HeartBeatModel heartBeat() {
        return new HeartBeatModel();
    }
}
