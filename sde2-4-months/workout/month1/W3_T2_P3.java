
class ListNode{
    int val;
    ListNode next;

    ListNode(int val){
        this.val = val;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        ListNode curr = this;

        while(curr!=null){
            sb.append(curr.val);
            sb.append("->");
            curr = curr.next;
        }
        sb.append("null");
        

        return sb.toString();
    }
}

public class W3_T2_P3 {



    public static ListNode mergeLL(ListNode head1, ListNode head2){
        ListNode head = new ListNode(0);

        ListNode curr = head;

        while(head1!=null && head2!=null){
            if(head1.val<head2.val){
                curr.next = head1;
                head1 = head1.next;
            }
            else if(head2.val<head1.val){
                curr.next = head2;
                head2 = head2.next;
            }
            curr = curr.next;
        }

        if(head1!=null){
            curr.next = head1;
        }

         if(head2!=null){
           curr.next = head2;
        }

        return head.next;

        
    }

    public static void main(String[] args){
        ListNode head1 = new ListNode(0);
        ListNode curr1 = head1;
        for (int i = 1; i <=10; i+=4) {
            curr1.next = new ListNode(i);
            curr1 = curr1.next;
        }

        ListNode head2 = new ListNode(2);
        ListNode curr2 = head2;
        for (int i = 4; i <=10; i+=2) {
            curr2.next = new ListNode(i);
            curr2 = curr2.next;
        }

        System.out.println("head1 : "  + head1);
        System.out.println("head2 : "  + head2);

        System.out.println("merged List : "  + mergeLL(head1,head2));


    }
    
}
