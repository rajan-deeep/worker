package org.panda;
import org.redisson.Redisson;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
public class Main {

    private final RedissonClient redisson;

    public Main() {
        // Initialize Redisson client
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        redisson = Redisson.create(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Component
    public class TaskProcessor implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) throws Exception {
            // Start multiple task processors to process tasks concurrently
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                executor.submit(() -> processTasks(finalI));
            }
        }

        @Async
        public void processTasks(int processId) {
            RQueue<String> queue = redisson.getQueue("taskQueue");

            while (true) {
                String task = queue.poll();
                if (task != null) {
                    // Process the task here
                    System.out.println("Process " + processId + " is processing task: " + task);
                } else {
                    System.out.println("Process " + processId + " found no tasks in the queue, waiting...");
                    try {
                        Thread.sleep(1000); // Wait for tasks to be available
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @PostMapping("/push")
    public String pushToQueue(@RequestBody String task) {
        // Push task into the Redis queue
        redisson.getQueue("taskQueue").add(task);
        return "Task pushed into the queue: " + task;
    }
}
