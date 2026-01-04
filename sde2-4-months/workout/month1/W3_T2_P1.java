class ListNode{
    int val;
    ListNode next;
    ListNode(int x) { this.val = x; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        ListNode current = this;
        while(current!=null){
            sb.append(current.val).append(" -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }
}


public class W3_T2_P1 {

    //iterative approach
    // public static ListNode reverseList(ListNode listNode){
    //     ListNode curr = listNode;
    //     ListNode prev = null;

    //     while(curr!=null){
    //         ListNode nextNode = curr.next;
    //         curr.next = prev;
    //         prev = curr;
    //         curr = nextNode;
    //     }
    //     return prev;
    // }


    public static ListNode reverseList(ListNode curr){
      return reverseHelper(curr,null);
    }

    //recursive approach
    public static ListNode reverseHelper(ListNode curr, ListNode prev){
       // base case
        if (curr == null) {
            return prev;
        }

        ListNode next = curr.next; // save next
        curr.next = prev;          // reverse link

        // tail recursive call
        return reverseHelper(next, curr);

    }

    public static void main(String[] args){
        ListNode listNode = new ListNode(0);
        ListNode head = listNode;
        for (int i = 1; i <=10; i++) {
            head.next = new ListNode(i);
            head = head.next;
        }

        System.out.println("no ops list :" + listNode);

        System.out.println("reversed list : " +  reverseList(listNode));
        

    }
    
}



// 1->2->3->4->5->6

// prev = null

// nextNode = curr->next
// curr->next = prev
// prev = curr;
// curr = nextNode;

