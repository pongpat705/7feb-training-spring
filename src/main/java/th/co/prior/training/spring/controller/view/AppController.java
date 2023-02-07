package th.co.prior.training.spring.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/app")
    public String appView(Model model){
        model.addAttribute("name", "Mao");
        return "index";
    }


}
