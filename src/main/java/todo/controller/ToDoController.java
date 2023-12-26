package todo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import todo.dto.ToDoUser;
import todo.dto.TodoTask;
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
	public String SignUp(ToDoUser user, @RequestParam String date, ModelMap map) {
		return service.signup(user, date, map);
	}

	@PostMapping("/login")
	public String login(@RequestParam String gmail, String password, HttpSession session, ModelMap map) {
		return service.login(gmail, password, session, map);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, ModelMap map) {
		return service.logout(session, map);
	}

	@GetMapping("/home")
	public String LoadHome(HttpSession session, ModelMap map) {
		return service.Loadhome(session, map);
	}

	@GetMapping("/add-task")
	public String LoadAddTask(HttpSession session, ModelMap map) {
		return service.addtask(session, map);
	}
}
