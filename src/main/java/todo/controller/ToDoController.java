package todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import todo.dto.ToDoUser;
import todo.service.TodoService;

@Controller
public class ToDoController {
	@Autowired
	TodoService service;
	
	@GetMapping({ "/", "login" })
	String Loadlogin() {
		return "Login";
	}

	@GetMapping({ "/signup" })
	String LoadSignUp() {
		return "Signup";
	}

	@PostMapping("/signup")
	public String SignUp(ToDoUser user, @RequestParam String date,ModelMap map) {
		return  service.signup(user,date,map);
	}
}
