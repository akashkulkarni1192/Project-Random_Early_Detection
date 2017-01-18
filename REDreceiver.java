import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


class REDreceiver {
	ArrayList<JLabel> frameLabel = new ArrayList<JLabel>();
	JPanel packetPanel,processPanel,discardPanel,southPanel,disPanel,proPanel;
	JFrame receiverFrame;
	ServerSocket server ;
	Socket client;
	JLabel test,processLabel,discardLabel,disLabel,proLabel;
	public static void main(String args[]){
		REDreceiver REDr = new REDreceiver();
		REDr.setGUI();
		REDr.setNetwork();
	}
	public void setGUI(){
		receiverFrame = new JFrame("RECEIVER NODE");
		packetPanel = new JPanel();
		
		receiverFrame.getContentPane().add(BorderLayout.CENTER,packetPanel);
		test = new JLabel("Testtin...");
		receiverFrame.getContentPane().add(BorderLayout.SOUTH,test);
		
		southPanel = new JPanel();
		
		proPanel = new JPanel();
		proPanel.setLayout(new BoxLayout(proPanel,BoxLayout.Y_AXIS));
		
		proLabel = new JLabel("Processed Packet",JLabel.CENTER);
		proPanel.add(proLabel);
		
		processPanel = new JPanel();
		processLabel = new JLabel("ProcessingPacket",JLabel.CENTER);
		processLabel.setPreferredSize(new Dimension(80,80));
		processLabel.setForeground(Color.WHITE);
		processLabel.setBackground(Color.darkGray);
		Border pB = BorderFactory.createLineBorder(Color.BLUE, 3);
		processLabel.setBorder(pB);
		processLabel.setOpaque(true);
		//processLabel.setText("Not Processed yet");
		processPanel.add(processLabel);
		proPanel.add(processPanel);
	
		disPanel = new JPanel();
		disPanel.setLayout(new BoxLayout(disPanel,BoxLayout.Y_AXIS));
		
		disLabel = new JLabel("Discarded Packet",JLabel.CENTER);
		disPanel.add(disLabel);
		discardPanel = new JPanel();
		discardLabel = new JLabel("DiscardedPacket",JLabel.CENTER);
		discardLabel.setPreferredSize(new Dimension(80,80));
		discardLabel.setForeground(Color.white);
		discardLabel.setBackground(Color.black);
		Border dB = BorderFactory.createLineBorder(Color.BLUE, 3);
		discardLabel.setBorder(dB);
		discardLabel.setOpaque(true);
		//discardLabel.setText("Not Processed yet");
		discardPanel.add(discardLabel);
		disPanel.add(discardPanel);
		//southPanel.add(processPanel);
		southPanel.add(proPanel);
		//southPanel.add(discardPanel);
		southPanel.add(disPanel);
		receiverFrame.getContentPane().add(BorderLayout.SOUTH,southPanel);
		
		receiverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		receiverFrame.setSize(675,250);
		receiverFrame.setLocation(new Point(676,0));
		receiverFrame.setVisible(true);
	}
	public void setNetwork(){
		Thread processThread = new Thread(new Runnable(){
			public void run(){
				
				while(true){
					int max=-1,pos=-1;
					for(JLabel L:frameLabel){
						if(max<Integer.valueOf(L.getText())){
							max=Integer.valueOf(L.getText());
							pos = frameLabel.indexOf(L);
						}
					}
					
					if(frameLabel.size()>0){
						processLabel.setText(frameLabel.get(pos).getText());
						/*processLabel.setForeground(Color.BLUE);
						processLabel.setBackground(Color.GREEN);
						processLabel.setOpaque(true);
						processLabel.setPreferredSize(new Dimension(100,100));*/
						//processPanel.add(processLabel);
						//southPanel.add(processPanel);
						//receiverFrame.getContentPane().add(BorderLayout.SOUTH,southPanel);
						System.out.println(pos+" removed");
						//packetPanel.getComponent(pos).setPreferredSize(new Dimension(100,100));
						
						//frameLabel.get(pos).setPreferredSize(new Dimension(80,80));
						JPanel panel = new JPanel();
						panel.add(frameLabel.get(pos));
						
						for(JLabel L:frameLabel){
							packetPanel.remove(frameLabel.get(frameLabel.indexOf(L)));
							packetPanel.revalidate();
							packetPanel.repaint();
						}
						for(int i=0;i<pos;i++){
							packetPanel.add(frameLabel.get(i));
						}
						frameLabel.get(pos).setPreferredSize(new Dimension(60,60));
						frameLabel.get(pos).setBackground(Color.gray);
						packetPanel.add(frameLabel.get(pos));
						if(pos<frameLabel.size()){
							for(int i=pos;i<frameLabel.size();i++){
								packetPanel.add(frameLabel.get(i));
							}
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						packetPanel.remove(frameLabel.get(pos));
					
						frameLabel.remove(pos);
						packetPanel.revalidate();
						packetPanel.repaint();
						receiverFrame.setVisible(true);
					}
					System.out.println("Thread Start");
					System.out.println("pos :"+pos+"\tmax :"+max);
					/*try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
				}
			}
			
		});
		processThread.start();
		try {
			server = new ServerSocket(6000);
			while(true){
				client = server.accept();
				BufferedReader clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				//clientReader.read();
				String msg="";
				try {
					while((msg=clientReader.readLine())!=null){
						if(frameLabel.size()>=10){
							int min=99,pos=-1;
							for(JLabel L:frameLabel){
								if(min>Integer.valueOf(L.getText())){
									min=Integer.valueOf(L.getText());
									pos = frameLabel.indexOf(L);
								}
							}
							discardLabel.setText(frameLabel.get(pos).getText());
							packetPanel.remove(frameLabel.get(pos));
							frameLabel.remove(pos);
							discardPanel.revalidate();
							discardPanel.repaint();
							
							try {
								Thread.sleep(2500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						System.out.println("here msg:"+msg);
						JLabel packetLabel = new JLabel(msg,JLabel.CENTER);
						
						packetLabel.setForeground(Color.GREEN);
						packetLabel.setBackground(Color.BLUE);
						Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
						packetLabel.setBorder(border);
						packetLabel.setOpaque(true);
						packetLabel.setPreferredSize(new Dimension(40,40));
						int counter=0;
					
						for (JLabel L:frameLabel){
							
							if(L.getText().equals(packetLabel.getText()))
								break;
							counter++;
						}
						//System.out.println(counter+ " &"+ frameLabel.size());
						test.setText(counter+ " &"+ frameLabel.size());
						if(counter==frameLabel.size()){
							frameLabel.add(packetLabel);
							packetPanel.add(frameLabel.get(frameLabel.size()-1));
							receiverFrame.getContentPane().add(BorderLayout.CENTER,packetPanel);
							receiverFrame.setVisible(true);
						}
					}
					//System.out.println("here");
				} catch (IOException e) {
					e.printStackTrace();
				}	
				
				clientReader.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
