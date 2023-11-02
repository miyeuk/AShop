package com.AShop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문 가격

    private int count; //수량


   /* private LocalDateTime regTime;

    private LocalDateTime updateTime;
*/
    public  static OrderItem createOrderItem(Item item , int count){
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        //주문할 상품
        orderItem.setCount(count);
        //주문할 수량 세팅
        orderItem.setOrderPrice(item.getPrice());
        //현재 시간 기준으로 상품 가격을 주문 가격으로 세팅. 상품 가격은 시간에 따라서 달라 질 수 있다.

        item.removeStock(count);
        //소주문 수량만큼 상품 재고 감
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
        //주문 가격과 수량을 곱해 해당 상품을 주문한 총 가격을 계산
    }

    public void cancel(){
        this.getItem().addStock(count);
        //주문 취소 시 주문 수량 만큼 상품의 재고를 더해줌
    }

}
