
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

public class W3_T3_P4 {


    //find length and remove
    // public static ListNode removeNth(ListNode head, int n){
        
    //     //find length
    //     int len = 1;
    //     var slow = head;
    //     var fast = head;
    //     while(fast!=null && fast.next!=null){
    //         slow = slow.next;
    //         fast = fast.next.next;
    //         len++;
    //     }

    //     if(fast==null){
    //         len = len*2-2;
    //     }
    //     else if(fast.next==null){
    //         len = len*2-1;
    //     }

        
    //     //len from begining = l-n+1;
    //     int N = len-n+1;
    //     var curr =  head;
    //     ListNode prev = null;

    //     while(N>1){
    //         prev = curr;
    //         curr = curr.next;
    //         N--;
    //     }
    //     if(prev==null){
    //         return head.next;
    //     }

    //     prev.next = curr.next;
    
    //     return head;

    // }


    //single pass
    //always maintain a gap of 2 between fast and slow

    // 0 1 2 3 4 5 6 7 8 9 10
    // 2nd from last or we can say when our slow reaches 8 and fast at null
    // we can give fast a headstart of 2
    public static ListNode removeNth(ListNode head, int n){
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        var fast = dummy;
        var slow = dummy;

        for(int i=0;i<=n;i++){
            fast = fast.next;
        }


        while(fast!=null){
            slow = slow.next;
            fast = fast.next;
        }
        
        slow.next = slow.next.next;
        
        return dummy.next;
    }

    public static void main(String[] args){
        ListNode head = new ListNode(0);
        ListNode curr1 = head;
        for (int i = 1; i <=3; i++) {
            curr1.next = new ListNode(i);
            curr1 = curr1.next;
        }

        System.out.println("head : "  + head);
        System.out.println("removing Nth node : "  + removeNth(head,   4));


    }
    
}


// 1 2 3 4 5 6 7 8 9 10 

// L = length
// L - N + 1 
// 2nd node from end = 10-2+1 = 9th node from begining


//n = 4
//1 2 3 4 5 6 7 8 
//1 2 3 4 5 6 7

// s = 5
// f = null
//0 1 2 3