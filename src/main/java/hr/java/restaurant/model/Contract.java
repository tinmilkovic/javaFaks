package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an employment contract for a worker, with details such as salary, contract dates, and contract type.
 */
public class Contract {

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractType contractType;

    /**
     * Constructs a new {@link Contract} object with the specified salary, start date, end date, and contract type.
     *
     * @param salary The salary for the employee.
     * @param startDate The start date of the contract.
     * @param endDate The end date of the contract.
     * @param contractType The type of the contract (either {@link ContractType#FULL_TIME} or {@link ContractType#PART_TIME}).
     */
    public Contract(BigDecimal salary, LocalDate startDate, LocalDate endDate, ContractType contractType) {
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
    }


    public BigDecimal getSalary() {
        return salary;
    }


    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }


    public LocalDate getStartDate() {
        return startDate;
    }


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    public LocalDate getEndDate() {
        return endDate;
    }


    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public ContractType getContractType() {
        return contractType;
    }


    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }
}
