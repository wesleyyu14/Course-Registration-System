import java.io.*;
import java.util.*;

public class CourseRegistrationSystem {
	//main method
	public static void main(String[] args) {
		
		//init. arrayLists
		List<Course> courseList = new ArrayList<>();
		List<Student> studentList = new ArrayList<>();
		
		File serCoursesFile = new File("CourseList.ser");
		File serStudentFile = new File("StudentList.ser");
		
		 //If the CourseList.ser file does not exist, 
		 //import from CSV file, then serialize.
		if (!serCoursesFile.exists()){
			
			//import data from CSV file
			String fileName = "MyUniversityCourses.csv";
			readCSV(courseList, fileName);
			
			//then serialize courses
			serializeCourses(courseList);
			
			if (serStudentFile.exists()) {       //unlikely that the StudentList.ser file would exist if the CourseList.ser didn't, but added just for safekeeping. 
				studentList = deserializeStudents(studentList);
			} else {
				serializeStudents(studentList); //serialize students
			}
			
			login(courseList, studentList); //login method
			
		} else {
			
			courseList = deserializeCourses(courseList); //else if CourseList.ser doesn't exist, de-serialize.
			
			if (serStudentFile.exists()) {
				studentList = deserializeStudents(studentList);
			} else {
				serializeStudents(studentList); //unlikely that StudentList.ser wouldn't exist, but here for safekeeping, again. 
			}
			
			login(courseList, studentList); //login method
		}
	}

	//imports CSV data into Course ArrayList
	public static void readCSV(List<Course> csvList, String fileName) {
		
		String line = null;
		int counter = 0; //to skip the first line of CSV file
		try{
			FileReader fileReader = new FileReader(fileName);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				
				//Splits CSV values into String array. Have to convert some to int.
				String[] val = line.split(",");
				
				//skip first line
				if (counter > 0) {
				Course courses = new Course(val[0], val[1], Integer.parseInt(val[2]), Integer.parseInt(val[3]), val[4], val[5], val[6], val[7]);
				csvList.add(courses);
				}
				
				counter++;
			}
			
			bufferedReader.close();
		}
		
		catch(FileNotFoundException ex){
			System.out.println( "Unable to open file '" + fileName + "'");
			ex.printStackTrace();
		}

