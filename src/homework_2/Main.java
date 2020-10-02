package homework_2;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws DummyException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader
                (new FileInputStream(args[0])));
             Connection ci = new ConnectionImpl();) {
            ci.start();
            Session session =  ci.createSession(true);
            Destination dest = session.createDestination("Queue");
            Producer producer = session.createProducer(dest);

            br.mark(1000);
            while (true) {
                Stream<String> strings = br.lines();
                strings.forEach(s -> {
                    producer.send(s);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                br.reset();
            }

        } catch (IOException io) {
            System.out.println("Ошибка при чтении файла!");
        }
    }
}

