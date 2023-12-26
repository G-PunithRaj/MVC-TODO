package todo.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import todo.dao.TodoDAO;
import todo.dto.ToDoUser;
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
			map.put("dob", "*Your Not Aligible*");
			return "Signup";
		} else {
			List<ToDoUser> list = dao.findBYGmail(user.getGmail());
			if (list.isEmpty()) {
				dao.Save(user);
				map.put("pass", "Account Created Successfully");
				return "Login";
			} else {
				map.put("gmail", "Gmail Already Exist");
				return "Signup";
			}
		}
	}
}
