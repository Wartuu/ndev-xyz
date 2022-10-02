package impl.utils;

import com.sun.net.httpserver.HttpExchange;

import java.net.InetAddress;
import java.util.*;

public class IPCollector {
    private final Set<InetAddress> addressSet = Collections.synchronizedSet(new HashSet<InetAddress>());

    public void add(HttpExchange exchange) {
        InetAddress newAddress = exchange.getRemoteAddress().getAddress();

        for (InetAddress address : addressSet) {
            if(address.equals(newAddress)) {return;}
        }


        addressSet.add(newAddress);
    }

    public Set<InetAddress> getAsSet() {
        return addressSet;
    }

    public List<InetAddress> getAsList() {
        return addressSet.stream().toList();
    }

    public InetAddress get(int index) {
        return addressSet.stream().toList().get(index);
    }
}
