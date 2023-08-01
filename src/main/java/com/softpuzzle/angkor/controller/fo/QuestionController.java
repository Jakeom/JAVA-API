package com.softpuzzle.angkor.controller.fo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {

    @RequestMapping(value = {"/question/agree/{language}"}, method = { RequestMethod.GET })
    public String inviteMain(HttpServletRequest request, ModelMap model, @PathVariable("language") String language) {

        if(StringUtils.isBlank(language)) language = "en";

        model.addAttribute("language",  language);
        return "question/agree";
    }
}
