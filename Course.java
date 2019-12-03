import java.util.*;

public class Course implements java.io.Serializable, Comparable<Course> {
	
	//List<String> courseBody;
	String courseName;
	String courseID;
	int maxStudents;
	int currentStudents;
	String fullName;
	List<String> nameList;
	String courseInstructor;
	String courseSectionNumber;
	String courseLocation;
	
	Course() {
		nameList = new ArrayList<>();
	}
	
	Course(String courseName, String courseID, int maxStudents, int currentStudents, String nameList, String courseInstructor, String courseSectionNumber, String courseLocation) {
		this.courseName = courseName;
		this.courseID = courseID; 
		this.maxStudents = maxStudents;
		this.currentStudents = currentStudents;
		this.courseInstructor = courseInstructor;
		this.courseSectionNumber = courseSectionNumber;
		this.courseLocation = courseLocation;
		//courseBody.add(this.courseName);
		
		this.nameList = new ArrayList<>();
		String[] val = nameList.split(";");
		for (int i = 0; i < val.length; i++) {
			this.nameList.add(val[i]);
		}
	}
	
	/*
	Course(String courseName, String courseID, int maxStudents, int currentStudents, String courseInstructor, int courseSectionNumber, String courseLocation) {
		this.courseName = courseName;
		this.courseID = courseID; 
		this.maxStudents = maxStudents;
		this.currentStudents = currentStudents;
		this.courseInstructor = courseInstructor;
		this.courseSectionNumber = courseSectionNumber;
		this.courseLocation = courseLocation;
		//courseBody.add(this.courseName);
		
		nameList = new ArrayList<>();
	}
	*/
	
	
	//setters -- add, remove, edit variables.
	public void addStudent(String firstName, String lastName, String username) {
		this.fullName = (firstName + " " + lastName);
		this.nameList.add(this.fullName + "," + username);
		this.currentStudents++;
		if (this.nameList.size() > 1) {
			removeStudent("NULL");
		}
	}
	
	public void addStudent(String fullName, String username) {
		this.fullName = fullName;
		this.nameList.add(this.fullName + "," + username);
		this.currentStudents++;
		if (this.nameList.size() > 1) {
			removeStudent("NULL");
		}
	}
	
	//doesn't increase currentStudents because the method is only used to add NULL back into the array. 
	//triggers this method because adding 'NULL' doesn't add a usernamec
	//example of overloading
	public void addStudent(String fullName) {
		this.fullName = fullName;
		this.nameList.add(this.fullName);
		if (this.nameList.size() > 1) {
			removeStudent("NULL");
		}
	}
	
	public void removeStudent(String firstName, String lastName, String username) {
		this.fullName = (firstName + " " + lastName);
		nameList.remove(this.fullName + "," + username);
		this.currentStudents--;
		if (this.nameList.size() < 1) {
			addStudent("NULL");
		}
	}
	
	public void removeStudent(String fullName, String username) {
		this.fullName = fullName; 
		nameList.remove(this.fullName + "," + username);
		this.currentStudents--;
		if (this.nameList.size() < 1) {
			addStudent("NULL");
		}
	}
	
	//same as above except reversed; doesn't decrease because it's only NULL. 
	public void removeStudent(String fullName) {
		this.fullName = fullName; 
		nameList.remove(this.fullName);
		if (this.nameList.size() < 1) {
			addStudent("NULL");
		}
	}
	
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}
	
	public void setCourseInstructor(String courseInstructor) {
		this.courseInstructor = courseInstructor;
	}
	public void setCourseSectionNumber(String courseSectionNumber) {
		this.courseSectionNumber = courseSectionNumber;
	}
	public void setCourseLocation(String courseLocation) {
		this.courseLocation = courseLocation;
	}
	
	//print StudentList of Course
	public void printStudents() {
		for (int i = 0; i < nameList.size(); i++) {
			System.out.println(nameList.get(i));
		}
	}
	
	//getters
	public String getCourseName() {
		return this.courseName;
	}
	
	public String getCourseID() {
		return this.courseID;
	}
	
	public int getMaxStudents() {
		return this.maxStudents;
	}
	
	public int getCurrentStudents() {
		
		return this.currentStudents;
	}

	public List<String> getNameList() {
		return this.nameList;
	}
	
	public String getCourseInstructor() {
		return this.courseInstructor;
	}
	
	public String getCourseSectionNumber() {
		return this.courseSectionNumber;
	}
	
	public String getCourseLocation() {
		return this.courseLocation;
	}
	
	public Boolean isFull() {
		if (this.currentStudents >= this.maxStudents) {
			return true;
		} else {
			return false;
		}
	}
	public Boolean isNotFull() {
		if (this.currentStudents < this.maxStudents) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Course o) {
		// TODO Auto-generated method stub
		return this.getCurrentStudents() - o.getCurrentStudents();
	}
}
