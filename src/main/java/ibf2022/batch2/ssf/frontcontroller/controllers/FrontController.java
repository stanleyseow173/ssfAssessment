package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ibf2022.batch2.ssf.frontcontroller.model.User;
import ibf2022.batch2.ssf.frontcontroller.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class FrontController {

	@Autowired
	AuthenticationService authServ;

	@GetMapping(path={"/", "/index.html"})
    public String getIndex(Model m , HttpSession sess){
        //sess.invalidate();
        //m.addAttribute("pizza", new Pizza());
		m.addAttribute("user", new User());
        return "view0";
    }

	@PostMapping(path={"/login"})
	public String postLogin(Model m, HttpSession sess, @Valid User user, BindingResult bindings) throws Exception{
		if (bindings.hasErrors()){
			return "view0";
		}

		authServ.saveUser(user);
		authServ.authenticate(user.getUsername(), user.getPassword());


		return "view1";
	}
	// TODO: Task 2, Task 3, Task 4, Task 6
	
}
