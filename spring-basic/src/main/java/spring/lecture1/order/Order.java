package spring.lecture1.order;

public class Order {
    private Long memberId;
    private String itemName;
    private int itemPrice; //아이템 가격
    private int discountPrice; //할인 가격

    /*
    * Order가 아이템의 가격과 할인 가격 모두 알고 있는 상황인데,
    * 구매 가격을 모르는건 말이 안됨
    * 그래서 Order 클래스에 calculatePrice메소드 구현
    * */
    public int calculatePrice() {
        return itemPrice - discountPrice;
    }

    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.discountPrice = discountPrice;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "memberId=" + memberId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
