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
public class OrderdetailsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "products_product_id")
    private int productsProductId;
    @Basic(optional = false)
    @Column(name = "order_id")
    private int orderId;

    public OrderdetailsPK() {
    }

    public OrderdetailsPK(int productsProductId, int orderId) {
        this.productsProductId = productsProductId;
        this.orderId = orderId;
    }

    public int getProductsProductId() {
        return productsProductId;
    }

    public void setProductsProductId(int productsProductId) {
        this.productsProductId = productsProductId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productsProductId;
        hash += (int) orderId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderdetailsPK)) {
            return false;
        }
        OrderdetailsPK other = (OrderdetailsPK) object;
        if (this.productsProductId != other.productsProductId) {
            return false;
        }
        if (this.orderId != other.orderId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iti.entity.OrderdetailsPK[ productsProductId=" + productsProductId + ", orderId=" + orderId + " ]";
    }

}
