import java.util.concurrent.locks.ReentrantLock;

class InventoryManagement {

    private int quantity;
    private final ReentrantLock lock = new ReentrantLock();

    public InventoryManagement(int quantity) {
        this.quantity = quantity;
    }

    public String purchaseItem(int quantity) {
        lock.lock();
        try {
            if (this.quantity >= quantity) {
                this.quantity -= quantity;
                return "Purchase successful. Remaining stock: " + this.quantity;
            } else {
                return "Sorry, we don't have enough to fill the requested purchase.";
            }
        } finally {
            lock.unlock();
        }
    }

   public void restock(int quantity) {
        lock.lock();
        try {
            this.quantity += quantity;
            System.out.println("Restocked " + quantity + " items. Total stock: " + this.quantity);
        } finally {
            lock.unlock();
        }
    }

    public int getQuantity() {
        return quantity;
    }
}

public class Inventory {

    public static void main(String[] args) {
        InventoryManagement inventory = new InventoryManagement(100);

        Thread purchaseThread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + inventory.purchaseItem(50));
        });

        Thread purchaseThread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + inventory.purchaseItem(30));
        });

        Thread restockThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) { 
                inventory.restock(20);
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        purchaseThread1.start();
        purchaseThread2.start();
        restockThread.start();

        try {
            purchaseThread1.join();
            purchaseThread2.join();
            restockThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
