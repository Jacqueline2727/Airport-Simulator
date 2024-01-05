/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airportSimulation;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.Timer;
/**
 *
 * @author 349737163
 */
public class MainFrame extends javax.swing.JFrame {
    
    static boolean startProgram=false; //start program boolean
    Timer t;
    //flight queues
    public static Queue<Integer> landing = new LinkedList<Integer>();
    public static Queue<Integer>takeoff = new LinkedList<Integer>();
    static long currentTime;
    static long lastUpdate;
    static int numArrive=0; //check how many landings have been processed
    static int curFlight; //current flight
    static int flightAdded; //user input for flight
    //display for flight queues
    static String arrivalsDisplay="";
    static String takeoffDisplay="";
    static int timer=0; //current time
    static boolean updated = false; //check if queue has been updated
    static int countdown; //countdown for flight times
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        int tDuration = 600; //duration of timer
        long lastTime;
        //start timer
        t = new Timer(tDuration, new TimerListener());
        t.start();    
        //generate flight strings for display
        landing=updateQueue(landing,'a','n');
        takeoff=updateQueue(takeoff,'t','n');
        //display the starting queues in the text areas 
        arrivalsArea.setText(arrivalsDisplay);
        takeoffsArea.setText(takeoffDisplay);
    }
    
 private class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(startProgram){ //if the program has started
                currentTime = System.currentTimeMillis();
                if(currentTime-lastUpdate>=600){
                    //when a timer unit passes, add 1 to timer
                    timer++;
                }
                //if fewer than 2 landings have been processed
                if(numArrive<=1){ 
                    //if landing is not empty and the queue has not been updated yet
                    if(!updated &&!landing.isEmpty()){
                        //update the queue
                    landing=updateQueue(landing,'a','r');
                    updated=true;
                    }else if (landing.isEmpty()&&updated){
                        //if landing is empty and updated, display message
                        updateMessage.setText("Flight "+curFlight+" has landed.");
                    }else if (landing.isEmpty()){
                        //if there are no flights waiting to land, process takeoffs
                        numArrive=2;
                    }
                    if(updated){
                    if(timer<4){ //if 4 timers units have not passed
                        DrawingArea.typeFlight='l';
                        countdown=4-timer; //update countdown and message
                        updateMessage.setText("Flight "+curFlight+" is next to land. "+countdown);
                        //update animation
                        DrawingArea.landingX+=20;
                        DrawingArea.landingY+=20;
                        repaint();
                    }else{ //if 4 timer units have been reached
                        //update message
                    updateMessage.setText("Flight "+curFlight+" has landed.");
                    //reset animation and current system time
                    DrawingArea.resetLanding();
                    DrawingArea.typeFlight='w';
                    lastUpdate = System.currentTimeMillis();
                    timer=-1;
                    numArrive++; //add 1 to numArrive
                    //update queue displays
                    arrivalsArea.setText(arrivalsDisplay);
                    takeoffsArea.setText(takeoffDisplay);
                    updated=false;
                    }
                    }
                    //if 2 landings have been processed or there are no flights waiting to land
                }else if (numArrive==2 || landing.isEmpty()){ 
                    if(!updated && !takeoff.isEmpty()){
                        //if takeoff is not empty and the queue has not been updated
                        timer=0; 
                        //update queue
                    takeoff=updateQueue(takeoff,'t','r');
                    updated=true;
                    }
                    if(updated){
                    if(timer<2){ //if 2 timer units have not passed
                        //update display
                        DrawingArea.typeFlight='t';
                        int countdown=2-timer;
                        updateMessage.setText("Flight "+curFlight+" is next to take off. "+countdown);
                        DrawingArea.takeOffX+=20;
                        DrawingArea.takeOffY-=20;
                    }else{ //if 2 timer units have passed
                        //update message
                        updateMessage.setText("Flight "+curFlight+" has taken off.");
                        DrawingArea.typeFlight='w';
                        DrawingArea.resetTakeoff();
                        numArrive=0; //reset numArrive
                        lastUpdate=System.currentTimeMillis();
                        //update queue displays
                        arrivalsArea.setText(arrivalsDisplay);
                        takeoffsArea.setText(takeoffDisplay);
                        updated=false;
                        timer=-1;
                    }
                    }else{
                        //if takeoff is empty, reset numArrive 
                        numArrive=0;
                        timer=-1;
                        DrawingArea.resetLanding();
                    }

                }
            }
            //update animation
           repaint();
        }
    }

 /**
     * @param flights - queue
     * @param type - arrivals or takeoffs 
     * @param function - add, remove, or simply display queues
     * @return 
     */
    public static Queue<Integer> updateQueue(Queue flights, char type, char function){
        String display=""; //display string for the text areas
        Queue<Integer>backup = new LinkedList<Integer>(); //backup queue
        if(function=='r'){ //remove a flight for takeoff
        curFlight=(int)flights.remove();
        }else{
            //don't do anything if only a displayed is needed
        }
        //add flights to a back up queue and a string
        while(!flights.isEmpty()){
            int current = (int)flights.remove();
            backup.add(current);
            display+=current+"\n";
        }
        //set the appropriate string values
        if(type=='a'){
            arrivalsDisplay=display;
        }else{
            takeoffDisplay=display;
        }
        //return the back up queue
        return(backup);
    }
    
    public static Queue<Integer> updateQueue(Queue flights, char type){
        String display=""; //display for the text areas
        Queue<Integer>backup = new LinkedList<Integer>(); //backup queue
        flights.add(flightAdded); //add flight
        
        //add flights to a back up queue and string
        while(!flights.isEmpty()){
            int current = (int)flights.remove();
            backup.add(current);
            display+=current+"\n";
        }
        //set the appropriate string values
        if(type=='a'){
            if(takeoff.isEmpty()&&timer>=4){
                timer=0; //reset timer
                updated=false;
            }
            arrivalsDisplay=display;
        }else{
            if(landing.isEmpty()&&timer>=3){
                timer=0;//reset timer
                updated=false;
            }
            takeoffDisplay=display;
        }
        //return the back up queue
        return(backup);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawingArea1 = new airportSimulation.DrawingArea();
        jLabel1 = new javax.swing.JLabel();
        updateMessage = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        arrivalsArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        takeoffsArea = new javax.swing.JTextArea();
        start = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        arrivingFlight = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        takeoffFlight = new javax.swing.JTextField();
        exit = new javax.swing.JButton();
        arrivalsError = new javax.swing.JLabel();
        takeoffsError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Airport Simulator (ZHO)");
        jLabel1.setFocusable(false);

        updateMessage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        updateMessage.setText("Press 'START' to begin simulation.");
        updateMessage.setFocusable(false);

        jLabel3.setText("Arrivals");
        jLabel3.setFocusable(false);

        jLabel2.setText("Takeoffs");

        arrivalsArea.setEditable(false);
        arrivalsArea.setColumns(20);
        arrivalsArea.setRows(5);
        arrivalsArea.setFocusable(false);
        jScrollPane1.setViewportView(arrivalsArea);

        takeoffsArea.setEditable(false);
        takeoffsArea.setColumns(20);
        takeoffsArea.setRows(5);
        takeoffsArea.setFocusable(false);
        jScrollPane2.setViewportView(takeoffsArea);

        start.setText("Start");
        start.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        start.setBorderPainted(false);
        start.setFocusable(false);
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        jLabel4.setText("Arriving Flight:");

        arrivingFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivingFlightActionPerformed(evt);
            }
        });

        jLabel5.setText("Takeoff Flight:");

        takeoffFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeoffFlightActionPerformed(evt);
            }
        });

        exit.setText("Exit");
        exit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        exit.setBorderPainted(false);
        exit.setFocusable(false);
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout drawingArea1Layout = new javax.swing.GroupLayout(drawingArea1);
        drawingArea1.setLayout(drawingArea1Layout);
        drawingArea1Layout.setHorizontalGroup(
            drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingArea1Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(updateMessage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, drawingArea1Layout.createSequentialGroup()
                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(drawingArea1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(drawingArea1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(drawingArea1Layout.createSequentialGroup()
                                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(arrivalsError, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(drawingArea1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(arrivingFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, drawingArea1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(drawingArea1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(95, 95, 95))
                                    .addGroup(drawingArea1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
                        .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(takeoffsError, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(drawingArea1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(takeoffFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(drawingArea1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(23, 23, 23))
        );
        drawingArea1Layout.setVerticalGroup(
            drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingArea1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateMessage)
                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(drawingArea1Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(start))
                    .addGroup(drawingArea1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))))
                .addGap(39, 39, 39)
                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(arrivingFlight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(takeoffFlight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(arrivalsError, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(takeoffsError, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(drawingArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(drawingArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // when quit button is pressed, exit the program
        System.exit(0); 
    }//GEN-LAST:event_exitActionPerformed

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // when start button is pressed
        startProgram=true; //start the program
        lastUpdate = System.currentTimeMillis();
    }//GEN-LAST:event_startActionPerformed

    private void arrivingFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivingFlightActionPerformed
        // When the user presses enter for the arriving flight text field
        if(startProgram){ //only process action if the program has started
        //get the user input string
        String input = arrivingFlight.getText();
        try{ //try to parse the string into an integer
            int flight = Integer.parseInt(input);
            flightAdded=flight;
            //add flight to queue
            landing=updateQueue(landing,'a');
            //update display and text fields
            arrivalsArea.setText(arrivalsDisplay);
            arrivalsError.setText("");
            arrivingFlight.setText("");
        }catch(NumberFormatException e){
            //if the user does not enter a valid integer, display error message
            arrivalsError.setText("Please enter a valid integer");
            arrivingFlight.setText("");
        }
        }else{ //if the user has not started the program, display error message
            updateMessage.setText("Please start the program first");
        }
    }//GEN-LAST:event_arrivingFlightActionPerformed

    private void takeoffFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeoffFlightActionPerformed
        // When enter is pressed in the takeoff flights text box
        if(startProgram){//only process action if the program has started
        String input = takeoffFlight.getText();
        try{
            //try to parse an integer
            int flight = Integer.parseInt(input);
            //add flight to queue
            flightAdded=flight;
            takeoff=updateQueue(takeoff,'t');
            //update display and text fields
            takeoffsArea.setText(takeoffDisplay);
            takeoffsError.setText("");
            takeoffFlight.setText("");
        }catch(NumberFormatException e){
            //if the user does not enter a valid integer, display error message
            takeoffsError.setText("Please enter a valid integer");
            takeoffFlight.setText("");
        }
        }else{//if the user has not started the program, display error message
            updateMessage.setText("Please start the program first");
        }
    }//GEN-LAST:event_takeoffFlightActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws FileNotFoundException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //create files and scanners for starting files
        File arrivals = new File("arrivals.txt");
        File takeOff = new File("takeoffs.txt");
        //load files into queues
        landing = readFile(arrivals);
        takeoff = readFile(takeOff);
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
              
            }
        });
    }
    
    /**
     * Reads the files into a queue
     * @param file
     * @return
     * @throws FileNotFoundException 
     */
public static Queue readFile(File file)throws FileNotFoundException{
    Queue<Integer> flightQueue = new LinkedList<Integer>();
    Scanner reader = new Scanner(file);
    while(reader.hasNextLine()){ //while there are more lines
        String nextFlight = reader.nextLine();
            int flight = Integer.parseInt(nextFlight);
            //add flight the queue
            flightQueue.add(flight);
            
    }
    //return queue
    return flightQueue;
}




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea arrivalsArea;
    private javax.swing.JLabel arrivalsError;
    private javax.swing.JTextField arrivingFlight;
    private airportSimulation.DrawingArea drawingArea1;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton start;
    private javax.swing.JTextField takeoffFlight;
    private javax.swing.JTextArea takeoffsArea;
    private javax.swing.JLabel takeoffsError;
    private javax.swing.JLabel updateMessage;
    // End of variables declaration//GEN-END:variables
}
