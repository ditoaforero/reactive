package com.ditosoft.springboot.reactor.app;

import com.ditosoft.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //executeWithJust();
        //executeWithIterator();
        //executeWithFlatMap();
        covertToFluxString();
    }

    private void executeWithJust() {
        Flux<String> nombres = Flux.just("Andres Guzman", "Pedro Fulano", "Maria Sutano", "Eduardo Arevalo", "Liliana Manjarres", "Bruce Lee", "Bruce Willis");

        Flux<Usuario> usuarios = nombres.map(nombre -> {
            return new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]);
        })
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null)
                        throw new RuntimeException("Nombres no pueden ser vacios");
                    System.out.println(usuario);
                });

        //nombres.subscribe(log::info);
        usuarios.subscribe(elemento -> log.info(elemento.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Se completó satisfactoriamente!!");
                    }
                }
        );
    }

    private void executeWithIterator() {

        List<String> nombresList = new ArrayList();
        nombresList.add("Andres Guzman");
        nombresList.add("Pedro Fulano");
        nombresList.add("Maria Sutano");
        nombresList.add("Eduardo Arevalo");
        nombresList.add("Liliana Manjarres");
        nombresList.add("Bruce Lee");
        nombresList.add("Bruce Willis");

        Flux<String> nombres = Flux.fromIterable(nombresList);

        Flux<Usuario> usuarios = nombres.map(nombre -> {
            return new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]);
        })
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null)
                        throw new RuntimeException("Nombres no pueden ser vacios");
                    System.out.println(usuario);
                });

        //nombres.subscribe(log::info);
        usuarios.subscribe(elemento -> log.info(elemento.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Se completó satisfactoriamente!!");
                    }
                }
        );
    }


    private void executeWithFlatMap() {

        List<String> nombresList = new ArrayList();
        nombresList.add("Andres Guzman");
        nombresList.add("Pedro Fulano");
        nombresList.add("Maria Sutano");
        nombresList.add("Eduardo Arevalo");
        nombresList.add("Liliana Manjarres");
        nombresList.add("Bruce Lee");
        nombresList.add("Bruce Willis");

        Flux.fromIterable(nombresList)
            .map(nombre -> new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]))
            .flatMap(usuario -> {
                if (usuario.getNombre().equalsIgnoreCase("bruce"))
                    return Mono.just(usuario);
                return Mono.empty();
            })
            .subscribe(elemento -> log.info(elemento.toString()));
    }


    private void covertToFluxString() {

        List<Usuario> usuarios = new ArrayList();
        usuarios.add(new Usuario("Andres","Guzman"));
        usuarios.add(new Usuario("Pedro","Fulano"));
        usuarios.add(new Usuario("Maria","Sutano"));
        usuarios.add(new Usuario("Eduardo","Arevalo"));
        usuarios.add(new Usuario("Liliana","Manjarres"));
        usuarios.add(new Usuario("Bruce","Lee"));
        usuarios.add(new Usuario("Bruce","Willis"));

        Flux.fromIterable(usuarios)
            .filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
            .map(usuario -> usuario.getNombre().concat(" ").concat(usuario.getApellido()))
            .subscribe(nombre -> log.info(nombre));
    }


}
