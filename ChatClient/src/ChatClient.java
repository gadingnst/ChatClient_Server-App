import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends JFrame {
    JLabel lblPesan = new JLabel("Kirim Pesan");
    TextArea taPesan = new TextArea(4,50);
    JLabel lblBlasan = new JLabel("Dari Teman");
    TextArea taBlasan = new TextArea(4,50);
    JButton btnSend = new JButton("Send");
    JButton btnOpen = new JButton("Open Connection");
    JButton btnClose = new JButton("Close Connection");
    Socket con = null;
    
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    String Blasan = null;
    String inputIPServer;
    
    public void openConnection() {
        try{
            inputIPServer=JOptionPane.showInputDialog("Inputkan IP Sever");
            con = new Socket(InetAddress.getByName(inputIPServer), 2000);
            toServer = new ObjectOutputStream(con.getOutputStream());
            }
        catch(EOFException ex) {
            ;
        }
        catch(IOException io) {
            System.out.println("IO Exception");
            io.printStackTrace();
        }
    }
    public void sendData() {
        try{
            toServer.writeObject(taPesan.getText());
            taPesan.setText("");
            taPesan.requestFocus();
        }
        catch(EOFException ex) {
            ;
        }
        catch(IOException io) {
            System.out.println("IO Exception");
            io.printStackTrace();
        }
    }
    public void getData() {
        try{
            fromServer = new ObjectInputStream(con.getInputStream());
            Blasan = (String) fromServer.readObject();
            taBlasan.setText(Blasan);
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error");
        }
        catch(EOFException ex) {
            ;
        }
        catch(IOException io){
            System.out.println("IO Exception");
            io.printStackTrace();
    }
}
    public void closeConnection() {
        try{
            toServer.writeObject("BYE");
            con.close();
            con = null;
            }
        catch(EOFException ex) {
            ;
        }
        catch(IOException io) {
            System.out.println("IO Exception");
            io.printStackTrace();
    }
    }
    public ChatClient() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(lblPesan);
        Box box = Box.createVerticalBox();
        Box photoBox = Box.createVerticalBox();

        Label lNama = new Label("Nama mu");
        Label lKelas = new Label("Kelas mu");

        JLabel labelPhoto = new JLabel();
        ImageIcon img = new ImageIcon("images/image1.png");
        labelPhoto.setIcon(img);
    
        photoBox.add(labelPhoto);

        box.add(lNama);
        box.add(lKelas);

        c.add(taPesan);
        c.add(lblBlasan);
        c.add(taBlasan);
        c.add(btnOpen);

        c.add(btnSend);
        c.add(btnClose);

        c.add(box);
        c.add(photoBox);
        
        btnOpen.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent evt) {
                      openConnection();
                  }
        });
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendData();
                getData();
            }
        });
        btnClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                closeConnection();
            }
        });
        setSize(500, 400);
        setMinimumSize(new Dimension(500, 400));
        setMaximumSize(new Dimension(500, 400));
        setResizable(false);
    }
    public  static void main(String[] args) {
        ChatClient klien = new ChatClient();
        klien.setTitle("Chatting - Client");
        klien.setLocation(300, 300);
        klien.setSize(500, 200);
        klien.setVisible(true);
        klien.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev){
                System.exit(0);
            }
        });
    }
}