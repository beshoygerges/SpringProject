/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class OrdersPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "User_email")
    private String useremail;

    public OrdersPK() {
    }

    public OrdersPK(int id, String useremail) {
        this.id = id;
        this.useremail = useremail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (useremail != null ? useremail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdersPK)) {
            return false;
        }
        OrdersPK other = (OrdersPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.useremail == null && other.useremail != null) || (this.useremail != null && !this.useremail.equals(other.useremail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iti.entity.OrdersPK[ id=" + id + ", useremail=" + useremail + " ]";
    }

}
