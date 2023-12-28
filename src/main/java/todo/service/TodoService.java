package todo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import todo.dao.TodoDAO;
import todo.dto.ToDoUser;
import todo.dto.TodoTask;
import todo.helper.AES;

@Component
public class TodoService {

	@Autowired
	TodoDAO dao;

	public String signup(ToDoUser user, String date, ModelMap map) {
		user.setDob(LocalDate.parse(date));
		user.setAge(Period.between(user.getDob(), LocalDate.now()).getYears());
		user.setPassword(AES.encrypt(user.getPassword(), "123"));

		if (user.getAge() < 18) {
			map.put("dob", "You are Not eligible");
			return "Signup";
		} else {
			List<ToDoUser> list = dao.findBYGmail(user.getGmail());
			if (list.isEmpty()) {
				dao.Save(user);
				map.put("pass", "Account Created Successfully");
				return "Login";
			} else {
				map.put("email", "Gmail Already Exist");
				return "Signup";
			}
		}
	}

	public String login(String gmail, String password, HttpSession session, ModelMap map) {
		List<ToDoUser> list = dao.findBYGmail(gmail);
		if (list.isEmpty()) {
			map.put("create", "Create Account");
			return "Login";
		} else {
			if (password.equals(AES.decrypt(list.get(0).getPassword(), "123"))) {
				session.setAttribute("TodoUser", list.get(0));

				map.put("list", dao.FetchAllTask(list.get(0).getId()));
				map.put("pass", "Login Successful");
				return "TodoHome";
			} else {
				map.put("password", "Incorrect email");
				return "Login";
			}
		}
	}

	public String logout(HttpSession session, ModelMap map) {
		session.invalidate();
		map.put("pass", "Logout Success");
		return "Login";

	}

	public String Loadhome(HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
			map.put("list", dao.FetchAllTask(user.getId()));
			return "TodoHome";
		}
	}

	public String addtask(HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
			return "add-task";
		}
	}

	public String addtask(TodoTask task, HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
			task.setCreatedTime(LocalDateTime.now());
			task.setUser(user);
			dao.save(task);

			map.put("list", dao.FetchAllTask(user.getId()));
			map.put("pass", "Task Added Successfully");
			return "TodoHome";
		}
	}

	public String changeStatus(int id, HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
			TodoTask task = (TodoTask) dao.fetchTaskByid(id);
			task.setStatus(true);
			dao.save(task);
			dao.delete(task);
			map.put("list", dao.FetchAllTask(user.getId()));
			return "TodoHome";
		}
	}

	public String Loadedit(int id, HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
			TodoTask task=dao.fetchTaskByid(id);
			map.put("task", task);
			return "EditTask";
		}
	}

	public String UpdateTask(TodoTask task, HttpSession session, ModelMap map) {
		ToDoUser user = (ToDoUser) session.getAttribute("TodoUser");
		if (user == null) {
			map.put("fail", "Invalid Session");
			return "Login";
		} else {
		  task.setUser(user);
          task.setCreatedTime(LocalDateTime.now());
		  dao.Update(task);
		  map.put("pass", "Updated Success");
		  map.put("list", dao.FetchAllTask(user.getId()));
		  return "TodoHome";
		}
	}
}
