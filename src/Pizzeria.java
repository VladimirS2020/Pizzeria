import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class Pizzeria {
    class Order {
        String pizzaName;
        long timeOfOrder;

        Order(String pizzaName, long timeOfOrder) {
            this.pizzaName = pizzaName;
            this.timeOfOrder = timeOfOrder;
        }
    }

    LinkedBlockingDeque<Order> orders = new LinkedBlockingDeque<>();
    final long START_OF_WORKING_HOURS;

    Pizzeria() {
        START_OF_WORKING_HOURS = System.currentTimeMillis();
        new PizzaCar().start();
        new PizzaCar().start();
    }


    class PizzaCar extends Thread {
        @Override
        public void run() {
            while (System.currentTimeMillis() - START_OF_WORKING_HOURS < 5000) {
                Order newOrder = null;
                try {
                    newOrder = orders.poll(10, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    break;
                }
                if (newOrder != null) {
                    if (System.currentTimeMillis() + 500 - newOrder.timeOfOrder <= 750) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            break;
                        }
                        System.out.println(newOrder.pizzaName + " was delivered.");
                    } else {
                        System.out.println(newOrder.pizzaName + " was not delivered.");
                    }
                }
            }
        }
    }

    void order(String pizzaName) {
        try {
            orders.put(new Order(pizzaName, System.currentTimeMillis()));
        } catch (InterruptedException e) {
            return;
        }
    }
}
