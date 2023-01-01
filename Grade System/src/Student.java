import java.util.ArrayList;


public class Student {

	final String name;
	int studentID;
	ArrayList<Course> courses = new ArrayList<Course>();

	public Student(String name) {
		this.name = name;
	}

	void addCourse(Course crs){
		courses.add(crs);
	}
	void removeCourse(Course crs){
		courses.remove(crs);
	}
	
	//statistics, field=-1 for all grades
	// 0: major, 1: science, 2: technology, 3: art, 4: liberals
	double[] mean(){
		int field = -1;
		double[] meanValue = new double[5];
		int[] coursesPerField = new int[5]; 
		
		for(int i=0; i < courses.size(); i++){
			field = courses.get(i).getField();
			meanValue[field] += courses.get(i).gradeCalculation();
			coursesPerField[field]++;
			
		}
		for(int i=0; i <5; i++){
			if(coursesPerField[i] != 0) {
				meanValue[i] /= coursesPerField[i];
			}
			else {
				meanValue[i] = 0;
			}
		}
		
		return meanValue;

	}
	void sd(int field){
		switch(field)
		{
			case 0: break;
			case 1: break;
			case 2: break;
			case 3: break;
			case 4: break;
			default: break;
		}
	}
}
