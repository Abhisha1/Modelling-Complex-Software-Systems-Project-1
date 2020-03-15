
public class Knight extends Thread{
	// An id to identify the knight
	private int id;
	//List of quests that are yet to be acquired
	private Agenda agendaNew;
	//List fo quests that have been completed and released by knights
	private Agenda agendaComplete;
	//The current quest the knight has
	private volatile Quest currentQuest;
	
	public volatile boolean isOutside;
	
	public volatile boolean isSeated;

	private Hall greatHall;
	
	public Knight(int id, Agenda agendaNew, Agenda agendaComplete, Hall greatHall) {
		this.id = id;
		this.agendaNew = agendaNew;
		this.agendaComplete = agendaComplete;
		this.isOutside = true;
		this.isSeated = false;
		this.greatHall = greatHall;
		this.currentQuest = null;
	}
	
	public Quest getQuest() {
		return this.currentQuest;
	}
	
	public void addQuest(Quest quest) {
		currentQuest = quest;
	}
	
	public void releaseQuest(Quest quest) {
		currentQuest = null;
	}
	
	// produce an identifying string for the quest
    public String toString() {
        return "Knight " + id;
    }
    
    
    
    public void run() {
        while (!isInterrupted()) {
            try {
            	while(true) {
	            	this.greatHall.knightEnter(this);
	            	//Knight mingles with other knights
	                sleep(Params.getMinglingTime());
	                
	                this.greatHall.sit(this);
                	if(currentQuest != null) {
                		this.agendaComplete.releases(this, this.greatHall);
                		
                	}
                	this.agendaNew.acquire(this, this.greatHall);
                	this.greatHall.stand(this);
                	//Knight mingles again with other knights before leaving
                	sleep(Params.getMinglingTime());
                	this.greatHall.knightExit(this);
	                this.agendaNew.setsOff(this);
	                //Knight is completing the quest
                	sleep(Params.getQuestingTime());
                	this.agendaComplete.completes(this);
            	}    
                
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
