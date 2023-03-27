package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final NavigableMap<Customer, String> customers = new TreeMap<>((Comparator.comparingLong(Customer::getScores)));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> first = customers.firstEntry();
        return first == null ? null : new AbstractMap.SimpleEntry<>(new Customer(first.getKey()), first.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higher = customers.higherEntry(customer);
        return higher == null ? null : new AbstractMap.SimpleEntry<>(new Customer(higher.getKey()), higher.getValue());
    }

    public void add(
        Customer customer,
        String data
    ) {
        customers.put(customer, data);
    }
}
