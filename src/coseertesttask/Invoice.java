/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coseertesttask;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author dkosyakov
 */
public class Invoice {
    private Date date;
    private Customer customer;
    private Map<Product, Double> order;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Map<Product, Double> getOrder() {
        return order;
    }

    public void setOrder(Map<Product, Double> order) {
        this.order = order;
    }
}
