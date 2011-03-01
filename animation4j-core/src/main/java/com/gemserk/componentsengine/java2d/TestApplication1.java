package com.gemserk.componentsengine.java2d;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.gemserk.animation4j.interpolators.CubicBezierInterpolator;
import com.gemserk.animation4j.interpolators.FloatInterpolator;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.timeline.TimelineAnimation;
import com.gemserk.animation4j.timeline.TimelineBuilder;
import com.gemserk.animation4j.timeline.TimelineValueBuilder;

public class TestApplication1 extends Java2dDesktopApplication {
	
	class Test1 implements Java2dGame {
		
		private TimelineAnimation timelineAnimation;
		
		float time;

		private final KeyboardInput keyboardInput;

		private final MouseInput mouseInput;

		public Test1(KeyboardInput keyboardInput, MouseInput mouseInput) {
			this.keyboardInput = keyboardInput;
			this.mouseInput = mouseInput;
		}

		@Override
		public void update(int delta) {
			
			float deltaF = (float)delta;
			
			timelineAnimation.update(deltaF);
			
			time += deltaF * 0.03f;
			
			if (keyboardInput.keyDownOnce(KeyEvent.VK_ENTER)) {
				timelineAnimation.restart();
			}
			
		}

		@Override
		public void render(Graphics2D graphics2d) {
			
			 graphics2d.clearRect(0, 0, 800, 480);
			
			Float xvalue = timelineAnimation.getValue("x");
			Float yvalue = timelineAnimation.getValue("y");
			
			int x = (int) (xvalue * 100 + 100);
//			int y = (int)time + 100;
			int y = (int) (yvalue * 100 + 100);
			
			graphics2d.fillOval(x, y, 5, 5);
			
		}

		@Override
		public void init() {
			
			time = 0;
			
			Timeline timeline = new TimelineBuilder() {{
				
				delay(1000);
				
				value("x", new TimelineValueBuilder<Float>() {{
					keyFrame(0, 0f, new FloatInterpolator(new CubicBezierInterpolator(0f, 0.42f, 1f, 1f)));
					keyFrame(1000, 1f, null);
				}});
				
				value("y", new TimelineValueBuilder<Float>() {{
					keyFrame(0, 0f, new FloatInterpolator(new CubicBezierInterpolator(0f, 0f, 1f, 1f)));
					keyFrame(1000, 1f, null);
				}});
				
			}}.build();
			
			timelineAnimation = new TimelineAnimation(timeline);
			timelineAnimation.setSpeed(1f);
			timelineAnimation.start(2);
			
		}
	}

	public static void main(String[] args) {
		TestApplication1 testApplication1 = new TestApplication1();
		testApplication1.init();
		testApplication1.start();
	}

	@Override
	public void init() {
		
		KeyboardInput keyboardInput = new KeyboardInput();
		MouseInput mouseInput = new MouseInput();
		
		createWindow("Title", new Dimension(800, 480), new Test1(keyboardInput, mouseInput), keyboardInput, mouseInput);
		
	}

}
