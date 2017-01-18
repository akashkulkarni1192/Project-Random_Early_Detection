import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


class RED implements ActionListener {
	JFrame senderFrame;
	JPanel packetPanel,bigPanel,southPanel,sentPanel;
	ArrayList<JLabel> frameLabel = new ArrayList<JLabel>();
	JLabel  bigFrameLabel;
	JButton sendButton;
	Socket server;

	public static void main(String args[]){
		RED red = new RED();
		red.setGUI();
		red.setNetwork();
	}
	public void setGUI(){
		senderFrame = new JFrame("SENDER NODE");
		
		southPanel = new JPanel();
		sendButton =  new JButton("Send Frame");
		sendButton.addActionListener(this);
		southPanel.add(sendButton);
		//senderFrame.getContentPane().add(BorderLayout.SOUTH,sendButton);
		
		
		sentPanel = new JPanel();
		sentPanel.setLayout(new BoxLayout(sentPanel,BoxLayout.Y_AXIS));
		
		bigPanel = new JPanel();
		bigPanel.setPreferredSize(new Dimension(120,120));
		//bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
		JLabel frameSent = new JLabel("Packet Sent",JLabel.CENTER);
		//bigPanel.add(frameSent);
		sentPanel.add(frameSent);
		
		bigFrameLabel = new JLabel("Nothing...",JLabel.CENTER);
		bigFrameLabel.setPreferredSize(new Dimension(100,100));
		bigFrameLabel.setForeground(Color.BLUE);
		bigFrameLabel.setBackground(Color.orange);
		Border B = BorderFactory.createLineBorder(Color.BLUE, 3);
		bigFrameLabel.setBorder(B);
		bigFrameLabel.setFont(new Font("Serif",Font.PLAIN,14));
		bigFrameLabel.setOpaque(true);
		bigPanel.add(bigFrameLabel);
		
		sentPanel.add(bigPanel);
		
		//southPanel.add(bigPanel);
		southPanel.add(sentPanel);
		senderFrame.getContentPane().add(BorderLayout.SOUTH,southPanel);
		
		
		
		senderFrame.setSize(675,250);
		senderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		senderFrame.setVisible(true);
	}
	public void setNetwork(){
		try {
			server = new Socket("127.0.0.1",6000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packetPanel = new JPanel();
		Thread inputThread = new Thread(new Runnable(){
			
			
			//frameLabel = new JLabel[5];
			/*Average Q length taken 5 */
			JLabel packet ; 
			public void run(){
				while(true){
					if(frameLabel.size()>1){
						System.out.println("BigPanel removed");
						packetPanel.remove(bigFrameLabel);
						packetPanel.revalidate();
						packetPanel.repaint();
						senderFrame.setVisible(true);
					}
					if(frameLabel.size()>13){
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					System.out.println("Bigger Loop");
					packet = new JLabel(String.valueOf(-1),JLabel.CENTER);
					packet.setPreferredSize(new Dimension(40,40));
					packet.setForeground(Color.red);
					packet.setBackground(Color.white);
					Border border = BorderFactory.createLineBorder(Color.RED, 3);
					packet.setBorder(border);
					packet.setOpaque(true);
			
					Random rand = new Random();
					int r,counter=0;
					while(true){
						System.out.println("Smaller Loop");
						counter=0;;
						r = rand.nextInt(100);
						for(JLabel L:frameLabel){
							if(r == Integer.valueOf(L.getText()))
								break;
							counter++;
						}
						System.out.println(counter + " & "+frameLabel.size());
						if(counter==frameLabel.size())
							break;
					}
					packet.setText(String.valueOf(r));
					frameLabel.add(packet);
					packetPanel.add(frameLabel.get(frameLabel.size()-1));
					senderFrame.getContentPane().add(BorderLayout.CENTER,packetPanel);
				
					
					/*frameLabel[i] = new JLabel();
					frameLabel[i].setForeground(Color.red);
					frameLabel[i].setBackground(Color.CYAN);
					frameLabel[i].setOpaque(true);*/
					
					
					senderFrame.setVisible(true);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
		});
		inputThread.start();
		
	}
	public void actionPerformed(ActionEvent ev) {
		if(frameLabel.size()<1){
			bigFrameLabel.setText("No Frame");
			return ;
		}
		bigFrameLabel.setText(frameLabel.get(frameLabel.size()-1).getText());
		PrintWriter  clientWriter;
		try {
			clientWriter = new PrintWriter(server.getOutputStream());
			clientWriter.println(frameLabel.get(frameLabel.size()-1).getText());
			clientWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//JLabel L = new JLabel();
		JLabel L = frameLabel.get(frameLabel.size()-1);
		//frameLabel.get(frameLabel.size()-1).setPreferredSize(new Dimension(60,60));
		L.setPreferredSize(new Dimension(60,60));
		senderFrame.setVisible(true);
		packetPanel.remove(L);
		//packetPanel.remove(frameLabel.size()-1);
		//frameLabel.remove(frameLabel.size()-1);
		frameLabel.remove(L);
		packetPanel.revalidate();
		packetPanel.repaint();
		senderFrame.setVisible(true);
		/*Random rand = new Random();
		int r,counter=0;
		while(true){
			counter=0;;
			r = rand.nextInt(10);
			for(int j=0;j<10;j++){
				if(r == Integer.valueOf(frameLabel[j].getText()))
					break;
				counter++;
			}
			if(counter==5)
				break;
		}
		frameLabel[0].setText(String.valueOf(r));
		
		packetPanel.add(frameLabel[0]);
		senderFrame.getContentPane().add(BorderLayout.CENTER,packetPanel);*/
	}
}

