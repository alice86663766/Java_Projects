import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * The ImagePeer class, used to represent an ImagePeer.
 * @author Chen, Hung-Yu
 *
 */
public class ImagePeer {
	/**
	 * User name of the peer.
	 */
	public String username = null;
	//public boolean free = true;
	private String password = null;
	private String host = "127.0.0.1";
	private JFrame frame = new JFrame("Image Peer"); // title of the frame
	private JPanel canvas = new DisplayImage();
	private JFrame loginFrame = new JFrame("Login");
	private JPanel loginPanel = new JPanel();
	private JFrame loginFailed = new JFrame("Login Failed");
	private JPanel buttonPanel = new JPanel();
	private BufferedImage[] imgs = new BufferedImage[400];
	private String imgName = null;
	private ArrayList<PeerInfo> peerList = new ArrayList<PeerInfo>();
	private String[] imgSource = new String[400];
	private String[] imgNames = new String[400];
	//private boolean imgReceived
	private int stage = -1;
	private int serverPort = -1;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImagePeer peer = new ImagePeer();
		peer.setup();
		peer.startProgram();
	}
	/**
	 * Set up before program starts.
	 */
	public void setup() {
		for (int i = 0; i < 400; i++) {
			imgs[i] = null;
			imgSource[i] = null;
			imgNames[i] = null;
		}
	}
	/**
	 * Entry point of the program.
	 */
	public void startProgram() {
		try {
			Socket s = new Socket(host, ImageServer.loginPort);
			System.out.println("Network Established");
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            login(input, output);
			//Runnable server = new ServerHandler(s);
			//Thread serverThread = new Thread(server);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Server is not on.");
			System.exit(0);
		}
	}
	/**
	 * Display login GUI and send login request to server.
	 * @param input Input stream of the socket to be read from.
	 * @param output Output stream of the socket to be written to.
	 */ 
	public void login(ObjectInputStream input, ObjectOutputStream output) {
		Thread serverThread = new Thread(new startPeerServer());
		serverThread.start();
		JLabel title = new JLabel("Host IP");
		JTextField textField = new JTextField(20);
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		title.setPreferredSize(new Dimension(280, 20));
		//input.setPreferredSize(new Dimension(150, 20));
		title.setHorizontalTextPosition(JLabel.LEFT);
		loginPanel.add(title);
		loginPanel.add(textField);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		loginFrame.add(BorderLayout.CENTER, loginPanel);
		loginFrame.add(BorderLayout.SOUTH, buttonPanel);
		loginFrame.setSize(300, 180);
		loginFrame.setVisible(true);
		
		JLabel failedTitle = new JLabel("Login Failed.");
		JButton failedOk = new JButton("OK");
		failedTitle.setPreferredSize(new Dimension(280, 20));
		loginFailed.add(BorderLayout.CENTER, failedTitle);
		loginFailed.add(BorderLayout.SOUTH, failedOk);
		loginFailed.setSize(150, 90);
		failedOk.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	System.exit(0);
		    }
		});
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ok.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String text = textField.getText();
		        if (stage == -1) {
		        	host = text;
		        	title.setText("Username");
		        	stage = 0;
		        }
		        else if (stage == 0) {
		        	username = text;
		        	title.setText("Password");
		        	PeerInfo peer = new PeerInfo(username, serverPort, host);
		        	peerList.add(peer);
		        	stage = 1;
		        }
		        else if (stage == 1) {
		        	password = text;
		        	Message m = new Message (username, "Teacher", "LOGIN", password, serverPort);
		            try {
		            	output.reset();
		    			output.writeObject(m);
		    			output.flush();
		    			m = (Message)input.readObject();
		    			if (m.command.equals("LOGIN_OK")) {
		    				System.out.println("Login OK");
		    				loginFrame.setVisible(false);
		    				frame.setSize(640, 665);
							frame.add(BorderLayout.CENTER, canvas);
							frame.setTitle(username);
							frame.setVisible(true);
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							updatePeerList(m.peerList);
							imgName = m.dataImageName;
							imgSharing();
		    			}
		    			else if (m.command.equals("LOGIN_FAILED")) {
		    				System.out.println("Login failed");
		    				loginFailed.setVisible(true);
		    				loginFailed.addWindowListener(new WindowAdapter() {
		    				    @Override
		    				    public void windowClosing(WindowEvent e) {
		    				        System.exit(0);
		    				    }
		    				});
		    			}
		    			else {
		    				System.out.println("?????");
		    			}
		    		} catch (IOException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		    }
		});
		cancel.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		    }
		});
	}
	/**
	 * Update peerList based on the received peerList.
	 * @param received The peerList received from other peers.
	 */
	public synchronized void updatePeerList(ArrayList<PeerInfo> received) { //////////synchronized?
		if (peerList.size() == 0) {
			peerList = received;
			return;
		}
		for (int i = 0; i < received.size(); i++) {
			Boolean found = false;
			PeerInfo info1 = received.get(i);
			for (int j = 0; j < peerList.size(); j++) {
				PeerInfo info2 = peerList.get(j);
				if (info2.getUserName().equals(info1.getUserName())) {
					found = true;
					if (info2.getPort() == -1) {
						info2.setPort(info1.getPort());
					}
					if (info2.getAddress() == null) {
						info2.setAddress(info1.getAddress());
					}
				}
			}
			if (!found) {
				peerList.add(info1);
			}
		}
	}
	/**
	 * Remove a peer from the peerList.
	 * @param peer The peer to be removed from the list.
	 */
	public synchronized void removePeer(PeerInfo peer) {
		boolean found = false;
		int i = 0;
		while (!found && i < peerList.size()) {
			PeerInfo p = peerList.get(i);
			if (p.getUserName().equals(peer.getUserName())) {
				found = true;
				peerList.remove(i);
			}
			i++;
		}
	}
	/**
	 * Update image block according to incoming request.
	 * @param inputMsg The message containing the update info.
	 */
	public synchronized void updateImgBlock(Message inputMsg) {
		SendableImage inputImg = inputMsg.img;
		imgSource[inputMsg.dataBlockNo] = inputMsg.sender;
		imgNames[inputMsg.dataBlockNo] = inputMsg.dataImageName;
		imgs[inputImg.getBlockNo()] = inputImg.recoverImage();
		
	}
	/**
	 * When update image request is received, reset imgSource variable.
	 */
	public synchronized void updateImg() {
		for (int i = 0; i < 400; i++) {
			imgSource[i] = null;
		}
	}
	/**
	 * Calculate number of received blocks.
	 * @return Number of received blocks.
	 */
	public synchronized int totalDone() {
		int counter = 0;
		for (int i = 0; i < 400; i++) {
			if (imgSource[i] != null) {
				counter++;
			}
		}
		return counter;
	}
	/**
	 * Pick a peer to connect to.
	 * @return The peer to connect to.
	 */
	public PeerInfo pickAPeer() {
		boolean me = true;
		PeerInfo peer = null;
		while (me) {
			Random rand = new Random();
			int num = rand.nextInt(peerList.size());
			if (!peerList.get(num).getUserName().equals(username)) {
				me = false;
				peer = peerList.get(num);
			}
		}
		return peer;
	}
	/**
	 * Pick a image block number to request from peers.
	 * @return The block number of image to be requested.
	 */
	public int pickABlockNo() {
		boolean had = true;
		int blockNo = -1;
		while(had) {
			Random rand = new Random();
			int num = rand.nextInt(400);
			if (imgSource[num] == null) {
				had = false;
				blockNo = num;
			}
		}
		
		return blockNo;
	}
	/**
	 * A function that request images continuously.
	 */
	public void imgSharing() {
		while (true) {
			if (totalDone() < 400) {
				//free = false;
				PeerInfo clientInfo = pickAPeer();
				int blockNo = pickABlockNo();
				try (Socket s = new Socket(host, clientInfo.getPort())) {
					Thread receiveImgThread = new Thread(new GetImageHandler(s, blockNo, clientInfo.getUserName()));
			        receiveImgThread.start();
					Thread.sleep(20);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					removePeer(clientInfo);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//free = true;
			}
		}
			
		
	}
	/**
	 * The GetImageHandler class, used as a thread to send get_image requests.
	 * @author Chen, Hung-Yu
	 */
	public class GetImageHandler implements Runnable {
		private Socket s = null;
		private int blockNo = -1;
		private String server = null;
		/**
		 * The constructor.
		 * @param s Socket that maintains the connection.
		 * @param blockNo Block number to request.
		 * @param serverName Name of the server to be requested.
		 */
		public GetImageHandler(Socket s, int blockNo, String serverName) {
			this.s = s;
			this.blockNo = blockNo;
			this.server = serverName;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
				Message outMsg = new Message(username, server, "GET_IMG_BLOCK", imgName, blockNo, null, null);
				output.writeObject(outMsg);
				output.flush();
				ObjectInputStream input = new ObjectInputStream(s.getInputStream());
				Message inputMsg = (Message)input.readObject();
				if (inputMsg != null && inputMsg.command.equals("SEND_IMG_BLOCK") && inputMsg.dataImageName.equals(imgName)) {
					updateImgBlock(inputMsg);
					updatePeerList(inputMsg.peerList);
					System.out.println("Received image from " + inputMsg.sender);
				}
				canvas.paintImmediately(20, 20, 600, 600); 
				s.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				
			} 
		}
	}
	/**
	 * The startPeerServer class, used as a thread to receive requests from other peers.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class startPeerServer implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			ServerSocket ss = null;
			int port = ImageServer.loginPort+1;
			while (ss == null && port < 65536) {
				try {
					ss = new ServerSocket(port);
					System.out.println("Succeed in finding port!" + port);
					serverPort = port;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					port++;
				}
			}
			while (true) {
				try {
					Socket s = ss.accept();
					ObjectInputStream input = new ObjectInputStream(s.getInputStream());
					Message m = (Message)input.readObject();
					if (m.command.equals("GET_IMG_BLOCK")) {
						Thread imgSharingThread = new Thread(new ImgSharing(s, m));
						imgSharingThread.start();
					}
					else if (m.command.equals("ACTIVE_CHECK")) {
						Thread activeCheckThread = new Thread(new ReplyActiveCheck(s, m));
						activeCheckThread.start();
					}
					else if (m.command.equals("IMAGE_UPDATE")) {
						System.out.println("update image!");
						imgName = m.dataImageName;
						updateImg();
						Thread imgUpdateThread = new Thread(new ImgUpdateHandler(s, m));
						imgUpdateThread.start();
					}
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Someone disconnected");
					//e.printStackTrace();
				}
				//Runnable client = new ClientHandler(s);
				//Thread clientThread = new Thread(client);
				//clientThread.start();
			}
		}
	}
	/**
	 * The ImgSharing class, used as a thread to send requested image back.
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
				SendableImage outImg = null;
				String command = "BLOCK_NOT_AVAILABLE";
				if (!m.dataImageName.equals(imgName) || !m.dataImageName.equals(imgNames[blockNo])) {
					command = "NAME_MISMATCH";
				}
				else if (temp != null) {
					outImg = new SendableImage(temp, blockNo);
					command = "SEND_IMG_BLOCK";
				}
				Message outMsg = new Message(username, m.sender, command, imgName, outImg, peerList);
				output.writeObject(outMsg);
				output.flush();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	/**
	 * The ReplyActiveCheck class, used as a thread to handle active check requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ReplyActiveCheck implements Runnable {
		private Socket s = null;
		private Message m = null;
		/**
		 * Constructor of ReplyActiveCheck class.
		 * @param s The socket that maintains connection.
		 * @param m The message received from connection.
		 */
		public ReplyActiveCheck(Socket s, Message m) {
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
				int done = totalDone();
				Message outMsg = new Message(username, m.sender, "ACTIVE_CHECK_OK", imgName, done, null, peerList);
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
	 * The ImgUpdateHandler class, used as a thread to reply to active check requests.
	 * @author Chen, Hung-Yu
	 *
	 */
	public class ImgUpdateHandler implements Runnable {
		private Socket s = null;
		private Message m = null;
		/**
		 * Constructor of ImgUpdateHandler class.
		 * @param s The socket that maintains connection.
		 * @param m The message received from connection.
		 */
		public ImgUpdateHandler(Socket s, Message m) {
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
				Message outMsg = new Message(username, "Teacher", "IMAGE_UPDATE_OK", imgName, 400, null, null);
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
	    	for (int i = 0; i < 400; i ++) {
	    		if (imgs[i] != null) {
	    			int row = i/20;
	    			int col = i%20;
	    			g.drawImage(imgs[i], 20+30*row, 20+30*col, 30, 30, this);
	    		}
	    	}
	    	
	    }
	}
}
