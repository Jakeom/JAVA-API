package com.softpuzzle.angkor.controller.fo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class InviteController {

    @RequestMapping(value = {"/"}, method = { RequestMethod.GET })
    public String inviteMain() {
        return "invite/invite";
    }

}
