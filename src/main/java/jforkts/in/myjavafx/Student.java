package jforkts.in.myjavafx;

public class Student {

    String usn;
    String sname;
    Integer sem;
    String branch;

    String gender;
    String participation;

    public Student(String usn, String sname, Integer sem, String branch,String gender, String participation) {
        this.usn = usn;
        this.sname = sname;
        this.sem = sem;
        this.branch = branch;
        this.gender=gender;
        this.participation=participation;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public void setName(String sname) {
        this.sname = sname;
    }

    public void setSem(Integer sem) {
        this.sem = sem;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
    public  void setGender(String gender){
        this.gender=gender;
    }
    public void setParticipation(String participation){
        this.participation=participation;
    }
    public String getUsn() {
        return usn;
    }

    public String getSname() {
        return sname;
    }

    public Integer getSem() {
        return sem;
    }

    public String getBranch() {
        return branch;
    }
    public String getGender(){return gender;}
    public String getParticipation(){return participation;}
}
