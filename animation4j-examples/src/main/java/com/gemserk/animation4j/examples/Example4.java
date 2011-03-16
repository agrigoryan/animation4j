package com.gemserk.animation4j.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gemserk.animation4j.event.AnimationHandlerManager;
import com.gemserk.animation4j.interpolator.ColorInterpolator;
import com.gemserk.animation4j.interpolator.Point2DInterpolator;
import com.gemserk.animation4j.interpolator.function.InterpolatorFunction;
import com.gemserk.animation4j.interpolator.function.InterpolatorFunctionFactory;
import com.gemserk.animation4j.transitions.AutoUpdateableTransition;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.componentsengine.java2d.Java2dDesktopApplication;
import com.gemserk.componentsengine.java2d.Java2dGame;
import com.gemserk.componentsengine.java2d.Java2dModule;
import com.gemserk.componentsengine.java2d.input.KeyboardInput;
import com.gemserk.componentsengine.java2d.input.MouseInput;
import com.gemserk.componentsengine.java2d.render.CurrentGraphicsProvider;
import com.gemserk.componentsengine.java2d.render.InitJava2dRenderer;
import com.gemserk.componentsengine.java2d.render.Java2dImageRenderObject;
import com.gemserk.componentsengine.java2d.render.Java2dRenderer;
import com.gemserk.componentsengine.modules.BasicModule;
import com.gemserk.componentsengine.modules.ResourcesManagerModule;
import com.gemserk.resources.Resource;
import com.gemserk.resources.ResourceManager;
import com.gemserk.resources.datasources.ClassPathDataSource;
import com.gemserk.resources.java2d.dataloaders.ImageLoader;
import com.gemserk.resources.resourceloaders.CachedResourceLoader;
import com.gemserk.resources.resourceloaders.ResourceLoaderImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Example4 extends Java2dDesktopApplication {

	public static void main(String[] args) {
		Java2dDesktopApplication java2dDesktopApplication = new Example4();
		java2dDesktopApplication.init();
		java2dDesktopApplication.start();
	}

	@Override
	public void init() {
		Injector injector = Guice.createInjector(new Java2dModule(), new BasicModule(), new ResourcesManagerModule());
		injector.getInstance(InitJava2dRenderer.class).config();
		Dimension resolution = new Dimension(640, 480);
		ExampleInternalGame game = injector.getInstance(ExampleInternalGame.class);
		createWindow("Example4", resolution, game, injector);
	}

	public void stop() {
		System.exit(0);
	}

	static class ExampleInternalGame implements Java2dGame {

		@Inject
		KeyboardInput keyboardInput;

		@Inject
		MouseInput mouseInput;

		@Inject
		ResourceManager resourceManager;

		@Inject
		AnimationHandlerManager animationHandlerManager;

		@Inject
		Java2dRenderer java2dRenderer;

		@Inject
		CurrentGraphicsProvider currentGraphicsProvider;

		private Resource<BufferedImage> buttonImageResource;

		private Resource<BufferedImage> buttonGlowImageResource;

		class Button {

			String text;

			// should be a property<T>...

			Transition<Point2D> position;

			Transition<Color> colorTransition;
			
			Transition<Color> glowColorTransition;
			
			Transition<Point2D> size;
			
			boolean mouseInside = false;

		}

		ArrayList<Button> buttons = new ArrayList<Button>();

		@Override
		public void init() {

			resourceManager.add("Background", new CachedResourceLoader<Image>(new ResourceLoaderImpl<Image>(new ImageLoader(new ClassPathDataSource("example4/background.jpg")))));
			
			resourceManager.add("Button", new CachedResourceLoader<Image>(new ResourceLoaderImpl<Image>(new ImageLoader(new ClassPathDataSource("example4/settings-button.png")))));
			resourceManager.add("ButtonGlow", new CachedResourceLoader<Image>(new ResourceLoaderImpl<Image>(new ImageLoader(new ClassPathDataSource("example4/settings-button-glow.png")))));

			buttonImageResource = resourceManager.get("Button");
			buttonGlowImageResource = resourceManager.get("ButtonGlow");

			final InterpolatorFunction linearInterpolationFunction = InterpolatorFunctionFactory.linear();

			buttons.add(new Button() {
				{
					text = "Play";
					position = new AutoUpdateableTransition<Point2D>(new Point2D.Float(320, 625), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
					colorTransition = new AutoUpdateableTransition<Color>(new Color(1f, 1f, 1f, 1f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					glowColorTransition = new AutoUpdateableTransition<Color>(new Color(1f, 0f, 0f, 0f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					position.set(new Point2D.Float(320,125), 1500);
					
					size = new AutoUpdateableTransition<Point2D>(new Point2D.Float(1f, 1f), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
				}
			});

			buttons.add(new Button() {
				{
					text = "Settings";
					position = new AutoUpdateableTransition<Point2D>(new Point2D.Float(320, 725), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
					colorTransition = new AutoUpdateableTransition<Color>(new Color(1f, 1f, 1f, 1f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					glowColorTransition = new AutoUpdateableTransition<Color>(new Color(0f, 1f, 0f, 0f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					position.set(new Point2D.Float(320,225), 1500);
					
					size = new AutoUpdateableTransition<Point2D>(new Point2D.Float(1f, 1f), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
				}
			});

			buttons.add(new Button() {
				{
					text = "Exit";
					position = new AutoUpdateableTransition<Point2D>(new Point2D.Float(320, 825), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
					colorTransition = new AutoUpdateableTransition<Color>(new Color(1f, 1f, 1f, 1f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					glowColorTransition = new AutoUpdateableTransition<Color>(new Color(0f, 0f, 1f, 0f), new ColorInterpolator(linearInterpolationFunction), 0.001f);
					position.set(new Point2D.Float(320,325), 1500);
					
					size = new AutoUpdateableTransition<Point2D>(new Point2D.Float(1f, 1f), new Point2DInterpolator(linearInterpolationFunction), 0.001f);
				}
			});
			
		}

		@Override
		public void render(Graphics2D graphics) {
//			graphics.setBackground(Color.black);
//			graphics.clearRect(0, 0, 800, 600);
			currentGraphicsProvider.setGraphics(graphics);

			Resource<BufferedImage> backgroundResource = resourceManager.get("Background");
			java2dRenderer.render(new Java2dImageRenderObject(0, backgroundResource.get(), 320, 240, 1, 1, 0f));
			
			graphics.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
			graphics.fillRect(0, 0, 640, 480);
			
			// render the image using the color of the transition

			for (Button button : buttons) {
				Point2D position = button.position.get();
				
				Point2D size = button.size.get();
				
				float x = (float) position.getX();
				float y = (float) position.getY();

				float sx = (float) size.getX();
				float sy = (float) size.getY();

				Color color = button.colorTransition.get();
				
				java2dRenderer.render(new Java2dImageRenderObject(1, buttonImageResource.get(), x, y, sx, sy, 0f, color));
				java2dRenderer.render(new Java2dImageRenderObject(1, buttonGlowImageResource.get(), x, y, sx, sy, 0f, button.glowColorTransition.get()));
				
//				Font previousFont = graphics.getFont();
//				
//				graphics.setFont(previousFont.deriveFont(16f));
//				graphics.setColor(button.glowColorTransition.get());
//				graphics.drawString(button.text, x - button.text.length() * 3, y);
//				
//				graphics.setFont(previousFont);
			}
			
		}

		@Override
		public void update(int delta) {
			
			BufferedImage image = buttonImageResource.get();

			Point mousePosition = mouseInput.getPosition();

			int height = image.getHeight();
			int width = image.getWidth();
			
			for (Button button : buttons) {
				
				Point2D position = button.position.get();
				
				int x = (int) position.getX();
				int y = (int) position.getY();

				if (new Rectangle(x - width / 2, y - height / 2, width, height).contains(mousePosition.x, mousePosition.y)) {
					if (!button.mouseInside) {
						button.mouseInside = true;
						// when the mouse is over the image, we set the color to white
						button.colorTransition.set(new Color(1f, 1f, 1f, 1f), 200);
						button.glowColorTransition.set(new Color(1f, 0f, 0f, 1f), 300);
						button.size.set(new Point2D.Float(1.05f, 1.05f), 100);
					}
				} else {
					if (button.mouseInside) {
						button.mouseInside = false;
						// when the mouse left the image, we set again the color to the previous color.
						button.colorTransition.set(new Color(1f, 1f, 1f, 1f), 200);
						button.glowColorTransition.set(new Color(1f, 0f, 0f, 0f), 700);
						button.size.set(new Point2D.Float(1f, 1f), 100);
					}
				}
				
			}


		}

	}

}
