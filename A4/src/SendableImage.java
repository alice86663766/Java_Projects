import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * The SendableImage class, used to store image information that can be sent through socket.
 * @author Chen, Hung-Yu
 *
 */
public class SendableImage implements Serializable{
	private int blockNo;
	private int pixels[][] = new int[30][30];
	/**
	 * The SendableImage constructor.
	 * @param img The image to be stored.
	 * @param blockNo Block number of the image.
	 */
	public SendableImage(BufferedImage img, int blockNo) {
		this.blockNo = blockNo;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				pixels[i][j] = img.getRGB(i, j);
			}
		}
	}
	/**
	 * Recover a image based on the information stored.
	 * @return The recovered image.
	 */
	public BufferedImage recoverImage() {
		BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB); 
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				int rgb = pixels[i][j];
				image.setRGB(i, j, rgb);
			}
		}
		return image;
	}
	/**
	 * Get block number of the image.
	 * @return Block number of the image.
	 */
	public int getBlockNo() {
		return this.blockNo;
	}
}
