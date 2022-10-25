package com.carlosmarin.reactor.app;

import com.carlosmarin.reactor.app.models.Comments;
import com.carlosmarin.reactor.app.models.User;
import com.carlosmarin.reactor.app.models.UserWithComments;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        iterableSample();
//        flatMapSample();
//        toStringSample();
//        collectListSample();
//        userWithCommentsFlatMapSample();
//        userWithCommentsZipWithSample();
//        userWithCommentsZipWithSampleTwo();
//        zipWithRangeSample();
//        intervalSample();
//        delayElementsSample();
//        endlessIntervalSample();
//        intervalFromCreateSample();
        backpressureSample();
    }

    public void backpressureSample() {
        Flux.range(1, 10)
                .log()
                .subscribe(new Subscriber<>() {
                    private Subscription s;
                    private final Integer LIMIT = 3;
                    private Integer consumed = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(LIMIT);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info(integer.toString());
                        consumed++;
                        if (consumed.equals(LIMIT)) {
                            consumed = 0;
                            s.request(LIMIT);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Flux.range(1, 10)
                .log()
                .limitRate(4)
                .subscribe();
    }

    public void intervalFromCreateSample() {
        Flux.create(fluxSink -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        int count = 0;

                        @Override
                        public void run() {
                            fluxSink.next(++count);
                            if (count == 10) {
                                timer.cancel();
                                fluxSink.complete();
                            }
                            if (count == 5) {
                                timer.cancel();
                                fluxSink.error(new InterruptedException("Error, se ha detenido el flux en 5!"));
                            }
                        }
                    }, 1000, 1000);
                })
                .subscribe(next -> log.info(next.toString()),
                        error -> log.error(error.getMessage()),
                        () -> log.info("Hemos terminado")
                );
    }

    public void endlessIntervalSample() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Flux.interval(Duration.ofSeconds(1))
                .doOnTerminate(countDownLatch::countDown)
                .flatMap(aLong -> aLong >= 5
                        ? Flux.error(new InterruptedException("Solo hasta 5"))
                        : Flux.just(aLong))
                .map(aLong -> "Hello " + aLong)
                .retry(2)
                .subscribe(log::info, error -> log.error(error.getMessage()));

        countDownLatch.await();
    }

    public void delayElementsSample() {
        Flux<Integer> range = Flux.range(1, 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(ran -> log.info(ran.toString()));

        range.blockLast();
    }

    public void intervalSample() {
        Flux<Integer> range = Flux.range(1, 12);
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        range.zipWith(interval, (ran, inter) -> ran)
                .doOnNext(ran -> log.info(ran.toString()))
                .blockLast();
    }

    public void zipWithRangeSample() {
        Flux<Integer> range = Flux.range(0, 4);
        Flux.just(1, 2, 3, 4)
                .map(integer -> integer * 2)
                .zipWith(range, (uno, dos) -> String.format("Primer flux: %d, Segundo Flux: %d", uno, dos))
                .subscribe(log::info);
    }

    public void userWithCommentsZipWithSampleTwo() {
        Mono<User> userMono = Mono.fromCallable(() -> new User("Carlos", "Marin"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> Comments.builder()
                .comment("Hola, que tal!")
                .comment("Mañana voy a la playa")
                .comment("Estoy viendo el curso de Spring Reactor")
                .build()
        );

        userMono
                .zipWith(commentsMono)
                .map(tuple -> {
                    User user = tuple.getT1();
                    Comments comment = tuple.getT2();
                    return new UserWithComments(user, comment);
                })
                .subscribe(userWithComment -> log.info(userWithComment.toString()));
    }

    public void userWithCommentsZipWithSample() {
        Mono<User> userMono = Mono.fromCallable(() -> new User("Carlos", "Marin"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> Comments.builder()
                .comment("Hola, que tal!")
                .comment("Mañana voy a la playa")
                .comment("Estoy viendo el curso de Spring Reactor")
                .build()
        );

        userMono
                .zipWith(commentsMono, (user, comment) -> new UserWithComments(user, comment))
                .subscribe(userWithComment -> log.info(userWithComment.toString()));
    }

    public void userWithCommentsFlatMapSample() {
        Mono<User> userMono = Mono.fromCallable(() -> new User("Carlos", "Marin"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> Comments.builder()
                .comment("Hola, que tal!")
                .comment("Mañana voy a la playa")
                .comment("Estoy viendo el curso de Spring Reactor")
                .build()
        );

        userMono
                .flatMap(user -> commentsMono.map(comment -> new UserWithComments(user, comment)))
                .subscribe(userWithComment -> log.info(userWithComment.toString()));
    }

    public void collectListSample() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("Carlos", "Marin"));
        userList.add(new User("Andres", "Martinez"));
        userList.add(new User("Maria", "Lopez"));
        userList.add(new User("Juan", "Perez"));
        userList.add(new User("Ian", "Marin"));
        userList.add(new User("Samuel", "Marin"));

        Flux.fromIterable(userList)
                .collectList()
                .subscribe(list -> log.info(list.toString()));
    }

    public void toStringSample() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("Carlos", "Marin"));
        userList.add(new User("Andres", "Martinez"));
        userList.add(new User("Maria", "Lopez"));
        userList.add(new User("Juan", "Perez"));
        userList.add(new User("Ian", "Marin"));
        userList.add(new User("Samuel", "Marin"));

        Flux.fromIterable(userList)
                .map(user -> user.getName().toUpperCase().concat(" ").concat(user.getSurname().toUpperCase()))
                .flatMap(name -> {
                    if (name.contains("MARIN")) {
                        return Mono.just(name);
                    } else {
                        return Mono.empty();
                    }
                })
                .map(String::toLowerCase)
                .subscribe(log::info);
    }

    public void flatMapSample() {
        List<String> userList = new ArrayList<>();
        userList.add("Carlos Marin");
        userList.add("Andres Martinez");
        userList.add("Maria Lopez");
        userList.add("Juan Perez");
        userList.add("Ian Marin");
        userList.add("Samuel Marin");

        Flux.fromIterable(userList)
                .map(name -> new User(name.split(" ")[0].toUpperCase(), name.split(" ")[1].toUpperCase()))
                .flatMap(user -> {
                    if (user.getSurname().equalsIgnoreCase("Marin")) {
                        return Mono.just(user);
                    } else {
                        return Mono.empty();
                    }
                })
                .map(user -> {
                    String name = user.getName().toLowerCase();
                    user.setName(name);
                    return user;
                })
                .subscribe(user -> log.info(user.toString()));
    }

    public void iterableSample() {
        List<String> userList = new ArrayList<>();
        userList.add("Carlos Marin");
        userList.add("Andres Martinez");
        userList.add("Maria Lopez");
        userList.add("Juan Perez");
        userList.add("Ian Marin");
        userList.add("Samuel Marin");

//        Flux<String> names = Flux.just("Carlos Marin", "Andres Martinez", "Maria Lopez", "Juan Perez", "Ian Marin", "Samuel Marin");
        Flux<String> names = Flux.fromIterable(userList);

        Flux<User> users = names.map(name -> new User(name.split(" ")[0].toUpperCase(), name.split(" ")[1].toUpperCase()))
                .filter(user -> user.getSurname().equalsIgnoreCase("Marin"))
                .doOnNext(user -> {
                    if (user == null) {
                        throw new RuntimeException("Nombres no pueden ser vacios");
                    }
                    System.out.println(user);
                })
                .map(user -> {
                    String name = user.getName().toLowerCase();
                    user.setName(name);
                    return user;
                });

        users.subscribe(user -> log.info(user.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecucion del observable con exito!"));
    }
}
