package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ibf2022.batch2.ssf.frontcontroller.model.Captcha;
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
		Captcha cap = new Captcha();
		m.addAttribute("captcha", cap);
		sess.setAttribute("captcha", cap);
        return "view0";
    }

	@PostMapping(path={"/login"})
	public String postLogin(Model m, HttpSession sess, Captcha captc, @Valid User user, BindingResult bindings) throws Exception{
		if (bindings.hasErrors()){
			return "view0";
		}

		int attempts=0;
		if(!(null==authServ.findUser(user.getUsername()))){
			System.out.println("went through");
			attempts = authServ.findUser(user.getUsername()).getAttempts();
			user.setAttempts(attempts);
		};

		if(attempts>=3){
			authServ.disableUser(user.getUsername());
			authServ.resetAttempt(user.getUsername());
			m.addAttribute("disableuser", user.getUsername());
			return "view2";
		}

		if(!(null==authServ.findDisable(user.getUsername()))){
			m.addAttribute("disableuser", user.getUsername());
			return "view2";
		};
		System.out.println("no. of attempts:   " + String.valueOf(attempts));

		authServ.saveUser(user);

		Captcha capt = (Captcha)sess.getAttribute("captcha");
		// System.out.println("This is the captcha answer");

		//Captcha capt = (Captcha)m.getAttribute("captcha");
		// System.out.println("Is captcha visible? : " + String.valueOf(capt.isVisible()));
		//System.out.println(((Captcha)m.getAttribute("captcha")).getAnswer());
		if(capt.isVisible()){
			// System.out.println("The first Number is " + String.valueOf(capt.getFirstNum()));
			// System.out.println("The secondNumber is" + String.valueOf(capt.getSecondNum()));
			// System.out.println("The operator is " + capt.getOperator());
			Float answer = authServ.calc(capt.getFirstNum(),capt.getSecondNum(),capt.getOperator());
			// System.out.println("The calc answer is " + String.valueOf(answer));
			// System.out.println("The provided answer is" + String.valueOf(captc.getAnswer()));
			if(answer.equals((float)captc.getAnswer())){
				System.out.println("Correct captcha answer");
				authServ.authenticate(user.getUsername(), user.getPassword());
				User authUser = authServ.findUser(user.getUsername());
				if (authUser.isAuthenticated()){
					sess.setAttribute("username", user.getUsername());
					return "view1";
				}
				m.addAttribute("error", AuthenticationService.ERROR_MSG);
			}else{
				System.out.println("Wrong captcha answer");
				m.addAttribute("error", "Wrong Captcha");
			}
			//Either wrong captcha or user is not authenticated
			System.out.println("Increaseing attempts....");
			authServ.increaseAttempt(user.getUsername());
			//authServ.saveUser(user);
			Captcha cap = new Captcha();
			cap.setVisible(true);
			//System.out.println("the captcha is now visible" + String.valueOf(cap.isVisible()));
			m.addAttribute("captcha", cap);
			sess.setAttribute("captcha", cap);
			return "view0";
		}else{ //first time (captcha not visible)
			authServ.authenticate(user.getUsername(), user.getPassword());
		
			User authUser = authServ.findUser(user.getUsername());
			
			if (authUser.isAuthenticated()){
				sess.setAttribute("username", user.getUsername());
				return "view1";
			}else{
				authServ.increaseAttempt(user.getUsername());
				m.addAttribute("error", AuthenticationService.ERROR_MSG);
				Captcha cap = new Captcha();
				cap.setVisible(true);
				//System.out.println("the captcha is now visible" + String.valueOf(cap.isVisible()));
				m.addAttribute("captcha", cap);
				sess.setAttribute("captcha", cap);
				return "view0";
			}
		}
	}
	// TODO: Task 2, Task 3, Task 4, Task 6
	@GetMapping(path={"/login/logout"})
	public String getLogout(Model m , HttpSession sess){
        //sess.invalidate();
        //m.addAttribute("pizza", new Pizza());
		String username = (String) sess.getAttribute("username");
		User user = authServ.findUser(username);
		user.setAuthenticated(false);
		authServ.saveUser(user);
		m.addAttribute("user", new User());
		Captcha cap = new Captcha();
		m.addAttribute("captcha", cap);
		sess.setAttribute("captcha", cap);
        return "view0";
    }
}
