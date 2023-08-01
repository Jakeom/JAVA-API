package com.softpuzzle.angkor.controller.fo;

import com.softpuzzle.angkor.gcache.GlobalObjects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class QrCodeController {

    @RequestMapping(value = {"/qrcode"}, method = { RequestMethod.GET })
    public String qrScan(@RequestParam("u") String userId, ModelMap model) {

        log.info("{},{},{}",model,userId);

        model.addAttribute("iosLink",  String.format(GlobalObjects.getQrLinkIos(),userId));
        model.addAttribute("androidLink", String.format(GlobalObjects.getQrLinkAndoid(),userId));

        return "qrcode/qrcode";
    }
}
