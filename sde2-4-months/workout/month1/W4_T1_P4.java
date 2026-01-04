
class QueuefromCircularArray{
    int size;
    int[] nums;
    int start;
    int end;
    int capacity;


    QueuefromCircularArray(int capacity){
        this.capacity = capacity;
        this.nums = new int[this.capacity];
        this.start = 0;
        this.end = 0;

    }

    public void offer(int val){
        if(isFull()) throw new IllegalStateException("Queue is full");
        nums[end] = val;
        size++;
        end = (end+1)%capacity;
    }

    public int poll(){
        if(isEmpty()) throw new IllegalStateException("Queue is empty");
        size--;
        int val = nums[start];
        start = (start+1)%capacity;
        return val;
    }

    public int peek(){
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return nums[start];
    }

    public boolean isEmpty(){
        return size==0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(nums[(start + i) % capacity]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }


}

public class W4_T1_P4 {


    public static void main(String[] args){
        
        QueuefromCircularArray q = new QueuefromCircularArray(5);
        q.offer(10);
        System.out.println(q.toString());
        q.offer(20);
        System.out.println(q.toString());
        q.poll();
        System.out.println(q.toString());
        q.offer(40);
        System.out.println(q.toString());
        q.offer(50);
        System.out.println(q.toString());
        q.poll();
        System.out.println(q.toString());
        q.offer(70);
        q.offer(80);
        q.offer(90);
        q.poll();
        System.out.println(q.toString());
    }

    
}


//[10]
// [10, 20]
// [20]
// [20, 40]
// [20, 40, 50]
// [40, 50]
// [50, 70, 80, 90]
// 50