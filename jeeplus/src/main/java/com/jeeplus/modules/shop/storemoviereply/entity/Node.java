package com.jeeplus.modules.shop.storemoviereply.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.store.entity.Store;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhh
 * @date 2019/11/29 0029
 */
@Data
public class Node {
    public Node() {

    }

    /**
     * 构造函数
     *
     * @param reply
     */
    public Node(StoreMovieReply reply) {
        this.id=reply.getId();
        this.movieId=reply.getMovie().getId();
        this.lastId=reply.getLastId();
        this.text=reply.getRemarks();
        this.customer=reply.getCustomer();
        this.store = reply.getStore();
        this.type = reply.getYesNo();
    }


    private String id;
    private String movieId;
    private String text;
    private Customer customer;
    private String lastId;
    private Store store;
    private String type;
    //下一条回复
    private List<Node> nextNodes=new ArrayList<Node>();

    /**
     * 将单个node添加到链表中
     *
     * @param list
     * @param node
     * @return
     */
    public static boolean addNode(List<Node> list, Node node) {
        for (Node node1 : list) {   //循环添加
            if (node1.getId().equals(node.getLastId())) {   //判断留言的上一段是都是这条留言
                node1.getNextNodes().add(node);   //是，添加，返回true;
                System.out.println("添加了一个");
                return true;
            } else {     //否则递归继续判断
                if (node1.getNextNodes().size() != 0)
                    if (Node.addNode(node1.getNextNodes(), node)) {
                        return true;
                    }
            }
        }
        return false;
    }

    /**
     * 将查出来的lastId不为null的回复都添加到第一层Node集合中
     *
     * @param firstList
     * @param thenList
     * @return
     */
    public static List addAllNode(List<Node> firstList, List<StoreMovieReply> thenList) {
        while (thenList.size() != 0) {
            int size=thenList.size();
            for (int i=0; i < size; i++) {
                if (Node.addNode(firstList, new Node(thenList.get(i)))) {
                    thenList.remove(i);
                    i--;
                    size--;
                }
            }
        }
        return firstList;
    }

    //打印
    public static void show(List<Node> list) {
        for (Node node : list) {
            System.out.println(node.getCustomer().getName()+" 用户回复了你："+node.getText());
            if (node.getNextNodes().size() != 0) {
                Node.show(node.getNextNodes());
            }
        }

    }
}
