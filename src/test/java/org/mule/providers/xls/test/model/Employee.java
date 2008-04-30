/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.providers.xls.test.model;

import java.util.Date;

public class Employee implements java.io.Serializable {
	private String name;
	private int age;
	private Double payment;
	private Double bonus;
	private Date birthDate;
	private Employee superior;

	public Employee() {

	}

	public Employee(String name, int age, Double payment, Double bonus) {
		this.name = name;
		this.age = age;
		this.payment = payment;
		this.bonus = bonus;
	}

	public Employee(String name, int age, double payment, double bonus, Date birthDate) {
		this.name = name;
		this.age = age;
		this.payment = new Double(payment);
		this.bonus = new Double(bonus);
		this.birthDate = birthDate;
	}

	public Employee(String name, int age, double payment, double bonus) {
		this.name = name;
		this.age = age;
		this.payment = new Double(payment);
		this.bonus = new Double(bonus);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Employee getSuperior() {
		return superior;
	}

	public void setSuperior(Employee superior) {
		this.superior = superior;
	}
}
