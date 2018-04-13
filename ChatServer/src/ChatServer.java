import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServer extends JFrame{
	JLabel labelPesan = new JLabel("Kirim Pesan");
	TextArea textPesan = new TextArea(4, 50);
	JLabel labelBalas = new JLabel("Dari Teman");
	TextArea textBalas = new TextArea(4, 50);
	JButton send = new JButton("Kirim");
	JButton close = new JButton("Close Connection");
	ServerSocket socketServer;
	Socket conClient;
	ObjectInputStream fromClient;
	ObjectOutputStream toClient;
	String string = null;
	Container container;
	
	public void sendData() {
		try {
			toClient = new ObjectOutputStream(conClient.getOutputStream());
			toClient.writeObject(textPesan.getText());
			System.out.println(textPesan.getText());
		}catch(EOFException ex) {
			ex.getMessage();
		}catch(NullPointerException npe) {
			JOptionPane.showMessageDialog(null, "Koneksi Belum tersambung","Pesan: ",JOptionPane.ERROR_MESSAGE);
		}catch(SocketException se) {
			JOptionPane.showMessageDialog(null, "Koneksi Putus","Pesan: ",JOptionPane.ERROR_MESSAGE);			
		}catch(IOException iox) {
			System.out.println("IO Except");
			iox.printStackTrace();
		}
	}
	public void closeConnection() {
		try {
			conClient.close();
			conClient = null;
			System.exit(0);
		}catch(EOFException ex) {
			ex.getMessage();
		}catch(IOException iox) {
			System.out.println("IO Except");
			iox.printStackTrace();
		}
	}
	public ChatServer()throws IOException {
		container = getContentPane();
		container.setLayout(new FlowLayout());
		Box box = Box.createVerticalBox();

        Label lNama = new Label("Nama mu");
        Label lKelas = new Label("Kelas mu");

        box.add(lNama);
        box.add(lKelas);

		container.add(labelPesan);
		container.add(textPesan);
		container.add(labelBalas);
		container.add(textBalas);
		container.add(send);
		container.add(close);
		container.add(box);

		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData();
			}
		});
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeConnection();
			}
		});
		setSize(550, 300);
        setMinimumSize(new Dimension(550, 300));
        setMaximumSize(new Dimension(550, 300));
        setResizable(false);
	}
	public void getConnection()throws IOException {
		socketServer = new ServerSocket(2000);
		conClient = socketServer.accept();
		JOptionPane.showMessageDialog(null, "Tersambung Dengan Client" + conClient.getInetAddress().toString(), "Pesan: ", JOptionPane.INFORMATION_MESSAGE);
		socketServer.close();
		try {
			fromClient = new ObjectInputStream(conClient.getInputStream());
			do {
				try {
					string = (String) fromClient.readObject();
					textBalas.setText(string);
				}catch(ClassNotFoundException ex) {
					ex.getMessage();
					System.out.println("Terjadi Error");
				}
			}while(!equals("bye"));
		}catch(EOFException eox) {
			eox.getMessage();
		}catch(IOException iox) {
			iox.getMessage();
			System.out.println("IO Exception");
			iox.printStackTrace();
		}finally{
			System.out.println("Closed");
			conClient.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		ChatServer chatServer = new ChatServer();
		chatServer.setTitle("Chatting Server - ");
		chatServer.setLocation(300, 300);
		chatServer.setSize(500, 250);
		chatServer.setVisible(true);
		chatServer.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
		chatServer.getConnection();
	}
}
