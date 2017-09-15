package test2;

class Emp {
	private int empno ;
	private String ename ;
	private String job ;
	private double sal ;
	private double comm ;
	public void setEmpno(int eno){
		empno =eno ;
	}
	public void setEname(String ena){
		ename=ena ;
	}
	public void setJob(String j){
		job= j ;
	}
	public void setSal(double s){
		sal=s ;
	}
	public void setComm(double c){
		comm= c ;
	}
	public int getEmpno(){
		return empno ;
	}
	public String getEname(){
		return ename ;
	}
	public String getJob(){
		return job ;
	}
	public double getSal (){
		return sal;
	}
	public double getComm() {
		return comm ;
	}
	public String getInfo (){
		return "empno="+ empno+"\n"+
				"ename="+ ename+"\n"+
				"job="+ job+"\n"+
				"sal="+ sal+"\n"+
				"comm="+ comm ;


	}
}