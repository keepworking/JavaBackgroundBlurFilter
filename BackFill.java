import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BackFill {

	public static void main(String[] args) {
		
		BufferedImage tgImg = null;

		try {
			tgImg = ImageIO.read(new File("target.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Dimension screen = getScreenSize();

		BufferedImage bgImg = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = (Graphics2D) bgImg.getGraphics();

		// ȭ�� ä��� ����
		
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		double balanceW = (double) bgImg.getWidth() / (double) tgImg.getWidth();
		double balanceH = (double) bgImg.getHeight() / (double) tgImg.getHeight();
		graphics.drawImage(
				tgImg,
				0,
				(int) (tgImg.getHeight() * balanceW - bgImg.getHeight()) / -2,
				(int) (tgImg.getWidth() * balanceW),
				(int) (tgImg.getHeight() * balanceW),
				null
				);
		
		// ȭ�� ä��� ��

		// ���� ����

		bgImg = Filtering(bgImg, 10, 10);

		// ���� ��
		
		
		// ������� ����
		
		Kernel kernel = new Kernel(1, 1, new float[] { 1.2f });

		ConvolveOp convolveOp = new ConvolveOp(kernel);

		bgImg = convolveOp.filter(bgImg, null);
		
		//��� ���� ��
		
		// ȭ�� �߾� �̹��� ��ġ
		graphics = (Graphics2D) bgImg.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.drawImage(
				tgImg,
				(int) (bgImg.getWidth() - tgImg.getWidth() * balanceH) / 2,
				0,
				(int) (tgImg.getWidth() * balanceH),
				(int) (tgImg.getHeight() * balanceH),
				null
				);
		
		// ȭ�� �߾� �̹��� ��ġ

		try {
			ImageIO.write(bgImg, "png", new File("out.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * getScreenSize : void
	 * 
	 * ���ȭ���� ����� �������� �Լ��̴�.
	 * 
	 * ��ȯ���� ~.getWidth(), ~.getHeight() �� �Է��ϸ� ����, ������ ���� �� �ִ�.
	 */
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	

	/*
	 * Filtering : BufferedImage
	 * 
	 * ����þ� ���� �ƴϰ� �׳� �ڽ� ��
	 * 
	 */
	public static BufferedImage Filtering(BufferedImage input, int x_, int y_) {
		BufferedImage output = input;

		for (int x = 0; x < input.getWidth(); x++) {
			for (int y = 0; y < input.getHeight(); y++) {
				Color here = null;
				long r = 0;
				long g = 0;
				long b = 0;
				int count = 0;
				for (int i = 0; i < x_; i++) {
					for (int j = 0; j < y_; j++) {
						try {
							Color color = new Color(input.getRGB(x - (x_ / 2) + i, y - (y_ / 2) + j));
							// System.out.print("aa");
							r += color.getRed();
							g += color.getGreen();
							b += color.getBlue();
							count++;
						} catch (IndexOutOfBoundsException e) {
							//�����ڸ��� �� ������ �鰡�� �ʴ� �κе� ������ ������ �Ѵ� ������ �߻���
						}
					}
				}

				r /= count;
				g /= count;
				b /= count;
				here = new Color((int) r, (int) g, (int) b);
				output.setRGB(x, y, here.getRGB());
			}
		}

		return output;
	}

}