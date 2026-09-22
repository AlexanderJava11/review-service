package alex.reviewservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${booking.service.web-url}")
    private String bookingServiceWebUrl;

    @GetMapping("/")
    public String home() {
        return "redirect:" + bookingServiceWebUrl + "/reviews";
    }
}