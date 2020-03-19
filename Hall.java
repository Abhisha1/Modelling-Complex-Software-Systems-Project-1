/***
 * A monitor that controls all the activities related to a hall, including entering/exiting, sitting/standing,
 * and commencing and concluding meetings with the relevant knights and/or King Arthur.
 * 
 * @author Abhisha Nirmalathas
 * Student Number: 913405
 *
 */
public class Hall {
	//name of the hall
	private String name;
	
	private Agenda agendaNew;
	
	private Agenda agendaComplete;

	// checks if there is currently a meeting occuring
    public volatile boolean meetingInProgress;

    // checks if King Arthur is in the hall
    private volatile boolean kingArthurPresent;
    
    // number of knights in Hall
    private volatile int nKnightsPresent;
    
    // number of knights seated in Hall
    private volatile int nKnightsSeated;
	
	public Hall(String name, Agenda agendaNew, Agenda agendaComplete) {
		this.name = name;
		this.agendaNew = agendaNew;
		this.agendaComplete = agendaComplete;
        this.meetingInProgress = false;
        this.kingArthurPresent = false;
        this.nKnightsPresent = 0;
        this.nKnightsSeated = 0;
	}
	// Knight enters the greathall with King Arthur isn't present
    public synchronized void knightEnter(Knight knight){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("%s enters %s.\n", knight.toString(), this.name);
        knight.isOutside = false;
        nKnightsPresent++;
        notifyAll();
    }
    //King Arthur enters the greathall
    public synchronized void kingEnter(KingArthur king){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("King Arthur enters %s.\n", this.name);
        this.kingArthurPresent = true;
        notifyAll();
    }
 // Knight exits the greathall with King Arthur isn't present
    public synchronized void knightExit(Knight knight){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("%s exits from %s.\n", knight.toString(), this.name);
        knight.isOutside = true;
        this.nKnightsPresent--;
        notifyAll();
    }
  //King Arthur exits the greathall once the meeting is complete
    public synchronized void KingExit(KingArthur king){
        while (meetingInProgress){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("King Arthur exits from %s.\n", this.name);
        this.kingArthurPresent = false;
        notifyAll();
    }
  //Knight sits in the greathall when the King is present
    public synchronized void sit(Knight knight) {
    	while(!kingArthurPresent || knight.isSeated) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("%s sits at the Round Table.\n", knight.toString());
    	knight.isSeated = true;
    	nKnightsSeated++;
        notifyAll();
    }
  //King starts meeting when all present knights are seated
    public synchronized void commenceMeeting(KingArthur king) {
    	while(!this.kingArthurPresent|| (this.nKnightsPresent != this.nKnightsSeated)) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("Meeting begins!\n");
    	this.meetingInProgress = true;
    	notifyAll();
    }
  //Knight stands during meeting when they have acquired a new quest
    public synchronized void stand(Knight knight) {
    	while(!kingArthurPresent || !knight.isSeated || knight.getQuest() == null) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("%s stands from the Round Table.\n", knight.toString());
    	knight.isSeated = false;
    	nKnightsSeated--;
        notifyAll();
    }
  //King ends a meeting when all knights are standing
    public synchronized void endMeeting(KingArthur king) {
    	while(this.nKnightsSeated != 0 || !this.kingArthurPresent) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("Meeting ends!\n");
    	this.meetingInProgress = false;
    	notifyAll();
    }
}
