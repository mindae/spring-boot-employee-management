package com.example.hello.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hello.model.Employee;
import com.example.hello.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Transactional(readOnly = true)
	public List<Employee> listAll() {
		return employeeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Employee> getById(Long id) {
		return employeeRepository.findById(id);
	}

	@Transactional
	public Employee create(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Transactional
	public Optional<Employee> update(Long id, Employee update) {
		return employeeRepository.findById(id)
			.map(existing -> {
				existing.setFirstName(update.getFirstName());
				existing.setLastName(update.getLastName());
				existing.setEmail(update.getEmail());
				existing.setPhoneNumber(update.getPhoneNumber());
				existing.setHireDate(update.getHireDate());
				existing.setJobId(update.getJobId());
				existing.setSalary(update.getSalary());
				existing.setCommissionPct(update.getCommissionPct());
				existing.setManagerId(update.getManagerId());
				existing.setDepartmentId(update.getDepartmentId());
				return employeeRepository.save(existing);
			});
	}

	@Transactional
	public boolean delete(Long id) {
		if (!employeeRepository.existsById(id)) {
			return false;
		}
		employeeRepository.deleteById(id);
		return true;
	}
}