		catch (IOException ex) {
			System.out.println( "Error reading file '" + fileName + "'");
			ex.printStackTrace();
		}
	}

	//serialize ArrayList of Course Objects to courseList.ser
	public static void serializeCourses(List<Course> courseList) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream("CourseList.ser");
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(courseList);
			
			oos.close();
			fos.close();
			//System.out.println("Serialization complete");
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	//de-serialize into ArrayList of Course Objects
	public static List<Course> deserializeCourses(List<Course> courseList) {
		
		try{
		      FileInputStream fis = new FileInputStream("CourseList.ser");
		     
		      ObjectInputStream ois = new ObjectInputStream(fis);
		      
		      //de-serialize into this List of Course Objects
		      courseList = (List<Course>) ois.readObject();
		      ois.close();
		      fis.close();
		      
		      //return the courseList
		      return courseList;
		 }
		 catch(IOException ioe) {
		       ioe.printStackTrace();
		       return null;
		 }
		 catch(ClassNotFoundException cnfe) {
		       cnfe.printStackTrace();
		       return null;
		 }
	}
	
	//serialize ArrayList of Student Objects to studentList.ser
	public static void serializeStudents(List<Student> studentList) {
		try {
			
			FileOutputStream fos = new FileOutputStream("StudentList.ser");
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(studentList);
			
			oos.close();
			fos.close();
			//System.out.println("Serialization complete");
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	//de-serialize into ArrayList of Student Objects
	public static List<Student> deserializeStudents(List<Student> studentList) {
		try{
		      FileInputStream fis = new FileInputStream("StudentList.ser");
		     
		      ObjectInputStream ois = new ObjectInputStream(fis);
		      
		      //de-serialize into List of Student Objects
		      studentList = (List<Student>) ois.readObject();
		      ois.close();
		      fis.close();
		      
		      //the return the List
		      return studentList;
		 }
		 catch(IOException ioe) {
		       ioe.printStackTrace();
		       return null;
		 }
		 catch(ClassNotFoundException cnfe) {
		       cnfe.printStackTrace();
		       return null;
		 }
	}
	
	//login method
	public static void login(List<Course> courseList, List<Student> studentList) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Type 'quit' or 'q' at any given point, to quit program");
		System.out.println("(The program saves your changes when you quit)");
		
		while(true) { //while loop for "Wrong u/p, please try again!"
			String username;
			String password;
			
			System.out.println("Username: ");
			username = input.nextLine();
			
			q(username, courseList, studentList); //quit function every time there is a prompt for userInput
			
			System.out.println("Password: ");
			password = input.nextLine();
			
			q(password, courseList, studentList);
			
			Boolean studentStatus = false; //is user a student or admin?
			int studentInt = 0; //if student, which student are they?
			
			Student tempUser = new Student(username, password, null, null); 
			//iterates through studentList attempting a match with tempStudent
			for (int i = 0; i < studentList.size(); i++) {
				if (tempUser.getUsername().equals(studentList.get(i).getUsername()) && (tempUser.getPassword().equals(studentList.get(i).getPassword()))) {
					studentStatus = true;
					studentInt = i;
				}
			}
			
			//if Admin, we go to mainMenuAdmin
			if (username.equals("Admin") && password.equals("Admin001")) {
				System.out.println("Welcome, Admin!"); 
				mainMenuAdmin(input, courseList, studentList);
				
			//else if studentStatus = true, we go to mainMenuStudent
			} else if (studentStatus) {
				tempUser.setFirstName(studentList.get(studentInt).getFirstName());
				tempUser.setLastName(studentList.get(studentInt).getLastName());
				System.out.println("Welcome, " + tempUser.getFullName());
				mainMenuStudent(input, courseList, studentList, tempUser);
			} else {
				System.out.println("Wrong username or password, please try again: ");
			}
		}
	}
	
	//main menu method for Admins 
	public static void mainMenuAdmin(Scanner input, List<Course> courseList, List<Student> studentList) {
		Admin tempUser = new Admin("Admin", "Admin001", null, null);
		//System.out.println(tempUser.permission()); 
		
		
		System.out.println();
		System.out.println("This is the main menu for Admins");
		System.out.println("Type 'quit' or 'q' at any given point, to quit program");
		System.out.println();
		System.out.println("-------Course Management-------");
		System.out.println("Create course: C1 ");
		System.out.println("Delete course: C2 ");
		System.out.println("Edit a course: C3 ");
		System.out.println("Display course info: C4 ");
		System.out.println("Create student: C5 ");
		System.out.println();
		System.out.println("-------View Reports-------");
		System.out.println("View all courses: R1 ");
		System.out.println("View all courses that are full: R2 ");
		System.out.println("View the student list of a course: R3 ");
		System.out.println("View the course list of a student: R4 ");
		System.out.println("Sort courses (by # of Students): R5 ");
		System.out.println();
		
		System.out.println("Please enter the action you want to perform: ");
		String nextAction = input.nextLine();
		
		q(nextAction, courseList, studentList);
		
		switchMainMenuAdmin(nextAction, input, courseList, studentList);
	}
	
	//switch for admin options
	public static void switchMainMenuAdmin(String nextAction, Scanner input, List<Course> courseList, List<Student> studentList) {
		switch (nextAction) {
		case "C1": 
			createCourse(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "C2":
			deleteCourse(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "C3": 
			editCourse(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "C4":
			displayCourseInfo(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "C5":
			String addrem;
			String addremLC;
			System.out.println("Do you want to add or remove a student from the registry?");
			System.out.println("(Enter 'add' or 'remove')");
			addrem = input.nextLine();
			addremLC = addrem.toLowerCase();
			if (addremLC.equals("add")) {
				addStudent(input, courseList, studentList);
				mainMenuAdmin(input, courseList, studentList);
			} else if (addremLC.equals("remove")) {
				remStudent(input, courseList, studentList);
				mainMenuAdmin(input, courseList, studentList);
			} else {
				System.out.println("That is not a valid input. Going back to main menu...");
				mainMenuAdmin(input, courseList, studentList);
			}
		case "R1":
			viewCourses(courseList);
			mainMenuAdmin(input, courseList, studentList);
		case "R2":
			viewCoursesFull(courseList);
			mainMenuAdmin(input, courseList, studentList);
		case "R3":
			viewCourseNameList(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "R4":
			viewStudentCourseList(input, courseList, studentList);
			mainMenuAdmin(input, courseList, studentList);
		case "R5":
			sortCourses(courseList);
			mainMenuAdmin(input, courseList, studentList);
		default: 
			System.out.println("That's not a valid command, please try again.");
			String nextAction2 = input.nextLine();
			q(nextAction2, courseList, studentList);
			switchMainMenuAdmin(nextAction2, input, courseList, studentList);
		}
	}
	
	//main menu method for Students
	public static void mainMenuStudent(Scanner input, List<Course> courseList, List<Student> studentList, Student tempUser) {
		//System.out.println(tempUser.permission());
		
		System.out.println();
		System.out.println("This is the main menu for Students");
		System.out.println("Type 'quit' or 'q' at any given point, to quit program");
		System.out.println();
		System.out.println("-------Course Management-------");
		System.out.println("View all courses: C1 ");
		System.out.println("View all courses that are not full: C2 ");
		System.out.println("Register in a course: C3 ");
		System.out.println("Withdraw from a course: C4 ");
		System.out.println("View all courses you are registered in: C5 ");
		System.out.println();
		
		System.out.println("Please enter the action you want to perform: ");
		String nextAction = input.nextLine();
		
		q(nextAction, courseList, studentList);
		
		switchMainMenuStudent(nextAction, input, courseList, studentList, tempUser);
	}
	
	//switch for student options
	public static void switchMainMenuStudent(String nextAction, Scanner input, List<Course> courseList, List<Student> studentList, Student tempUser) {
		switch (nextAction) {
		case "C1": 
			viewCourses(courseList);
			mainMenuStudent(input, courseList, studentList, tempUser);
		case "C2":
			viewCoursesNotFull(courseList);
			mainMenuStudent(input, courseList, studentList, tempUser);
		case "C3": 
			registerStudent(input, courseList, studentList, tempUser);
			mainMenuStudent(input, courseList, studentList, tempUser);
		case "C4":
			for (int l = 0; l < studentList.size(); l++ ) {
				if (tempUser.getUsername().equals(studentList.get(l).getUsername())) {
					withdrawStudent(input, studentList.get(l).getCourseList(), courseList, studentList, tempUser);
				}
			}
			mainMenuStudent(input, courseList, studentList, tempUser);
		case "C5":
			System.out.println("Courses you are registered in: ");
			for (int l = 0; l < studentList.size(); l++ ) {
				if (tempUser.getUsername().equals(studentList.get(l).getUsername())) {
						studentList.get(l).printCourses();
				}
			}
			
			mainMenuStudent(input, courseList, studentList, tempUser);
		default: 
			System.out.println("That's not a valid command, please try again.");
			String nextAction2 = input.nextLine();
			q(nextAction, courseList, studentList);
			switchMainMenuStudent(nextAction2, input, courseList, studentList, tempUser);
		}
	}
	
	//creates a course with inputed variables
	public static void createCourse(Scanner input, List<Course> courseList, List<Student> studentList) {
		String courseName;
		String courseID;
		String maxStudents;
		String nameList = "NULL";
		String courseInstructor;
		String courseSectionNumber;
		String courseLocation;
		
		System.out.println("Please enter the following information for your new course:");
		
		System.out.println("Course Name: ");
		courseName = input.nextLine();
		q(courseName, courseList, studentList);
		
		System.out.println("Course ID: ");
		courseID = input.nextLine();
		q(courseID, courseList, studentList);
		
		System.out.println("Max Students Allowed: ");
		maxStudents = input.nextLine();
		q(maxStudents, courseList, studentList);
		
		System.out.println("Course Instructor: ");
		courseInstructor = input.nextLine();
		q(courseInstructor, courseList, studentList);
		
		System.out.println("Course Section Number: ");
		courseSectionNumber = input.nextLine();
		q(courseSectionNumber, courseList, studentList);
		
		System.out.println("Course Location: ");
		courseLocation = input.nextLine();
		q(courseLocation, courseList, studentList);
		
		Course newCourse = new Course(courseName, courseID, Integer.parseInt(maxStudents), 0, nameList, courseInstructor, courseSectionNumber, courseLocation);
		courseList.add(newCourse);
		
		System.out.println(courseName + " has been added to the system!");
	}
	
	//deletes a course based on user input
	public static void deleteCourse(Scanner input, List<Course> courseList, List<Student> studentList) {
		String tempCourseName;
		
		System.out.println("Please enter the name of the course you would like to delete: ");
		tempCourseName = input.nextLine();
		q(tempCourseName, courseList, studentList);
		
		
		//part2 method to loop if user enters invalid input
		deleteCourse2(tempCourseName, input, courseList, studentList);
	}
	
	//deleteCourse part2 -- so it can loop if user input is wack
	public static void deleteCourse2(String tempCourseName, Scanner input, List<Course> courseList, List<Student> studentList) {
		List<String> tempSectionNumbers = new ArrayList<>(); //new arrayList to check for duplicate course names with different section #s.
		String areYouSure;
		
		for (int i = 0; i < courseList.size(); i++) {
			if (tempCourseName.equals(courseList.get(i).getCourseName())) { //just checking course name first
				tempSectionNumbers = compareCourseName(tempCourseName, tempSectionNumbers, courseList); //now checking section numbers
				if (tempSectionNumbers.size() > 1) { //tempSectionNumbers will only have numbers if there are duplicate courses
					String sectionDelete;
					System.out.println("There are " + (tempSectionNumbers.size()) + " sections of " + tempCourseName + ".");
					System.out.println("The section numbers are: ");
					for (int j = 0; j < tempSectionNumbers.size(); j++) {
						System.out.println("Section " + tempSectionNumbers.get(j));
					}
					System.out.println("Which section would you like to delete? (Enter Section #) "); 
					sectionDelete = input.nextLine();
					q(sectionDelete, courseList, studentList);
					
					String areYouSure2;
					for (int k = 0; k < courseList.size(); k++) {
						if (tempCourseName.equals(courseList.get(k).getCourseName()) && sectionDelete.equals(courseList.get(k).getCourseSectionNumber())) { //if course name AND section number matches
							System.out.println("Are you sure you would like to delete " + courseList.get(k).getCourseName() + ", Section " + courseList.get(k).getCourseSectionNumber() + "?");
							System.out.println("Y/N");
							areYouSure2 = input.nextLine();
							q(areYouSure2, courseList, studentList);
							if (areYouSure2.equals("Y") || areYouSure2.equals("y") || areYouSure2.equals("Yes") || areYouSure2.equals("yes")) {
								
								for (int l = 0; l < courseList.get(k).getNameList().size(); l++) {
									String[] val = courseList.get(k).getNameList().get(l).split(",");
									for (int z = 0; z < studentList.size(); z++) {
										if (val[1].equals(studentList.get(z).getUsername())) {
											studentList.get(z).removeCourse(courseList.get(k)); //deletes course from student's courseList
										}
									}
									
									courseList.get(k).removeStudent(val[0], val[1]); //then delete students from course 
								}
								
								courseList.remove(k);
								System.out.println("You have deleted " + tempCourseName + ", Section " + sectionDelete + ".");
								System.out.println("Returning you to Main Menu...");
								mainMenuAdmin(input, courseList, studentList);
							} else if (areYouSure2.equals("N") || areYouSure2.equals("n") || areYouSure2.equals("No") || areYouSure2.equals("no")) {
								System.out.println("Okay. Returning you to Main Menu");
								mainMenuAdmin(input, courseList, studentList);
							} else {
								System.out.println("Invalid input, please try again: ");
								deleteCourse2(tempCourseName, input, courseList, studentList); //if user does not enter Y/N (or quit), go back to beginning of method. 
							}
						} else if ((k == courseList.size() - 1) && !(tempCourseName.equals(courseList.get(k).getCourseName()) && sectionDelete.equals(courseList.get(k).getCourseSectionNumber()))) {
							System.out.println("That section number doesn't exist! Please try again.");
							deleteCourse2(tempCourseName, input, courseList, studentList);
						}
					}
					
				} else { //if there's only one course with the requested course name: 
					System.out.println("Are you sure you would like to delete " + courseList.get(i).getCourseName() + "?");
					System.out.println("Y/N");
					areYouSure = input.nextLine();
					q(areYouSure, courseList, studentList);
					if (areYouSure.equals("Y") || areYouSure.equals("y") || areYouSure.equals("Yes") || areYouSure.equals("yes")) {
						for (int l = 0; l < courseList.get(i).getNameList().size(); l++) {
							String[] val = courseList.get(i).getNameList().get(l).split(",");
							for (int z = 0; z < studentList.size(); z++) {
								if (val[1].equals(studentList.get(z).getUsername())) {
									studentList.get(z).removeCourse(courseList.get(i)); //deletes course from student's courseList
								}
							}
							courseList.get(i).removeStudent(val[0], val[1]); //then delete students from course 
						}
						courseList.remove(i);
						System.out.println("You have deleted " + tempCourseName + ".");
						System.out.println("Returning you to Main Menu...");
						mainMenuAdmin(input, courseList, studentList);
					} else if (areYouSure.equals("N") || areYouSure.equals("n") || areYouSure.equals("No") || areYouSure.equals("no")) {
						System.out.println("Okay. Returning you to Main Menu");
						mainMenuAdmin(input, courseList, studentList);
					} else {
						System.out.println("Invalid input, please try again: ");
						deleteCourse2(tempCourseName, input, courseList, studentList); //if user does not enter Y/N (or quit), go back to beginning of method. 
					}
				}
			} else if(i == courseList.size() - 1 && !tempCourseName.equals(courseList.get(i).getCourseName())) {
				System.out.println("That course doesn't exist! Please try again.");
				deleteCourse(input, courseList, studentList); //if user enters in a course name that does not exist, go back to beginning of part 1 of deleteCourse. 
			}
		} 
	}
	
	//edits a course
	public static void editCourse(Scanner input, List<Course> courseList, List<Student> studentList) {
		String tempCourseName;
		String variable;
		List<String> tempSectionNumbers = new ArrayList<>();
		
		System.out.println("Please enter the name of course you would like to edit: "); 
		tempCourseName = input.nextLine();
		q(tempCourseName, courseList, studentList);
		
		for (int i = 0; i < courseList.size(); i++) {
			if (tempCourseName.equals(courseList.get(i).getCourseName())) {
				tempSectionNumbers = compareCourseName(tempCourseName, tempSectionNumbers, courseList); //now checking section numbers
				if (tempSectionNumbers.size() > 1) { //tempSectionNumbers will only have numbers if there are duplicate courses
					String sectionEdit;
					System.out.println("There are " + (tempSectionNumbers.size()) + " sections of " + tempCourseName + ".");
					System.out.println("The section numbers are: ");
					for (int j = 0; j < tempSectionNumbers.size(); j++) {
						System.out.println("Section " + tempSectionNumbers.get(j));
					}
					System.out.println("Which section would you like to edit? (Enter Section #) "); 
					sectionEdit = input.nextLine();
					q(sectionEdit, courseList, studentList);
					
					String variable2;
					for (int k = 0; k < courseList.size(); k++) {
						if (tempCourseName.equals(courseList.get(k).getCourseName()) && sectionEdit.equals(courseList.get(k).getCourseSectionNumber())) { //if course name AND section number matches
							System.out.println("What variable of " + courseList.get(k).getCourseName() + ", Section " + courseList.get(k).getCourseSectionNumber() + " would you like to edit?");
							System.out.println("-------Edit-------");
							System.out.println("Max Students Allowed: msa");
							System.out.println("Course Instructor: ci");
							System.out.println("Course Section Number: csn");
							System.out.println("Course Location: cl");
							System.out.println("Add Students: addstud");
							System.out.println("Remove Students: remstud");
							
							variable2 = input.nextLine();
							q(variable2, courseList, studentList);
							
							//part2 method to loop if user enters invalid input
							editCourseSwitch(variable2, k, input, courseList, studentList);
						} else if (k == courseList.size() - 1 && !(tempCourseName.equals(courseList.get(k).getCourseName()) && sectionEdit.equals(courseList.get(k).getCourseSectionNumber()))) {
							System.out.println("Thats not a valid section number. Please try again.");
							editCourse(input, courseList, studentList);
						}
					}
				} else {
				
					System.out.println("What variable of " + courseList.get(i).getCourseName() + " would you like to edit?");
					System.out.println("-------Edit-------");
					System.out.println("Max Students Allowed: msa");
					System.out.println("Course Instructor: ci");
					System.out.println("Course Section Number: csn");
					System.out.println("Course Location: cl");
					System.out.println("Add Students: addstud");
					System.out.println("Remove Students: remstud");
					
					variable = input.nextLine();
					q(variable, courseList, studentList);
					
					//part2 method to loop if user enters invalid input
					editCourseSwitch(variable, i, input, courseList, studentList);
				}
				
			} else if(i == courseList.size() - 1 && !tempCourseName.equals(courseList.get(i).getCourseName())) {
				System.out.println("That course doesn't exist! Please try again.");
				editCourse(input, courseList, studentList);
				
			}
		}
	}
	
	//switch method for editCourse options
	//e.g.: 'msa' changes max students allowed, 'addstud' registers a new student, etc.
	public static void editCourseSwitch(String variable, int t, Scanner input, List<Course> courseList, List<Student> studentList) {
		switch (variable.toLowerCase()) {
		case "msa": 
			String newMaxInt;
			System.out.println("The maximum amount of students allowed in " + courseList.get(t).getCourseName() + " is currently " + courseList.get(t).getMaxStudents() + ".");
			System.out.println("What would you like to change the max to? (Enter an integer)");
			newMaxInt = input.nextLine();
			q(newMaxInt, courseList, studentList);
			courseList.get(t).setMaxStudents(Integer.parseInt(newMaxInt));
			System.out.println("Changed max students allowed to " + courseList.get(t).getMaxStudents() + "!");
			mainMenuAdmin(input, courseList, studentList);
			
		case "ci":
			String newCourseInstructor;
			System.out.println("The course instructor for " + courseList.get(t).getCourseName() + " is currently " + courseList.get(t).getCourseInstructor() + ".");
			System.out.println("Who would you like to change the instructor to?");
			newCourseInstructor = input.nextLine();
			q(newCourseInstructor, courseList, studentList);
			courseList.get(t).setCourseInstructor(newCourseInstructor);
			System.out.println("Changed instructor to " + courseList.get(t).getCourseInstructor() + "!");
			mainMenuAdmin(input, courseList, studentList);
			
		case "csn":
			String newCourseSectionNumber;
			System.out.println("The course section number for " + courseList.get(t).getCourseName() + " is currently " + courseList.get(t).getCourseSectionNumber() + ".");
			System.out.println("What would you ilke to change the course section number to? (Enter an integer)");
			newCourseSectionNumber = input.nextLine();
			q(newCourseSectionNumber, courseList, studentList);
			courseList.get(t).setCourseSectionNumber(newCourseSectionNumber);
			System.out.println("Changed section number to " + courseList.get(t).getCourseSectionNumber() + "!");
			mainMenuAdmin(input, courseList, studentList);
			
		case "cl":
			String newCourseLocation;
			System.out.println("The course location for " + courseList.get(t).getCourseName() + " is currently " + courseList.get(t).getCourseLocation() + ".");
			System.out.println("Where would you ilke to change the course location to?");
			newCourseLocation = input.nextLine();
			q(newCourseLocation, courseList, studentList);
			courseList.get(t).setCourseLocation(newCourseLocation);
			System.out.println("Changed location to " + courseList.get(t).getCourseLocation() + "!");
			mainMenuAdmin(input, courseList, studentList);
			
		case "addstud":
			String newUsername;
			String newFirstName;
			String newLastName;
			
			System.out.println("Enter Student's First Name: ");
			newFirstName = input.nextLine();
			q(newFirstName, courseList, studentList);
			System.out.println("Enter Student's Last Name: ");
			newLastName = input.nextLine();
			q(newLastName, courseList, studentList);
			System.out.println("Enter Student's Username: ");      //pretty straightforward
			newUsername = input.nextLine();						   //just creates a student object with the inputed info
			q(newUsername, courseList, studentList);			   //and then add to student ArrayList
		
			for (int i = 0; i < studentList.size(); i++ ) {
				if (newUsername.equals(studentList.get(i).getUsername()) && 
					newFirstName.equals(studentList.get(i).getFirstName()) &&
					newLastName.equals(studentList.get(i).getLastName())) {
					if(courseList.get(t).isNotFull()) {
						courseList.get(t).addStudent(studentList.get(i).getFullName(), studentList.get(i).getUsername());
						studentList.get(i).addCourse(courseList.get(t));
						System.out.println("Successfully added " + studentList.get(i).getFirstName() + " to " + courseList.get(t).getCourseName() + ", Section " + courseList.get(t).getCourseSectionNumber() + "." );
						mainMenuAdmin(input, courseList, studentList);
					} else {
						System.out.println("This course is FULL.");
						System.out.println("Either change the max students allowed or remove students from the course.");
						mainMenuAdmin(input, courseList, studentList);
					}
				} else if (i == studentList.size() - 1 && 
					!(newUsername.equals(studentList.get(i).getUsername()) && 
					  newFirstName.equals(studentList.get(i).getFirstName()) &&
					  newLastName.equals(studentList.get(i).getLastName()))) {
					System.out.println("That student does not exist, or the incorrect information has been given.");
					System.out.println("Please try again.");
					editCourseSwitch(variable, t, input, courseList, studentList);
				}
			}
			
			
			
		case "remstud": 
			String oldUsername;
			String oldFirstName;
			String oldLastName;
			
			System.out.println("To remove a student: ");
			System.out.println("Enter Student's First Name: ");
			oldFirstName = input.nextLine();
			q(oldFirstName, courseList, studentList);
			System.out.println("Enter Student's Last Name: ");    //same as addStud, but just with the compare (.equals) to find Student from list and remove.
			oldLastName = input.nextLine();
			q(oldLastName, courseList, studentList);
			System.out.println("Enter Student's Username: ");
			oldUsername = input.nextLine();
			q(oldUsername, courseList, studentList);
			
			for (int i = 0; i < studentList.size(); i++ ) {
				if (oldUsername.equals(studentList.get(i).getUsername()) && 
					oldFirstName.equals(studentList.get(i).getFirstName()) &&
					oldLastName.equals(studentList.get(i).getLastName())) {
						courseList.get(t).removeStudent(studentList.get(i).getFullName(), studentList.get(i).getUsername());
						studentList.get(i).removeCourse(courseList.get(t));
						System.out.println("Successfully removed " + studentList.get(i).getFirstName() + " from " + courseList.get(t).getCourseName() + ".");
						mainMenuAdmin(input, courseList, studentList);
				} else if (i == studentList.size() - 1 && 
					!(oldUsername.equals(studentList.get(i).getUsername()) && 
					oldFirstName.equals(studentList.get(i).getFirstName()) &&
					oldLastName.equals(studentList.get(i).getLastName()))) {
					System.out.println("That student does not exist, or the incorrect information has been given.");
					System.out.println("Please try again.");
					editCourseSwitch(variable, t, input, courseList, studentList);
				}
			}
			
		default: 
			System.out.println("That's not a valid command, please try again.");
			String variable2 = input.nextLine();
			q(variable2, courseList, studentList);
			editCourseSwitch(variable2, t, input, courseList, studentList);
		}
	}
	
	//displays the info of a specific course
	public static void displayCourseInfo(Scanner input, List<Course> courseList, List<Student> studentList) {
		String tempCourseID; 
		List<Course> tempSectionNumbers = new ArrayList<>();
		System.out.println("To see more info on a course--");
		System.out.println("Please enter the Course ID:");
		tempCourseID = input.nextLine();
		for (int i = 0; i < courseList.size(); i++) {
			if (tempCourseID.equals(courseList.get(i).getCourseID())) { //just checking course ID first
				tempSectionNumbers = compareCourseID(tempCourseID, tempSectionNumbers, courseList); //now checking section numbers
				System.out.println("This course has " + tempSectionNumbers.size() + " section(s).");
				System.out.println("Course Name: " + courseList.get(i).getCourseName());
				
				System.out.println("Max Students Allowed: ");
				for (int j = 0; j < tempSectionNumbers.size(); j++) {
					System.out.println("Section " + tempSectionNumbers.get(j).getCourseSectionNumber() + ": " + tempSectionNumbers.get(j).getMaxStudents());
				}
				//for loop iterates between different section numbers of the same course
				System.out.println("Current # of Students Enrolled: ");
				for (int j = 0; j < tempSectionNumbers.size(); j++) {
					System.out.println("Section " + tempSectionNumbers.get(j).getCourseSectionNumber() + ": " + tempSectionNumbers.get(j).getCurrentStudents());
				}
				
				//System.out.println("List of Names of Students: ");
				
				System.out.println("Course Instructor: ");
				for (int j = 0; j < tempSectionNumbers.size(); j++) {
					System.out.println("Section " + tempSectionNumbers.get(j).getCourseSectionNumber() + ": " + tempSectionNumbers.get(j).getCourseInstructor());
				}
				
				System.out.println("Course Location: ");
				for (int j = 0; j < tempSectionNumbers.size(); j++) {
					System.out.println("Section " + tempSectionNumbers.get(j).getCourseSectionNumber() + ": " + tempSectionNumbers.get(j).getCourseLocation());
				}
				
				mainMenuAdmin(input, courseList, studentList);
			} else if(i == courseList.size() - 1 && !tempCourseID.equals(courseList.get(i).getCourseID())) {
				System.out.println("That course ID doesn't exist! Please try again.");
				displayCourseInfo(input, courseList, studentList); 
			}
		}
	}
	
	//creates a student object and adds it to studentList
	public static void addStudent(Scanner input, List<Course> courseList, List<Student> studentList) {
		String newUsername;
		String newPassword;
		String newFirstName;
		String newLastName;
		
		System.out.println("Enter Student's First Name: ");
		newFirstName = input.nextLine();
		q(newFirstName, courseList, studentList);
		System.out.println("Enter Student's Last Name: ");
		newLastName = input.nextLine();
		q(newLastName, courseList, studentList);
		System.out.println("Enter Student's Username: ");      //pretty straightforward
		newUsername = input.nextLine();						   //just creates a student object with the inputed info
		q(newUsername, courseList, studentList);			   //and then add to student ArrayList
		System.out.println("Enter Student's Password: ");
		newPassword = input.nextLine();
		q(newPassword, courseList, studentList);
		
		Student newStudent = new Student(newUsername, newPassword, newFirstName, newLastName);
		studentList.add(newStudent);
		
		System.out.println("Here is your updated list of students: "); //prints out studentList for Admin to see.
		viewStudents(studentList);
		mainMenuAdmin(input, courseList, studentList);
	}
	
	//removes a student object from studentList
	public static void remStudent(Scanner input, List<Course> courseList, List<Student> studentList) {
		String oldUsername;
		String oldPassword;
		String oldFirstName;
		String oldLastName;
		
		
		System.out.println("To remove a student: ");
		System.out.println("Enter Student's First Name: ");
		oldFirstName = input.nextLine();
		q(oldFirstName, courseList, studentList);
		System.out.println("Enter Student's Last Name: ");    //same as addStud, but just with the compare (.equals) to find Student from list and remove.
		oldLastName = input.nextLine();
		q(oldLastName, courseList, studentList);
		System.out.println("Enter Student's Username: ");
		oldUsername = input.nextLine();
		q(oldUsername, courseList, studentList);
		System.out.println("Enter Student's Password: ");
		oldPassword = input.nextLine();
		q(oldPassword, courseList, studentList);
		
		for (int i = 0; i < studentList.size(); i++ ) {
			if (oldUsername.equals(studentList.get(i).getUsername()) && 
				oldUsername.equals(studentList.get(i).getPassword()) &&
				oldFirstName.equals(studentList.get(i).getFirstName()) &&
				oldLastName.equals(studentList.get(i).getLastName())) {
					studentList.remove(i);
					System.out.println("Successfully removed student from registration system.");
					System.out.println("Here is your updated list of students: ");
					viewStudents(studentList);
					mainMenuAdmin(input, courseList, studentList);
			} else if (i == studentList.size() - 1 && 
				!(oldUsername.equals(studentList.get(i).getUsername()) && 
				  oldUsername.equals(studentList.get(i).getPassword()) &&
				  oldFirstName.equals(studentList.get(i).getFirstName()) &&
			      oldLastName.equals(studentList.get(i).getLastName()))) {
				System.out.println("That student does not exist, or the incorrect information has been given.");
				System.out.println("Please try again.");
				remStudent(input, courseList, studentList);
			}
		}
	}
	
	//this short compare method was used to delete and edit courses.
	public static List<String> compareCourseName(String requestedCourseName, List<String> tempSectionNumbers, List<Course> courseList) {
		String tempCourseName2;
		for (int j = 0; j < courseList.size(); j++) { 
			tempCourseName2 = courseList.get(j).getCourseName();
			if (tempCourseName2.equals(requestedCourseName)) {
				tempSectionNumbers.add(courseList.get(j).getCourseSectionNumber());
			}
		}
		return tempSectionNumbers;
	}
	
	
	//ditto, but for displaying course info (based on CourseID)
	public static List<Course> compareCourseID(String requestedCourseID, List<Course> tempSectionNumbers, List<Course> courseList) {
		String tempCourseName2;
		for (int j = 0; j < courseList.size(); j++) { 
			tempCourseName2 = courseList.get(j).getCourseID();
			if (tempCourseName2.equals(requestedCourseID)) {
				tempSectionNumbers.add(courseList.get(j));
			}
		}
		return tempSectionNumbers;
	}
	
	//View methods: 
	public static void viewCourses(List<Course> courseList) {
		System.out.println("Course_Name, Course_ID, Maximum_Students, Current_Students, [List_Of_Names], Course_Instructor, Course_Section_Number, Course_Location");
				
			for (int i = 0; i < courseList.size(); i++) {
				System.out.print(courseList.get(i).getCourseName() + ", ");
				System.out.print(courseList.get(i).getCourseID() + ", ");
				System.out.print(courseList.get(i).getMaxStudents() + ", ");
				System.out.print(courseList.get(i).getCurrentStudents() + ", ");
				System.out.print(courseList.get(i).getNameList() + ", ");
				System.out.print(courseList.get(i).getCourseInstructor() + ", ");
				System.out.print(courseList.get(i).getCourseSectionNumber() + ", ");
				System.out.print(courseList.get(i).getCourseLocation() + ", ");
				System.out.println();
			}
		}

	
	//Method to view students in studentList
	
	public static void viewStudents(List<Student> studentList) {
			System.out.println("----FirstName, LastName, Username, Password----");
			for (int i = 0; i < studentList.size(); i++) {
				System.out.println();
				
				System.out.print(studentList.get(i).getFirstName() + ", ");
				System.out.print(studentList.get(i).getLastName() + ", ");
				System.out.print(studentList.get(i).getUsername() + ", ");
				System.out.print(studentList.get(i).getPassword());
				
				System.out.println();
			}
		}
		
	
	public static void viewCoursesFull(List<Course> courseList) {
		String fileName = "FullCourseList.txt";
		System.out.println("Course_Name, Course_ID, Maximum_Students, Current_Students, [List_Of_Names], Course_Instructor, Course_Section_Number, Course_Location");
		
		try{
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			//String text = scan.nextLine();
			//bufferedWriter.write(text);
			
			for (int i = 0; i < courseList.size(); i++) {
				if (courseList.get(i).getCurrentStudents() >= courseList.get(i).getMaxStudents()) {
					System.out.print(courseList.get(i).getCourseName() + ", ");
					bufferedWriter.write(courseList.get(i).getCourseName() + ",");
					System.out.print(courseList.get(i).getCourseID() + ", ");
					bufferedWriter.write(courseList.get(i).getCourseID() + ",");
					System.out.print(courseList.get(i).getMaxStudents() + ", ");
					bufferedWriter.write(courseList.get(i).getMaxStudents() + ",");
					System.out.print(courseList.get(i).getCurrentStudents() + ", ");
					bufferedWriter.write(courseList.get(i).getCurrentStudents() + ",");
					System.out.print(courseList.get(i).getNameList() + ", ");
					bufferedWriter.write(courseList.get(i).getNameList() + ",");
					System.out.print(courseList.get(i).getCourseInstructor() + ", ");
					bufferedWriter.write(courseList.get(i).getCourseInstructor() + ",");
					System.out.print(courseList.get(i).getCourseSectionNumber() + ", ");
					bufferedWriter.write(courseList.get(i).getCourseSectionNumber() + ",");
					System.out.print(courseList.get(i).getCourseLocation() + ", ");
					bufferedWriter.write(courseList.get(i).getCourseLocation() + ",");
					System.out.println();
					bufferedWriter.newLine();
				}
			}
			bufferedWriter.close();
		}
		catch (IOException exk) {
			System.out.println( "Error writing file '" + fileName + "'");
			exk.printStackTrace();
		}
	}
	
	public static void viewCoursesNotFull(List<Course> courseList) {
		System.out.println("Course_Name, Course_ID, Maximum_Students, Current_Students, [List_Of_Names], Course_Instructor, Course_Section_Number, Course_Location");
		
		for (int i = 0; i < courseList.size(); i++) {
			if (courseList.get(i).getCurrentStudents() < courseList.get(i).getMaxStudents()) {
				System.out.print(courseList.get(i).getCourseName() + ", ");
				System.out.print(courseList.get(i).getCourseID() + ", ");
				System.out.print(courseList.get(i).getMaxStudents() + ", ");
				System.out.print(courseList.get(i).getCurrentStudents() + ", ");
				System.out.print("[");
				for (int j = 0; j < courseList.get(i).getNameList().size(); j++ ) {
					if (j != courseList.get(i).getNameList().size() - 1) {
						System.out.print(courseList.get(i).getNameList() + "; ");
					} else {
						System.out.print(courseList.get(i).getNameList());
					}
				}
				System.out.print("]");
				System.out.print(courseList.get(i).getCourseInstructor() + ", ");
				System.out.print(courseList.get(i).getCourseSectionNumber() + ", ");
				System.out.print(courseList.get(i).getCourseLocation() + ", ");
				System.out.println();
			}
		}
	}
	
	public static void viewCourseNameList(Scanner input, List<Course> courseList, List<Student> studentList) {
		String tempCourseName;
		List<String> tempSectionNumbers = new ArrayList<>();
		
		System.out.println("To view student list of a course--");
		System.out.println("Please enter the name of the course: "); 
		tempCourseName = input.nextLine();
		q(tempCourseName, courseList, studentList);
		
		for (int i = 0; i < courseList.size(); i++) {
			if (tempCourseName.equals(courseList.get(i).getCourseName())) {
				tempSectionNumbers = compareCourseName(tempCourseName, tempSectionNumbers, courseList); //now checking section numbers
				if (tempSectionNumbers.size() > 1) { //tempSectionNumbers will only have numbers if there are duplicate courses
					String viewStudentList;
					System.out.println("There are " + (tempSectionNumbers.size()) + " sections of " + tempCourseName + ".");
					System.out.println("The section numbers are: ");
					for (int j = 0; j < tempSectionNumbers.size(); j++) {
						System.out.println("Section " + tempSectionNumbers.get(j));
					}
					System.out.println("Which section would you like to see the student list of? (Enter Section #) "); 
					viewStudentList = input.nextLine();
					q(viewStudentList, courseList, studentList);
					for (int k = 0; k < courseList.size(); k++) {
						if (tempCourseName.equals(courseList.get(k).getCourseName()) && viewStudentList.equals(courseList.get(k).getCourseSectionNumber())) { 
							courseList.get(k).printStudents();
							mainMenuAdmin(input, courseList, studentList);
						} else if (k == (courseList.size() - 1) && !(tempCourseName.equals(courseList.get(k).getCourseName()) && viewStudentList.equals(courseList.get(k).getCourseSectionNumber()))) {
							System.out.println("That section number doesnt exist. Please try again.");
							viewCourseNameList(input, courseList, studentList);
						}
					}
				} else {
					for (int k = 0; k < courseList.size(); k++) {
						if (tempCourseName.equals(courseList.get(k).getCourseName())) { 
							courseList.get(k).printStudents();
							mainMenuAdmin(input, courseList, studentList);
						} else if (k == (courseList.size() - 1) && !(tempCourseName.equals(courseList.get(k).getCourseName()))) {
							System.out.println("That section number doesnt exist. Please try again.");
							viewCourseNameList(input, courseList, studentList);
						}
					}
				}
			} else if (i == (courseList.size() - 1) && !(tempCourseName.equals(courseList.get(i).getCourseName()))) {
				System.out.println("That course does not exist. Please try again.");
				viewCourseNameList(input, courseList, studentList);
			}
		}
	}
	
	public static void viewStudentCourseList(Scanner input, List<Course> courseList, List<Student> studentList) {
		String tempFirstName; 
		String tempLastName; 
		String tempUsername;
		
		
		System.out.println("Enter Student's First Name: ");
		tempFirstName = input.nextLine();
		q(tempFirstName, courseList, studentList);
		System.out.println("Enter Student's Last Name: ");
		tempLastName = input.nextLine();
		q(tempLastName, courseList, studentList);
		System.out.println("Enter Student's Username: ");      
		tempUsername = input.nextLine();						   
		q(tempUsername, courseList, studentList);

		for (int i = 0; i < studentList.size(); i++ ) {
			if (tempUsername.equals(studentList.get(i).getUsername()) && 
				tempFirstName.equals(studentList.get(i).getFirstName()) &&
				tempLastName.equals(studentList.get(i).getLastName())) {
					System.out.println(tempFirstName + "'s classes: ");
					studentList.get(i).printCourses();
					mainMenuAdmin(input, courseList, studentList);
			} else if (i == studentList.size() - 1 && 
				!(tempUsername.equals(studentList.get(i).getUsername()) && 
				  tempFirstName.equals(studentList.get(i).getFirstName()) &&
				  tempLastName.equals(studentList.get(i).getLastName()))) {
				System.out.println("That student does not exist, or the incorrect information has been given.");
				System.out.println("Please try again.");
				viewStudentCourseList(input, courseList, studentList);
			}
		}
	}
	
	//sort object -- utilizing Comparable interface in Course Object
	public static void sortCourses(List<Course> courseList) {
		System.out.println("Sorting....");
		Collections.sort(courseList);
		viewCourses(courseList);
	}

	//register a student into a class -- method used by the student themselves.
	public static void registerStudent(Scanner input, List<Course> courseList, List<Student> studentList, Student tempUser) {
		String tempCourseName;
		List<String> tempSectionNumbers = new ArrayList<>();
		
		System.out.println("Please enter the name of course you would like to register for: "); 
		tempCourseName = input.nextLine();
		q(tempCourseName, courseList, studentList);
		
		for (int i = 0; i < courseList.size(); i++) {
			if (tempCourseName.equals(courseList.get(i).getCourseName())) {
				tempSectionNumbers = compareCourseName(tempCourseName, tempSectionNumbers, courseList); //now checking section numbers
				if (tempSectionNumbers.size() > 1) { //tempSectionNumbers will only have numbers if there are duplicate courses
					String sectionRegister;
					System.out.println("There are " + (tempSectionNumbers.size()) + " sections of " + tempCourseName + ".");
					System.out.println("The section numbers are: ");
					for (int j = 0; j < tempSectionNumbers.size(); j++) {
						System.out.println("Section " + tempSectionNumbers.get(j));
					}
					System.out.println("Which section would you like to register in? (Enter Section #) "); 
					sectionRegister = input.nextLine();
					q(sectionRegister, courseList, studentList);
					
					for (int k = 0; k < courseList.size(); k++) {
						if (tempCourseName.equals(courseList.get(k).getCourseName()) && sectionRegister.equals(courseList.get(k).getCourseSectionNumber())) { //if course name AND section number matches
							if(courseList.get(k).isNotFull()) {
								courseList.get(k).addStudent(tempUser.getFullName(), tempUser.getUsername());
								for (int l = 0; l < studentList.size(); l++ ) {
									if (tempUser.getUsername().equals(studentList.get(i).getUsername())) {
											studentList.get(l).addCourse(courseList.get(k));
									}
								}
								System.out.println("Successfully added " + tempUser.getFirstName() + " to " + courseList.get(k).getCourseName() + ".");
								mainMenuStudent(input, courseList, studentList, tempUser);
							} else {
								System.out.println("Sorry, this course is full!");
								registerStudent(input, courseList, studentList, tempUser);
							}
						} else if (k == courseList.size() - 1 && !(tempCourseName.equals(courseList.get(k).getCourseName()) && sectionRegister.equals(courseList.get(k).getCourseSectionNumber()))) {
							System.out.println("Thats not a valid section number. Please try again.");
							registerStudent(input, courseList, studentList, tempUser);
						}
					}
				} else {
					if (courseList.get(i).isNotFull()) {
						courseList.get(i).addStudent(tempUser.getFullName(), tempUser.getUsername());
						for (int l = 0; l < studentList.size(); l++ ) {
							if (tempUser.getUsername().equals(studentList.get(l).getUsername())) {
									studentList.get(l).addCourse(courseList.get(i));
							}
						}
						System.out.println("Successfully added " + tempUser.getFirstName() + " to " + courseList.get(i).getCourseName() + ".");
						mainMenuStudent(input, courseList, studentList, tempUser);
					} else {
						System.out.println("Sorry, this course is full!");
						registerStudent(input, courseList, studentList, tempUser);
					}
				}
				
			} else if(i == courseList.size() - 1 && !tempCourseName.equals(courseList.get(i).getCourseName())) {
				System.out.println("That course doesn't exist! Please try again.");
				registerStudent(input, courseList, studentList, tempUser);
				
			}
		}
	}
	
	//withdraw a student from a class -- method used by the student themselves.
	public static void withdrawStudent(Scanner input, List<Course> studentCourseList, List<Course> courseList, List<Student> studentList, Student tempUser) {
		String tempCourseName;
		List<String> tempSectionNumbers = new ArrayList<>();
		
		System.out.println("Please enter the name of course you would like to withdraw from: "); 
		tempCourseName = input.nextLine();
		q(tempCourseName, courseList, studentList);
		
		for (int i = 0; i < studentCourseList.size(); i++) {
			if (tempCourseName.equals(studentCourseList.get(i).getCourseName())) {
				studentCourseList.get(i).removeStudent(tempUser.getFullName(), tempUser.getUsername());
				for (int l = 0; l < studentList.size(); l++ ) {
					if (tempUser.getUsername().equals(studentList.get(i).getUsername())) {
							studentList.get(l).removeCourse(courseList.get(i));
					}
				}
				System.out.println("Successfully removed " + tempUser.getFirstName() + " to " + studentCourseList.get(i).getCourseName() + ".");
				mainMenuStudent(input, studentCourseList, studentList, tempUser);
				
			} else if(i == studentCourseList.size() - 1 && !tempCourseName.equals(studentCourseList.get(i).getCourseName())) {
				System.out.println("That course doesn't exist! Please try again.");
				withdrawStudent(input, studentCourseList, courseList, studentList, tempUser);
				
			}
		}
	}

	//quit function -- every time program is waiting for user input, the user has to ability to quit.
	public static void q(String userInput, List<Course> courseList, List<Student> studentList) {
		if (userInput.equals("quit") || userInput.equals("Quit") || userInput.equals("q") || userInput.equals("Q")) {
			//serialize before quit to save data
			serializeCourses(courseList);
			serializeStudents(studentList);
			System.exit(0);
		}		
	}
}
