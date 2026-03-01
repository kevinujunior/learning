
import java.util.*;

class ListNode {

    int val;
    ListNode next;

    ListNode(int num) {
        this.val = num;
        this.next = null;
    }

}

public class W11_T1_P2 {

    public static void main(String[] args) {

        // List 1: 1 -> 7 -> 8 -> 12
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(7);
        l1.next.next = new ListNode(8);
        l1.next.next.next = new ListNode(12);

        // List 2: 3 -> 5 -> 9 -> 16
        ListNode l2 = new ListNode(3);
        l2.next = new ListNode(5);
        l2.next.next = new ListNode(9);
        l2.next.next.next = new ListNode(16);

        // List 3: 2 -> 10 -> 52 -> 64
        ListNode l3 = new ListNode(2);
        l3.next = new ListNode(10);
        l3.next.next = new ListNode(52);
        l3.next.next.next = new ListNode(64);

        // List 4: 8 -> 9 -> 11 -> 15
        ListNode l4 = new ListNode(17);
        l4.next = new ListNode(18);
        l4.next.next = new ListNode(22);
        ListNode[] list = { l1, l2, l3, l4 };

        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparing(n -> n.val));

        for (ListNode lis : list) {
            pq.offer(lis);
        }

        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            int val = node.val;
            System.out.println(val);
            if (node.next != null) {
                pq.offer(node.next);
            }

        }

    }

}
