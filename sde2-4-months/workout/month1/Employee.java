import java.util.*;

class EmployeeComparator implements Comparator<Employee>{
    @Override
    public int compare(Employee e1, Employee e2){

        return e1.salary-e2.salary;
    }
}

public class Employee {

    public String name;
    public int age;
    public int salary;

    public Employee(String name, int age, int salary){
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString(){

        return "{name=" + this.name +"," +
        "salary="+ this.salary + "}";

    }


    public static void main(String[] args){
        List<Employee> list = new ArrayList<>(List.of(new Employee("Udit",25,120),
                                                        new Employee("Smile",24,130),
                                                        new Employee("Dora",25,100)));

        Collections.sort(list,new EmployeeComparator());    
        System.out.println(list);
    }


   
    
}
