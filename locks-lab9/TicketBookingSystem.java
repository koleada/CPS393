import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import java.util.LinkedList;

class Theater {
    private final boolean[] seats;
    private final ReentrantLock lock = new ReentrantLock();
    private final Queue<Integer> waitingList = new LinkedList<>();

    public Theater(int numberOfSeats) {
        this.seats = new boolean[numberOfSeats];
    }

    public void bookSeat(int seatNumber) {
        boolean booked = false;
        
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) { 
                try {
                    if (!seats[seatNumber]) {
                        seats[seatNumber] = true;
                        System.out.println("Seat " + seatNumber + " booked successfully by " + Thread.currentThread().getName());
                        booked = true;
                    } else {
                        System.out.println("Seat " + seatNumber + " is already booked. Adding " + Thread.currentThread().getName() + " to the waiting list.");
                        waitingList.add(seatNumber);
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not book seat " + seatNumber + " within the timeout.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!booked) {
            manageWaitingList(seatNumber);
        }
    }

    public void cancelBooking(int seatNumber) {
        lock.lock();
        try {
            if (seats[seatNumber]) {
                seats[seatNumber] = false;
                System.out.println("Seat " + seatNumber + " is now available.");
                manageWaitingList(seatNumber);
            }
        } finally {
            lock.unlock();
        }
    }

    private void manageWaitingList(int seatNumber) {
        lock.lock();
        try {
            if (!seats[seatNumber] && !waitingList.isEmpty() && waitingList.peek() == seatNumber) {
                System.out.println("Notifying waiting list for seat " + seatNumber);
                waitingList.poll();
                bookSeat(seatNumber); 
            }
        } finally {
            lock.unlock();
        }
    }
}

public class TicketBookingSystem {
    public static void main(String[] args) {
        Theater theater = new Theater(10); 
        
        Thread user1 = new Thread(() -> theater.bookSeat(3), "User1");
        Thread user2 = new Thread(() -> theater.bookSeat(3), "User2");
        Thread user3 = new Thread(() -> theater.bookSeat(4), "User3");

        user1.start();
        user2.start();
        user3.start();

        new Thread(() -> {
            try {
                Thread.sleep(3000); 
                theater.cancelBooking(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        try {
            user1.join();
            user2.join();
            user3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

