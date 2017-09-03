import java.io.Serializable;

/**
 * The PeerInfo class, used to store peer information.
 * @author Chen, Hung-Yu
 *
 */
public class PeerInfo implements Serializable {
	private String uName;
	private int port;
	private String address;
	/**
	 * Constructor of PeerInfo class.
	 * @param uName Peer's username.
	 * @param port The port that this peer is listening to.
	 * @param address The IP address of the peer.
	 */
	public PeerInfo(String uName, int port, String address) {
		this.uName = uName;
		this.port = port;
		this.address = address;
	}
	/**
	 * Get username of this peer.
	 * @return Username of this peer.
	 */
	public String getUserName() {
		return this.uName;
	}
	/**
	 * Get the port that this peer is listening to.
	 * @return The port that this peer is listening to.
	 */
	public int getPort() {
		return this.port;
	}
	/**
	 * Get IP address of this peer.
	 * @return IP address of this peer.
	 */
	public String getAddress() {
		return this.address;
	}
	/**
	 * Store the port that this peer is listening to.
	 * @param port The port that this peer is listening to.
	 */ 
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Store the IP address of this peer.
	 * @param addr The IP address of this peer.
	 */
	public void setAddress(String addr) {
		this.address = addr;
	}
}
