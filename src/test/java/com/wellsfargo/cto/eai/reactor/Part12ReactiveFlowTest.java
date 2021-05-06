package com.wellsfargo.cto.eai.reactor;

import com.wellsfargo.cto.eai.reactor.domain.User;
import com.wellsfargo.cto.eai.reactor.repository.ReactiveRepository;
import com.wellsfargo.cto.eai.reactor.repository.ReactiveUserRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

public class Part12ReactiveFlowTest {
    Part12ReactiveFlow workshop = new Part12ReactiveFlow();

    @Test
    public void sequentialFlow() {
        ReactiveRepository<User> repository = new ReactiveUserRepository();
        Flux<String> flux = workshop.sequentialFlow(repository);

      StepVerifier.create(flux)
                .expectNext("swhite", "jpinkman", "wwhite", "sgoodman")
                .verifyComplete();
    }

    @Test
    public void parallelFlow() {
        ReactiveRepository<User> repository = new ReactiveUserRepository();
        ParallelFlux<String> flux = workshop.parallelFlow(repository);

        StepVerifier.create(flux)
                .expectNext("SWHITE", "JPINKMAN", "WWHITE", "SGOODMAN")
                .verifyComplete();
    }
}
