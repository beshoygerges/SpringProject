/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "orders")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o")
    , @NamedQuery(name = "Orders.findByDate", query = "SELECT o FROM Orders o WHERE o.date = :date")
    , @NamedQuery(name = "Orders.findByStatus", query = "SELECT o FROM Orders o WHERE o.status = :status")
    , @NamedQuery(name = "Orders.findById", query = "SELECT o FROM Orders o WHERE o.ordersPK.id = :id")
    , @NamedQuery(name = "Orders.findByUseremail", query = "SELECT o FROM Orders o WHERE o.ordersPK.useremail = :useremail")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrdersPK ordersPK;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "status")
    private Integer status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    private Collection<Orderdetails> orderdetailsCollection;
    @JoinColumn(name = "User_email", referencedColumnName = "email", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Orders() {
    }

    public Orders(OrdersPK ordersPK) {
        this.ordersPK = ordersPK;
    }

    public Orders(int id, String useremail) {
        this.ordersPK = new OrdersPK(id, useremail);
    }

    public OrdersPK getOrdersPK() {
        return ordersPK;
    }

    public void setOrdersPK(OrdersPK ordersPK) {
        this.ordersPK = ordersPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Orderdetails> getOrderdetailsCollection() {
        return orderdetailsCollection;
    }

    public void setOrderdetailsCollection(Collection<Orderdetails> orderdetailsCollection) {
        this.orderdetailsCollection = orderdetailsCollection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordersPK != null ? ordersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.ordersPK == null && other.ordersPK != null) || (this.ordersPK != null && !this.ordersPK.equals(other.ordersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iti.entity.Orders[ ordersPK=" + ordersPK + " ]";
    }

}
