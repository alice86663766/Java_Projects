import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The ImageServer class, used to represent an image server.
 * @author Chen, Hung-Yu
 *
 */
public class ImageServer extends ImagePeer{
	//public static final int serverId = 0;
	/**
	 * The port listening to login requests.
	 */
	public static final int loginPort = 8000;
	/**
	 * The port listening to image sharing requests.
	 */
	public static final int imgPort = 5000;
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private int maxPeers = 5;
	private BufferedImage img;
	private BufferedImage[] imgs = new BufferedImage[400];
	private String imgName = null;
	private JFrame frame = new JFrame("Image Server"); // title of the frame
	private JPanel canvas = new DisplayImage();
	Map<String, String> accounts = new HashMap<String, String>();
	Map<String, Boolean> activeAccounts = new HashMap<String, Boolean>();
	Map<String, Calendar> lastLogin = new HashMap<String, Calendar>();
	private ArrayList<PeerInfo> peerList = new ArrayList<PeerInfo>();

	/**
	 * @param args One argument is allowed, indicating the maximum number of peers that server can connect to.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int temp = 5;
		if (args.length > 0) {
			temp = Integer.parseInt(args[0].substring(1));
		}
		ImageServer server = new ImageServer(temp);
		server.startProgram();
		//System.out.println(maxPeers);
	}
	/**
	 * Constructor of the ImageServer.
	 * @param maxNo Maximum number of peers that server can connect to.
	 */
	public ImageServer(int maxNo) {
		maxPeers = maxNo;
	}
	/* (non-Javadoc)
	 * @see ImagePeer#startProgram()
	 */
	public void startProgram() {
		JFileChooser file = new JFileChooser();
		JButton button = new JButton("Load another image");
		file.showOpenDialog(null);
		if (file.getSelectedFile() != null) {
			try {
				img = ImageIO.read(file.getSelectedFile());
				if (img != null) {
					imgName = file.getSelectedFile().getName();
					System.out.println("filename:" + imgName);
					img = resize(img, 600, 600);
					frame.setSize(640, 800);
					frame.add(BorderLayout.CENTER, canvas);
					frame.add(BorderLayout.SOUTH, button);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					cropImage();
					setup();
					Thread startThread = new Thread(new StartConnection());
					startThread.start();
				}
				else {
					System.exit(0);
				}
				
			} catch(IOException e1) {
				e1.printStackTrace();
			}
			button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					JFileChooser newFile = new JFileChooser();
					newFile.showOpenDialog(null);
					if (newFile.getSelectedFile() != null) {
						try {
							img = ImageIO.read(newFile.getSelectedFile());
							if (img != null) {
								imgName = newFile.getSelectedFile().getName();
								img = resize(img, 600, 600);
								System.out.println("filename:" + imgName);
								cropImage();
								Thread broadcastThread = new Thread(new BroadcastChange());
								broadcastThread.start();
								canvas.repaint();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
		}
	}
	/**
	 * Resize the input image.
	 * @param img The image to be resized.
	 * @param newW New width of the resized image.
	 * @param newH New height of the resized image.
	 * @return The resized image.
	 */
	public BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	} 
	/* (non-Javadoc)
	 * @see ImagePeer#setup()
	 */
	public void setup() {
		accounts.put("admin", "admin");
		accounts.put("alice", "alice");
		accounts.put("ben", "ben");
		accounts.put("cat", "cat");
		accounts.put("dog", "dog");
		activeAccounts.put("admin", false);
		activeAccounts.put("alice", false);
		activeAccounts.put("ben", false);
		activeAccounts.put("cat", false);
		activeAccounts.put("dog", false);
		lastLogin.put("admin", null);
		lastLogin.put("alice", null);
		lastLogin.put("ben", null);
		lastLogin.put("cat", null);
		lastLogin.put("dog", null);
		PeerInfo peer = new PeerInfo("Teacher", imgPort, null);
		peerList.add(peer);
		Runnable activeCheck = new ActiveCheckHandler();
		Runnable imgSharing = new ImgSharingHandler();
		Thread acThread = new Thread(activeCheck);
		Thread isThread = new Thread(imgSharing);
		acThread.start();
		isThread.start();
	}
	/**
	 * Crop image to 400 blocks.
	 */
	public void cropImage() {
		for (int i = 0; i < 400; i++) {
			int r = i/20;
			int c = i%20;
			imgs[i] = img.getSubimage(30*r, 30*c, 30, 30);
		}
	}
	/**
	 * The StartConnection class, a thread handling incoming connection requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class StartConnection implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			ServerSocket ss;
			try {
				ss = new ServerSocket(loginPort);
				while (true) {
					Socket s = ss.accept();
					Runnable client = new ClientHandler(s);
					Thread clientThread = new Thread(client);
					clientThread.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The ClientHandler class, a thread handling incoming connection requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ClientHandler implements Runnable {
		private Socket s = null;
		private Message m = null;
		/**
		 * Constructor of ClientHandler class.
		 * @param s The socket that maintains connection.
		 */
		public ClientHandler(Socket s) {
			this.s = s;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				ObjectInputStream input = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
				try {
					m = (Message)input.readObject();
					if (m.command.equals("LOGIN")) {
						Thread loginThread = new Thread(new LoginHandler(s, input, output, m));
						loginThread.start();
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * The LoginHandler class, a thread handling login requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class LoginHandler implements Runnable {
		private Socket s = null;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;
		Message m = null;
		/**
		 * Constructor of LoginHandler class.
		 * @param s The socket that maintains connection.
		 * @param input Input stream of the socket to be read from.
		 * @param output Output stream of the socket to be written to.
		 * @param m Message read from the socket.
		 */
		public LoginHandler(Socket s, ObjectInputStream input, ObjectOutputStream output, Message m) {
			this.s = s;
			this.input = input;
			this.output = output;
			this.m = m;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				String command = "LOGIN_FAILED";
				if (accounts.get(m.sender) != null && accounts.get(m.sender).equals(m.dataContent) && peerList.size()<=maxPeers && activeAccounts.get(m.sender) != null && !activeAccounts.get(m.sender)) {
					PeerInfo peer = new PeerInfo(m.sender, m.dataBlockNo, s.getInetAddress().getHostAddress()); //block no. indicates the port that the peer is listening to
					peerList.add(peer);
					activeAccounts.put(m.sender, true);
					System.out.println(peer.getAddress());
					command = "LOGIN_OK";
					Calendar cal = Calendar.getInstance();
					lastLogin.put(m.sender, cal);
				}
				Message outMsg = new Message("Teacher", m.sender, command, imgName, 400, null, peerList);
				output.writeObject(outMsg);
				output.flush();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/* (non-Javadoc)
	 * @see ImagePeer#removePeer(PeerInfo)
	 */
	public synchronized void removePeer(PeerInfo peer) {
		boolean found = false;
		int i = 0;
		while (!found && i < peerList.size()) {
			PeerInfo p = peerList.get(i);
			if (p.getUserName().equals(peer.getUserName())) {
				found = true;
				peerList.remove(i);
				activeAccounts.put(p.getUserName(), false);
			}
			i++;
		}
	}
	/**
	 * The BroadcastChange class, a thread sending image change requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class BroadcastChange implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			for (int i = 1; i < peerList.size(); i++) {
				PeerInfo peer = peerList.get(i);
				try (Socket s = new Socket(peer.getAddress(), peer.getPort())) {
					ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
					Message outMsg = new Message("Teacher", peer.getUserName(), "IMAGE_UPDATE", imgName, 400, null, null);
					output.writeObject(outMsg);
					output.flush();
					ObjectInputStream input = new ObjectInputStream(s.getInputStream());
					Message m = (Message)input.readObject();
					if (m.command.equals("IMAGE_UPDATE_OK")) {
						System.out.println(m.sender + " image updated");
						s.close();
					}
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					removePeer(peer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("io");
					removePeer(peer);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * The ActiveCheckHandler class, a thread conducting active check regularly.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ActiveCheckHandler implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (true) {
		        Calendar now = Calendar.getInstance();
				System.out.println("--------------------Active check at " + sdf.format(now.getTime()) + "--------------------");
				System.out.println("Number of active peers now (including me): " + peerList.size());
				System.out.println();
				for (int i = 1; i < peerList.size(); i++) {
					PeerInfo peer = peerList.get(i);
					try (Socket s = new Socket(peer.getAddress(), peer.getPort())) {
						ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
						Message outMsg = new Message("Teacher", peer.getUserName(), "ACTIVE_CHECK");
						output.writeObject(outMsg);
						output.flush();
						ObjectInputStream input = new ObjectInputStream(s.getInputStream());
						Message m = (Message)input.readObject();
						if (m.command.equals("ACTIVE_CHECK_OK")) {
							System.out.println(m.sender + " is active");
							Calendar cal = lastLogin.get(m.sender);
							System.out.println("Last login: " + sdf.format(cal.getTime()));
							System.out.println("Current file name: " + m.dataImageName);
							System.out.println("Number of blocks done: " + m.dataBlockNo);
							System.out.println();
						}
						
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						removePeer(peer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("io");
						removePeer(peer);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * The ImgSharingHandler class, a thread handling get image requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ImgSharingHandler implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			ServerSocket ss;
			try {
				ss = new ServerSocket(imgPort);
				while (true) {
					Socket s = ss.accept();
					ObjectInputStream input = new ObjectInputStream(s.getInputStream());
					try {
						Message m = (Message)input.readObject();
						if (m.command.equals("GET_IMG_BLOCK")) {
							System.out.println("Sending image to " + m.sender);
							Thread imgSharingThread = new Thread(new ImgSharing(s, m));
							imgSharingThread.start();
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * The ImgSharing class, a thread that sends image.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ImgSharing implements Runnable {
		private Socket s = null;
		private Message m = null;
		/**
		 * The constructor of ImgSharing class.
		 * @param s The socket that maintains connection.
		 * @param m The message received from connection.
		 */
		public ImgSharing(Socket s, Message m) {
			this.s = s;
			this.m = m;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			ObjectOutputStream output;
			try {
				output = new ObjectOutputStream(s.getOutputStream());
				int blockNo = m.dataBlockNo;
				BufferedImage temp = imgs[blockNo];
				SendableImage outImg = new SendableImage(temp, blockNo);
				Message outMsg = new Message("Teacher", m.sender, "SEND_IMG_BLOCK", imgName, outImg, peerList);
				output.writeObject(outMsg);
				output.flush();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * The DisplayImage class, used as a canvas to display image.
	 * @author Chen, Hung-Yu
	 *
	 */
	class DisplayImage extends JPanel {
	    /* (non-Javadoc)
	     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	     */
	    @Override public void paintComponent(Graphics g) { 
	    	g.drawImage(img, 20, 20, 600, 600, this);
	    }
	}
}
