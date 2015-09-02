package org.badun.jwtdemo.web;

import org.badun.jwtdemo.web.model.HeartBeatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Artsiom Badun.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    public HeartBeatModel heartBeat() {
        return new HeartBeatModel();
    }
}
