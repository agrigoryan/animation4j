package com.gemserk.animation4j.transitions;

import com.gemserk.animation4j.converters.TypeConverter;
import com.gemserk.animation4j.interpolator.Interpolator;

class InternalTransition<T> {

	private int totalTime;

	private int currentTime;

	private int defaultTime;

	private Interpolator<float[]> interpolator;

	private float[] startValue;

	private float[] currentValue;

	private float[] desiredValue;

	private T currentObject;

	private final TypeConverter<T> typeConverter;

	InternalTransition(T startValue, TypeConverter<T> typeConverter, Interpolator<float[]> interpolator) {
		this(startValue, typeConverter, interpolator, 0);
	}

	InternalTransition(T startValue, TypeConverter<T> typeConverter, Interpolator<float[]> interpolator, int defaultTime) {
		this.typeConverter = typeConverter;
		this.startValue = typeConverter.copyFromObject(startValue, null);
		this.currentValue = null;
		this.interpolator = interpolator;
		this.defaultTime = defaultTime;
	}

	public T get() {
		if (currentValue == null)
			currentObject = typeConverter.copyToObject(currentObject, startValue);
		else
			currentObject = typeConverter.copyToObject(currentObject, currentValue);
		return currentObject;
	}

	public void set(T t) {
		this.set(t, defaultTime);
	}

	public void set(T t, int time) {
		this.desiredValue = typeConverter.copyFromObject(t, desiredValue);
		this.totalTime = time;
		
		if (currentValue != null)
			copyArray(startValue, currentValue);
		
		this.currentTime = 0;
		
		if (time == 0)
			this.currentValue = interpolator.interpolate(startValue, desiredValue, 1f);
	}

	protected void copyArray(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++) 
			a[i] = b[i];
	}

	public void update(int time) {
		if (isFinished())
			return;

		currentTime += time;

		if (currentTime > totalTime)
			currentTime = totalTime;

		if (desiredValue == null)
			return;

		float alpha = (float) currentTime / (float) totalTime;

		currentValue = interpolator.interpolate(startValue, desiredValue, alpha);
	}
	
	public boolean isFinished() {
		return currentTime == totalTime;
	}

}