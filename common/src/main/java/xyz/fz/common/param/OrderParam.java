package xyz.fz.common.param;

import java.util.List;

public class OrderParam extends Msg {

    private Long orderId;

    private List<OrderDetail> orderDetailList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public String toString() {
        return "OrderParam{" +
                "orderId=" + orderId +
                ", orderDetailList=" + orderDetailList +
                '}';
    }

    public static class OrderDetail {
        private Integer productId;

        private Integer productCount;

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

        @Override
        public String toString() {
            return "OrderDetail{" +
                    "productId=" + productId +
                    ", productCount=" + productCount +
                    '}';
        }
    }
}
