import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Message class, used to represent the message sent between peers.
 * @author Chen, Hung-Yu
 *
 */
public class Message implements Serializable{
	/**
	 * Sender's name.
	 */
	public String sender = "Hi";
	/**
	 * Receiver's name.
	 */
	public String receiver = null;
	/**
	 * Command to be done.
	 */
	public String command = null;
	/**
	 * Name of the image concerned.
	 */
	public String dataImageName = null;
	/**
	 * Block number of the data image.
	 */
	public int dataBlockNo = -1;
	/**
	 * Content of the message.
	 */
	public String dataContent = null;
	/**
	 * List of maintained active peers.
	 */
	public ArrayList<PeerInfo> peerList = null;
	/**
	 * If an image is requested, attach the image here.
	 */
	public SendableImage img = null;
	/**
	 * Normal constructor of Message class.
	 * @param sender Sender's name.
	 * @param receiver Receiver's name.
	 * @param command Command to be done.
	 * @param imageName Name of the image concerned.
	 * @param blockNo Block number of the data image.
	 * @param content Content of the message.
	 * @param list List of maintained active peers.
	 */
	public Message(String sender, String receiver, String command, String imageName, int blockNo, String content, ArrayList<PeerInfo> list) {
		this.sender = sender;
		this.receiver = receiver;
		this.command = command;
		this.dataImageName = imageName;
		this.dataBlockNo = blockNo;
		this.dataContent = content;
		this.peerList = list;
	}
	
	/**
	 * Constructor of Message class for active check.
	 * @param Sender's name.
	 * @param receiver Receiver's name.
	 * @param command Command to be done.
	 */
	public Message(String sender, String receiver, String command) {
		this.sender = sender;
		this.receiver = receiver;
		this.command = command;
	}
	/**
	 * Constructor of Message class for port information exchange.
	 * @param sender Sender's name.
	 * @param receiver Receiver's name.
	 * @param command Command to be done.
	 * @param content Content of the message.
	 * @param port The port that the sender is listening to.
	 */
	public Message(String sender, String receiver, String command, String content, int port) {
		this.sender = sender;
		this.receiver = receiver;
		this.command = command;
		this.dataContent = content;
		this.dataBlockNo = port;
	}
	/**
	 * Constructor of Message class for port image exchange.
	 * @param sender Sender's name.
	 * @param receiver Receiver's name.
	 * @param command Command to be done.
	 * @param imageName Name of the image to be sent.
	 * @param content The image to be sent.
	 * @param list List of maintained active peers.
	 */
	public Message(String sender, String receiver, String command, String imageName, SendableImage content, ArrayList<PeerInfo> list) {
		this.sender = sender;
		this.receiver = receiver;
		this.command = command;
		this.dataImageName = imageName;
		if (content != null) {
			this.dataBlockNo = content.getBlockNo();
		}
		this.img = content;
		this.peerList = list;
	}
}
