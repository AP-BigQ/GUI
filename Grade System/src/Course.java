import java.util.Scanner;


public class Course {
	
	// inputs variables
	String coursename;
	// course id
	int id;
	// 1-4
	int credits;
	// 0: major, 1: science, 2: technology, 3: art, 4: liberals
	int field;
	// A-F
	double grade_obj;
	// default 1
	int passtime = 1;

	//subjective grade A--D
	double grade_subj;
	// subjective intents 1-5 (confidence)
	int subjFactor;
	// personal interest 1-5
	int subjInterest;
	final int MAXSCALE = 5;
	
	// outputs 
	double grade_tot;

	public Course() {

	}
	
	
	public Course(int field, String name, int credits) {
		this.field = field;
		this.coursename = name;
		this.credits = credits;
		
	}

	public Course(int coursefield, String coursename, 
			int credits, String grade, String selfEvaluate, 
			String passTime, int interests, int confidence){
		
		this.field = coursefield;
		this.coursename = coursename;
		this.credits = credits;
		this.grade_obj = parseGrade(grade);
		this.grade_subj = parseGrade(selfEvaluate);
		this.passtime = parsePasstime(passTime);
		this.subjInterest = interests;
		this.subjFactor = confidence;
	}
	double parseGrade(String grade){
		double grade_obj = 0;
		switch(grade)
		{
			case "A":  grade_obj =  4.0; break;
			case "A-": grade_obj =  3.7; break;
			case "B+": grade_obj =  3.3; break;
			case "B":  grade_obj =  3.0; break;
			case "B-": grade_obj =  2.7; break;
			case "C+": grade_obj =  2.3; break;
			case "C":  grade_obj =  2.0; break;
			case "C-": grade_obj =  1.7; break;
			case "D":  grade_obj =  1.0; break;
			default: break;
		}
		
		return grade_obj;
	}
	int parsePasstime(String passtime){
		int ptime = 1;
		switch(passtime)
		{
			case "1":   ptime =  1; break;
			case "2":   ptime =  2; break;
			case ">=3": ptime =  3; break;
			default: break;
		}
		
		return ptime;
	}
	void readData(Scanner in){
		String line = in.nextLine();
		String[] tokens = line.split("\\|");
		
		coursename = tokens[0];
		credits = Integer.parseInt(tokens[1]);
	}
	double gradeCalculation(){
		//algorithm to calculate comprehensive grade
		double subj = grade_subj*subjFactor/MAXSCALE;
		
		double wt1;
		if(grade_obj < subj) wt1 = 1.0 - grade_obj / subj;
		else wt1 = 1.0 -  subj / grade_obj;

		double wt2 = 1.0 - wt1;
		
		grade_tot = wt2*grade_obj + wt1*grade_subj;
		
		grade_tot /= Math.pow(passtime, 1.0/3.0);
		grade_tot *= subjInterest/MAXSCALE;
		
		return grade_tot;
	}
	String getFieldName(int field){
		
		String str = null;
		switch(field)
		{
			case 0: str = "Major"; break;
			case 1: str = "Science"; break;
			case 2: str = "Technology"; break;
			case 3: str = "Art"; break;
			case 4: str = "Liberals"; break;
			default: break;
		}
		return str;
		}
	
	//setter & getter functions
	void setCourseID(int x){ id = x;}
	int getCourseID(){return id;}
	String getCourseName(){return coursename; }
	void setCredits(int x){ credits = x;}
	int getCredits(){return credits;}
	void setField(int x){field = x;}
	int getField(){return field;}
	void setPasstime(int x){passtime = x;}
	int getPasstime(){return passtime;}
	void setGradeObj(double x){grade_obj = x;}
	double getGradeObj(){return grade_obj;}
	void setGradeSubj(double x){ grade_subj = x;}
	double getGradeSubj(){return grade_subj;}
	void setSubjFactor(int x){subjFactor = x;}
	int getSubjFactor(){return subjFactor;}
	void setSubjInterest(int x){subjInterest = x;}
	int getSubjInterest(){return subjInterest;}
}
