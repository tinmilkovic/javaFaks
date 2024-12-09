package hr.java.restaurant.sort;

import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Waiter;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class EmployeeContractDurationComparator<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        long duration1 = 0;
        long duration2 = 0;

        if (o1 instanceof Chef) {
            Chef chef = (Chef) o1;
            duration1 = ChronoUnit.DAYS.between(chef.getContract().getStartDate(), chef.getContract().getEndDate());
        } else if (o1 instanceof Waiter) {
            Waiter waiter = (Waiter) o1;
            duration1 = ChronoUnit.DAYS.between(waiter.getContract().getStartDate(), waiter.getContract().getEndDate());
        } else if (o1 instanceof Deliverer) {
            Deliverer deliverer = (Deliverer) o1;
            duration1 = ChronoUnit.DAYS.between(deliverer.getContract().getStartDate(), deliverer.getContract().getEndDate());
        }

        if (o2 instanceof Chef) {
            Chef chef = (Chef) o2;
            duration2 = ChronoUnit.DAYS.between(chef.getContract().getStartDate(), chef.getContract().getEndDate());
        } else if (o2 instanceof Waiter) {
            Waiter waiter = (Waiter) o2;
            duration2 = ChronoUnit.DAYS.between(waiter.getContract().getStartDate(), waiter.getContract().getEndDate());
        } else if (o2 instanceof Deliverer) {
            Deliverer deliverer = (Deliverer) o2;
            duration2 = ChronoUnit.DAYS.between(deliverer.getContract().getStartDate(), deliverer.getContract().getEndDate());
        }

        return Long.compare(duration1, duration2); // Sortiranje od najkraćeg do najdužeg trajanja
    }
}
