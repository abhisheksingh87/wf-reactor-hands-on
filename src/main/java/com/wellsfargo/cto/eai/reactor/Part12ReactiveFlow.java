package com.wellsfargo.cto.eai.reactor;

import com.wellsfargo.cto.eai.reactor.domain.User;
import com.wellsfargo.cto.eai.reactor.repository.BlockingRepository;
import com.wellsfargo.cto.eai.reactor.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 *
 *
 */
public class Part12ReactiveFlow {

//========================================================================================

	Flux<String> sequentialFlow(ReactiveRepository<User> repository) {
		return repository.findAll()
				         .map(User::getUsername)
				         .map(String::toLowerCase);
	}

//========================================================================================

	//The parallelFlow method returns ParallelFlux which needs a Scheduler to run.
	// So, If we use parallel, then chain that with runOn method to pass the schedulers.
	// For more info on Schedulers, check this. Do note that to really parallelize, you have to use runOn method along with parallel!

	ParallelFlux<String> parallelFlow(ReactiveRepository<User> repository) {
		return repository.findAll()
				.parallel()
				.runOn(Schedulers.parallel())
				.map(User::getUsername)
				.map(String::toUpperCase);
	}

}
