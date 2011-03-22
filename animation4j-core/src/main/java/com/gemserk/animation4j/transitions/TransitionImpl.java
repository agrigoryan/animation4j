package com.gemserk.animation4j.transitions;

import com.gemserk.animation4j.time.TimeProvider;

/**
 * Provides default implementation of transition.
 * 
 * @author acoppes
 */
public class TransitionImpl<T> implements Transition<T> {

	private final float speed;

	private final TimeProvider timeProvider;

	private long lastTime;
	
	private InternalTransition<T> transition;

	public TransitionImpl(InternalTransition<T> transition, float speed, TimeProvider timeProvider) {
		this.speed = speed;
		this.timeProvider = timeProvider;
		this.transition = transition;
	}

	public T get() {
		long currentTime = timeProvider.getTime();
		long delta = currentTime - lastTime;

		if (delta <= 0)
			return transition.get();

		float time = ((float) delta) * speed;
		transition.update((int) (time * 1000f));
		lastTime = currentTime;
		return transition.get();
	}

	public void set(T t) {
		this.set(t, 1000);
	}

	public void set(T t, int time) {
		lastTime = timeProvider.getTime();
		transition.set(t, time);
	}
}
