package hr.java.restaurant.sort;

import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Waiter;

import java.util.Comparator;

public class EmployeeSalaryComparator <T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        if (o1 instanceof Chef && o2 instanceof Chef) {
            return ((Chef) o2).getContract().getSalary().compareTo(((Chef) o1).getContract().getSalary());
        } else if (o1 instanceof Waiter && o2 instanceof Waiter) {
            return ((Waiter) o2).getContract().getSalary().compareTo(((Waiter) o1).getContract().getSalary());
        } else if (o1 instanceof Deliverer && o2 instanceof Deliverer) {
            return ((Deliverer) o2).getContract().getSalary().compareTo(((Deliverer) o1).getContract().getSalary());
        }
        return 0;
    }
}
