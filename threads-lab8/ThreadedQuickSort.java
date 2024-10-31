package mysql.threads-lab8;

import java.util.Arrays;

public class ThreadedQuickSort {
    public static void main(String[] args) {
        int[] a = new int[Integer.parseInt(args[0])];
        fill(a);
        System.out.println("initial: \n" + Arrays.toString(a));

        quickSort(a, 0, a.length - 1);

        System.out.println("sorted: \n" + Arrays.toString(a));
    }

    public static void fill(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (int) (Math.random() * 100);
        }
    }

    public static void quickSort(int[] a, int s, int e) {
        if (e - s > 1) {
            int pivot = a[s];
            int next = s + 1;
            for (int i = next; i < e; i++) {
                if (a[i] < pivot) {
                    int temp = a[next];
                    a[next] = a[i];
                    a[i] = temp;
                    next++;
                }
            }
            a[s] = a[next - 1];
            a[next - 1] = pivot;

            // Create two threads for recursive calls
            Thread t1 = new Thread(() -> quickSort(a, s, next - 1));
            Thread t2 = new Thread(() -> quickSort(a, next, e));

            // Start the threads
            t1.start();
            t2.start();

            // Wait for both threads to finish
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
