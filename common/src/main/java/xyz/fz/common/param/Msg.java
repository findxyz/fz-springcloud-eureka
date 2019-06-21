package xyz.fz.common.param;

public class Msg implements Cloneable {

    private Long msgId;

    private String exchange;

    private String routingKey;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msgId=" + msgId +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                '}';
    }
}
