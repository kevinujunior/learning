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

public class W3_T2_P2 {

    public static boolean detectCycle(ListNode head){
        if (head == null || head.next == null) {
            return false;
        }
        var slow = head;
        var fast = head;

        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow==fast) return true;
            
        }
        return false;
    }

    public static int findCycleLength(ListNode head){
        if (head == null || head.next == null) {
            return -1;
        }
        var slow = head;
        var fast = head;

        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow==fast) {
                var curr = slow.next;
                int len = 1;
                while(curr!=fast){
                    curr = curr.next;
                    len++;
                }
                return len;

            }
            
        }
        return -1;
    }


    public static int findEntryPoint(ListNode head){
        if (head == null || head.next == null) {
            return -1;
        }
        var slow = head;
        var fast = head;

        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow==fast) {
                var curr = head;
                while(curr!=slow){
                    slow = slow.next;
                    curr = curr.next;
                }
                return slow.val;

            }
            
        }
        return -1;
    }
    
    public static void main(String[] args){
        ListNode head1 = new ListNode(0);
        ListNode curr1 = head1;
        for (int i = 1; i <=10; i++) {
            curr1.next = new ListNode(i);
            curr1 = curr1.next;
        }

        ListNode head2 = new ListNode(0);
        ListNode curr2 = head2;
        ListNode exPtr = null;
        for (int i = 1; i <=10; i++) {
            curr2.next = new ListNode(i);
            curr2 = curr2.next;
            if(i==5){
                exPtr = curr2;
            }
        }

        curr2.next = exPtr;

        System.out.println("head1 has cycle? : " + detectCycle(head1));

        System.out.println("head2 has cycle? : " +  detectCycle(head2));


        System.out.println("head2 cycle Length : " +  findCycleLength(head2));

        System.out.println("head2 cycle Entry Point : " +  findEntryPoint(head2));
        

    }
}
