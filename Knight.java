
public class Knight extends Thread{
	
	private int id;
	
	private Agenda agendaNew;
	
	private Agenda agendaComplete;
	
	private Quest currentQuest;
	
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
	
	
	
	public boolean isKnightOutside() {
		return this.isOutside;
	}
	
	public boolean isKnightSeated() {
		return this.isSeated;
	}
	
	public boolean hasQuest() {
		if (this.currentQuest != null) {
			return true;
		}
		return false;
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
	            	
	                sleep(Params.getMinglingTime());
	                
	                this.greatHall.sit(this);
                	if(currentQuest != null) {
                		this.agendaComplete.releases(this, this.greatHall);;
                		
                	}
                	this.agendaNew.acquire(this, this.greatHall);
                	this.greatHall.stand(this);
                	sleep(Params.getMinglingTime());
                	this.greatHall.knightExit(this);
	                this.agendaNew.setsOff(this);
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
