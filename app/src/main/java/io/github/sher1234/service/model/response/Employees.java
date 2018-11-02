package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Employee;

public class Employees implements Serializable {

    public List<Employee> Employees;
    public String Message;
    public int Code;

    public Employees() {

    }
}