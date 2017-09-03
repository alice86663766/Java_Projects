
/**
 * The PlasticCup class, used to represent a plastic cup container.
 * @author Chen Hung-Yu
 */
public class PlasticCup implements Container {
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		int vol = 300;
		return vol;
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Plastic cup";
	}
}
