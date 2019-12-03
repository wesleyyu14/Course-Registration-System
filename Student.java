import java.util.*;

interface StudentMethods {
	public boolean permission();
	public void setUsername(String username);
	public void setPassword(String password);
	public void setFirstName(String firstName);
	public void setLastName(String lastName);
	public String getUsername();
	public String getPassword();
	public String getFirstName();
	public String getLastName();
	public String getFullName();
	public List<Course> getCourseList();
	public void addCourse(Course c);
	public void removeCourse(Course c);
	public void printCourses();
	
}

public class Student extends User implements StudentMethods {
	
	List<Course> courseList; 
	
	Student(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		
		courseList = new ArrayList<>(); //the courses that the Student will be enrolled in
	}
	
	//Most getters and setters inherited from Users class
	
	public List<Course> getCourseList() {
		return this.courseList;
	}
	
	public void printCourses() {
		for (int i = 0; i < courseList.size(); i++) {
			System.out.println(courseList.get(i).getCourseName() + ", Section " + courseList.get(i).getCourseSectionNumber());
		}
	}
	
	public void addCourse(Course c) {
		this.courseList.add(c);
	}
	
	public void removeCourse(Course c) {
		for (int i = 0; i < courseList.size(); i++) {
			if (c.getCourseName().equals(courseList.get(i).getCourseName())) {
				this.courseList.remove(i);
			}
		} 
	}
}
