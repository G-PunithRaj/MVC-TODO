package todo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import todo.dto.ToDoUser;
import todo.dto.TodoTask;

@Component
public class TodoDAO {
	@Autowired
	EntityManager Manager;

	public void Save(ToDoUser user) {
		Manager.getTransaction().begin();
		Manager.persist(user);
		Manager.getTransaction().commit();
	}

	public List<ToDoUser> findBYGmail(String gmail) {

		return Manager.createQuery("select x from ToDoUser x where gmail=?1").setParameter(1, gmail).getResultList();
	}

	public void save(TodoTask task) {
		Manager.getTransaction().begin();
		Manager.persist(task);
		Manager.getTransaction().commit();

	}

	public  List<TodoTask> FetchAllTask(int id) {
		return Manager.createQuery("select x from TodoTask x where user_id=?1").setParameter(1, id).getResultList();

	}

	public TodoTask fetchTaskByid(int id) {
		
		return Manager.find(TodoTask.class, id);
	}

	public void delete(TodoTask task) {
		Manager.getTransaction().begin();
		Manager.remove(task);
		Manager.getTransaction().commit();
		
	}
	public void Update(TodoTask task) {
		Manager.getTransaction().begin();
		Manager.merge(task);
		Manager.getTransaction().commit();
		
	}
}
