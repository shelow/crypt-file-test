package fr.salim.equisign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping(value = "/download")
    public String download() {
        return "download";
    }
    @GetMapping(value = "/upload")
    public String upload() {
        return "upload";
    }
}
