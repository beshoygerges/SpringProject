/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "rechargecards")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rechargecards.findAll", query = "SELECT r FROM Rechargecards r")
    , @NamedQuery(name = "Rechargecards.findByNumber", query = "SELECT r FROM Rechargecards r WHERE r.number = :number")
    , @NamedQuery(name = "Rechargecards.findByValue", query = "SELECT r FROM Rechargecards r WHERE r.value = :value")
    , @NamedQuery(name = "Rechargecards.findByStatus", query = "SELECT r FROM Rechargecards r WHERE r.status = :status")})
public class Rechargecards implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "number")
    private Integer number;
    @Column(name = "value")
    private Integer value;
    @Column(name = "status")
    private String status;

    public Rechargecards() {
    }

    public Rechargecards(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (number != null ? number.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rechargecards)) {
            return false;
        }
        Rechargecards other = (Rechargecards) object;
        if ((this.number == null && other.number != null) || (this.number != null && !this.number.equals(other.number))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iti.entity.Rechargecards[ number=" + number + " ]";
    }

}
