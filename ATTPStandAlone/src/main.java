import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class main {
	/*
	 * result contain the final result
	 * pairResult contain all the value after return calculation (x-y)/y
	 * prices contain all the buy and sell prices which positive means buy and negative means sell
	 */
	public static void main(String[] args) throws IOException {
		

		//JLabel textLabel = new JLabel("the return value is "+ numberFormat.format(result),SwingConstants.CENTER); 
		
		//Create and set up the window.
		//JFrame frame = new JFrame("Simple GUI");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(new Dimension(300, 300));
		//frame.add(textLabel);
		//frame.setVisible(true);
		
		final JFrame frame = new JFrame("Just Module");
		JOptionPane.showMessageDialog(frame, "Make sure the data file to be tested is in the same directory as the jar file",
				"Reminder", JOptionPane.WARNING_MESSAGE);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(500, 500));
		frame.setLocationRelativeTo(null);
		
		final JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(new JLabel("Enter output file name:"));
        final JTextField fileName = new JTextField(40);
        panel.add(fileName);
        
        JButton runButton = new JButton("Run Module");
        panel.add(runButton);

        runButton.addActionListener(new ActionListener() {

        @Override

        public void actionPerformed(ActionEvent arg0) {

        	String data = (fileName.getText());
        	//ReadCVS.run(data,sma,th);
			String output = doCalculation(data);
			JPanel pan2 = new JPanel();
			JLabel textLabel = new JLabel("The return value is " + output);
			textLabel.setHorizontalAlignment(JLabel.CENTER);

			JButton done = new JButton("Try with different output file?");
			JButton close = new JButton("Close Program");
			
			pan2.add(textLabel, JLabel.CENTER);
			pan2.add(done);
			
			pan2.add(done);
			pan2.add(close);
			frame.setContentPane(pan2);
			frame.setVisible(true);
			
			done.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(frame, "Make sure you the output file is saved in the same directory",
							"Reminder", JOptionPane.WARNING_MESSAGE);
					frame.setContentPane(panel);
				}
			});
			
			close.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

        } 

        });
		
	
		frame.setVisible(true);
		
		
	}
	
	
	public static String doCalculation(String data) {
		BigDecimal temp;
		//String data = args[0];
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<BigDecimal> prices = new ArrayList<BigDecimal>();
		List<BigDecimal> pairResult = new ArrayList<BigDecimal>();
		try {

			br = new BufferedReader(new FileReader(data));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] marketData = line.split(cvsSplitBy);
				//System.out.println(marketData[2]);
				if (marketData[5].equals("B")){
					prices.add(new BigDecimal(marketData[2]));
				}else if (marketData[5].equals("S")){
					temp = new BigDecimal(marketData[2]);
					temp = temp.negate();
					prices.add(temp);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(int i = 0; i < prices.size()-1;i++){
			if (prices.get(i).compareTo(BigDecimal.ZERO) >= 0 && prices.get(i+1).compareTo(BigDecimal.ZERO)<0){
				temp = prices.get(i).add(prices.get(i+1));
				temp = temp.divide(prices.get(i+1),3,BigDecimal.ROUND_HALF_UP);
				temp = temp.negate();
				pairResult.add(temp);
			}
		}
		//http://java.about.com/od/creatinguserinterfaces/ss/simplewindow_4.htm#step-heading
		
		
		BigDecimal result =new BigDecimal("0");
		for(BigDecimal d : pairResult){
			result = result.add(d);
		}
		NumberFormat numberFormat = NumberFormat.getPercentInstance(); 
		numberFormat.setMinimumFractionDigits(1);
		//System.out.println(pairResult);
		//System.out.println(prices);
		System.out.println("the return value is "+numberFormat.format(result));
		
		return numberFormat.format(result);
	}
}