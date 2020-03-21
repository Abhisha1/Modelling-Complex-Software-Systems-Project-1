/***
 * A knight which handles the various tasks that a knight does,
 * including acquiring and completing quests and attending and exiting\
 * meetings with the King.
 * 
 * @author Abhisha Nirmalathas
 * Student Number: 913405
 *
 */
public class Knight extends Thread{
	// An id to identify the knight
	private int id;
	//List of quests that are yet to be acquired
	private Agenda agendaNew;
	//List of quests that have been completed and released by knights
	private Agenda agendaComplete;
	//The current quest the knight has
	private volatile Quest currentQuest;
	
	// Is the knight outside
	public volatile boolean isOutside;
	
	//Is the knight seated
	public volatile boolean isSeated;
	
	//Is the knight currently questing (ie has set off on a quest)
	public volatile boolean questing;

	private Hall greatHall;
	
	public Knight(int id, Agenda agendaNew, Agenda agendaComplete,
			Hall greatHall) {
		this.id = id;
		this.agendaNew = agendaNew;
		this.agendaComplete = agendaComplete;
		this.isOutside = true;
		this.isSeated = false;
		this.greatHall = greatHall;
		this.questing = false;
		this.currentQuest = null;
	}
	
	//Returns the knights current quest
	public Quest getQuest() {
		return this.currentQuest;
	}
	// Adds a new quest for knight to complete
	public void addQuest(Quest quest) {
		currentQuest = quest;
	}
	// Releases a completed quest
	public void releaseQuest(Quest quest) {
		currentQuest = null;
	}
	
	// produce an identifying string for the quest
    public String toString() {
        return "Knight " + id;
    }
    
    
 /* Thread that controls Knights entering. mingling, taking a seat and
  * eventually releasing and acquiring quests, 
  * standing and exiting the hall, to then complete the quest. */
    public void run() {
        while (!isInterrupted()) {
            try {
	            	this.greatHall.knightEnter(this);
	            	//Knight mingles with other knights
	                sleep(Params.getMinglingTime());
	                
	                this.greatHall.sit(this);
                	if(currentQuest != null && currentQuest.completed) {
                		this.agendaComplete.releases(this, this.greatHall);
                		
                	}
                	this.agendaNew.acquire(this, this.greatHall);
                	this.greatHall.stand(this);
                	//Knight mingles again with other knights before leaving
                	sleep(Params.getMinglingTime());
                	this.greatHall.knightExit(this);
	                this.agendaNew.setsOff(this);
	                // Knight is completing quest
                	sleep(Params.getQuestingTime());
                	this.agendaComplete.completes(this);
                
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
