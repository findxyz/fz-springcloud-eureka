package xyz.fz.provider2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_inventory")
public class Inventory {
    @Id
    private Long id;

    @Column
    private Integer productId;

    @Column
    private Integer productCount;

    @Column
    private Integer freezeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }
}
