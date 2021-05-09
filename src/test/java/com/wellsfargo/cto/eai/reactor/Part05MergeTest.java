package com.wellsfargo.cto.eai.reactor;

import com.wellsfargo.cto.eai.reactor.domain.User;
import com.wellsfargo.cto.eai.reactor.repository.ReactiveRepository;
import com.wellsfargo.cto.eai.reactor.repository.ReactiveUserRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Learn how to merge flux.
 *
 * */
public class Part05MergeTest {

	Part05Merge workshop = new Part05Merge();

	final static User MARIE = new User("mschrader", "Marie", "Schrader");
	final static User MIKE = new User("mehrmantraut", "Mike", "Ehrmantraut");

	ReactiveRepository<User> repositoryWithDelay = new ReactiveUserRepository(500);
	ReactiveRepository<User> repository          = new ReactiveUserRepository(MARIE, MIKE);

//========================================================================================

	@Test
	public void mergeWithInterleave() {
		Flux<User> flux = workshop.mergeFluxWithInterleave(repositoryWithDelay.findAll(), repository.findAll());
		StepVerifier.create(flux)
				.expectNext(MARIE, MIKE, User.SKYLER, User.JESSE, User.WALTER, User.MIKE)
				.verifyComplete();
	}

//========================================================================================

	@Test
	public void mergeWithNoInterleave() {
		Flux<User> flux = workshop.mergeFluxWithNoInterleave(repositoryWithDelay.findAll(), repository.findAll());
		StepVerifier.create(flux)
				.expectNext(User.SKYLER, User.JESSE, User.WALTER, User.MIKE, MARIE, MIKE)
				.verifyComplete();
	}

//========================================================================================

	@Test
	public void multipleMonoToFlux() {
		Mono<User> skylerMono = repositoryWithDelay.findFirst();
		Mono<User> marieMono = repository.findFirst();
		Flux<User> flux = workshop.createFluxFromMultipleMono(skylerMono, marieMono);
		StepVerifier.create(flux)
				.expectNext(User.SKYLER, MARIE)
				.verifyComplete();
	}

	@Test
	public void givenFluxes_whenCombineLatestIsInvoked_thenCombineLatest() {

		Flux<Integer> evenNumbers = Flux
				.range(1, 5)
				.filter(x -> x % 2 == 0)
				.log(); // i.e. 2, 4

		Flux<Integer> oddNumbers = Flux
				.range(1, 5)
				.filter(x -> x % 2 > 0);  // ie. 1, 3, 5

		Flux<Integer> fluxOfIntegers = Flux.combineLatest(
				evenNumbers,
				oddNumbers,
				(a, b) -> a + b)
				.log();

		StepVerifier.create(fluxOfIntegers)
				.expectNext(5) // 4 + 1
				.expectNext(7) // 4 + 3
				.expectNext(9) // 4 + 5
				.expectComplete()
				.verify();
	}

}
